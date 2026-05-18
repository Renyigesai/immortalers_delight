#!/usr/bin/env python3
"""Convert set_nbt loot functions to set_components for Minecraft 1.21."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
LOOT = ROOT / "src/main/resources/data/immortalers_delight/loot_table"

EFFECT_IDS = {
    1: "minecraft:speed",
    2: "minecraft:slowness",
    3: "minecraft:haste",
    4: "minecraft:mining_fatigue",
    5: "minecraft:strength",
    6: "minecraft:instant_health",
    7: "minecraft:instant_damage",
    8: "minecraft:jump_boost",
    9: "minecraft:nausea",
    10: "minecraft:regeneration",
    11: "minecraft:resistance",
    12: "minecraft:fire_resistance",
    13: "minecraft:water_breathing",
    14: "minecraft:invisibility",
    15: "minecraft:blindness",
    16: "minecraft:night_vision",
    17: "minecraft:hunger",
    18: "minecraft:weakness",
    19: "minecraft:poison",
    20: "minecraft:wither",
}


def parse_effects(tag: str) -> list[dict]:
    effects = []
    for m in re.finditer(
        r"\{Id:(\d+),Duration:(\d+)(?:,Amplifier:(\d+))?\}", tag
    ):
        eid = int(m.group(1))
        duration = int(m.group(2))
        amp = int(m.group(3)) if m.group(3) else 0
        eff = {
            "id": EFFECT_IDS.get(eid, f"minecraft:effect_{eid}"),
            "duration": duration,
        }
        if amp:
            eff["amplifier"] = amp
        effects.append(eff)
    return effects


def parse_potion_tag(tag: str) -> dict | None:
    color_m = re.search(r"CustomPotionColor:(\d+)", tag)
    effects = parse_effects(tag)
    if not color_m and not effects:
        return None
    contents: dict = {}
    if color_m:
        contents["custom_color"] = int(color_m.group(1))
    if effects:
        contents["custom_effects"] = effects
    return {"minecraft:potion_contents": contents}


def parse_chest_tag(tag: str) -> dict | None:
    m = re.search(r'LootTable:\\"([^\\"]+)\\"', tag)
    if not m:
        m = re.search(r'LootTable:"([^"]+)"', tag)
    if not m:
        return None
    table = m.group(1)
    return {
        "minecraft:block_entity_data": {
            "LootTable": table,
        }
    }


def convert_function(fn: dict) -> dict | None:
    if fn.get("function") != "minecraft:set_nbt":
        return None
    tag = fn.get("tag", "")
    if "CustomPotion" in tag or "CustomPotionEffects" in tag:
        components = parse_potion_tag(tag)
        if components:
            return {"function": "minecraft:set_components", "components": components}
    if "BlockEntityTag" in tag and "LootTable" in tag:
        components = parse_chest_tag(tag)
        if components:
            return {"function": "minecraft:set_components", "components": components}
    return None


def walk(obj):
    if isinstance(obj, dict):
        if obj.get("function") == "minecraft:set_nbt":
            replacement = convert_function(obj)
            if replacement:
                obj.clear()
                obj.update(replacement)
                return True
            print(f"  WARN: unhandled set_nbt: {obj.get('tag', '')[:80]}")
        for v in obj.values():
            walk(v)
    elif isinstance(obj, list):
        for item in obj:
            walk(item)


def fix_item_names(data: dict) -> None:
    def walk_names(obj):
        if isinstance(obj, dict):
            if obj.get("name") == "minecraft:grass":
                obj["name"] = "minecraft:short_grass"
            for v in obj.values():
                walk_names(v)
        elif isinstance(obj, list):
            for item in obj:
                walk_names(item)

    walk_names(data)


def process_file(path: Path) -> bool:
    data = json.loads(path.read_text(encoding="utf-8"))
    before = json.dumps(data)
    fix_item_names(data)
    walk(data)
    after = json.dumps(data)
    if before != after:
        path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
        return True
    return False


def main():
    changed = []
    for path in sorted(LOOT.rglob("*.json")):
        if process_file(path):
            changed.append(path.relative_to(ROOT))
    print(f"Updated {len(changed)} files:")
    for p in changed:
        print(f"  {p}")


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
"""Fix NeoForge 1.21.1 recipe JSON compatibility issues."""
from __future__ import annotations

import copy
import json
import pathlib

ROOT = pathlib.Path(__file__).resolve().parents[1] / "src" / "main" / "resources" / "data"
RECIPE_DIR = ROOT / "immortalers_delight" / "recipe"

SUGAR_ARRAY = [{"item": "minecraft:sugar"}, {"tag": "forge:sugar"}]
RAW_MEATS_ARRAY = [
    {"tag": "forge:raw_chicken"},
    {"tag": "forge:raw_pork"},
    {"tag": "forge:raw_beef"},
]
BEEF_PORK_ARRAY = [{"tag": "forge:raw_beef"}, {"tag": "forge:raw_pork"}]
MUSHROOM_ARRAY = [
    {"item": "minecraft:brown_mushroom"},
    {"item": "minecraft:red_mushroom"},
]
CHICKEN_ARRAY = [{"tag": "forge:rawchicken"}, {"item": "minecraft:chicken"}]
RICE_ARRAY = [{"tag": "forge:crops/rice"}, {"tag": "forge:seeds/rice"}]
WHEAT_ARRAY = [{"tag": "forge:crops/wheat"}, {"tag": "forge:seeds/wheat"}]


def norm(value: object) -> str:
    return json.dumps(value, sort_keys=True)


RAW_MEATS_C_ARRAY = [
    {"tag": "c:foods/raw_chicken"},
    {"tag": "c:foods/raw_pork"},
    {"tag": "c:foods/raw_beef"},
]
BEEF_PORK_C_ARRAY = [{"tag": "c:foods/raw_beef"}, {"tag": "c:foods/raw_pork"}]
CHICKEN_C_ARRAY = [{"tag": "c:foods/raw_chicken"}, {"item": "minecraft:chicken"}]

REPLACEMENTS = {
    norm(SUGAR_ARRAY): {"tag": "forge:sugar"},
    norm(RAW_MEATS_ARRAY): {"tag": "forge:common_raw_meats"},
    norm(RAW_MEATS_C_ARRAY): {"tag": "forge:common_raw_meats"},
    norm(BEEF_PORK_ARRAY): {"tag": "forge:beef_or_pork"},
    norm(BEEF_PORK_C_ARRAY): {"tag": "forge:beef_or_pork"},
    norm(MUSHROOM_ARRAY): {"tag": "forge:mushrooms"},
    norm(CHICKEN_ARRAY): {"tag": "forge:rawchicken"},
    norm(CHICKEN_C_ARRAY): {"tag": "forge:rawchicken"},
    norm(RICE_ARRAY): {"tag": "forge:crops/rice"},
    norm(WHEAT_ARRAY): {"tag": "forge:wheat_ingredients"},
}


def fix_obj(obj: object, path: pathlib.Path) -> tuple[bool, object]:
    changed = False
    if isinstance(obj, dict):
        if obj.get("type") == "minecraft:stonecutting" and isinstance(obj.get("result"), str):
            count = obj.pop("count", 1)
            obj["result"] = {"count": count, "id": obj["result"]}
            changed = True
        if "ingredients" in obj and isinstance(obj["ingredients"], list):
            new_ings = []
            for ing in obj["ingredients"]:
                if isinstance(ing, list):
                    rep = REPLACEMENTS.get(norm(ing))
                    if rep:
                        new_ings.append(copy.deepcopy(rep))
                        changed = True
                    else:
                        print(f"UNKNOWN ARRAY in {path}: {ing}")
                        new_ings.append(ing)
                elif isinstance(ing, dict) and ing.get("item") == "minecraft:grass":
                    new_ings.append({"item": "minecraft:short_grass"})
                    changed = True
                else:
                    new_ings.append(ing)
            obj["ingredients"] = new_ings
        for key, value in list(obj.items()):
            sub_changed, obj[key] = fix_obj(value, path)
            changed = changed or sub_changed
    elif isinstance(obj, list):
        new_list = []
        for item in obj:
            sub_changed, fixed = fix_obj(item, path)
            changed = changed or sub_changed
            new_list.append(fixed)
        return changed, new_list
    return changed, obj


def main() -> None:
    fixed: list[str] = []
    for path in sorted(RECIPE_DIR.rglob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        changed, data = fix_obj(data, path)
        if changed:
            path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
            fixed.append(str(path.relative_to(ROOT.parents[2])))
    print(f"fixed {len(fixed)} files")
    for name in fixed:
        print(f"  {name}")


if __name__ == "__main__":
    main()

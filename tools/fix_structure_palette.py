#!/usr/bin/env python3
"""Fix only invalid Minecraft block ids in structure templates (1.21 renames).

Farmer's Delight blocks are kept as-is — this mod is an FD addon.
"""
from pathlib import Path

import nbtlib
from nbtlib import String

ROOT = Path(__file__).resolve().parent.parent
STRUCTURE = ROOT / "src/main/resources/data/immortalers_delight/structure"

# Minecraft-only fixes required for 1.21.1
REPLACEMENTS = {
    "minecraft:grass": "minecraft:short_grass",
    "minecraft:cave_air": "minecraft:air",
}


def main() -> None:
    for path in sorted(STRUCTURE.rglob("*.nbt")):
        nbt = nbtlib.load(path)
        count = 0
        for entry in nbt.get("palette", []):
            name = str(entry.get("Name", ""))
            if name in REPLACEMENTS:
                entry["Name"] = String(REPLACEMENTS[name])
                count += 1
        if count:
            nbt.save(path, gzipped=True)
            print(f"{path.relative_to(STRUCTURE)}: {count} minecraft palette fix(es)")


if __name__ == "__main__":
    main()

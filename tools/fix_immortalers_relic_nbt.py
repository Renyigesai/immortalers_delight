#!/usr/bin/env python3
"""Replace optional mod blocks in immortalers_relic structures with vanilla equivalents."""
from pathlib import Path

import nbtlib

STRUCTURES = (
    Path(__file__).resolve().parent.parent
    / "src/main/resources/data/immortalers_delight/structure/immortalers_relic"
)

REPLACEMENTS = {
    "farmersdelight:rich_soil": "minecraft:farmland",
    "farmersdelight:straw_bale": "minecraft:hay_block",
}


def fix_file(path: Path) -> int:
    nbt = nbtlib.load(path)
    changed = 0
    for entry in nbt["palette"]:
        name = str(entry["Name"])
        if name in REPLACEMENTS:
            entry["Name"] = nbtlib.String(REPLACEMENTS[name])
            changed += 1
    if changed:
        nbt.save(path, gzipped=True)
    return changed


def main():
    total = 0
    for path in sorted(STRUCTURES.glob("immortalers_relic_*.nbt")):
        n = fix_file(path)
        if n:
            print(f"{path.name}: {n} palette entries")
            total += n
    print(f"Done. {total} replacements.")


if __name__ == "__main__":
    main()

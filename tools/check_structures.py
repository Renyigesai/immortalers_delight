#!/usr/bin/env python3
"""Validate structure NBT palettes for 1.21.1 compatibility."""
import gzip
import json
import re
from collections import Counter, defaultdict
from pathlib import Path

import nbtlib

STRUCTURES = Path(__file__).resolve().parent.parent / "src/main/resources/data/immortalers_delight/structure"

# Blocks removed or renamed since 1.20.1 (non-exhaustive; expand as needed)
BAD_BLOCKS = {
    "minecraft:grass",
    "minecraft:grass_path",
    "minecraft:water_source",
    "minecraft:lava_source",
    "minecraft:wall_sign",
    "minecraft:sign",
    "minecraft:rose_bush",  # may still exist - verify
}

RENAMES = {
    "minecraft:grass": "minecraft:short_grass",
    "minecraft:grass_path": "minecraft:dirt_path",
    "minecraft:cave_air": "minecraft:air",
}

# FD 1.21.1 block ids (if missing, structure piece fails when palette is resolved)
FD_BLOCKS = {
    "farmersdelight:rich_soil",
    "farmersdelight:straw_bale",
    "farmersdelight:cherry_cabinet",
    "farmersdelight:cabinet",
}


def analyze_file(path: Path) -> dict:
    nbt = nbtlib.load(path)
    palette = [str(e["Name"]) for e in nbt["palette"]]
    counts = Counter()
    for b in nbt["blocks"]:
        counts[palette[int(b["state"])]] += 1

    bad = [p for p in palette if p in BAD_BLOCKS]
    fd = [p for p in palette if p in FD_BLOCKS]
    foreign = [p for p in palette if ":" in p and not p.startswith("minecraft:") and not p.startswith("immortalers_delight:")]

    block_entities = []
    for b in nbt["blocks"]:
        if "nbt" not in b:
            continue
        be = b["nbt"]
        pal_name = palette[int(b["state"])]
        block_entities.append(
            {
                "pos": [int(x) for x in b["pos"]],
                "block": pal_name,
                "id": str(be.get("id", "")),
                "keys": list(be.keys()),
            }
        )

    return {
        "file": str(path.relative_to(STRUCTURES)),
        "size": [int(x) for x in nbt["size"]],
        "palette_size": len(palette),
        "solid_blocks": sum(v for k, v in counts.items() if k not in {"minecraft:air", "minecraft:cave_air", "minecraft:void_air"}),
        "bad_blocks": bad,
        "fd_blocks": fd,
        "foreign": foreign,
        "block_entities": block_entities[:20],
        "all_palette": palette,
    }


def main():
    results = [analyze_file(p) for p in sorted(STRUCTURES.rglob("*.nbt"))]
    problems = [r for r in results if r["bad_blocks"] or r["foreign"]]

    print("=== Structure audit ===\n")
    for r in results:
        flags = []
        if r["bad_blocks"]:
            flags.append(f"BAD:{r['bad_blocks']}")
        if r["foreign"]:
            flags.append(f"FOREIGN:{r['foreign']}")
        if r["fd_blocks"]:
            flags.append(f"FD:{r['fd_blocks']}")
        status = " | ".join(flags) if flags else "ok"
        print(f"{r['file']}: solid={r['solid_blocks']} size={r['size']} [{status}]")

    print("\n=== Block entities (first file each group) ===")
    for r in results:
        if r["block_entities"]:
            print(f"\n{r['file']}:")
            for be in r["block_entities"][:5]:
                print(f"  {be}")

    if problems:
        print("\n!!! Files with problems:", len(problems))
    else:
        print("\nNo bad/foreign palette IDs found.")


if __name__ == "__main__":
    main()

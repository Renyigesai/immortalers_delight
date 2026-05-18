#!/usr/bin/env python3
"""Restore cabinet structure pieces from git HEAD and set correct block entity ids."""
from __future__ import annotations

import subprocess
from pathlib import Path

import nbtlib
from nbtlib import String

ROOT = Path(__file__).resolve().parent.parent
STRUCTURE = ROOT / "src/main/resources/data/immortalers_delight/structure"
GIT_PREFIX = "src/main/resources/data/immortalers_delight/structures/"

FILES = [
    "immortalers_relic/immortalers_relic_1.nbt",
    "immortalers_relic/immortalers_relic_3.nbt",
    "ruins_with_cherry_tree/ruins_with_cherry_tree_7.nbt",
]

MOD_CABINET_BLOCKS = {
    "immortalers_delight:ancient_wood_cabinet",
    "immortalers_delight:himekaido_cabinet",
    "immortalers_delight:leisamboo_cabinet",
    "immortalers_delight:pearlip_shell_cabinet",
    "immortalers_delight:a_bush_cabinet",
}


def restore_file(rel: str) -> None:
    git_path = f"{GIT_PREFIX}{rel}"
    data = subprocess.check_output(["git", "show", f"HEAD:{git_path}"], cwd=ROOT)
    dest = STRUCTURE / rel
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_bytes(data)

    nbt = nbtlib.load(dest)
    palette = [str(e.get("Name", "")) for e in nbt.get("palette", [])]
    fixed = 0
    for block in nbt.get("blocks", []):
        if "nbt" not in block:
            continue
        state = palette[block["state"]]
        be = block["nbt"]
        if state in MOD_CABINET_BLOCKS:
            be["id"] = String("immortalers_delight:cabinet")
            fixed += 1
        elif state.startswith("farmersdelight:") and "cabinet" in state:
            be["id"] = String("farmersdelight:cabinet")
            fixed += 1
    if fixed:
        nbt.save(dest, gzipped=True)
    print(f"{rel}: restored from git, fixed {fixed} cabinet BE id(s)")


def main() -> None:
    for rel in FILES:
        restore_file(rel)


if __name__ == "__main__":
    main()

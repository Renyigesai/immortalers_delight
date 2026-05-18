#!/usr/bin/env python3
"""Fix cabinet block-entity tags in 1.21.1 structure templates (structure/)."""
from pathlib import Path

import nbtlib

ROOT = Path(__file__).resolve().parent.parent
STRUCTURE = ROOT / "src/main/resources/data/immortalers_delight/structure"

MOD_CABINET_BLOCKS = {
    "immortalers_delight:ancient_wood_cabinet",
    "immortalers_delight:himekaido_cabinet",
    "immortalers_delight:leisamboo_cabinet",
    "immortalers_delight:pearlip_shell_cabinet",
    "immortalers_delight:a_bush_cabinet",
}


def fix_file(path: Path) -> int:
    nbt = nbtlib.load(path)
    palette = [str(e.get("Name", "")) for e in nbt.get("palette", [])]
    fixed = 0
    for block in nbt.get("blocks", []):
        if "nbt" not in block:
            continue
        state = palette[block["state"]]
        be_id = str(block["nbt"].get("id", ""))
        if state in MOD_CABINET_BLOCKS:
            del block["nbt"]
            fixed += 1
        elif be_id == "farmersdelight:cabinet" and not state.startswith("farmersdelight:"):
            del block["nbt"]
            fixed += 1
    if fixed:
        nbt.save(path, gzipped=True)
    return fixed


def main() -> None:
    if not STRUCTURE.is_dir():
        print(f"Missing {STRUCTURE}", file=__import__("sys").stderr)
        raise SystemExit(1)
    total = 0
    for path in sorted(STRUCTURE.rglob("*.nbt")):
        n = fix_file(path)
        if n:
            print(f"{path.relative_to(STRUCTURE)}: fixed {n}")
            total += n
    print(f"Done. {total} fix(es).")


if __name__ == "__main__":
    main()

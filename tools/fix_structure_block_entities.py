#!/usr/bin/env python3
"""Set correct cabinet block-entity ids in 1.21.1 structure templates (structure/)."""
from pathlib import Path

import nbtlib
from nbtlib import String

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
        state = palette[int(block["state"])]
        be = block["nbt"]
        if state in MOD_CABINET_BLOCKS:
            if str(be.get("id", "")) != "immortalers_delight:cabinet":
                be["id"] = String("immortalers_delight:cabinet")
                fixed += 1
        elif state.startswith("farmersdelight:") and "cabinet" in state:
            if str(be.get("id", "")) != "farmersdelight:cabinet":
                be["id"] = String("farmersdelight:cabinet")
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

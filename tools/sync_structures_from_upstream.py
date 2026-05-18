#!/usr/bin/env python3
"""Restore 1.21.1 structure/ templates from upstream/master structures/ and fix cabinet BE ids."""
from __future__ import annotations

import subprocess
import sys
from pathlib import Path

import nbtlib
from nbtlib import String

ROOT = Path(__file__).resolve().parent.parent
GIT_PREFIX = "src/main/resources/data/immortalers_delight/structures/"
OUT = ROOT / "src/main/resources/data/immortalers_delight/structure"
REF = "upstream/master"

MOD_CABINET_BLOCKS = {
    "immortalers_delight:ancient_wood_cabinet",
    "immortalers_delight:himekaido_cabinet",
    "immortalers_delight:leisamboo_cabinet",
    "immortalers_delight:pearlip_shell_cabinet",
    "immortalers_delight:a_bush_cabinet",
}


def git_list_nbt() -> list[str]:
    out = subprocess.check_output(
        ["git", "ls-tree", "-r", REF, "--name-only", GIT_PREFIX.rstrip("/")],
        cwd=ROOT,
        text=True,
    )
    return [line.strip() for line in out.splitlines() if line.endswith(".nbt")]


def fix_cabinet_block_entities(nbt: nbtlib.Compound) -> int:
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
    return fixed


def main() -> int:
    paths = git_list_nbt()
    if not paths:
        print(f"No NBT under {REF}:{GIT_PREFIX}", file=sys.stderr)
        return 1

    total_be = 0
    for git_path in paths:
        rel = git_path[len(GIT_PREFIX) :]
        dest = OUT / rel
        dest.parent.mkdir(parents=True, exist_ok=True)
        dest.write_bytes(subprocess.check_output(["git", "show", f"{REF}:{git_path}"], cwd=ROOT))
        nbt = nbtlib.load(dest)
        fixed = fix_cabinet_block_entities(nbt)
        if fixed:
            nbt.save(dest, gzipped=True)
            total_be += fixed
        print(f"{rel}: restored ({dest.stat().st_size} bytes), cabinet BE fixes={fixed}")

    print(f"Done. {len(paths)} file(s), {total_be} cabinet BE id(s) corrected.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

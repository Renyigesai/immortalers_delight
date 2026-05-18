#!/usr/bin/env python3
"""Restore all structure/*.nbt from git HEAD structures/ (FD blocks intact)."""
from __future__ import annotations

import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
GIT_PREFIX = "src/main/resources/data/immortalers_delight/structures/"
OUT = ROOT / "src/main/resources/data/immortalers_delight/structure"


def git_list_nbt() -> list[str]:
    out = subprocess.check_output(
        ["git", "ls-tree", "-r", "HEAD", "--name-only", GIT_PREFIX.rstrip("/")],
        cwd=ROOT,
        text=True,
    )
    return [line.strip() for line in out.splitlines() if line.endswith(".nbt")]


def main() -> int:
    paths = git_list_nbt()
    if not paths:
        print("No NBT in git HEAD structures/", file=sys.stderr)
        return 1
    for git_path in paths:
        rel = git_path[len(GIT_PREFIX) :]
        dest = OUT / rel
        dest.parent.mkdir(parents=True, exist_ok=True)
        dest.write_bytes(subprocess.check_output(["git", "show", f"HEAD:{git_path}"], cwd=ROOT))
        print(f"restored {rel}")
    print(f"Restored {len(paths)} files to {OUT.relative_to(ROOT)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

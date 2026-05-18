#!/usr/bin/env python3
import subprocess
import tempfile
from pathlib import Path

import nbtlib

ROOT = Path(__file__).resolve().parent.parent
GIT = "src/main/resources/data/immortalers_delight/structures/"


def load_git(rel: str):
    data = subprocess.check_output(["git", "show", f"HEAD:{GIT}{rel}"], cwd=ROOT)
    f = tempfile.NamedTemporaryFile(suffix=".nbt", delete=False)
    f.write(data)
    f.close()
    return nbtlib.load(f.name)


def describe_items(be) -> str:
    if "Items" not in be:
        return "  (无柜内物品 NBT)"
    lines = []
    for slot in be["Items"]:
        if "id" in slot:
            lines.append(f"  槽位 {slot.get('Slot')}: {slot['id']} x{slot.get('count', 1)}")
    return "\n".join(lines) if lines else "  (空柜)"


for rel in [
    "immortalers_relic/immortalers_relic_1.nbt",
    "immortalers_relic/immortalers_relic_3.nbt",
    "ruins_with_cherry_tree/ruins_with_cherry_tree_7.nbt",
]:
    nbt = load_git(rel)
    pal = [str(e.get("Name", "")) for e in nbt["palette"]]
    print("===", rel, "===")
    for b in nbt["blocks"]:
        if "nbt" not in b:
            continue
        be = b["nbt"]
        st = pal[b["state"]]
        bid = str(be.get("id", ""))
        if "cabinet" in st or "cabinet" in bid:
            print(f"  相对坐标 pos={list(b['pos'])}")
            print(f"  方块 block={st}")
            print(f"  方块实体 entity={bid}")
            print(describe_items(be))
    print()

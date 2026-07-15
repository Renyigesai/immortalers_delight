#!/usr/bin/env python3
import json, os, re
REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
LANG = json.load(open(os.path.join(REPO, "src/generated/resources/assets/immortalers_delight/lang/zh_cn.json"), encoding="utf-8"))
keys = set()
for root, _, files in os.walk(os.path.join(REPO, "src/main/java")):
    for fn in files:
        if not fn.endswith(".java"): continue
        t = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
        for m in re.finditer(r'translatable\(\s*"tooltip\.immortalers_delight\.([^"]+)"', t):
            keys.add("tooltip.immortalers_delight." + m.group(1))
        for m in re.finditer(r'translatable\(\s*"tooltip\." \+ ImmortalersDelightMod\.MODID \+ "\.([^"]+)"', t):
            keys.add("tooltip.immortalers_delight." + m.group(1))
missing = sorted(k for k in keys if k not in LANG)
print("Mod tooltip keys used in Java but missing from zh_cn:")
for k in missing: print(" ", k)
if not missing: print("  (none)")

# FD custom tooltip items that override appendHoverText (now via TooltipUtils)
fd_items = [
    "pillager_knife", "jeng_nanu", "ku_mesh_non", "large_column", "lonely_spirit_wine",
    "morning_fizz", "hong_mei_ling", "pearlip_bubble_milk", "drink_block_item",
    "ancient_wood_boat", "ancient_wood_chest_boat", "drill_rod_wand",
    "evolutcorn_hard_candy", "bizarre_sausage", "pearlip_beer", "vara_ji",
]
print("\nFD tooltip keys for custom-hover items:")
for item in fd_items:
    key = f"farmersdelight.tooltip.{item}"
    ok = key in LANG or any(k.startswith(f"farmersdelight.tooltip.{item}.") for k in LANG)
    print(f"  {item}: {'OK' if ok else 'MISSING'}")

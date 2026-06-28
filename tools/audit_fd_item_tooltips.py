#!/usr/bin/env python3
"""Verify every item with FD custom tooltip has lang entries."""
import json
import os
import re

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ITEMS_JAVA = os.path.join(REPO, "src", "main", "java", "com", "renyigesai", "immortalers_delight", "init", "ImmortalersDelightItems.java")
LANG = os.path.join(REPO, "src", "generated", "resources", "assets", "immortalers_delight", "lang", "zh_cn.json")

with open(LANG, encoding="utf-8") as f:
    lang = json.load(f)

text = open(ITEMS_JAVA, encoding="utf-8").read()

# registerWithTab("name", ...) and foodItem("name", ...)
item_names: set[str] = set()
for m in re.finditer(r'registerWithTab\(\s*"([^"]+)"', text):
    item_names.add(m.group(1))

fd_tooltip_items = {k.removeprefix("farmersdelight.tooltip.") for k in lang if k.startswith("farmersdelight.tooltip.") and ":" not in k}

# Items that use TooltipUtils / hasCustomToolTip at runtime (from Languages.java createFarmersdelightTooltip)
declared = set()
languages = open(os.path.join(REPO, "src", "main", "java", "com", "renyigesai", "immortalers_delight", "data", "Languages.java"), encoding="utf-8").read()
for m in re.finditer(r'createFarmersdelightTooltip\(\s*"([^"]+)"', languages):
    key = m.group(1)
    declared.add(key.split(".")[0] if "." in key else key)

# Items with hasCustomToolTip=true in ImmortalersDelightItems - heuristic: DrinkItem(...,true,true) or foodItem(...,true)
custom_tooltip_items: set[str] = set()
for m in re.finditer(r'registerWithTab\(\s*"([^"]+)"[^;]*?(?:DrinkItem|foodItem|ShieldLikeFoodItem|PlaceableShieldItem|PowerfulAbleFoodItem|EnchantAbleFoodItem)[^;]*?,\s*true\s*,\s*true', text, re.DOTALL):
    custom_tooltip_items.add(m.group(1))
for m in re.finditer(r'foodItem\(\s*"([^"]+)"[^)]*,\s*true\s*\)', text):
    custom_tooltip_items.add(m.group(1))

print("=== Items likely using farmersDelight custom tooltip (hasCustomToolTip=true) ===")
missing: list[str] = []
for name in sorted(custom_tooltip_items):
    has = name in fd_tooltip_items or any(k.startswith(name + ".") for k in fd_tooltip_items)
    status = "OK" if has else "MISSING"
    if not has:
        missing.append(name)
    print(f"  {name}: {status}")

print(f"\nTotal custom-tooltip items scanned: {len(custom_tooltip_items)}")
print(f"Missing farmersdelight.tooltip.<path>: {len(missing)}")
for n in missing:
    print(f"  - {n} (need farmersdelight.tooltip.{n})")

print("\n=== All createFarmersdelightTooltip base keys ===")
for k in sorted(declared):
    print(f"  {k}")

#!/usr/bin/env python3
"""Audit tooltip translation keys referenced in Java vs lang files."""
import json
import os
import re

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
LANG_DIR = os.path.join(REPO, "src", "generated", "resources", "assets", "immortalers_delight", "lang")
JAVA_DIR = os.path.join(REPO, "src", "main", "java")

# TextUtils prepends farmersdelight. to keys passed to getTranslation
FD_PREFIX = "farmersdelight."


def load_lang(locale: str) -> dict[str, str]:
    path = os.path.join(LANG_DIR, f"{locale}.json")
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def collect_static_keys() -> set[str]:
    keys: set[str] = set()
    for root, _, files in os.walk(JAVA_DIR):
        for name in files:
            if not name.endswith(".java"):
                continue
            text = open(os.path.join(root, name), encoding="utf-8", errors="replace").read()
            for m in re.finditer(r'Component\.translatable\(\s*"([^"]+)"', text):
                keys.add(m.group(1))
            for m in re.finditer(r'TextUtils\.getTranslation\(\s*"([^"]+)"', text):
                keys.add(FD_PREFIX + m.group(1))
            for m in re.finditer(r'TooltipUtils\.farmersDelightTooltipKey\([^)]+\)(?:,\s*"([^"]+)")?', text):
                # suffix-only matches are incomplete; handled via item registry below
                pass
    return keys


def farmersdelight_keys_from_lang(lang: dict[str, str]) -> set[str]:
    return {k for k in lang if k.startswith("farmersdelight.tooltip.")}


def expected_fd_keys_from_lang_generator() -> set[str]:
    """Keys declared via createFarmersdelightTooltip in Languages.java."""
    path = os.path.join(REPO, "src", "main", "java", "com", "renyigesai", "immortalers_delight", "data", "Languages.java")
    text = open(path, encoding="utf-8").read()
    keys: set[str] = set()
    for m in re.finditer(r'createFarmersdelightTooltip\(\s*"([^"]+)"', text):
        base = m.group(1)
        keys.add(f"farmersdelight.tooltip.{base}")
        keys.add(f"farmersdelight.tooltip.immortalers_delight:{base}")
    return keys


def main() -> None:
    zh = load_lang("zh_cn")
    en = load_lang("en_us")
    static = collect_static_keys()

    # Remaining bad pattern?
    bad_pattern_files: list[str] = []
    for root, _, files in os.walk(JAVA_DIR):
        for name in files:
            if not name.endswith(".java"):
                continue
            path = os.path.join(root, name)
            text = open(path, encoding="utf-8", errors="replace").read()
            if '"tooltip." + this' in text:
                bad_pattern_files.append(os.path.relpath(path, REPO))

    print("=== Remaining tooltip.+this patterns ===")
    if bad_pattern_files:
        for p in bad_pattern_files:
            print(" ", p)
    else:
        print("  (none)")

    print("\n=== Static Java keys missing from zh_cn ===")
    missing_zh = sorted(k for k in static if k not in zh and (
        k.startswith(("tooltip.", "farmersdelight.", "message.", "effect.", "container.", "emi.", "entity."))
    ))
    for k in missing_zh:
        print(" ", k)
    if not missing_zh:
        print("  (none)")

    print("\n=== createFarmersdelightTooltip keys missing from zh_cn ===")
    expected = expected_fd_keys_from_lang_generator()
    missing_fd = sorted(k for k in expected if k not in zh)
    for k in missing_fd:
        print(" ", k)
    if not missing_fd:
        print("  (none)")

    print("\n=== tooltip.immortalers_delight.* keys in zh_cn (mod-native tooltips) ===")
    mod_tooltips = sorted(k for k in zh if k.startswith("tooltip.immortalers_delight."))
    print(f"  count: {len(mod_tooltips)}")

    print("\n=== farmersdelight.tooltip.* count ===")
    fd = farmersdelight_keys_from_lang(zh)
    print(f"  zh_cn: {len(fd)}, en_us: {len([k for k in en if k.startswith('farmersdelight.tooltip.')])}")


if __name__ == "__main__":
    main()

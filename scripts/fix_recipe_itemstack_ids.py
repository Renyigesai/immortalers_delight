"""
Rewrite recipe JSON item stacks from legacy \"item\" to 1.21+ \"id\" for known stack keys.
Does not touch Ingredient objects under ingredients / key / smithing slots / tool / ingredient.
"""
from __future__ import annotations

import json
import sys
from pathlib import Path

SKIP_SUBTREES = frozenset(
    {"ingredients", "ingredient", "key", "template", "base", "addition", "tool"}
)
STACK_KEYS = frozenset({"result", "output", "container", "previous_result"})


def fix_stack_dict(obj: object) -> None:
    if isinstance(obj, dict):
        if "item" in obj and "id" not in obj and isinstance(obj["item"], str) and "tag" not in obj:
            obj["id"] = obj.pop("item")
        return
    if isinstance(obj, list):
        for el in obj:
            fix_stack_dict(el)


def walk(node: object) -> None:
    if isinstance(node, dict):
        for k, v in list(node.items()):
            if k in SKIP_SUBTREES:
                continue
            if k in STACK_KEYS:
                fix_stack_dict(v)
            else:
                walk(v)
    elif isinstance(node, list):
        for el in node:
            walk(el)


def main() -> int:
    root = Path(__file__).resolve().parents[1] / "src" / "main" / "resources" / "data" / "immortalers_delight" / "recipe"
    if not root.is_dir():
        print(f"missing {root}", file=sys.stderr)
        return 1
    n = 0
    for path in sorted(root.rglob("*.json")):
        text = path.read_text(encoding="utf-8")
        data = json.loads(text)
        walk(data)
        new_text = json.dumps(data, indent=2, ensure_ascii=False) + "\n"
        if new_text != text:
            path.write_text(new_text, encoding="utf-8")
            n += 1
    print(f"updated {n} files under {root}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

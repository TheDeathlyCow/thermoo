---
title: ⛏️ Item Attribute Modifiers
status: deprecated
---
# Item Attribute Modifiers (Removed in 1.21+)

Item attribute modifiers allow datapacks to modify the default attributes of items easily. This is particularly useful for mod pack developers who want to add [attributes](../entity_attributes.md) to non-Thermoo based modded armors. It allows for attributes to be applied to items based on both tags and item IDs.

!!! warning
    This feature was *removed* in Thermoo 4.0 (for 1.21) due to the massive changes to items.  In Thermoo 4.x for Minecraft 1.21.1, mods can use the [item attribute modifier event](../mods/item_attribute.md). For datapacks and modpacks, I would recommend checking out the [Default Components mod](https://modrinth.com/mod/default-components).

## Example JSON file

JSON files should be placed in `data/<namespace>/thermoo/item_attribute_modifier/<path>.json`.

```json5
{
    "attribute": "thermoo:generic.heat_resistance",
    "modifier": {
        "uuid": "413a10a0-bf0b-47db-a9a9-2eb3dda3bbaf",
        "name": "Test",
        "value": -1.0,
        // one of: "addition", "multiply_base", "multiply_total"
        // note, in 1.20.1 it must be all caps 
        "operation": "addition"
    },
    "item": {
        "items": [
            "minecraft:diamond_helmet",
            "minecraft:iron_helmet",
            "minecraft:leather_helmet"
        ],
        // you can also use a tag here
        "tag": "example:all_helmets"
    },
    // replace with any equipment slot, like chest, legs, feet, mainhand, offhand
    // note, in 1.20.1 it must be all caps 
    "slot": "head",
    // boolean value, false by default. See below for an explanation.
    "require_preferred_slot": false
}
```

The `require_preferred_slot` field only applies the modifier if the given slot is the item's preferred slot. For example: if the item is a helmet, then it only applies to the head slot. If this is true and the item does not have a preferred slot, then the preferred slot will fall back to the main hand. This is useful when using tags that overlap different types of equipment across multiple item attribute modifiers, but you only want to apply the modifier to one slot. 
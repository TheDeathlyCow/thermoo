---
title: 🛡️ Armor Materials API
status: deprecated
---
# Armor Materials API

The primary use of this API is to provide ways to add [Frost and Heat Resistance](../entity_attributes.md) to armors as easily as possible.

!!! warning
    This feature is exclusive to Thermoo 4.x for Minecraft versions 1.21-1.21.1, and is deprecated for that version. In 1.21.2, Minecraft removed the Armor Material registry making this API impossible to update. Instead of using this API, mods can use the [item attribute modifier event](../mods/item_attribute.md). For datapacks and modpacks, I would recommend checking out the [Default Components mod](https://modrinth.com/mod/default-components).

## Events

* `ArmorMaterialEvents#GET_HEAT_RESISTANCE` - Gets the heat resistance for a particular armor material and armor type
* `ArmorMaterialEvents#GET_FROST_RESISTANCE` - Gets the frost resistance for a particular armor material and armor type

You *must* register a listener to these events for resistances to be applied to any armours, the tags below are just for convention/ease of use between mods.

## Armor Material Tags

Again, these tags are just for convention. You don't have to use them. In fact, there is a known issue when using the tags to apply heat and frost resistance where it doesn't really work with a datapack's normal world-independence. For now, the tags should only be used in datapacks that apply to all worlds on a modpack/server.

* `thermoo:very_resistant_to_cold` and `thermoo:very_resistant_to_heat`- armor materials that should add a lot of frost/heat resistance (respectively)
* `thermoo:resistant_to_cold` and `thermoo:resistant_to_heat`- armor materials that should add a little frost/heat resistance (respectively)
* `thermoo:very_weak_to_cold` and `thermoo:very_weak_to_heat`- armor materials that should remove a lot of frost/heat resistance (respectively)
* `thermoo:weak_to_cold` and `thermoo:weak_to_heat`- armor materials that should remove a little frost/heat resistance (respectively)

!!! note
    These are *Armor Material* tags, so their elements are armour materials (like `minecraft:chainmail`), and they go in `data/thermoo/tags/armor_material/${tag_path}.json`. 
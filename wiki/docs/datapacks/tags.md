---
title: 📋 Tags
---
# Tags

Thermoo provides a few builtin datapack tags that you may find useful.

## Entity type tags

Thermoo comes with a number of important entity type tags that control which entities can be affected by temperature. Thermoo will only define the names of the tags and how they are used - it will not populate them with any entity types. You will have to decide which entity types these tags are applicable to your needs. The tags are as follows:

* `#thermoo:cold_immune`: Entities belonging to this tag cannot be frozen.
* `#thermoo:heat_immune`: Entities belonging to this tag cannot be heated.
* `#thermoo:benefits_from_cold`: Entities belonging to this tag benefit from the effects of cold. If the type is also included in `#thermoo:cold_immune`, this tag will override their cold immunity and ensure that they are actually capable of freezing.
* `#thermoo:benefits_from_heat`: Entities belonging to this tag benefit from the effects of heat. If the type is also included in `#thermoo:heat_immune`, this tag will override their cold immunity and ensure that they are actually capable of heating.

## Conventional Consumable Item Tags

These are custom conventional tags in Thermoo. They provide no functionality in Thermoo as-is, they are just meant to provide a standardized way of handling consumable items within the context of the Thermoo Library.

* `#thermoo:consumable/cooling`: Foods that are cold
* `#thermoo:consumable/warming`: Foods that are warm

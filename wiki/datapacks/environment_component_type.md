---
title: Environment Component Types
---
# Environment Component Types

Environment Component Types are a special set of [Components](https://minecraft.wiki/w/Data_component_format) (like those found on `ItemStack`s) that are reserved for the parameters of the environment. 

## Component Formats

- [`thermoo:temperature`](#temperature)
- [`thermoo:relative_humidity`](#relative-humidity)

### Temperature
- `{}` **components**: Parent tag.
    - `{}` **thermoo:temperature**: A [Temperature Record](../../mods/temperature_unit#temperature-record-data-format).

### Relative Humidity
- `{}` **components**: Parent tag.
    - `D` **thermoo:relative_humidity**: A double value between 0 and 1 (inclusive).

## Defining Custom Component Types

Creating a new environment component type is just like creating a new item component type, except that you must register it to the registry `ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE`. All component type objects should be immutable 
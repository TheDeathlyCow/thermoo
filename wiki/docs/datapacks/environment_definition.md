---
title: 🌎 Environment Definition
status: new
---
# Environment Definition

The Environment Definition is a datapack registry that defines the environment parameters for a set of biomes. When you define an entry in this registry, Thermoo will automatically pick it up and apply its environment parameters to players in the biomes that you define for it and pass them to mods via the [Tick Events](../mods/tick_events.md). Entries in this registry is not reloadable, you must restart the world to get update, add, or remove entries. It has a registry path of `thermoo/environment`.

For mods, you should place your files in `src/main/resources/data/[modid]/thermoo/environment/` and for datapacks, they should instead be placed in `[world]/datapacks/your_datapack/data/[namespace]/thermoo/environment/`.

## Format

- `{}`: The root tag.
    - `"` `[]` **biomes**: Identifier of a biome, a list of biome IDs, or a `#`-prefixed biome tag ID, for example `minecraft:plains` or `#c:is_hot/overworld`. Biomes that this environment provides for.
    - `"` `[]` **exclude_biomes**: Optional, Identifier of a biome, a list of biome IDs, or a `#`-prefixed biome tag ID, for example `minecraft:plains` or `#c:is_hot/overworld`. Biomes that this environment does _not_ provide for. Defaults to an empty list if not specified.
    - `"` `{}` **provider**: An [Environment Provider](./environment_provider_definition.md) ID, or in-line defined Environment Provider.
    - `I` **priority**: Optional integer. Determines the priority for which this environment should be applied to a biome. Environments with a HIGHER priority will be applied FIRST, and environments with a LOWER priority will be applied LAST. Environments with the same priority may be applied in any order. Defaults to `1000`.

The environment will provide for all biomes that are included in `biomes` _and not included in_ `exclude_biomes`.

## Examples

Using an inline provider:

```json title="data/example/thermoo/environment/snowy_climate.json"
{
    "biomes": "#c:is_snowy",
    "exclude_biomes": "#c:is_icy",
    "provider": {
        "type": "thermoo:constant",
        "components": {
            "type": "thermoo:constant",
            "components": {
                "thermoo:temperature": {
                    "value": 263.15,
                    "unit": "kelvin"
                },
                "thermoo:relative_humidity": 0.75
            }
        }
    }
}
```

Using a provider ID:
```json title="data/example/thermoo/environment/desert_climate.json"
{
    "biomes": [
        "minecraft:desert"
    ],
    "provider": "example:desert_climate_provider"
}
```

```json title="data/example/thermoo/environment_provider/desert_climate_provider.json"
{
    "type": "thermoo:constant",
    "components": {
        "thermoo:temperature": 35.0,
        "thermoo:relative_humidity": 0.15
    }
}
```



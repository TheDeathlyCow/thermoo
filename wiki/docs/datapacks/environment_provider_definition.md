---
title: ☁️ Environment Provider Definition
status: new
---
# Environment Provider Definition

The Environment Provider is a datapack registry that is used by the [Environment Registry](./environment_definition.md) to actually do the job of providing the environment parameters for a world position. It has a registry path of `thermoo/environment_provider`.

For mods, you should place your files in `src/main/resources/data/[modid]/thermoo/environment_provider/` and for datapacks, they should instead be placed in `[world]/datapacks/your_datapack/data/[namespace]/thermoo/environment_provider/`.

Environment Providers work as a tree-like structure that has two categories of types: branching types that choose a child provider based on some world condition, and leaf types that can provide the environment components directly, or modify the current set in the evaluation tree.

## Environment Provider Formats

- `{}`: The root tag.
    - `"` **type**: The type of the environment provider. See the list below for the format of each type. 
    - `...`: Based on the value of **type**. 

Environment Provider Types:

- [`thermoo:constant`](#constant)
- [`thermoo:seasonal/temperate`](#temperate-seasonal)
- [`thermoo:seasonal/tropical`](#tropical-seasonal)
- [`thermoo:light_threshold`](#light-threshold)
- [`thermoo:weather_state`](#weather-state)
- [`thermoo:precipitation_type`](#precipitation-type)
- [`thermoo:temperature_shift`](#temperature-shift)
- [`thermoo:modify`](#modify)

Just to be clear on how **type** delegation works, here is a complete example of an environment provider using the `thermoo:constant` type.

```json
{
    "type": "thermoo:constant",
    "components": {
        "thermoo:temperature": 35.0
    }
}
```

### Constant

The primary leaf provider type that provides a constant set of the components.

- `{}`: The root tag.
    - `"` **type**: Must be `thermoo:constant`.
    - `{}` **components**: A component map containing pairs of environment component ID keys to the component value. The entries in this map must be [Environment Components](./environment_component_type.md).

!!! info
    If a component ID specified by an instance of this provider type is already defined in the current evaluation tree during lookup, then this will overwrite it. If a component ID is already defined in the evaluation tree, but not specified by an instance of this provider, then it will be left alone. So you can have multiple `thermoo:constant` providers in the same provider tree - see [`thermoo:modify`](#modify) for one way of doing this.

### Temperate Seasonal

A branch provider that chooses a child provider based on the current [Temperate Season](../mods/seasons.md) (Spring, Summer, Autumn, or Winter). This also works in biomes with Tropical Seasons.

- `{}`: The root tag.
    - `"` **type**: Must be `thermoo:seasonal/temperate`.
    - `"` **fallback_season**: Optional, the key of an entry in the **seasons** compound to use if no temperate season exists at the queried world position (usually because a seasons mod like Fabric Seasons is not installed). If there is no season and the fallback season is empty, then this provider will return empty.
    - `{}` **seasons**: A map of Temperate Season IDs to Environment Providers. Must contain at least one entry.
        - `"` `{}` **spring**: Optional, the Environment Provider ID or in-line Environment Provider to use when the current season is Spring.
        - `"` `{}` **summer**: Optional, the Environment Provider ID or in-line Environment Provider to use when the current season is Summer.
        - `"` `{}` **autumn**: Optional, the Environment Provider ID or in-line Environment Provider to use when the current season is Autumn.
        - `"` `{}` **winter**: Optional, the Environment Provider ID or in-line Environment Provider to use when the current season is Winter.

!!! warning
    In order for a season to be chosen, you will need to install both a Seasons mod and a compatibility patch mod. [Thermoo Patches](https://www.github.com/TheDeathlyCow/thermoo-patches) is such a compatibility patch mod which works for [Fabric Seasons](https://modrinth.com/mod/fabric-seasons), [Serene Seasons](https://modrinth.com/mod/serene-seasons), and [Simple Seasons](https://modrinth.com/mod/simple-seasons). If no season mod is installed, then the specified `fallback_season` will be used. 

### Tropical Seasonal

A branch provider that chooses a child provider based on the current [Tropical Season](../mods/seasons.md) (Wet or Dry).

- `{}`: The root tag.
    - `"` **type**: Must be `thermoo:seasonal/tropical`.
    - `"` **fallback_season**: Optional, a key in the **seasons** map to use if no tropical season exists at the queried world position (usually because a seasons mod like Fabric Seasons is not installed, or because the queried world position is not in a tropical biome). If there is no season and the fallback season is empty, then this provider will return empty.
    - `{}` **seasons**: A map of Tropical Season IDs to Environment Providers. Must contain at least one entry.
        - `"` `{}` **wet**: Optional, the Environment Provider ID or in-line Environment Provider to use when the queried world position is currently in the Wet Season.
        - `"` `{}` **dry**: Optional, the Environment Provider ID or in-line Environment Provider to use when the queried world position is currently in the Dry Season.

!!! warning
      In order for a season to be chosen, you will need to install both a Seasons mod and a compatibility patch mod, and the Seasons mod must have support for Tropical Seasons. [Thermoo Patches](https://www.github.com/TheDeathlyCow/thermoo-patches) is such a compatibility patch mod which works for [Fabric Seasons](https://modrinth.com/mod/fabric-seasons), [Serene Seasons](https://modrinth.com/mod/serene-seasons), and [Simple Seasons](https://modrinth.com/mod/simple-seasons). If no season mod is installed, then the specified `fallback_season` will be used.

### Light Threshold

A branch provider that chooses a child provider if the [light level](https://minecraft.wiki/w/Light#Light_level) at the queried world position is at or above some threshold.

- `{}`: The root tag.
    - `"` **type**: Must be `thermoo:light_threshold`.
    - `"` **light_type**: Optional string. One of `block` or `sky`. Applies a filter to the type of light used for calculating the threshold value. If not specified, then the light level will be the combined light level of `max(block_light, internal_sky_light)`.
    - `T/F` **apply_ambient_darkness**: Optional boolean. If the **light_type** is `sky`, then will subtract off an "ambient darkness" value from the raw skylight value at the queried world position. This is a value used by the game to determine the final "internal" skylight level when its night or stormy. No effect if **light_type** is not `sky`. Defaults to `true`.
    - `I` **threshold**: Integer, the threshold light level used to choose the provider to use after the above filters to use. Must be in the range `[0, 15]`.
    - `"` `{}` **above**: The Environment Provider ID or in-line Environment Provider to use when the light level at the queried world position is _at or above_ the **threshold** value.
    - `"` `{}` **below**: The Environment Provider ID or in-line Environment Provider to use when the light level at the queried world position is _strictly below_ the **threshold** value.

### Weather State

A branch provider that chooses a child provider based on the world's current [global weather state](https://minecraft.wiki/w/Weather). To choose a provider based on a biome's particular precipitation type (snow, rain, cloudy), use [Precipitation Type](#precipitation-type).

- `{}`: The root tag.
    - `"` **type**: Must be `thermoo:weather_state`.
    - `"` `{}` **clear**: Optional, the Environment Provider ID or in-line Environment Provider to use when the current weather state is clear.
    - `"` `{}` **rain**: Optional, the Environment Provider ID or in-line Environment Provider to use when the current weather state is rainy, but not thundering. Applies regardless of the queried world position's biome [precipitation type](#precipitation-type).
    - `"` `{}` **thunder**: Optional, the Environment Provider ID or in-line Environment Provider to use when the current weather state is thundering. Applies regardless of the queried world position's biome [precipitation type](#precipitation-type).

### Precipitation Type

A branch provider that chooses a child provider based on a biome's [precipitation type](https://minecraft.wiki/w/Precipitation). This is always the same whether it is raining or not. To choose a provider based on the world's current weather state, use [Weather State](#weather-state). 

- `{}`: The root tag.
    - `"` **type**: Must be `thermoo:precipitation_type`.
      - `{}` **precipitation_type**: Map of precipitation type to child provider. Must have at least one entry.
        - `"` `{}` **none**: Optional, the Environment Provider ID or in-line Environment Provider to use in biomes where it never rains.
        - `"` `{}` **rain**: Optional, the Environment Provider ID or in-line Environment Provider to use in biomes where it can rain but not snow.
        - `"` `{}` **snow**: Optional, the Environment Provider ID or in-line Environment Provider to use in biomes where it can snow.

### Temperature Shift

A leaf provider that adds a temperature shift to the current value of the [`thermoo:temperature` component](./environment_component_type.md#temperature) in the evaluation tree. This is only recommended to be used as an entry in the `modifiers` list of the [`thermoo:modify`](#modify) environment provider type.

If there is not an instance of the `thermoo:temperature` component in the current evaluation tree, then this provider will not do anything and log a warning.

- `{}`: The root tag.
    - `D` `{}` **shift**: A [temperature record](../mods/temperature_unit.md#temperature-record-data-format) that is added to the current instance of the `thermoo:temperature` component in the evaluation tree. It is recommended to use the absolute units (Kelvin or Rankine) for this record, but not mandatory.

### Modify

A branch provider that sets a base provider, then applies a list of providers as "modifiers" to it in order.

- `{}`: The root tag.
    - `"` **type**: Must be `thermoo:modify`. 
    - `"` `[]` **modifiers**: Identifier of an Environment Provider, a list of environment provider IDs, or a `#`-prefixed environment provider tag ID, for example `example:night_temperature_shift` or `#example:temperature_modifiers`. Applies a series of providers to the base in the order specified as "modifiers".
    - `"` `{}` **base**: An Environment Provider ID, or in-line defined Environment Provider.

## Examples

The [Thermoo Test Mod](https://github.com/TheDeathlyCow/thermoo/tree/1.21.3-dev/src/testmod/resources/data/thermoo-test/thermoo) provides examples for all environment provider types, but here are a few other ones.

---
Provide a low temperature in places it is currently snowing.

```json
{
    "type": "thermoo:weather_state",
    "rain": {
        "type": "thermoo:precipitation_type",
        "precipitation_type": {
            "snow": {
                "type": "thermoo:constant",
                "components": {
                    "thermoo:temperature": -10.0
                }
            }
        }
    },
    "thunder": {
        "type": "thermoo:precipitation_type",
        "precipitation_type": {
            "snow": {
                "type": "thermoo:constant",
                "components": {
                    "thermoo:temperature": -10.0
                }
            }
        }
    }
}
```

---
Make an area warmer when exposed to sunlight.

```json title="example:area_temperature"
{
    "type": "thermoo:modify",
    "modifiers": [
        "example:sun_light_warmth"
    ],
    "base": {
        "type": "thermoo:constant",
        "components": {
            "thermoo:temperature": 20.0
        }
    }
}
```

```json title="example:sun_light_warmth"
{
    "type": "thermoo:light_threshold",
    "light_type": "sky",
    "threshold": 15,
    "above": {
        "type": "thermoo:temperature_shift",
        "shift": {
            "value": 18.0,
            "unit": "rankine"
        }
    },
    "below": "example:empty"
}
```

```json title="example:empty"
{
    "type": "thermoo:constant",
    "components": {}
}
```

## Custom Environment Provider Types

Mods may define their own environment provider types. To do this, create a class which implements `EnvironmentProvider` and write your logic for it. This class should be immutable, so it is highly recommended to use `record` classes (in Java) or `data` classes (in Kotlin) for this purpose.

You will then need to create a `MapCodec` to be assigned to an `EnvironmentProviderType` which is then registered to `ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE`.

### Example

A leaf provider type that provides a constant temperature value.

=== "Java"
    ```java
    public record ConstantTemperatureEnvironmentProvider(
            TemperatureRecord temperature
    ) implements EnvironmentProvider {
        public static final MapCodec<ConstantTemperatureEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        TemperatureRecord.CODEC
                                .fieldOf("temperature")
                                .forGetter(ConstantTemperatureEnvironmentProvider::temperature)
                ).apply(instance, ConstantTemperatureEnvironmentProvider::new)
        );
        
        // usually would go in its own EnvironmentProviderTypes-like class, but kept here for simplicity. 
        public static final EnvironmentProviderType<ConstantTemperatureEnvironmentProvider> TYPE = new EnvironmentProviderType(CODEC);
        
        @Override
        public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ComponentMap.Builder builder) {
            builder.add(EnvironmentComponentTypes.TEMPERATURE, this.temperature);
        }
    
        @Override
        public EnvironmentProviderType<ConstantTemperatureEnvironmentProvider> getType() {
            return TYPE;
        }
        
        public static void initialize() {
            Registry.register(
                    ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE,
                    Identifier.of("example", "constant_temperature"),
                    TYPE
            );
        }
    }
    ```

=== "Kotlin"
    ```kotlin
    data class ConstantTemperatureEnvironmentProvider(
            val temperature: TemperatureRecord
    ): EnvironmentProvider {
        
        companion object {
            val CODEC: MapCodec<ConstantTemperatureEnvironmentProvider> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    TemperatureRecord.CODEC
                        .fieldOf("temperature")
                        .forGetter(ConstantTemperatureEnvironmentProvider::temperature)
                ).apply(instance, ::ConstantTemperatureEnvironmentProvider)
            }
    
            // usually would go in its own EnvironmentProviderTypes-like class, but kept here for simplicity. 
            val TYPE: EnvironmentProviderType<ConstantTemperatureEnvironmentProvider> = EnvironmentProviderType(CODEC)
    
            fun initialize() {
                Registry.register(
                    ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE,
                    Identifier.of("example", "constant_temperature"),
                    TYPE
                )
            }
        }
    
        override fun buildCurrentComponents(
            world: World,
            pos: BlockPos,
            biome: RegistryEntry<Biome>,
            builder: ComponentMap.Builder
        ) {
            builder.add(EnvironmentComponentTypes.TEMPERATURE, this.temperature)
        }
    
        override fun getType(): EnvironmentProviderType<ConstantTemperatureEnvironmentProvider> {
            return TYPE
        }
    }
    ```
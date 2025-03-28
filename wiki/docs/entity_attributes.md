---
title: 🛡️ Entity Attributes
---
# Attributes

Thermoo defines several custom attribute types. For a full explanation of how attributes work in general see the [Minecraft Wiki page on Attributes](https://minecraft.wiki/w/Attribute). This page will just document the attributes provided by Thermoo.

## Attributes List

| Attribute Name                         | Default Base | Minimum ~ Maximum | Description                                                                                                                                                               |
|----------------------------------------|--------------|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `thermoo:min_temperature`              | 0            | 0 ~ 8192          | Sets the minimum allowed temperature value to `-140 * attribute_value`.                                                                                                   | 
| `thermoo:max_temperature`              | 0            | 0 ~ 8192          | Sets the maximum allowed temperature value to `140 * attribute_value`.                                                                                                    | 
| `thermoo:max_soaking_tick_multiplier`  | 1            | 0 ~ 8192          | Sets the maximum allowed wet ticks value to `600 * attribute_value`.                                                                                                      |
| `thermoo:frost_resistance`             | 0            | -10 ~ 10          | Reduces the impact of negative temperature changes from non-environmental sources.                                                                                        |
| `thermoo:heat_resistance`              | 0            | -10 ~ 10          | Reduces the impact of positive temperature changes from non-environmental sources.                                                                                        |
| `thermoo:environment_frost_resistance` | 0            | -1 ~ 1            | Positive values provide a random chance of "dodging" a negative temperature change from an environmental source. Negative values provide a chance of doubling it instead. |   
| `thermoo:environment_heat_resistance`  | 0            | -1 ~ 1            | Positive values provide a random chance of "dodging" a positive temperature change from an environmental source. Negative values provide a chance of doubling it instead. |           

!!! info
    In Minecraft versions 1.21.1 and earlier, all attribute IDs are prefixed with `generic.` For example, `thermoo:generic.frost_resistance`.

## Setting the Base Value (mods)

Mods are able to use an event listener to set the base value of the Thermoo attributes. The base value provided by this event is stored as a temporary `add_value` attribute modifier with the ID `thermoo:base_{attributeName}` (for example, `thermoo:base_frost_resistance`).

### Example

=== "Java"
    ```java
    static void initialize() {
        ThermooAttributes.baseValueEvent(ThermooAttributes.FROST_RESISTANCE)
                .register((entity, baseValue) -> {
                    return entity.getType() == EntityType.PLAYER
                            ? baseValue + 45
                            : baseValue;
                });
    }
    ```
=== "Kotlin"
    ```kotlin
    fun initialize() {
        ThermooAttributes.baseValueEvent(ThermooAttributes.FROST_RESISTANCE)
            .register { entity, baseValue -> 
                return@register if (entity.type === EntityType.PLAYER) baseValue + 45 else baseValue
            }
    }
    ```

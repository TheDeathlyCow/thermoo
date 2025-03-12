---
title: 📦 Environment Component Types
status: new
---
# Environment Component Types

Environment Component Types are a special set of [Components](https://minecraft.wiki/w/Data_component_format) (like those found on `ItemStack`s) that are reserved for the parameters of the environment. 

## Component Formats

- [`thermoo:temperature`](#temperature)
- [`thermoo:relative_humidity`](#relative-humidity)

### Temperature
- `{}` **components**: Parent tag.
    - `{}` **thermoo:temperature**: A [Temperature Record](../mods/temperature_unit.md#temperature-record-data-format).

### Relative Humidity
- `{}` **components**: Parent tag.
    - `D` **thermoo:relative_humidity**: A double value between 0 and 1 (inclusive).

## Defining Custom Component Types

Creating a new environment component type is just like creating a new item component type, except that you must register it to the registry `ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE`. All component type objects should be immutable, and any operations on them should return a new instance. For this reason, it is recommended to use `record` (in Java) or `data` (in Kotlin) classes to create your components.

You will need to register a `Codec` for your component type. The Fabric docs have a useful page on the subject if you are unfamiliar: https://docs.fabricmc.net/develop/codecs 

### Example

A dew point component type.

=== "Java"
    ```java
    public record DewPointComponent(
            TemperatureRecord dewPoint
    )  {
        public static final Codec<DewPointComponent> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        TemperatureRecord.CODEC
                                .fieldOf("dew_point")
                                .forGetter(DewPointComponent::dewPoint)
                ).apply(instance, DewPointComponent::new)
        );
    
        public static final DewPointComponent DEFAULT = new DewPointComponent(new TemperatureRecord(20));
    
        // usually this would go in its own EnvironmentComponentTypes-like class, but kept here as an example
        public static final ComponentType<DewPointComponent> COMPONENT = ComponentType.builder()
                .codec(CODEC)
                .build();
        
        public static void initialize() {
            Registry.register(
                    ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE,
                    Identifier.of("example", "dew_point"),
                    COMPONENT
            );
        }
    }
    ```
=== "Kotlin"
    ```kotlin
    data class DewPointComponent(
        val dewPoint: TemperatureRecord 
    ) {
        companion object {
            val CODEC: Codec<DewPointComponent> = RecordCodecBuilder.create { instance ->
                instance.group(
                    TemperatureRecord.CODEC
                        .fieldOf("dew_point")
                        .forGetter(DewPointComponent::dewPoint)
                ).apply(instance, ::DewPointComponent)
            }
            
            val DEFAULT: DewPointComponent = DewPointComponent(20.C)
            
            // usually this would go in its own EnvironmentComponentTypes-like class, but kept here as an example
            val COMPONENT: ComponentType<DewPointComponent> = ComponentType.builder()
                    .codec(CODEC)
                    .build()
            
            fun initialize() {
                Register.register(
                    ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE,
                    Identifier.of("example", "dew_point"),
                    COMPONENT
                )
            }
        }
    }
    ```
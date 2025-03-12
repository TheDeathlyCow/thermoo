---
title: 🌡️ Temperature Unit API
---

Thermoo provides four units for storing temperature records: Celsius, Fahrenheit, Kelvin, and Rankine. These units are stored in the `TemperatureUnit` enum class, which provides the facilities for converting values between these units.

The `TemperatureRecord` class stores a `(value, unit)` tuple of a temperature recording. This class can also handle things like shifting temperature, conversion between units, and comparisons. It is highly recommended to use this class when working with temperature values.

In Kotlin, you can create `TemperatureRecord` instances using field extensions on numbers provided by `TemperatureRecordExtensions`.

## Usage Examples

Apply a 10°C/18°F temperature increase to a temperature record.

=== "Java"
    ```java
    TemperatureRecord shiftTemperature(TemperatureRecord base) {
        TemperatureRecord shift = new TemperatureRecord(18, TemperatureUnit.FAHRENHEIT);
        return base.add(shift);
    }
    ```
=== "Kotlin"
    ```kotlin
    import com.github.thedeathlycow.thermoo.api.kt.TemperatureRecordExtensions
    
    fun shiftTemperature(base: TemperatureRecord): TemperatureRecord {
        return base + 18.F
    }
    ```

Check if a temperature record is at or below the freezing point of water (0°C/32°F).

=== "Java"
    ```java
    boolean isFreezing(TemperatureRecord temperature) {
        TemperatureRecord freezing = new TemperatureRecord(0, TemperatureUnit.CELSIUS);
        return temperature.compareTo(freezing) <= 0;
    }
    ```
=== "Kotlin"
    ```kotlin
    import com.github.thedeathlycow.thermoo.api.kt.TemperatureRecordExtensions
    
    fun isFreezing(temperature: TemperatureRecord): Boolean {
        return temperature <= 0.C
    }
    ```

<br/>

# Temperature Record Data Format

Temperature records can be stored as a number (where the number will be interpreted as the record's value in Celsius), or as a compound with `value` and `unit` keys that specify the unit of the record.

* `D`: Number. The value of the temperature record in Celsius, or;
* `{}`: The root tag.
    - `D` **value**: Number. The value of the temperature record.
    - `E` **unit**: The unit of the temperature record. Must be one of `celsius`, `fahrenheit`, `kelvin`, or `rankine`.

## Examples

Recording a temperature of 30°C in an [Environment Component](../datapacks/environment_component_type.md) map.

```json
{
    "thermoo:temperature": 30.0
}
```

Recording a temperature of 68°F in an [Environment Component](../datapacks/environment_component_type.md) map.

```json
{
    "thermoo:temperature": {
        "value": 68.0,
        "unit": "fahrenheit"
    }
}
```

Recording a temperature of 0°K in an [Environment Component](../datapacks/environment_component_type.md) map.

```json
{
    "thermoo:temperature": {
        "value": 0.0,
        "unit": "kelvin"
    }
}
```
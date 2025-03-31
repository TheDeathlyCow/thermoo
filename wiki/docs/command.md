---
title: ✨ Thermoo Commands
---
# Thermoo Commands

Thermoo introduces many different commands for interacting with its systems, as analogs to many of the methods in the Java/Kotlin modded side of the library. All commands in Thermoo are registered as subcommands of `/thermoo` for compatibility. Commands all print translated messages to chat, but come with fallbacks to US English.

## Environment

The environment command interfaces with the [Environment API](./datapacks/environment_definition.md) to lookup various environment parameters. This is currently only implemented for the `thermoo:temperature` and `thermoo:relative_humidity` environment component types. Mods that create their own environment component types must also create their own lookup command for them.

### Environment Temperature

Temperature is used to check the temperature reading at a specific location, or the environment temperature point change of a specific player. It is used as follows:

```mcfunction
thermoo environment temperature <location> [<unit>] [<scale>]
```
Returns the temperature of the location in some unit, as determined by the local [environment conditions](./datapacks/environment_definition.md).

Arguments:
- `location`: A block position.
- `unit`: An optional string, either `celsius`, `fahrenheit`, `kelvin`, or `rankine`. Determines what [unit](./mods/temperature_unit.md) to display the temperature in. Defaults to `celsius`.
- `scale`: An optional float. Multiplies the final temperature value so that commands can read the decimal point, if desired. Defaults to `1.0`.
    - For example: a scale of `10.0` and a temperature of `31.3` Celsius would have the command return a result of `313` that can be stored in any of the normal targets of `execute store result`.

```mcfunction
thermoo environment temperature <target>
```
Returns the environment temperature point change of the player's current position as calculated by mod event listeners.

Arguments:
- `target`: A single player target.

### Environment Humidity

```mcfunction
thermoo environment relativehumidity <location> [<scale>]
```

Returns the relative humidity of the location in some unit, as determined by the local [environment conditions](./datapacks/environment_definition.md).

Arguments:
- `location`: A block position.
- `scale`: An optional float. Multiplies the final relative humidity value so that commands can read it (the internal humidity value is stored as a 0-1 percentage). Defaults to `100.0`.
    - For example: A scale of `10.0` and a relative humidity of `54.3%` would have the command result of `5` that can be stored in any of the normal targets of `execute store result`.

<br/>

## Temperature
### Get Temperature

```mcfunction
thermoo temperature get <target> [<mode>]
```

The `mode` can be one of the following:

* `current`: Result is the current temperature of the `target` entity. This is the default behavior if not specified.
* `max`: Result is the maximum temperature of the `target` entity.
* `min`: Result is the minimum temperature of the `target` entity.

If `mode` is set to `scale`, then an additional argument may be specified:

```mcfunction
thermoo temperature get <target> scale [<scale>]
```

The `scale` argument is an integer multiplier of the temperature scale of the entity, which is a percentage of how cold/warm they are, where -100% is maximum cold, and +100% is maximum hot. The result is the floor of the temperature scale of the `target` entity multiplied by `scale`. If `scale` is not specified then it will default to 100.

For example, a scale argument of `1000.0` and a temperature scale of `54.3%` would have the command result of `543` that can be stored in any of the normal targets of `execute store result`.

### Set Temperature

```mcfunction
thermoo temperature set <targets> <amount>
```

Sets the temperature of each entity specified by `targets` to the specified `amount`, clamping between the target's minimum and maximum temperature.

Result: The sum of all amounts successfully added.

### Add and Remove Temperature

```mcfunction
thermoo temperature (add|remove) <targets> <amount> [<mode>]
```

Adds or removes the specified `amount` of temperature to the `target`'s current temperature, clamped between their min and max temperature. The change is applied in the specified `mode`.

The `mode` specified how and when thermal resistances should be applied. By default, if not specified it will be treated as `absolute`. The modes are as follows:

* `absolute`: No resistance will be applied
* `active`: Resistance will always be applied
* `passive`: Only applies thermal resistance when the target is currently in the relevant temperature range. For example, cold resistance is only applied to targets that are cold; and heat resistance only to targets that are warm.

Result: The sum of the amounts successfully added or removed before resistance is applied. If removing, the result will be negative.


## Soaking

A command to control the wet ticks of an entity.

### Get Wet Ticks

```mcfunction
thermoo soaking get <target> [<mode>]
```

Gets the current, min, or max wet ticks value of the target.

Arguments:
- `target`: A single entity target.
- `mode`: Optional enum, must be one of `current`, `min`, `max`, `scale`. Defaults to `current`.
    - `current`: Returns the current wet tick value of the target.
    - `min`: Returns the minimum allowed wet tick value of the target (always 0).
    - `max`: Returns the maximum allowed wet tick value of the target. Controlled by the attribute `thermoo:max_soaking_tick_multiplier`.

If the `mode` is scale, there is an additional argument:

```mcfunction
thermoo soaking get <target> scale [<scale>]
```

Arguments:
- `scale`: An optional float. Multiplies the final temperature value so that commands can read the decimal point, if desired. Defaults to `100.0`.
    - For example: a scale of `100.0` and a soaking scale of `31.3%` would have the command return a result of `31` that can be stored in any of the normal targets of `execute store result`.

### Set Wet Ticks

```mcfunction
thermoo soaking set <target> <value>
```

Sets the target's wet ticks to the specified value.

Arguments:
- `target`: A single entity target.
- `value`: The integer value to set the target's wet ticks to.

### Add and Remove Wet Ticks

```mcfunction
thermoo soaking (add|remove) <target> <value>
```

Adds or removes the specified `value` of wet ticks to the `target`'s current wet ticks, clamped between their min and max wet ticks.

Arguments:
- `target`: A single entity target.
- `value`: Integer, amount of wet ticks to add or remove. Cannot be negative.
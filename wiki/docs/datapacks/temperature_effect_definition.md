---
title: 🥶 Temperature Effect Definition
---
# Temperature Effect Definition

Temperature effects are a data-driven way of adding effects to temperature under certain conditions. Some examples include damage, status effects, and attribute modifiers. They are used by both mods and datapacks. For mods, you should place your files in `src/main/resources/data/[modid]/thermoo/temperature_effect` and for datapacks, they should instead be placed in `[world]/datapacks/your_datapack/data/[namespace]/thermoo/temperature_effect`.

!!! info
    For 1.20.4 and below: the path of temperature effects was `thermoo/temperature_effects` in these versions, but it has since been updated to a singular form to reflect similar changes made to the vanilla game.

## JSON format

Temperature effects come in the following JSON format:

* `{}`: The root tag
    - `"` **type**: Resource location/Identifier of the type (see below). By default, Thermoo allows for `thermoo:empty`, `thermoo:status_effect`, `thermoo:attribute_modifier`, `thermoo:scaling_attribute_modifier`, `thermoo:damage`, `thermoo:function`, or `thermoo:sequence`
    - `{}` **entity**: A [Datapack Predicate](https://minecraft.wiki/w/Predicate). Optional: if not specified then evaluates to `true`.
    - `"` `[]` **entity_type**: Identifier of an entity type, a list of entity type IDs, or a `#`-prefixed entity type tag ID, for example `minecraft:player` or `#minecraft:freeze_hurts_extra_types`. Only ticks the effect for those entity types. Is generally more efficient than using a predicate, as the effect won't be ticked at all for other types. Optional: Defaults to an empty list, which ticks the effect for all entity types.
    - `D` `{}` **temperature_scale_range**: A double range that specifies at what temperature *scales* the effect should apply on. Note that the scale is a percentage from -1 to +1, not their actual temperature value. Use an object with `D` **min** and `D` **max** to check for number ranges. Optional: will accept any temperature scale if not specified.
    - `{}` **config**: A JSON compound. Its form is dependent on the value of `type` (see below for details).
    - `I` **loading_priority**: An integer. Allows load-order independent overriding of temperature effects from other mods/datapacks, to support the creation of compatibility patches. An effect of a higher priority will always load before an effect at the same resource location with a lower priority. Optional: defaults to 0.
    - Temperature effects also support the Fabric Resource Conditions API. See: https://github.com/FabricMC/fabric/pull/1656

## Temperature Effect Types

---

### Empty

The empty temperature effect, as the name implies, does nothing. It is primarily useful for overriding/deleting temperature effects from lower-priority mods or datapacks.

#### Config JSON format

If the `type` is `thermoo:empty`, the `config` can be left as an empty object. Note that it must still be specified!

---

### Status Effect

The status effect temperature effect applies the specified status effect(s) (aka potion effects) to the affected entity when they match the predicate

#### Config JSON format
If the type is `thermoo:status_effect`, the `config` has the following format:

* `[]` **effects**: A list of effects, whose entries have the following format:
    - `{}`: The root tag.
        - `"` **type**: A resource location/identifier of the status effect type. For example, `minecraft:speed`.
        - `I` **duration**: Integer. The time (in ticks) the status effect will be applied for. Optional: defaults to 20 if not specified.
        - `I` **amplifier**: Integer. The amplifier of the status effect.

---

### Attribute Modifier

The attribute modifier applies a fixed attribute modifier to the affected entity

#### Config JSON format

If the type is `thermoo:attribute_modifier`, the `config` has the following format:

* `{}`: The root tag.
    - `F` **value**: Float, the value of the modifier.
    - `"` **attribute_type**: Resource Location/Identifier of the attribute type to apply the modifier to.
    - `"` **id**: The ID of the modifier. Must be formatted as a valid identifier/resource location, e.g. `example:slow_from_freezing`. Note that if two modifiers have the same ID for the same attribute type, then the one applied later will override the earlier one.
    - `E` **operation**: How to apply the modifier. Must be one of `add_value`, `add_multiplied_base`, or `add_multiplied_total`.

---

### Scaling Attribute Modifier

The scaling attribute modifier applies an attribute modifier that scales with the entity's temperature

#### Config JSON format

If the type is `thermoo:scaling_attribute_modifier`, the `config` has the following format:

* `{}`: The root tag.
    - `F` **scale**: Float, the value of the modifier which is multiplied against the temperature scale of the entity. Optional: defaults to 1 if not specified.
    - `"` **attribute_type**: Resource Location/Identifier of the attribute type to apply the modifier to.
    - `"` **id**: The id of the modifier. Must be formatted as a valid identifier/resource location, e.g. `thermoo:slow_from_freezing`. Note that if two modifiers have the same ID for the same attribute type, then the one applied later will override the earlier one.
    - `E` **operation**: How to apply the modifier. Must be one of `add_value`, `add_multiplied_base`, or `add_multiplied_total`.

!!! note
    For 1.20.4 and below: The `id` field is instead a pair of fields, one being a string UUID field called `modifier_uuid` and another being a regular string field called `name`. Furthermore, the `operation` field required one of `add`, `multiply_base` or `multiply_total`.

---

### Damage

Applies damage to the entity in regular intervals, or "pulses".

#### Config JSON format

If the type is `thermoo:damage`, the `config` has the following format:

* `{}`: The root tag.
    - `F` **amount**: Float, the amount of damage to apply on each pulse. Must be strictly positive.
    - `I` **damage_interval**: Integer, the number of ticks between each damage pulse.
    - `"` **damage_type**: String Resource Location/Identifier, the damage type to apply to the entity.

---

### Freeze Damage Legacy (removed in 1.20.2+)

The freeze damage legacy affect will apply freeze damage every few ticks to entities when they match the predicate. This is called "legacy" because in 1.19.4, the way damage is applied is changed to be data-driven. This effect hard codes freeze damage rather than allowing for arbitrary damage types. This effect is deprecated in 1.19.4+, but is kept for posterity.

#### Config JSON format

If the type is `thermoo:freeze_damage_legacy`, the `config` has the following format:

* `{}`: The root tag.
    - `F` **amount**: Float, the amount of freeze damage to apply.
    - `I` **damage_interval**: Integer, the number of ticks between pulses of damage.

---

### Function

A temperature effect that executes [datapack functions](https://minecraft.wiki/w/Function_(Java_Edition)) on an interval. Supports macro functions.

#### Config JSON format

If the type is `thermoo:function`, the `config` has the following format:

* `{}`: The root tag.
    - `"` **function**: The ID of the function to execute.
    - `"` **arguments**: An SNBT string containing the function's macro arguments. Must be supplied if and only if the specified `"` **function** is a macro function.
    - `I` **interval**: A positive integer that is the time, in ticks, between executions of the `function`. OPTIONAL: Defaults to 20, or once per second.
    - `"` **permission_level**: The [permission level](https://minecraft.wiki/w/Permission_level) of the function to execute. Optional: Defaults to 2, which is the normal permission level for functions. Note that this does not respect the function permission level set in `server.properties`, it is only what is set here.

---

### Sequence

A meta-temperature effect that can be used to apply several other child effects in sequence. The child effects will only apply if the parent effect can be applied as well. This is useful when you have many different effects that should apply under the same conditions.

Note: For child effects, there is not much additional performance gain from the `entity_type` field - but it will still function identically.

#### Config JSON format

If the type is `thermoo:sequence`, the `config` has the following format:

* `{}`: The root tag.
    - `[]` **children**: An inline list of other temperature effects.

---

## Defining Custom Effect Types (Mods only!)

You can easily define your own custom temperature effects by extending the class `TemperatureEffect` and then registering an instance into the custom registry `ThermooRegistries.TEMPERATURE_EFFECTS`. Scorchful does this to create sound temperature effect, which you can see [here](https://github.com/TheDeathlyCow/scorchful/blob/main/src/main/java/com/github/thedeathlycow/scorchful/temperature/SoundTemperatureEffect.java).

## Examples

The best examples of how to use these effects can be found in [Frostiful](https://github.com/TheDeathlyCow/frostiful/tree/main/src/main/resources/data/frostiful/thermoo/temperature_effect) and [Scorchful](https://github.com/TheDeathlyCow/scorchful/tree/main/src/main/resources/data/scorchful/thermoo/temperature_effect).

You can also get examples in the [Thermoo Test Mod](https://github.com/TheDeathlyCow/thermoo/tree/main/src/testmod/resources/data/thermoo-test/thermoo/temperature_effect).
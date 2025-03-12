---
title: ❔ Loot Conditions
---
# Loot Conditions

Thermoo defines some custom types of [loot conditions](https://minecraft.wiki/w/Predicate). There is one for temperature, and one for wet ticks.

## Temperature Loot Condition

- `{}`: The root tag.
    - `"` **condition**: Must be `thermoo:temperature`
    - `I` `{}` **value**: Integer range. Either an exact temperature value or a temperature value range compound with `I` **min** and `I` **max** keys.
    - `D` `{}` **scale**: Integer range. Either an exact temperature scale or a temperature scale range compound with `D` **min** and `D` **max** keys.

Requires a 'this' entity loot context variable.

If the 'this' entity is not temperature aware, returns false.

Both scale and value must pass to return true.

## Soaked loot condition

- `{}`: The root tag.
    - `"` **condition**: Must be `thermoo:soaked`
    - `I` `{}` **value**: Integer range. Either an exact soaked value or a soaked value range compound with `I` **min** and `I` **max** keys.
    - `D` `{}` **scale**: Integer range. Either an exact soaked scale or a soaked scale range compound with `D` **min** and `D` **max** keys.

Requires a 'this' entity loot context variable.

If the 'this' entity is not soakable, returns false.

Both scale and value must pass to return true.

## Examples

Is cold predicate:
```json
{
    "condition": "thermoo:temperature",
    "value": {
        "max": 0
    }
}
```

Is warm predicate:
```json
{
    "condition": "thermoo:temperature",
    "value": {
        "min": 0
    }
}
```

Is max temperature predicate:
```json
{
    "condition": "thermoo:temperature",
    "scale": 1.0
}
```

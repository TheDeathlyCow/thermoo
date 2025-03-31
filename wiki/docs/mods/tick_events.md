---
title: ⏲️ Tick Events
status: new
---
# Tick Events

Thermoo introduces several events to control status changes each tick on entities and players. There are essentially four sets of events: Active Temperature Changes, Passive Temperature Changes, Soaking Changes, and Player Environment Temperature Changes.

Each set of events has three events associated with it, following the pattern of:

1. `ALLOW_X_UPDATE`: Whether to allow the tick update to proceed.
2. `GET_X_CHANGE`: Computes the status point change for the update.
3. `ALLOW_X_CHANGE`: Whether to apply the status point change to the affected entity.

These events are applied in order, but each set can be invoked in any order.

## Event Categories
| Event Category                         | Containing Class                    | Description                                                                                       | 
|----------------------------------------|-------------------------------------|---------------------------------------------------------------------------------------------------|
| Active Temperature Changes             | `LivingEntityTemperatureTickEvents` | Targeted temperature changing effects, such as being on fire.                                     |
| Passive Temperature Changes            | `LivingEntityTemperatureTickEvents` | Temperature changes sourced from nearby blocks, such as standing on Ice or being near a Campfire. |
| Soaking Changes                        | `LivingEntitySoakingTickEvents`     | Changes to an entity's wet ticks.                                                                 |
| Player Environment Temperature Changes | `ServerPlayerEnvironmentTickEvents` | Temperature changes applied to players from their local [Environment](../datapacks/environment_definition.md).                       |

All event listeners will receive an `EnvironmentTickContext<T>` object that contains the information about the entity, their world, and their block position in that world. For players, and players only, updated [Environment Components](../datapacks/environment_component_type.md) will also be available.
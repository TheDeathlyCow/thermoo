---
title: 🍂 Seasons API
---

Thermoo provides the ability for mods to integrate with Seasons mods. Thermoo does not actually add any functionality around seasons - it just provides mods the ability to handle them if they want. In 1.20.1, [Fabric Seasons](https://modrinth.com/mod/fabric-seasons) is supported out of the box with just Thermoo installed. However, for newer versions, you must use [Thermoo Patches](https://github.com/TheDeathlyCow/thermoo-patches/) instead.

# Thermoo Seasons

`ThermooSeason` is a mod-agnostic enum provided by Thermoo to represent the four temperate seasons and the two tropical seasons. The temperate seasons are Spring, Summer, Autumn (or Fall), and Winter. The tropical seasons are wet and dry.

# Season Events

* `ThermooSeasonEvents.GET_CURRENT_SEASON` - gets the current temperate season (autumn, spring, winter, summer) for the world, or empty if no season mod is available. Mods can use `ThermooSeason.getCurrentSeason(World)` as a shorthand to invoke this event to query the current season.
* `ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON` - gets the current *tropical* season (wet or dry) at the position in the world, or empty if either the queried position is not tropical, or if no tropical season mod is available. Mods can use `ThermooSeason.getCurrentTropicalSeason(World, BlockPos)` as a shorthand to invoke this event to query the current season.

!!! info
    Most of the time, seasons do not need to be queried directly. Instead, use the relevant [Environment Provider type](../datapacks/environment_provider_definition.md).
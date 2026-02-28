package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusDefinition;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record TemperatureStatusDefinitionImpl(
        int interval,
        HolderSet<EntityType<?>> entityTypes,
        MinMaxBounds.Doubles temperatureScaleRange,
        Optional<LootItemCondition> predicate
) implements TemperatureStatusDefinition {

}
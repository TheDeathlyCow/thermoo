package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusSelectorImpl;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@ApiStatus.NonExtendable
public interface TemperatureStatusSelector {
    MapCodec<TemperatureStatusSelector> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE)
                            .optionalFieldOf("entity_type", HolderSet.empty())
                            .forGetter(TemperatureStatusSelector::entityTypes),
                    MinMaxBounds.Doubles.CODEC
                            .fieldOf("temperature_scale_range")
                            .orElse(MinMaxBounds.Doubles.ANY)
                            .forGetter(TemperatureStatusSelector::temperatureScaleRange),
                    LootItemCondition.DIRECT_CODEC
                            .optionalFieldOf("predicate")
                            .forGetter(TemperatureStatusSelector::predicate)
            ).apply(instance, TemperatureStatusSelectorImpl::new)
    );

    /**
     * If not null, then only applies this effect to entities of the specific type. This is more
     * performant than using predicates if you want to apply an effect only to one specific type.
     */
    HolderSet<EntityType<?>> entityTypes();

    /**
     * The temperature scale at which this should be applied to an entity. This is more
     * performant than using predicates if you want to apply an effect only within a particular
     * temperature range
     */
    MinMaxBounds.Doubles temperatureScaleRange();

    /**
     * If not null, then only applies the effect to entities for which this predicate is TRUE.
     */
    Optional<LootItemCondition> predicate();
}
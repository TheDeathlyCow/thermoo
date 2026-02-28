package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusDefinitionImpl;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

import java.util.Optional;

@ApiStatus.NonExtendable
public interface TemperatureStatusDefinition {
    public static final MapCodec<TemperatureStatusDefinition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.POSITIVE_INT
                            .fieldOf("interval")
                            .forGetter(TemperatureStatusDefinition::interval),
                    RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE)
                            .optionalFieldOf("entity_type", HolderSet.empty())
                            .forGetter(TemperatureStatusDefinition::entityTypes),
                    MinMaxBounds.Doubles.CODEC
                            .fieldOf("temperature_scale_range")
                            .orElse(MinMaxBounds.Doubles.ANY)
                            .forGetter(TemperatureStatusDefinition::temperatureScaleRange),
                    LootItemCondition.DIRECT_CODEC
                            .optionalFieldOf("predicate")
                            .forGetter(TemperatureStatusDefinition::predicate)
            ).apply(instance, TemperatureStatusDefinitionImpl::new)
    );

    @Range(from = 1, to = Integer.MAX_VALUE)
    int interval();

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
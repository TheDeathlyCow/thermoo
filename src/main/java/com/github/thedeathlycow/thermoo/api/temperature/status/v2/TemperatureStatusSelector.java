package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusSelectorImpl;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@ApiStatus.NonExtendable
public interface TemperatureStatusSelector {
    MapCodec<TemperatureStatusSelector> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE)
                            .optionalFieldOf("entity_type", HolderSet.empty())
                            .forGetter(TemperatureStatusSelector::entityTypes),
                    MinMaxBounds.Doubles.CODEC
                            .optionalFieldOf("temperature_scale_range", MinMaxBounds.Doubles.ANY)
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

    final class Builder {
        private final HolderSet<EntityType<?>> entityTypes;
        @Nullable
        private MinMaxBounds.Doubles temperatureScaleRange = null;
        @Nullable
        private LootItemCondition.Builder predicateBuilder = null;

        Builder(HolderSet<EntityType<?>> entityTypes) {
            this.entityTypes = entityTypes;
        }

        public Builder withCondition(LootItemCondition.Builder predicateBuilder) {
            Preconditions.checkState(this.predicateBuilder == null, "Predicate already set");
            Preconditions.checkNotNull(predicateBuilder, "Predicate may not be null");

            this.predicateBuilder = predicateBuilder;
            return this;
        }

        public Builder temperatureIsExactly(double value) {
            Preconditions.checkState(this.temperatureScaleRange == null, "Temperature range already set");
            Preconditions.checkArgument(Double.isFinite(value), "Temperature range must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.exactly(value);
            return this;
        }

        public Builder temperatureIsBetween(double min, double max) {
            Preconditions.checkState(this.temperatureScaleRange == null, "Temperature range already set");
            Preconditions.checkArgument(Double.isFinite(min), "Temperature range minimum must be finite");
            Preconditions.checkArgument(Double.isFinite(max), "Temperature range maximum must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.between(min, max);
            return this;
        }

        public Builder temperatureIsAtLeast(double value) {
            Preconditions.checkState(this.temperatureScaleRange == null, "Temperature range already set");
            Preconditions.checkArgument(Double.isFinite(value), "Temperature range minimum must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.atLeast(value);
            return this;
        }

        public Builder temperatureIsAtMost(double value) {
            Preconditions.checkState(this.temperatureScaleRange == null, "Temperature range already set");
            Preconditions.checkArgument(Double.isFinite(value), "Temperature range maximum must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.atMost(value);
            return this;
        }

        public TemperatureStatusSelector build() {
            return new TemperatureStatusSelectorImpl(
                    this.entityTypes != null ? this.entityTypes : HolderSet.empty(),
                    this.temperatureScaleRange != null ? this.temperatureScaleRange : MinMaxBounds.Doubles.ANY,
                    Optional.ofNullable(predicateBuilder).map(LootItemCondition.Builder::build)
            );
        }
    }
}
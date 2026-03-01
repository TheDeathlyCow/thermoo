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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Provides the data for selecting entities in a {@link TemperatureStatus}.
 * <p>
 * API users should not implement this interface.
 */
@ApiStatus.NonExtendable
public interface TemperatureStatusSelector {
    /**
     * The codec for the selector object.
     */
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
     * Ensures the status will only ever be checked for the given set of entity types. This is computed ahead of time,
     * so it is much more performant than using predicates to check for entity types.
     */
    HolderSet<EntityType<?>> entityTypes();

    /**
     * Dynamically evaluates the current temperature scale of an entity to ensure it is in this inclusive range when
     * attempting to apply the status.
     */
    MinMaxBounds.Doubles temperatureScaleRange();

    /**
     * Dynamically evaluates this predicate when attempting to apply the status.
     */
    Optional<LootItemCondition> predicate();

    /**
     * Builder for selectors. Primarily intended to be used for data generation.
     *
     * @see TemperatureStatus#selector(HolderSet)
     * @see TemperatureStatus#selectAllEntities()
     */
    final class Builder {
        private final HolderSet<EntityType<?>> entityTypes;
        private MinMaxBounds.Doubles temperatureScaleRange = MinMaxBounds.Doubles.ANY;
        @Nullable
        private LootItemCondition.Builder predicateBuilder = null;

        Builder(@NotNull HolderSet<EntityType<?>> entityTypes) {
            this.entityTypes = entityTypes;
        }

        /**
         * Updates the predicate of the builder.
         *
         * @return Returns this builder.
         * @throws NullPointerException if the {@code predicateBuilder} is {@code null}.
         */
        public Builder withCondition(LootItemCondition.Builder predicateBuilder) {
            Preconditions.checkNotNull(predicateBuilder, "Predicate may not be null");

            this.predicateBuilder = predicateBuilder;
            return this;
        }

        /**
         * Sets the {@link TemperatureStatusSelector#temperatureScaleRange() temperature range} to only affect entities
         * with a temperature scale that exactly matches the {@code value}.
         *
         * @param value A finite double.
         * @return Returns this builder.
         * @throws IllegalArgumentException if the {@code value} is infinite or NaN.
         */
        public Builder temperatureIsExactly(double value) {
            Preconditions.checkArgument(Double.isFinite(value), "Temperature range must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.exactly(value);
            return this;
        }

        /**
         * Sets the {@link TemperatureStatusSelector#temperatureScaleRange() temperature range} to only affect entities
         * with a temperature scale that is between {@code min} and {@code max} (both are inclusive).
         *
         * @param min A finite double.
         * @param max A finite double.
         * @return Returns this builder.
         * @throws IllegalArgumentException if either {@code min} or {@code max} are infinite or NaN.
         */
        public Builder temperatureIsBetween(double min, double max) {
            Preconditions.checkArgument(Double.isFinite(min), "Temperature range minimum must be finite");
            Preconditions.checkArgument(Double.isFinite(max), "Temperature range maximum must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.between(min, max);
            return this;
        }

        /**
         * Sets the {@link TemperatureStatusSelector#temperatureScaleRange() temperature range} to only affect entities
         * with a temperature scale is at least the {@code value} (inclusive).
         *
         * @param value A finite double.
         * @return Returns this builder.
         * @throws IllegalArgumentException if the {@code value} is infinite or NaN.
         */
        public Builder temperatureIsAtLeast(double value) {
            Preconditions.checkArgument(Double.isFinite(value), "Temperature range minimum must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.atLeast(value);
            return this;
        }

        /**
         * Sets the {@link TemperatureStatusSelector#temperatureScaleRange() temperature range} to only affect entities
         * with a temperature scale is at most the {@code value} (inclusive).
         *
         * @param value A finite double.
         * @return Returns this builder.
         * @throws IllegalArgumentException if the {@code value} is infinite or NaN.
         */
        public Builder temperatureIsAtMost(double value) {
            Preconditions.checkArgument(Double.isFinite(value), "Temperature range maximum must be finite");

            this.temperatureScaleRange = MinMaxBounds.Doubles.atMost(value);
            return this;
        }

        /**
         * Produces a new selector from this builder.
         */
        public TemperatureStatusSelector build() {
            return new TemperatureStatusSelectorImpl(
                    this.entityTypes,
                    this.temperatureScaleRange,
                    Optional.ofNullable(predicateBuilder).map(LootItemCondition.Builder::build)
            );
        }
    }
}
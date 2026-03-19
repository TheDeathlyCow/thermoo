package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusImpl;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * A temperature status periodically applies {@linkplain #effects() effects} to a {@linkplain #selector() specified set}
 * of {@linkplain net.minecraft.world.entity.LivingEntity living entities}.
 * <p>
 * API users should not implement this interface.
 */
@ApiStatus.NonExtendable
public interface TemperatureStatus {
    /**
     * Codec for the temperature status object.
     */
    Codec<TemperatureStatus> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            TemperatureStatusSelector.CODEC
                                    .forGetter(TemperatureStatus::selector),
                            ExtraCodecs.POSITIVE_INT
                                    .optionalFieldOf("interval", TemperatureStatusImpl.DEFAULT_INTERVAL)
                                    .forGetter(TemperatureStatus::interval),
                            Codec.BOOL
                                    .optionalFieldOf("enabled_by_default", true)
                                    .forGetter(TemperatureStatus::enabledByDefault),
                            TemperatureEffect.DIRECT_CODEC.listOf()
                                    .fieldOf("effects")
                                    .forGetter(TemperatureStatus::effects)
                    )
                    .apply(instance, TemperatureStatusImpl::new)
    );

    /**
     * Codec for temperature status holders.
     */
    Codec<Holder<TemperatureStatus>> CODEC = RegistryFixedCodec.create(ThermooRegistryKeys.TEMPERATURE_STATUS);

    /**
     * Creates a selector builder that applies to all entity types in the specified holder set. Primarily intended to be
     * used for data generation.
     */
    static TemperatureStatusSelector.Builder selector(HolderSet<EntityType<?>> entityTypes) {
        Preconditions.checkNotNull(entityTypes, "Entity types may not be null");
        return new TemperatureStatusSelector.Builder(entityTypes);
    }

    /**
     * Creates a selector builder that applies to all entity types. Primarily intended to be used for data generation.
     */
    static TemperatureStatusSelector.Builder selectAllEntities() {
        return new TemperatureStatusSelector.Builder(HolderSet.empty());
    }

    /**
     * Creates a temperature status builder. Primarily intended to be used for data generation.
     *
     * @param selectorBuilder The selector for the status
     * @see #selector(HolderSet)
     * @see #selector()
     */
    static Builder builder(TemperatureStatusSelector.Builder selectorBuilder) {
        Preconditions.checkNotNull(selectorBuilder, "Selector must be defined");
        return new Builder(selectorBuilder);
    }

    /**
     * Used to select the entities that are affected by this status.
     */
    TemperatureStatusSelector selector();

    /**
     * The interval, in ticks, in which the status will attempt to apply its {@link #effects()}. The default interval is
     * {@value TemperatureStatusImpl#DEFAULT_INTERVAL}.
     *
     * @return Returns an int between {@code 1} and {@value Integer#MAX_VALUE}.
     */
    @Range(from = 1, to = Integer.MAX_VALUE)
    int interval();

    /**
     * Whether this status is enabled by default.
     *
     * @return Returns {@code true} by default.
     */
    boolean enabledByDefault();

    /**
     * A list of the {@linkplain TemperatureEffect effects} that are applied periodically to affected entities.
     */
    List<TemperatureEffect> effects();

    /**
     * Builder for temperature statuses. Primarily intended to be used for data generation.
     *
     * @see #builder(TemperatureStatusSelector.Builder)
     */
    final class Builder {
        private final TemperatureStatusSelector.Builder selectorBuilder;
        private final List<TemperatureEffect> effects = new ArrayList<>();
        private boolean enabledByDefault = true;
        private int interval = TemperatureStatusImpl.DEFAULT_INTERVAL;

        private Builder(TemperatureStatusSelector.Builder selectorBuilder) {
            this.selectorBuilder = selectorBuilder;
        }

        /**
         * Sets the interval, in ticks, between applications of the effects of the status.
         *
         * @param value An int between {@code 1} and {@value Integer#MAX_VALUE}
         * @return Returns this builder.
         * @throws IllegalArgumentException if the value is less than 1
         */
        public Builder withInterval(int value) {
            Preconditions.checkArgument(value >= 1, "Interval must be at least 1");

            this.interval = value;
            return this;
        }

        /**
         * Sets this status to be disabled by default.
         *
         * @return Returns this builder.
         */
        public Builder disabledByDefault() {
            this.enabledByDefault = false;
            return this;
        }

        /**
         * Adds an effect to this status.
         *
         * @param effect The effect ot add.
         * @return Returns this builder.
         * @throws NullPointerException if the effect is {@code null}
         */
        public Builder addEffect(TemperatureEffect effect) {
            Preconditions.checkNotNull(effect, "Null effects are not allowed");

            this.effects.add(effect);
            return this;
        }

        /**
         * Produces a new status from this builder.
         */
        public TemperatureStatus build() {
            return new TemperatureStatusImpl(
                    this.selectorBuilder.build(),
                    this.interval,
                    this.enabledByDefault,
                    this.effects
            );
        }
    }
}
package com.github.thedeathlycow.thermoo.api.core.v2.source;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.core.TemperatureSourceImpl;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Optional;

/**
 * Describes a source of a temperature change and provides methods for reduction.
 */
@ApiStatus.NonExtendable
public interface TemperatureSource {
    /**
     * Direct codec for a temperature source object.
     */
    Codec<TemperatureSource> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ComponentSerialization.CODEC
                                    .fieldOf("description")
                                    .forGetter(TemperatureSource::description),
                            TemperatureReduction.DIRECT_CODEC
                                    .optionalFieldOf("reduction")
                                    .forGetter(TemperatureSource::reduction),
                            ExtraCodecs.POSITIVE_INT
                                    .optionalFieldOf("tick_interval", 0)
                                    .forGetter(TemperatureSource::tickInterval)
                    )
                    .apply(instance, TemperatureSourceImpl::new)
    );

    /**
     * Codec for temperature source registry objects.
     */
    Codec<Holder<TemperatureSource>> CODEC = RegistryFixedCodec.create(ThermooRegistryKeys.TEMPERATURE_SOURCE);

    /**
     * Creates a new {@link Builder}.
     *
     * @param description Text component that describes the source, may not be {@code null}.
     */
    static Builder builder(Component description) {
        Preconditions.checkNotNull(description);

        return new Builder(description);
    }

    /**
     * A text component that provides a human-readable name for the source.
     */
    Component description();

    /**
     * An optional method of reduction of temperature changes applied from this source.
     */
    Optional<TemperatureReduction> reduction();

    /**
     * If this source provides temperature changes on a ticked interval, this controls how often those updates apply.
     * <p>
     * If the interval is {@code 0}, then it will not tick at all. For any value greater than {@code 0}, a listener must be
     * registered to the event {@link com.github.thedeathlycow.thermoo.api.core.v2.event.LivingEntityTemperatureTickEvents#getTemperatureChange(ResourceKey)}
     * for it to tick.
     *
     * @return Returns an int that is not negative.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    int tickInterval();

    /**
     * Builder interface for temperature sources, primarily intended for use with data generation.
     */
    final class Builder {
        private final Component description;
        @Nullable
        private TemperatureReduction reduction = null;
        @Range(from = 0, to = Integer.MAX_VALUE)
        private int tickInterval = 0;

        private Builder(Component description) {
            this.description = description;
        }

        /**
         * Adds a reduction method to this builder.
         *
         * @param reduction The reduction method, may not be {@code null}.
         * @return Returns this builder.
         */
        public Builder withReduction(TemperatureReduction reduction) {
            Preconditions.checkNotNull(reduction);

            this.reduction = reduction;
            return this;
        }

        /**
         * Sets a {@link TemperatureSource#tickInterval() tick interval} for this temperature source.
         *
         * @param interval The interval between updates, in ticks. May not be negative.
         * @return Returns this builder.
         */
        public Builder withTickInterval(int interval) {
            Preconditions.checkArgument(interval >= 0, "Intervals may not be negative");

            this.tickInterval = interval;
            return this;
        }

        /**
         * Creates a new temperature source from this builder.
         */
        public TemperatureSource build() {
            return new TemperatureSourceImpl(
                    this.description,
                    Optional.ofNullable(this.reduction),
                    this.tickInterval
            );
        }
    }
}

package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.core.TemperatureSourceImpl;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Optional;

@ApiStatus.NonExtendable
public interface TemperatureSource {
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

    Codec<Holder<TemperatureSource>> CODEC = RegistryFixedCodec.create(ThermooRegistryKeys.TEMPERATURE_SOURCE);

    static Builder builder(Component description) {
        return new Builder(description);
    }

    Component description();

    Optional<TemperatureReduction> reduction();

    @Range(from = 0, to = Integer.MAX_VALUE)
    int tickInterval();

    final class Builder {
        private final Component description;
        @Nullable
        private TemperatureReduction reduction = null;
        @Range(from = 0, to = Integer.MAX_VALUE)
        private int tickInterval = 0;

        private Builder(Component description) {
            this.description = description;
        }

        public Builder withReduction(TemperatureReduction reduction) {
            Preconditions.checkNotNull(reduction);

            this.reduction = reduction;
            return this;
        }

        public Builder withTickInterval(int interval) {
            Preconditions.checkArgument(interval >= 0, "Intervals may not be negative");

            this.tickInterval = interval;
            return this;
        }

        public TemperatureSource build() {
            return new TemperatureSourceImpl(
                    this.description,
                    Optional.ofNullable(this.reduction),
                    this.tickInterval
            );
        }
    }
}

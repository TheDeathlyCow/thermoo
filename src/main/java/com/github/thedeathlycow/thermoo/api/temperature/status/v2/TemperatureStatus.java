package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
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
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.NonExtendable
public interface TemperatureStatus {
    Codec<TemperatureStatus> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            TemperatureStatusSelector.CODEC
                                    .forGetter(TemperatureStatus::selector),
                            ExtraCodecs.POSITIVE_INT
                                    .optionalFieldOf("interval", 1)
                                    .forGetter(TemperatureStatus::interval),
                            TemperatureEffect.DIRECT_CODEC.listOf()
                                    .fieldOf("effects")
                                    .forGetter(TemperatureStatus::effects)
                    )
                    .apply(instance, TemperatureStatusImpl::new)
    );

    Codec<Holder<TemperatureStatus>> CODEC = RegistryFixedCodec.create(ThermooRegistryKeys.TEMPERATURE_STATUS);

    static TemperatureStatusSelector.Builder selector(HolderSet<EntityType<?>> entityTypes) {
        Preconditions.checkNotNull(entityTypes, "Entity types may not be null");
        return new TemperatureStatusSelector.Builder(entityTypes);
    }

    static TemperatureStatusSelector.Builder selectAllEntities() {
        return new TemperatureStatusSelector.Builder(HolderSet.empty());
    }

    static Builder builder(TemperatureStatusSelector.Builder selectorBuilder) {
        Preconditions.checkNotNull(selectorBuilder, "Selector must be defined");
        return new Builder(selectorBuilder);
    }

    TemperatureStatusSelector selector();

    @Range(from = 1, to = Integer.MAX_VALUE)
    int interval();

    List<TemperatureEffect> effects();

    final class Builder {
        private final TemperatureStatusSelector.Builder selectorBuilder;
        @Nullable
        private Integer interval = null;
        private List<TemperatureEffect> effects = new ArrayList<>();

        private Builder(TemperatureStatusSelector.Builder selectorBuilder) {
            this.selectorBuilder = selectorBuilder;
        }

        public Builder withInterval(int value) {
            Preconditions.checkState(this.interval != null, "Interval already set");
            Preconditions.checkArgument(value >= 1, "Interval must be at least 1");

            this.interval = value;
            return this;
        }

        public Builder addEffect(TemperatureEffect effect) {
            Preconditions.checkNotNull(effect, "Null effects are not allowed");

            this.effects.add(effect);
            return this;
        }

        public TemperatureStatus build() {
            return new TemperatureStatusImpl(
                    this.selectorBuilder.build(),
                    this.interval != null ? this.interval : 1,
                    this.effects
            );
        }
    }
}
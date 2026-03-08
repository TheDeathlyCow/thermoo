package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.core.TemperatureSourceImpl;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

@ApiStatus.NonExtendable
public interface TemperatureSource {
    Codec<TemperatureSource> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ComponentSerialization.CODEC
                                    .fieldOf("description")
                                    .forGetter(TemperatureSource::description),
                            TemperatureReduction.DIRECT_CODEC
                                    .fieldOf("reduction")
                                    .forGetter(TemperatureSource::reduction),
                            ExtraCodecs.POSITIVE_INT
                                    .optionalFieldOf("tick_interval", 0)
                                    .forGetter(TemperatureSource::tickInterval)
                    )
                    .apply(instance, TemperatureSourceImpl::new)
    );

    Codec<Holder<TemperatureSource>> CODEC = RegistryFixedCodec.create(ThermooRegistryKeys.TEMPERATURE_SOURCE);

    Component description();

    TemperatureReduction reduction();

    @Range(from = 0, to = Integer.MAX_VALUE)
    int tickInterval();

    int applyReduction(LivingEntity target, int temperatureChange);
}

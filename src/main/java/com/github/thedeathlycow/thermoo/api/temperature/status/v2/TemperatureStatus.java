package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusImpl;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.NonExtendable
public interface TemperatureStatus {
    Codec<TemperatureStatus> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            TemperatureStatusDefinition.CODEC
                                    .forGetter(TemperatureStatus::definition),
                            TemperatureEffectV2.DIRECT_CODEC.listOf()
                                    .fieldOf("effects")
                                    .forGetter(TemperatureStatus::effects)
                    )
                    .apply(instance, TemperatureStatusImpl::new)
    );

    Codec<Holder<TemperatureStatus>> CODEC = RegistryFixedCodec.create(ThermooRegistryKeys.TEMPERATURE_STATUS);

    // TODO: client effects?
//    StreamCodec<RegistryFriendlyByteBuf, Holder<TemperatureStatus>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ThermooRegistryKeys.TEMPERATURE_STATUS);

    TemperatureStatusDefinition definition();

    List<TemperatureEffectV2> effects();
}
package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public interface TemperatureEffect {
    Codec<TemperatureEffect> DIRECT_CODEC = ThermooRegistries.TEMPERATURE_EFFECT_TYPE.byNameCodec()
            .dispatch(TemperatureEffect::codec, Function.identity());

    Codec<Holder<TemperatureEffect>> CODEC = RegistryFileCodec.create(ThermooRegistryKeys.TEMPERATURE_EFFECT, DIRECT_CODEC);

    boolean apply(LivingEntity victim, Level level);

    default void remove(LivingEntity victim, Level level) {
    }

    MapCodec<? extends TemperatureEffect> codec();
}

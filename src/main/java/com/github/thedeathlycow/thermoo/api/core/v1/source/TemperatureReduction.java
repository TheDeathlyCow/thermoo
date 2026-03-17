package com.github.thedeathlycow.thermoo.api.core.v1.source;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

/**
 * A method for reducing or adjusting temperature changes. Built in implementations currently use attributes to adjust
 * temperature.
 */
public interface TemperatureReduction {
    Codec<TemperatureReduction> DIRECT_CODEC = ThermooRegistries.TEMPERATURE_REDUCTION_TYPE.byNameCodec()
            .dispatch(TemperatureReduction::codec, Function.identity());

    /**
     * Applies a reduction in the temperature change to the target.
     *
     * @param target            The target being affected by the temperature change.
     * @param context           The context of the temperature change.
     * @param temperatureChange The amount of the temperature change.
     * @return Returns an adjusted temperature change.
     */
    int applyReduction(LivingEntity target, TemperatureChange context, int temperatureChange);

    /**
     * The codec for the reduction type. Implementors should register this codec to
     * {@link com.github.thedeathlycow.thermoo.api.ThermooRegistries#TEMPERATURE_REDUCTION_TYPE} in an entry point.
     */
    MapCodec<? extends TemperatureReduction> codec();
}
/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.api.temperature.status.v2;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooBuiltInRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

/**
 * Base interface for temperature effects. Temperature effects are what actually affect entities with a particular
 * {@linkplain TemperatureStatus temperature status}.
 */
public interface TemperatureEffect {
    /**
     * Codec for the temperature effect object.
     */
    Codec<TemperatureEffect> DIRECT_CODEC = ThermooBuiltInRegistries.TEMPERATURE_EFFECT_TYPE.byNameCodec()
            .dispatch(TemperatureEffect::codec, Function.identity());

    /**
     * Attempts to apply the effect to the target.
     * <p>
     * This method is called periodically based on the {@link TemperatureStatus#interval()}.
     *
     * @param target  The entity receiving the effect.
     * @param context Additional context for the effect.
     * @return context {@code true} if the effect was successfully applied or remains valid. Returns {@code false} if
     * the should no longer remain active, <b>which will immediately triggers a call to
     * {@link #remove(LivingEntity, TemperatureEffectContext)}.</b>
     */
    boolean apply(LivingEntity target, TemperatureEffectContext context);

    /**
     * Performs cleanup logic for this effect on the entity.
     * <p>
     * This method is invoked when the effect is no longer active on the entity. Implementations should use this to
     * revert any persistent changes, such as removing attribute modifiers.
     *
     * @param target  The entity the effect is being removed from.
     * @param context Additional context for the effect.
     */
    default void remove(LivingEntity target, TemperatureEffectContext context) {
    }

    /**
     * @return Returns the codec of this effect's type.
     */
    MapCodec<? extends TemperatureEffect> codec();
}

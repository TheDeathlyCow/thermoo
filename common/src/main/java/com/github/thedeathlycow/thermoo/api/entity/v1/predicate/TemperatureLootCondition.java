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

package com.github.thedeathlycow.thermoo.api.entity.v1.predicate;

import com.github.thedeathlycow.thermoo.api.core.v2.Soakable;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureAware;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/// Loot condition used to test the temperature values of an entity in a predicate. Only works for entities that implement
/// [TemperatureAware], which is all instances [net.minecraft.world.entity.LivingEntity]. All other entities will cause this
/// condition to be `false`.
public final class TemperatureLootCondition implements LootItemCondition {

    public static final MapCodec<TemperatureLootCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MinMaxBounds.Ints.CODEC
                            .fieldOf("value")
                            .orElse(MinMaxBounds.Ints.ANY)
                            .forGetter(TemperatureLootCondition::value),
                    MinMaxBounds.Doubles.CODEC
                            .fieldOf("scale")
                            .orElse(MinMaxBounds.Doubles.ANY)
                            .forGetter(TemperatureLootCondition::scale)
            ).apply(instance, TemperatureLootCondition::new)
    );

    private final MinMaxBounds.Ints value;
    private final MinMaxBounds.Doubles scale;

    private TemperatureLootCondition(MinMaxBounds.Ints value, MinMaxBounds.Doubles scale) {
        this.value = value;
        this.scale = scale;
    }

    @Override
    public MapCodec<TemperatureLootCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof TemperatureAware temperatureAware) {
            return this.value.matches(temperatureAware.thermoo$getTemperature())
                    && this.scale.matches(temperatureAware.thermoo$getTemperatureScale());
        }

        return false;
    }

    public static LootItemCondition.Builder builder(MinMaxBounds.Ints value) {
        return () -> new TemperatureLootCondition(value, MinMaxBounds.Doubles.ANY);
    }

    public static LootItemCondition.Builder builder(MinMaxBounds.Doubles scale) {
        return () -> new TemperatureLootCondition(MinMaxBounds.Ints.ANY, scale);
    }

    /// Range of [soaking ticks][Soakable#thermoo$getWetTicks()] that are required for this condition to pass.
    public MinMaxBounds.Ints value() {
        return value;
    }

    /// Range of [soaking scale][Soakable#thermoo$getSoakedScale()] that are required for this condition to pass.
    public MinMaxBounds.Doubles scale() {
        return scale;
    }
}

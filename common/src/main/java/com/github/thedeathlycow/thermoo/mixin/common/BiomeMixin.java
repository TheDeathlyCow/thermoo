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

package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.impl.environment.ThermooBiome;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(Biome.class)
public class BiomeMixin implements ThermooBiome {
    @Unique
    private final List<Holder<EnvironmentProvider>> thermoo$environments = new ArrayList<>();

    @Override
    @Unique
    public List<Holder<EnvironmentProvider>> thermoo$getEnvironmentProviders() {
        return this.thermoo$environments;
    }

    @Override
    public void thermoo$replaceProviders(Collection<Holder<EnvironmentProvider>> providers) {
        this.thermoo$environments.clear();
        this.thermoo$environments.addAll(providers);
    }
}
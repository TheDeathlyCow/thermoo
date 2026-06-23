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

package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.v2.attribute.ThermooEnvironmentAttributes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EnvironmentLookupImpl implements EnvironmentLookup {
    public static final EnvironmentLookupImpl INSTANCE = new EnvironmentLookupImpl();

    public static void initialize() {
        ThermooServerLifecycleEvents.SERVER_STARTED.register(INSTANCE::addProvidersToBiomes);
    }

    @Override
    public DataComponentMap findEnvironmentComponents(Level level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);
        return this.findEnvironmentComponentsForBiome(level, pos, biome);
    }

    public DataComponentMap findEnvironmentComponentsForBiome(Level level, BlockPos pos, Holder<Biome> biome) {
        DataComponentMap.Builder builder = DataComponentMap.builder();

        setDefaultValuesFromEnvAttributes(level, pos, builder);

        for (Holder<EnvironmentProvider> provider : this.getProviders(biome)) {
            provider.value().buildCurrentComponents(level, pos, biome, builder);
        }
        return builder.build();
    }

    private static void setDefaultValuesFromEnvAttributes(Level level, BlockPos pos, DataComponentMap.Builder builder) {
        EnvironmentAttributeSystem attributes = level.environmentAttributes();

        TemperatureRecord baseTemperature = attributes.getValue(ThermooEnvironmentAttributes.TEMPERATURE, pos);
        double basePressure = attributes.getValue(ThermooEnvironmentAttributes.ATMOSPHERIC_PRESSURE, pos);

        builder.set(EnvironmentComponentTypes.TEMPERATURE, baseTemperature);
        builder.set(EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE, basePressure);
    }

    private List<Holder<EnvironmentProvider>> getProviders(Holder<Biome> biomeEntry) {
        Biome biome = biomeEntry.value();
        return ((ThermooBiome) (Object) biome).thermoo$getEnvironmentProviders();
    }

    private void addProvidersToBiomes(MinecraftServer server) {
        Registry<EnvironmentDefinition> envRegistry = server.registryAccess().lookupOrThrow(ThermooRegistries.ENVIRONMENT);
        Registry<Biome> biomeRegistry = server.registryAccess().lookupOrThrow(Registries.BIOME);

        biomeRegistry.listElements().forEach(holder -> {
            List<Holder<EnvironmentProvider>> providers = getAllMatchingEnvironments(holder, envRegistry)
                    .map(EnvironmentDefinition::provider)
                    .toList();

            Biome biome = holder.value();
            Objects.requireNonNull(biome);
            ThermooBiome extendedBiome = ((ThermooBiome) (Object) biome);
            extendedBiome.thermoo$replaceProviders(providers);

            if (Thermoo.LOGGER.isDebugEnabled()) {
                Thermoo.LOGGER.debug("Found {} providers for {}.", providers.size(), holder.key().identifier());
            }
        });
    }

    public static Stream<EnvironmentDefinition> getAllMatchingEnvironments(Holder<Biome> biome, Registry<EnvironmentDefinition> envRegistry) {
        return envRegistry.stream()
                .filter(entry -> entry.providesFor(biome))
                .sorted(Comparator.comparingInt(EnvironmentDefinition::priority).reversed());
    }
}
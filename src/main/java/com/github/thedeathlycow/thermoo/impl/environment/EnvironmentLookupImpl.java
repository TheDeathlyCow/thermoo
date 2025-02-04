package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.*;

public class EnvironmentLookupImpl implements EnvironmentLookup {
    public static final EnvironmentLookupImpl INSTANCE = new EnvironmentLookupImpl();

    private final Map<RegistryKey<Biome>, List<EnvironmentProvider>> biomeProviderCache = new IdentityHashMap<>();

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            INSTANCE.clearCache();
        });
    }

    @Override
    public double findTemperature(World world, BlockPos pos, TemperatureUnit unit) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return this.findTemperatureForBiome(world, pos, unit, biome);
    }

    @Override
    public double findRelativeHumidity(World world, BlockPos pos) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return this.findRelativeHumidityForBiome(world, pos, biome);
    }

    public double findTemperatureForBiome(World world, BlockPos pos, TemperatureUnit unit, RegistryEntry<Biome> biome) {
        List<EnvironmentProvider> providers = this.getProviders(biome, world.getRegistryManager());

        if (providers.isEmpty()) {
            return EnvironmentLookup.fallbackTemperature(unit);
        }

        TemperatureRecord totalTemperatureK = new TemperatureRecord(0, TemperatureUnit.KELVIN);
        int totalProviders = 0;

        for (EnvironmentProvider provider : providers) {
            Optional<TemperatureRecord> result = provider.getTemperature(world, pos, biome);
            if (result.isPresent()) {
                totalTemperatureK = totalTemperatureK.add(result.get());
                totalProviders++;
            }
        }

        if (totalProviders == 0) {
            return EnvironmentLookup.fallbackTemperature(unit);
        }

        return unit.convertTemperature(totalTemperatureK.value() / totalProviders, TemperatureUnit.KELVIN);
    }

    public double findRelativeHumidityForBiome(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        List<EnvironmentProvider> providers = this.getProviders(biome, world.getRegistryManager());

        if (providers.isEmpty()) {
            return EnvironmentLookup.fallbackRelativeHumidity();
        }

        double totalRelativeHumidity = 0.0;
        int totalProviders = 0;

        for (EnvironmentProvider provider : providers) {
            OptionalDouble result = provider.getRelativeHumidity(world, pos, biome);
            if (result.isPresent()) {
                totalRelativeHumidity = result.getAsDouble();
                totalProviders++;
            }
        }

        if (totalProviders == 0) {
            return EnvironmentLookup.fallbackRelativeHumidity();
        }

        return totalRelativeHumidity / totalProviders;
    }

    private List<EnvironmentProvider> getProviders(RegistryEntry<Biome> biome, DynamicRegistryManager manager) {
        RegistryKey<Biome> key = biome.getKey().orElse(null);
        if (key == null) {
            return Collections.emptyList();
        }

        return this.biomeProviderCache.computeIfAbsent(
                key,
                k -> {
                    List<EnvironmentProvider> providers = manager.getOrThrow(ThermooRegistryKeys.ENVIRONMENT)
                            .stream()
                            .filter(definition -> definition.biomes().contains(biome))
                            .map(EnvironmentDefinition::provider)
                            .toList();
                    if (Thermoo.LOGGER.isDebugEnabled()) {
                        Thermoo.LOGGER.debug("Found {} providers for biome {}", providers.size(), k);
                    }
                    return providers;
                }
        );
    }

    private void clearCache() {
        this.biomeProviderCache.clear();
        Thermoo.LOGGER.info("Environment lookup cache cleared");
    }
}
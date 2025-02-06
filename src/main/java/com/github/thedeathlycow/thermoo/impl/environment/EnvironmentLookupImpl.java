package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class EnvironmentLookupImpl implements EnvironmentLookup {
    public static final EnvironmentLookupImpl INSTANCE = new EnvironmentLookupImpl();

    private final Map<RegistryKey<Biome>, List<RegistryEntry<EnvironmentProvider>>> biomeProviderCache = new IdentityHashMap<>();

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> INSTANCE.clearCache());
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> INSTANCE.clearCache());
    }

    @Override
    public ComponentMap lookupCurrentEnvironmentParameters(World world, BlockPos pos) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return this.findCurrentComponentsForBiome(world, pos, biome);
    }

    public double findTemperature(World world, BlockPos pos, TemperatureUnit unit) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return this.findTemperatureForBiome(world, pos, unit, biome);
    }

    public double findRelativeHumidity(World world, BlockPos pos) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return this.findRelativeHumidityForBiome(world, pos, biome);
    }

    public ComponentMap findCurrentComponentsForBiome(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ComponentMap.Builder builder = ComponentMap.builder();
        for (RegistryEntry<EnvironmentProvider> provider : this.getProviders(biome, world.getRegistryManager())) {
            builder.addAll(provider.value().findCurrentComponents(world, pos, biome));
        }
        return builder.build();
    }

    public double findTemperatureForBiome(World world, BlockPos pos, TemperatureUnit unit, RegistryEntry<Biome> biome) {
        ComponentMap components = this.findCurrentComponentsForBiome(world, pos, biome);

        TemperatureRecordComponent temperature = components.getOrDefault(
                EnvironmentComponentTypes.TEMPERATURE,
                TemperatureRecordComponent.DEFAULT
        );

        return unit.convertTemperature(temperature.value());
    }

    public double findRelativeHumidityForBiome(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ComponentMap components = this.findCurrentComponentsForBiome(world, pos, biome);
        return components.getOrDefault(
                EnvironmentComponentTypes.RELATIVE_HUMIDITY,
                RelativeHumidityComponent.DEFAULT
        );
    }

    private List<RegistryEntry<EnvironmentProvider>> getProviders(RegistryEntry<Biome> biome, DynamicRegistryManager manager) {
        RegistryKey<Biome> key = biome.getKey().orElse(null);
        if (key == null) {
            return Collections.emptyList();
        }

        return this.biomeProviderCache.computeIfAbsent(
                key,
                k -> {
                    List<RegistryEntry<EnvironmentProvider>> providers = manager.getOrThrow(ThermooRegistryKeys.ENVIRONMENT)
                            .stream()
                            .filter(definition -> definition.providesFor(biome))
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
        Thermoo.LOGGER.debug("Environment lookup cache cleared");
    }
}
package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
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
    public ComponentMap findEnvironmentComponents(World world, BlockPos pos) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return this.findEnvironmentComponentsForBiome(world, pos, biome);
    }

    public ComponentMap findEnvironmentComponentsForBiome(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ComponentMap.Builder builder = ComponentMap.builder();
        for (RegistryEntry<EnvironmentProvider> provider : this.getProviders(biome, world.getRegistryManager())) {
            // create a new reduction builder for each biome's provider
            ReducibleComponentMapBuilder reduceBuilder = ReducibleComponentMapBuilder.create();
            provider.value().buildCurrentComponents(world, pos, biome, reduceBuilder);
            builder.addAll(reduceBuilder.build());
        }
        return builder.build();
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
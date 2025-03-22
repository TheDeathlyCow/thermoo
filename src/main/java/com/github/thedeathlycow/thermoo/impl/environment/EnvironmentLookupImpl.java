package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class EnvironmentLookupImpl implements EnvironmentLookup {
    public static final EnvironmentLookupImpl INSTANCE = new EnvironmentLookupImpl();

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(INSTANCE::addProvidersToBiomes);
    }

    @Override
    public ComponentMap findEnvironmentComponents(World world, BlockPos pos) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return this.findEnvironmentComponentsForBiome(world, pos, biome);
    }

    public ComponentMap findEnvironmentComponentsForBiome(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ComponentMap.Builder builder = ComponentMap.builder();
        for (EnvironmentProvider provider : this.getProviders(biome)) {
            provider.buildCurrentComponents(world, pos, biome, builder);
        }
        return builder.build();
    }

    private List<EnvironmentProvider> getProviders(RegistryEntry<Biome> biomeEntry) {
        Biome biome = biomeEntry.value();
        return ((ThermooBiome) (Object) biome).thermoo$getEnvironmentProviders();
    }

    private void addProvidersToBiomes(MinecraftServer server) {
        Registry<EnvironmentDefinition> envRegistry = server.getRegistryManager().get(ThermooRegistryKeys.ENVIRONMENT);
        Registry<Biome> biomeRegistry = server.getRegistryManager().get(RegistryKeys.BIOME);

        biomeRegistry.streamEntries().forEach(entry -> {
            List<EnvironmentProvider> providers = this.getAllMatchingProviders(entry, envRegistry);

            ThermooBiome extendedBiome = ((ThermooBiome) (Object) entry.value());
            extendedBiome.thermoo$replaceProviders(providers);

            if (Thermoo.LOGGER.isDebugEnabled()) {
                Thermoo.LOGGER.debug("Found {} providers for {}.", providers.size(), entry.registryKey().getValue());
            }
        });
    }

    private List<EnvironmentProvider> getAllMatchingProviders(RegistryEntry<Biome> biome, Registry<EnvironmentDefinition> envRegistry) {
        return envRegistry.stream()
                .filter(entry -> entry.providesFor(biome))
                .map(env -> env.provider().value())
                .toList();
    }
}
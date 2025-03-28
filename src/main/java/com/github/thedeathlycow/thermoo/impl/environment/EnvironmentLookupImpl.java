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
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
        for (RegistryEntry<EnvironmentProvider> provider : this.getProviders(biome)) {
            provider.value().buildCurrentComponents(world, pos, biome, builder);
        }
        return builder.build();
    }

    private List<RegistryEntry<EnvironmentProvider>> getProviders(RegistryEntry<Biome> biomeEntry) {
        Biome biome = biomeEntry.value();
        return ((ThermooBiome) (Object) biome).thermoo$getEnvironmentProviders();
    }

    private void addProvidersToBiomes(MinecraftServer server) {
        Registry<EnvironmentDefinition> envRegistry = server.getRegistryManager().get(ThermooRegistryKeys.ENVIRONMENT);
        Registry<Biome> biomeRegistry = server.getRegistryManager().get(RegistryKeys.BIOME);

        biomeRegistry.streamEntries().forEach(entry -> {
            List<RegistryEntry<EnvironmentProvider>> providers = getAllMatchingEnvironments(entry, envRegistry)
                    .map(EnvironmentDefinition::provider)
                    .toList();

            Biome biome = entry.value();
            Objects.requireNonNull(biome);
            ThermooBiome extendedBiome = ((ThermooBiome) (Object) biome);
            extendedBiome.thermoo$replaceProviders(providers);

            if (Thermoo.LOGGER.isDebugEnabled()) {
                Thermoo.LOGGER.debug("Found {} providers for {}.", providers.size(), entry.registryKey().getValue());
            }
        });
    }

    @VisibleForTesting
    public static Stream<EnvironmentDefinition> getAllMatchingEnvironments(RegistryEntry<Biome> biome, Registry<EnvironmentDefinition> envRegistry) {
        return envRegistry.stream()
                .filter(entry -> entry.providesFor(biome))
                .sorted(Comparator.comparingInt(EnvironmentDefinition::priority).reversed());
    }
}
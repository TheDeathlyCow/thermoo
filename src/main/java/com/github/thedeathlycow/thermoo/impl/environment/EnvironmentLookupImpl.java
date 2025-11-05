package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentLookup;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
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
    public DataComponentMap findEnvironmentComponents(Level world, BlockPos pos) {
        Holder<Biome> biome = world.getBiome(pos);
        return this.findEnvironmentComponentsForBiome(world, pos, biome);
    }

    public DataComponentMap findEnvironmentComponentsForBiome(Level world, BlockPos pos, Holder<Biome> biome) {
        DataComponentMap.Builder builder = DataComponentMap.builder();
        for (Holder<EnvironmentProvider> provider : this.getProviders(biome)) {
            provider.value().buildCurrentComponents(world, pos, biome, builder);
        }
        return builder.build();
    }

    private List<Holder<EnvironmentProvider>> getProviders(Holder<Biome> biomeEntry) {
        Biome biome = biomeEntry.value();
        return ((ThermooBiome) (Object) biome).thermoo$getEnvironmentProviders();
    }

    private void addProvidersToBiomes(MinecraftServer server) {
        Registry<EnvironmentDefinition> envRegistry = server.registryAccess().registryOrThrow(ThermooRegistryKeys.ENVIRONMENT);
        Registry<Biome> biomeRegistry = server.registryAccess().registryOrThrow(Registries.BIOME);

        biomeRegistry.holders().forEach(entry -> {
            List<Holder<EnvironmentProvider>> providers = getAllMatchingEnvironments(entry, envRegistry)
                    .map(EnvironmentDefinition::provider)
                    .toList();

            Biome biome = entry.value();
            Objects.requireNonNull(biome);
            ThermooBiome extendedBiome = ((ThermooBiome) (Object) biome);
            extendedBiome.thermoo$replaceProviders(providers);

            if (Thermoo.LOGGER.isDebugEnabled()) {
                Thermoo.LOGGER.debug("Found {} providers for {}.", providers.size(), entry.key().location());
            }
        });
    }

    @VisibleForTesting
    public static Stream<EnvironmentDefinition> getAllMatchingEnvironments(Holder<Biome> biome, Registry<EnvironmentDefinition> envRegistry) {
        return envRegistry.stream()
                .filter(entry -> entry.providesFor(biome))
                .sorted(Comparator.comparingInt(EnvironmentDefinition::priority).reversed());
    }
}
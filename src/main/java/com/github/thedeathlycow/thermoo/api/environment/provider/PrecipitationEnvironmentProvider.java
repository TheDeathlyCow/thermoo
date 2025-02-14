package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Map;

public final class PrecipitationEnvironmentProvider implements EnvironmentProvider {
    private final Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> precipitation;

    public PrecipitationEnvironmentProvider(Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> precipitation) {
        this.precipitation = precipitation;
    }

    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        Biome.Precipitation localPrecipitation = biome.value().getPrecipitation(pos, world.getSeaLevel());
        RegistryEntry<EnvironmentProvider> provider = this.precipitation.get(localPrecipitation);
        if (provider != null) {
            provider.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<PrecipitationEnvironmentProvider> getType() {
        return null;
    }
}
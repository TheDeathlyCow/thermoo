package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

public abstract class EnvironmentProvider {

    private final RegistryEntryList<Biome> biomes;

    public EnvironmentProvider(RegistryEntryList<Biome> biomes) {
        this.biomes = biomes;
    }

    public final double getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return getTemperature(world, pos, biome, TemperatureUnit.CELSIUS);
    }

    public abstract double getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome, TemperatureUnit unit);

    public abstract double getHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome);

    public final RegistryEntryList<Biome> biomes() {

    }
}
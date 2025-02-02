package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.Codec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public abstract class EnvironmentProvider {
    public static final Codec<EnvironmentProvider> PROVIDER_CODEC = ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE.getCodec()
            .dispatch("type", EnvironmentProvider::getType, EnvironmentProviderType::codec);

    public abstract TemperatureRecord getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome);

    public abstract double getRelativeHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome);

    public abstract EnvironmentProviderType<?> getType();
}
package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Provides the temperature and relative humidity of a position in a biome.
 */
public interface EnvironmentProvider {
    Codec<EnvironmentProvider> PROVIDER_CODEC = ThermooRegistries.ENVIRONMENT_PROVIDER_TYPE.getCodec()
            .dispatch("type", EnvironmentProvider::getType, EnvironmentProviderType::codec);

    ComponentMap lookup(World world, BlockPos pos, RegistryEntry<Biome> biome);

    /**
     * Gets the temperature of a position within a biome. The returned record may be in any unit.
     * <p>
     * If multiple providers return a temperature record at the same time and position, the mean temperature will be used.
     * <p>
     * If no providers return a value, a fallback room temperature will be used.
     * <p>
     * The temperature may change over time.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns an optional temperature record. If no record is returned, it will not be counted towards the mean
     * temperature.
     */
    default Optional<TemperatureRecord> getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return Optional.empty();
    }

    /**
     * Gets the relative humidity of a position within a biome.
     * <p>
     * Relative humidity is "the ratio of how much water vapour is in the air to how much water vapour the air could
     * potentially contain" <a href="https://en.m.wikipedia.org/wiki/Humidity#Relative_humidity">[1]</a> and is
     * expressed here on a 0-1 scale.
     * <p>
     * If multiple providers return a relative humidity value at the same time and position, the mean value will be used.
     * <p>
     * The relative humidity may change over time.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns an optional relative humidity value as a 0-1 double. If no humidity is returned, it will not be
     * counted towards the mean relative humidity.
     * @see <a href="https://en.m.wikipedia.org/wiki/Humidity">Humidity on Wikipedia</a>
     */
    default OptionalDouble getRelativeHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return OptionalDouble.empty();
    }

    /**
     * @return Returns the type of this provider for dispatch
     */
    EnvironmentProviderType<?> getType();
}
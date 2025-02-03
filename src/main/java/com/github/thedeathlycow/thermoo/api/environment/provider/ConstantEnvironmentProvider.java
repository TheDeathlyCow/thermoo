package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;
import java.util.OptionalDouble;

public class ConstantEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<ConstantEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    TemperatureRecord.CODEC
                            .fieldOf("temperature")
                            .forGetter(ConstantEnvironmentProvider::temperature),
                    Codec.doubleRange(0, 1)
                            .fieldOf("relative_humidity")
                            .forGetter(ConstantEnvironmentProvider::relativeHumidity)
            ).apply(instance, ConstantEnvironmentProvider::new)
    );

    private final TemperatureRecord temperature;
    private final double relativeHumidity;

    public ConstantEnvironmentProvider(TemperatureRecord temperature, double relativeHumidity) {
        this.temperature = temperature;
        this.relativeHumidity = relativeHumidity;
    }

    @Override
    public Optional<TemperatureRecord> getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return Optional.ofNullable(this.temperature);
    }

    @Override
    public OptionalDouble getRelativeHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return OptionalDouble.of(this.relativeHumidity);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.CONSTANT;
    }

    public TemperatureRecord temperature() {
        return temperature;
    }

    public double relativeHumidity() {
        return relativeHumidity;
    }
}
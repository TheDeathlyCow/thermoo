package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Provides constant environment values
 */
public final class ConstantEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<ConstantEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    TemperatureRecord.CODEC
                            .optionalFieldOf("temperature")
                            .forGetter(ConstantEnvironmentProvider::temperature),
                    Codec.doubleRange(0, 1)
                            .optionalFieldOf("relative_humidity")
                            .forGetter(ConstantEnvironmentProvider::relativeHumidity)
            ).apply(instance, ConstantEnvironmentProvider::new)
    );

    private final Optional<TemperatureRecord> temperature;
    private final Optional<Double> relativeHumidity;

    private ConstantEnvironmentProvider(Optional<TemperatureRecord> temperature, Optional<Double> relativeHumidity) {
        this.temperature = temperature;
        this.relativeHumidity = relativeHumidity;
    }

    /**
     * Returns a constant value for temperature, if present.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns the value stored in {@link #temperature()}
     */
    @Override
    public Optional<TemperatureRecord> getTemperature(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return this.temperature;
    }

    /**
     * Returns a constant value for relative humidity, if present.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns the value stored in {@link #relativeHumidity()}
     */
    @Override
    public OptionalDouble getRelativeHumidity(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return this.relativeHumidity.map(OptionalDouble::of)
                .orElseGet(OptionalDouble::empty);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.CONSTANT;
    }

    /**
     * @return Returns the constant
     * {@linkplain EnvironmentProvider#getTemperature(World, BlockPos, RegistryEntry) temperature} value.
     */
    public Optional<TemperatureRecord> temperature() {
        return temperature;
    }

    /**
     * @return Returns the constant
     * {@linkplain EnvironmentProvider#getRelativeHumidity(World, BlockPos, RegistryEntry) relative humidity} value.
     */
    public Optional<Double> relativeHumidity() {
        return this.relativeHumidity;
    }

    /**
     * @return Returns a new {@link Builder}
     */
    @Contract("->new")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for a constant temperature provider. By default, all values are empty.
     */
    public static final class Builder {
        @Nullable
        private TemperatureRecord temperature = null;

        @Nullable
        private Double relativeHumidity = null;

        private Builder() {

        }

        @Contract("_->this")
        public Builder withTemperature(@NotNull TemperatureRecord temperatureRecord) {
            Objects.requireNonNull(temperatureRecord);
            this.temperature = temperatureRecord;
            return this;
        }

        @Contract("_->this")
        public Builder withRelativeHumidity(double relativeHumidity) {
            this.relativeHumidity = relativeHumidity;
            return this;
        }

        @Contract("->new")
        public ConstantEnvironmentProvider build() {
            return new ConstantEnvironmentProvider(
                    Optional.ofNullable(this.temperature),
                    Optional.ofNullable(this.relativeHumidity)
            );
        }
    }
}
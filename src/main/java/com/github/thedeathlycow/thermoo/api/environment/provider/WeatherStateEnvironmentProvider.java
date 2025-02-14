package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * An environment provider that delegates to a child provider based on the global weather state of a world (clear, rain,
 * or thunder).
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class WeatherStateEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<WeatherStateEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnvironmentProvider.ENTRY_CODEC
                            .optionalFieldOf("clear")
                            .forGetter(WeatherStateEnvironmentProvider::clear),
                    EnvironmentProvider.ENTRY_CODEC
                            .optionalFieldOf("rain")
                            .forGetter(WeatherStateEnvironmentProvider::rain),
                    EnvironmentProvider.ENTRY_CODEC
                            .optionalFieldOf("thunder")
                            .forGetter(WeatherStateEnvironmentProvider::thunder)
            ).apply(instance, WeatherStateEnvironmentProvider::new)
    );

    private final Optional<RegistryEntry<EnvironmentProvider>> clear;
    private final Optional<RegistryEntry<EnvironmentProvider>> rain;
    private final Optional<RegistryEntry<EnvironmentProvider>> thunder;

    private WeatherStateEnvironmentProvider(
            Optional<RegistryEntry<EnvironmentProvider>> clear,
            Optional<RegistryEntry<EnvironmentProvider>> rain,
            Optional<RegistryEntry<EnvironmentProvider>> thunder
    ) {
        this.clear = clear;
        this.rain = rain;
        this.thunder = thunder;
    }

    /**
     * Creates a new builder
     *
     * @return Returns a new builder instance
     */
    @Contract("->new")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Delegates to a child provider based on the global weather state. If a provider is not defined for the current
     * weather state, then does nothing.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A reducible component map builder to append to
     */
    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        if (world.isThundering()) {
            this.thunder.ifPresent(p -> p.value().buildCurrentComponents(world, pos, biome, builder));
        } else if (world.isRaining()) {
            this.rain.ifPresent(p -> p.value().buildCurrentComponents(world, pos, biome, builder));
        } else {
            this.clear.ifPresent(p -> p.value().buildCurrentComponents(world, pos, biome, builder));
        }
    }

    @Override
    public EnvironmentProviderType<WeatherStateEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.WEATHER_STATE;
    }

    /**
     * Provider to use when the world is neither raining nor thundering
     */
    public Optional<RegistryEntry<EnvironmentProvider>> clear() {
        return clear;
    }

    /**
     * Provider to use when the world is raining but not thundering
     */
    public Optional<RegistryEntry<EnvironmentProvider>> rain() {
        return rain;
    }

    /**
     * Provider to use when the world is thundering
     */
    public Optional<RegistryEntry<EnvironmentProvider>> thunder() {
        return thunder;
    }

    /**
     * Builder for weather state providers. All fields are empty by default.
     */
    public static final class Builder {
        @Nullable
        private RegistryEntry<EnvironmentProvider> clear = null;
        @Nullable
        private RegistryEntry<EnvironmentProvider> rain = null;
        @Nullable
        private RegistryEntry<EnvironmentProvider> thunder = null;

        private Builder() {

        }

        /**
         * Provider to use when the world is not raining or thundering
         *
         * @param clear A non-null registry entry
         * @return Returns this builder
         */
        @Contract("_->this")
        public Builder withClear(RegistryEntry<EnvironmentProvider> clear) {
            Objects.requireNonNull(clear);
            this.clear = clear;
            return this;
        }

        /**
         * Provider to use when the world is raining but not thundering
         *
         * @param rain A non-null registry entry
         * @return Returns this builder
         */
        @Contract("_->this")
        public Builder withRain(RegistryEntry<EnvironmentProvider> rain) {
            Objects.requireNonNull(rain);
            this.rain = rain;
            return this;
        }

        /**
         * Provider to use when the world is raining thundering
         *
         * @param thunder A non-null registry entry
         * @return Returns this builder
         */
        @Contract("_->this")
        public Builder withThunder(RegistryEntry<EnvironmentProvider> thunder) {
            Objects.requireNonNull(thunder);
            this.thunder = thunder;
            return this;
        }

        /**
         * @return Returns a new weather state provider from this current's current state
         */
        @Contract("->new")
        public WeatherStateEnvironmentProvider build() {
            return new WeatherStateEnvironmentProvider(
                    Optional.ofNullable(this.clear),
                    Optional.ofNullable(this.rain),
                    Optional.ofNullable(this.thunder)
            );
        }
    }
}
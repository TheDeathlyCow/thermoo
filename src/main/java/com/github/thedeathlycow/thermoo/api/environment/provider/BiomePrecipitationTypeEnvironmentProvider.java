package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * A provider that delegates to a child provider based on the precipitation-type of a biome. At least one precipitation
 * type provider must be given.
 */
public final class BiomePrecipitationTypeEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<BiomePrecipitationTypeEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createPrecipitationMapCodec()
                            .fieldOf("precipitation_type")
                            .forGetter(BiomePrecipitationTypeEnvironmentProvider::precipitationType)
            ).apply(instance, BiomePrecipitationTypeEnvironmentProvider::new)
    );

    private final Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> precipitationTypeMap;

    private BiomePrecipitationTypeEnvironmentProvider(Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> precipitationTypeMap) {
        this.precipitationTypeMap = new EnumMap<>(Biome.Precipitation.class);
        this.precipitationTypeMap.putAll(precipitationTypeMap);
    }

    /**
     * Delegates to a child provider based on the local precipitation type.
     * <p>
     * <strong>IMPORTANT:</strong> This is not based on current weather state. For example, snowy biomes will ALWAYS
     * return the provider mapped to {@link Biome.Precipitation#SNOW}, even when it is not snowing.
     * <p>
     * If no provider is mapped to the local precipitation type, then nothing is built.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A reducible component map builder to append to
     */
    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        Biome.Precipitation biomePrecipitationType = biome.value().getPrecipitation(pos);
        RegistryEntry<EnvironmentProvider> provider = this.precipitationTypeMap.get(biomePrecipitationType);
        if (provider != null) {
            provider.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<BiomePrecipitationTypeEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.PRECIPITATION_TYPE;
    }

    /**
     * Maps that is used to choose the child provider to delegate to based on local precipitation
     *
     * @return Returns an unmodifiable enum map
     */
    public Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> precipitationType() {
        return Collections.unmodifiableMap(precipitationTypeMap);
    }

    private static MapCodec<Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>>> createPrecipitationMapCodec() {
        return Codec.simpleMap(
                Biome.Precipitation.CODEC,
                EnvironmentProvider.ENTRY_CODEC,
                StringIdentifiable.toKeyable(Biome.Precipitation.values())
        ).validate(map -> {
            if (map.isEmpty()) {
                return DataResult.error(() -> "No precipitation key in: " + map);
            } else {
                return DataResult.success(map);
            }
        });
    }

    /**
     * A builder for local precipitation environment providers.
     */
    public static final class Builder {
        private final Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> precipitationMap = new EnumMap<>(Biome.Precipitation.class);

        private Builder() {

        }

        /**
         * Registers a child provider to a precipitation type.
         *
         * @param precipitation Precipitation type to add
         * @param child         The child for the precipitation type
         * @return Returns this builder
         */
        @Contract("_,_->this")
        public Builder addChild(Biome.Precipitation precipitation, RegistryEntry<EnvironmentProvider> child) {
            Objects.requireNonNull(precipitation);
            Objects.requireNonNull(child);

            this.precipitationMap.put(precipitation, child);

            return this;
        }

        /**
         * Builds into a new provider. At least one precipitation-child relationship must have been defined.
         *
         * @return Returns a new provider from this builder's state
         */
        @Contract("->new")
        public BiomePrecipitationTypeEnvironmentProvider build() {
            if (this.precipitationMap.keySet().isEmpty()) {
                throw new IllegalArgumentException("Precipitation map requires at least one key!");
            }

            return new BiomePrecipitationTypeEnvironmentProvider(this.precipitationMap);
        }
    }
}
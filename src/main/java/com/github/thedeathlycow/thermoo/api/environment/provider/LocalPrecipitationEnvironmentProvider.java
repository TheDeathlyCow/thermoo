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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * A provider that delegates to a child provider based on the local precipitation state of a world position. At least
 * one precipitation type provider must be given.
 */
public final class LocalPrecipitationEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<LocalPrecipitationEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createPrecipitationMapCodec()
                            .fieldOf("local_precipitation")
                            .forGetter(LocalPrecipitationEnvironmentProvider::localPrecipitation)
            ).apply(instance, LocalPrecipitationEnvironmentProvider::new)
    );

    private final Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> localPrecipitationMap;

    private LocalPrecipitationEnvironmentProvider(Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> localPrecipitationMap) {
        this.localPrecipitationMap = new EnumMap<>(Biome.Precipitation.class);
        this.localPrecipitationMap.putAll(localPrecipitationMap);
    }

    /**
     * Delegates to a child provider based on the local precipitation type. This is local, so if for example the
     * position is under a roof then the precipitation will be {@link Biome.Precipitation#NONE}, regardless of global
     * weather state.
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
        Biome.Precipitation localPrecipitation = biome.value().getPrecipitation(pos, world.getSeaLevel());
        RegistryEntry<EnvironmentProvider> provider = this.localPrecipitationMap.get(localPrecipitation);
        if (provider != null) {
            provider.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<LocalPrecipitationEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.LOCAL_PRECIPITATION;
    }

    /**
     * Maps that is used to choose the child provider to delegate to based on local precipitation
     *
     * @return Returns an unmodifiable enum map
     */
    public Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> localPrecipitation() {
        return Collections.unmodifiableMap(localPrecipitationMap);
    }

    private static MapCodec<Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>>> createPrecipitationMapCodec() {
        return Codec.simpleMap(
                Biome.Precipitation.CODEC,
                EnvironmentProvider.ENTRY_CODEC,
                StringIdentifiable.toKeyable(Biome.Precipitation.values())
        ).validate(seasonMap -> {
            if (seasonMap.isEmpty()) {
                return DataResult.error(() -> "No season key in: " + seasonMap);
            } else {
                return DataResult.success(seasonMap);
            }
        });
    }
}
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

import java.util.Map;

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
        this.localPrecipitationMap = localPrecipitationMap;
    }

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

    public Map<Biome.Precipitation, RegistryEntry<EnvironmentProvider>> localPrecipitation() {
        return localPrecipitationMap;
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
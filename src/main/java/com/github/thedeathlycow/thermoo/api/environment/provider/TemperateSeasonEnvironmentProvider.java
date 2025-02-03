package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

/**
 * A seasonal environment provider for the temperate seasons (spring, summer, autumn, and winter).
 */
public final class TemperateSeasonEnvironmentProvider extends SeasonalEnvironmentProvider {
    public static final MapCodec<TemperateSeasonEnvironmentProvider> CODEC = validate(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            ThermooSeason.CODEC
                                    .optionalFieldOf("fallback_season")
                                    .forGetter(TemperateSeasonEnvironmentProvider::fallbackSeason),
                            SeasonalEnvironmentProvider.createSeasonMapCodec()
                                    .validate(TemperateSeasonEnvironmentProvider::allKeysAreTemperate)
                                    .fieldOf("seasons")
                                    .forGetter(TemperateSeasonEnvironmentProvider::seasons)
                    ).apply(instance, TemperateSeasonEnvironmentProvider::new)
            )
    );

    private TemperateSeasonEnvironmentProvider(Optional<ThermooSeason> fallbackSeason, Map<ThermooSeason, EnvironmentProvider> seasons) {
        super(fallbackSeason, seasons);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.TEMPERATE_SEASONAL;
    }

    @Override
    protected Optional<ThermooSeason> getCurrentSeason(World world, BlockPos pos) {
        return ThermooSeason.getCurrentSeason(world);
    }

    private static DataResult<Map<ThermooSeason, EnvironmentProvider>> allKeysAreTemperate(Map<ThermooSeason, EnvironmentProvider> seasonMap) {
        for (ThermooSeason season : seasonMap.keySet()) {
            if (season.isTropical()) {
                return DataResult.error(() -> "Found tropical season '" + season.name() + "' in a temperate season map!");
            }
        }
        return DataResult.success(seasonMap);
    }
}
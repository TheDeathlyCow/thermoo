package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;
import java.util.Optional;

public final class TemperateSeasonEnvironmentProvider extends SeasonalEnvironmentProvider {
    public static final MapCodec<TemperateSeasonEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ThermooSeason.CODEC
                            .fieldOf("fallback_season")
                            .forGetter(TemperateSeasonEnvironmentProvider::fallbackSeason),
                    SeasonalEnvironmentProvider.createSeasonMapCodec()
                            .validate(TemperateSeasonEnvironmentProvider::allKeysAreTemperate)
                            .fieldOf("seasons")
                            .forGetter(TemperateSeasonEnvironmentProvider::seasons)
            ).apply(instance, TemperateSeasonEnvironmentProvider::new)
    );

    public TemperateSeasonEnvironmentProvider(ThermooSeason fallbackSeason, Map<ThermooSeason, EnvironmentProvider> seasons) {
        super(fallbackSeason, seasons);
    }

    @Override
    protected Optional<EnvironmentProvider> getForSeason(ThermooSeason season) {
        if (season.isTropical()) {
            return Optional.empty();
        }
        return super.getForSeason(season);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.TEMPERATE_SEASONAL;
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
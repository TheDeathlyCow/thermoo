package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;
import java.util.Optional;

public class TropicalSeasonEnvironmentProvider extends SeasonalEnvironmentProvider {
    public static final MapCodec<TropicalSeasonEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ThermooSeason.CODEC
                            .optionalFieldOf("fallback_season")
                            .forGetter(TropicalSeasonEnvironmentProvider::fallbackSeason),
                    SeasonalEnvironmentProvider.createSeasonMapCodec()
                            .validate(TropicalSeasonEnvironmentProvider::allKeysAreTropical)
                            .fieldOf("seasons")
                            .forGetter(TropicalSeasonEnvironmentProvider::seasons)
            ).apply(instance, TropicalSeasonEnvironmentProvider::new)
    );

    public TropicalSeasonEnvironmentProvider(Optional<ThermooSeason> fallbackSeason, Map<ThermooSeason, EnvironmentProvider> seasons) {
        super(fallbackSeason, seasons);
    }

    @Override
    protected Optional<EnvironmentProvider> getForSeason(ThermooSeason season) {
        if (season.isTropical()) {
            return super.getForSeason(season);
        }
        return Optional.empty();
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.TROPICAL_SEASONAL;
    }

    private static DataResult<Map<ThermooSeason, EnvironmentProvider>> allKeysAreTropical(Map<ThermooSeason, EnvironmentProvider> seasonMap) {
        for (ThermooSeason season : seasonMap.keySet()) {
            if (!season.isTropical()) {
                return DataResult.error(() -> "Found temperate season '" + season.name() + "' in a tropical season map!");
            }
        }
        return DataResult.success(seasonMap);
    }
}
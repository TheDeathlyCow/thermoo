package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;
import java.util.Optional;

public class TropicalSeasonEnvironmentProvider extends SeasonalEnvironmentProvider {
    public static final MapCodec<TropicalSeasonEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnvironmentProvider.PROVIDER_CODEC
                            .fieldOf("fallback")
                            .forGetter(TropicalSeasonEnvironmentProvider::fallback),
                    Codec.simpleMap(ThermooSeason.CODEC, EnvironmentProvider.PROVIDER_CODEC, StringIdentifiable.toKeyable(ThermooSeason.values()))
                            .validate(TropicalSeasonEnvironmentProvider::allKeysAreTropical)
                            .fieldOf("seasons")
                            .forGetter(TropicalSeasonEnvironmentProvider::seasons)
            ).apply(instance, TropicalSeasonEnvironmentProvider::new)
    );

    public TropicalSeasonEnvironmentProvider(EnvironmentProvider fallback, Map<ThermooSeason, EnvironmentProvider> seasons) {
        super(fallback, seasons);
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
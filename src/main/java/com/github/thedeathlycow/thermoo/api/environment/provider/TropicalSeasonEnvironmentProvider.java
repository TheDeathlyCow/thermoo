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
 * A seasonal environment provider for the tropical seasons (wet and dry).
 */
public final class TropicalSeasonEnvironmentProvider extends SeasonalEnvironmentProvider {
    public static final MapCodec<TropicalSeasonEnvironmentProvider> CODEC = validate(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            ThermooSeason.CODEC
                                    .optionalFieldOf("fallback_season")
                                    .forGetter(TropicalSeasonEnvironmentProvider::fallbackSeason),
                            SeasonalEnvironmentProvider.createSeasonMapCodec()
                                    .validate(TropicalSeasonEnvironmentProvider::allKeysAreTropical)
                                    .fieldOf("seasons")
                                    .forGetter(TropicalSeasonEnvironmentProvider::seasons)
                    ).apply(instance, TropicalSeasonEnvironmentProvider::new)
            )
    );

    private TropicalSeasonEnvironmentProvider(Optional<ThermooSeason> fallbackSeason, Map<ThermooSeason, EnvironmentProvider> seasons) {
        super(fallbackSeason, seasons);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.TROPICAL_SEASONAL;
    }

    @Override
    protected Optional<ThermooSeason> getCurrentSeason(World world, BlockPos pos) {
        return ThermooSeason.getCurrentTropicalSeason(world, pos);
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
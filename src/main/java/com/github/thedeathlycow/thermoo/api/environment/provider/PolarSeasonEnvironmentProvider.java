package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.season.PolarSeason;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;

public final class PolarSeasonEnvironmentProvider extends SeasonalEnvironmentProvider<PolarSeason> {
    public static final MapCodec<PolarSeasonEnvironmentProvider> CODEC = validate(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            PolarSeason.CODEC
                                    .optionalFieldOf("fallback_season")
                                    .forGetter(PolarSeasonEnvironmentProvider::fallbackSeason),
                            SeasonalEnvironmentProvider.createSeasonMapCodec(PolarSeason.CODEC, PolarSeason.values())
                                    .fieldOf("seasons")
                                    .forGetter(PolarSeasonEnvironmentProvider::seasons)
                    ).apply(instance, PolarSeasonEnvironmentProvider::new)
            )
    );

    private PolarSeasonEnvironmentProvider(
            Optional<PolarSeason> fallbackSeason,
            Map<PolarSeason, Holder<EnvironmentProvider>> seasons
    ) {
        super(fallbackSeason, seasons, PolarSeason.class);
    }

    @Override
    public EnvironmentProviderType<PolarSeasonEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.POLAR_SEASONAL;
    }

    @Override
    protected Optional<PolarSeason> getCurrentSeason(Level level, BlockPos pos) {
        return PolarSeason.getCurrentSeason(level, pos);
    }
}
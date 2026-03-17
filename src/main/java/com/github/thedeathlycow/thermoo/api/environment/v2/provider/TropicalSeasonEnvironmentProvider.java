package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonState;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.impl.environment.SeasonalProviderBuilderHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A seasonal environment provider for the tropical seasons (wet, dry, and mild).
 */
public final class TropicalSeasonEnvironmentProvider extends SeasonalEnvironmentProvider<TropicalSeason> {
    public static final MapCodec<TropicalSeasonEnvironmentProvider> CODEC = validate(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            TropicalSeason.CODEC
                                    .optionalFieldOf("fallback_season")
                                    .forGetter(TropicalSeasonEnvironmentProvider::fallbackSeason),
                            SeasonalEnvironmentProvider.createSeasonMapCodec(TropicalSeason.CODEC, TropicalSeason.values())
                                    .fieldOf("seasons")
                                    .forGetter(TropicalSeasonEnvironmentProvider::seasons)
                    ).apply(instance, TropicalSeasonEnvironmentProvider::new)
            )
    );

    /**
     * @return Returns a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    private TropicalSeasonEnvironmentProvider(
            Optional<TropicalSeason> fallbackSeason,
            Map<TropicalSeason, Holder<EnvironmentProvider>> seasons
    ) {
        super(fallbackSeason, seasons, TropicalSeason.class);
    }

    @Override
    public EnvironmentProviderType<TropicalSeasonEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.TROPICAL_SEASONAL;
    }

    @Override
    protected Optional<TropicalSeason> getCurrentSeason(Level level, BlockPos pos) {
        return TropicalSeason.getCurrentState(level, pos).map(ThermooSeasonState::season);
    }

    /**
     * Builder for tropical season providers. By default, there is no fallback season and the seasons map is empty.
     */
    public static final class Builder {
        private final SeasonalProviderBuilderHelper<TropicalSeason> helper = new SeasonalProviderBuilderHelper<>(TropicalSeason.class);

        private Builder() {

        }

        /**
         * Adds a fallback season. If a fallback season is already provided, it will be overwritten.
         *
         * @param season A non-null tropical season to add as fallback.
         * @return Returns this builder
         */
        public Builder withFallbackSeason(@NotNull TropicalSeason season) {
            Objects.requireNonNull(season);
            this.helper.setFallbackSeason(season);
            return this;
        }

        /**
         * Sets the provider for a season. If a provider is already mapped to the given season, it will be overwritten.
         *
         * @param season   A non-null tropical season to add a provider for
         * @param provider A non-null provider to add
         * @return Returns this builder
         */
        public Builder addSeasonProvider(@NotNull TropicalSeason season, @NotNull Holder<EnvironmentProvider> provider) {
            Objects.requireNonNull(season);
            this.helper.setSeasonProvider(season, provider);
            return this;
        }

        /**
         * Builds a new provider from this builder. The provider must have a non-empty seasons map, and if a fallback season
         * is provided then it must be a key of the seasons map.
         *
         * @return Returns a new {@link TropicalSeasonEnvironmentProvider}
         * @throws IllegalStateException if this builder cannot build a legal provider
         */
        public TropicalSeasonEnvironmentProvider build() {
            this.helper.validate();
            return new TropicalSeasonEnvironmentProvider(
                    Optional.ofNullable(this.helper.getFallbackSeason()),
                    this.helper.getSeasons()
            );
        }
    }
}
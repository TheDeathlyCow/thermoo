package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonState;
import com.github.thedeathlycow.thermoo.impl.environment.SeasonalProviderBuilderHelper;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A seasonal environment provider for the temperate seasons (spring, summer, autumn, and winter).
 */
public final class TemperateSeasonEnvironmentProvider extends SeasonalEnvironmentProvider<TemperateSeason> {
    public static final MapCodec<TemperateSeasonEnvironmentProvider> CODEC = validate(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            TemperateSeason.CODEC
                                    .optionalFieldOf("fallback_season")
                                    .forGetter(TemperateSeasonEnvironmentProvider::fallbackSeason),
                            SeasonalEnvironmentProvider.createSeasonMapCodec(TemperateSeason.CODEC, TemperateSeason.values())
                                    .fieldOf("seasons")
                                    .forGetter(TemperateSeasonEnvironmentProvider::seasons)
                    ).apply(instance, TemperateSeasonEnvironmentProvider::new)
            )
    );

    /**
     * @return Returns a new {@link Builder}
     */
    @Contract("->new")
    public static Builder builder() {
        return new Builder();
    }

    private TemperateSeasonEnvironmentProvider(
            Optional<TemperateSeason> fallbackSeason,
            Map<TemperateSeason, Holder<EnvironmentProvider>> seasons
    ) {
        super(fallbackSeason, seasons, TemperateSeason.class);
    }

    @Override
    public EnvironmentProviderType<TemperateSeasonEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.TEMPERATE_SEASONAL;
    }

    @Override
    protected Optional<TemperateSeason> getCurrentSeason(Level level, BlockPos pos) {
        return TemperateSeason.getCurrentState(level, pos).map(ThermooSeasonState::season);
    }

    /**
     * Builder for temperate season providers. By default, there is no fallback season and the seasons map is empty.
     */
    public static final class Builder {
        private final SeasonalProviderBuilderHelper<TemperateSeason> helper = new SeasonalProviderBuilderHelper<>(TemperateSeason.class);

        private Builder() {

        }

        /**
         * Adds a fallback season. If a fallback season is already provided, it will be overwritten.
         *
         * @param season A non-null temperate season to add as fallback.
         * @return Returns this builder
         */
        @Contract("_->this")
        public Builder withFallbackSeason(@NotNull TemperateSeason season) {
            Objects.requireNonNull(season);
            this.helper.setFallbackSeason(season);
            return this;
        }

        /**
         * Sets the provider for a season. If a provider is already mapped to the given season, it will be overwritten.
         *
         * @param season   A non-null temperate season to add a provider for
         * @param provider A non-null provider to add
         * @return Returns this builder
         */
        @Contract("_,_->this")
        public Builder addSeasonProvider(@NotNull TemperateSeason season, @NotNull Holder<EnvironmentProvider> provider) {
            Objects.requireNonNull(season);
            this.helper.setSeasonProvider(season, provider);
            return this;
        }

        /**
         * Builds a new provider from this builder. The provider must have a non-empty seasons map, and if a fallback season
         * is provided then it must be a key of the seasons map.
         *
         * @return Returns a new {@link TemperateSeasonEnvironmentProvider}
         * @throws IllegalStateException if this builder cannot build a legal provider
         */
        @Contract("->new")
        public TemperateSeasonEnvironmentProvider build() {
            this.helper.validate();
            return new TemperateSeasonEnvironmentProvider(
                    Optional.ofNullable(this.helper.getFallbackSeason()),
                    this.helper.getSeasons()
            );
        }
    }
}
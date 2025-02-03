package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
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

    /**
     * @return Returns a new {@link Builder}
     */
    @Contract("->new")
    public static Builder builder() {
        return new Builder();
    }

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

    /**
     * Builder for temperate season providers. By default, there is no fallback season and the seasons map is empty.
     */
    public static class Builder {
        private final BuilderHelper helper = new BuilderHelper();

        private Builder() {

        }

        /**
         * Adds a fallback season. If a fallback season is already provided, it will be overwritten.
         *
         * @param season A non-null temperate season to add as fallback.
         * @return Returns this builder
         */
        @Contract("_->this")
        public Builder withFallbackSeason(@NotNull ThermooSeason season) {
            Objects.requireNonNull(season);
            if (!season.isTropical()) {
                this.helper.setFallbackSeason(season);
            }
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
        public Builder addSeasonProvider(@NotNull ThermooSeason season, @NotNull EnvironmentProvider provider) {
            Objects.requireNonNull(season);
            if (!season.isTropical()) {
                this.helper.setSeasonProvider(season, provider);
            }
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
                    Optional.ofNullable(this.helper.fallbackSeason),
                    this.helper.seasons
            );
        }
    }
}
package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Used to pick between two child providers based on a light level threshold. Can filter for {@link LightLayer} and apply
 * or ignore {@link Level#getSkyDarken() ambient darkness} to sky light.
 */
public class LightThresholdLightProvider implements EnvironmentProvider {
    public static final MapCodec<LightThresholdLightProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.stringResolver(
                                    light -> light.name().toLowerCase(),
                                    name -> LightLayer.valueOf(name.toUpperCase())
                            )
                            .optionalFieldOf("light_type")
                            .forGetter(LightThresholdLightProvider::lightLayer),
                    Codec.BOOL
                            .optionalFieldOf("apply_ambient_darkness", true)
                            .forGetter(LightThresholdLightProvider::applyAmbientDarkness),
                    Codec.intRange(0, 15)
                            .fieldOf("threshold")
                            .forGetter(LightThresholdLightProvider::threshold),
                    EnvironmentProvider.HOLDER_CODEC
                            .fieldOf("above")
                            .forGetter(LightThresholdLightProvider::above),
                    EnvironmentProvider.HOLDER_CODEC
                            .fieldOf("below")
                            .forGetter(LightThresholdLightProvider::below)
            ).apply(instance, LightThresholdLightProvider::new)
    );

    private final Optional<LightLayer> lightLayer;
    private final boolean applyAmbientDarkness;
    private final int threshold;
    private final Holder<EnvironmentProvider> above;
    private final Holder<EnvironmentProvider> below;

    /**
     * Creates a new builder with the mandatory threshold, above, and below fields
     *
     * @param threshold The light level threshold - must be between 0 and 15 (inclusive)
     * @param above     The provider to use when a positions light level is at or above the {@code threshold}. Must not be null.
     * @param below     The provider to use when a positions light level is below the {@code threshold}. Must not be null.
     * @return Returns a new builder instance
     */
    @Contract("_,_,_->new")
    public static Builder builder(
            int threshold,
            @NotNull Holder<EnvironmentProvider> above,
            @NotNull Holder<EnvironmentProvider> below
    ) {
        if (threshold < 0 || threshold > 15) {
            throw new IllegalArgumentException("Threshold must be between 0 and 15 but is " + threshold);
        }
        Objects.requireNonNull(above);
        Objects.requireNonNull(below);

        return new Builder(threshold, above, below);
    }

    private LightThresholdLightProvider(
            Optional<LightLayer> lightLayer,
            boolean applyAmbientDarkness,
            int threshold,
            Holder<EnvironmentProvider> above,
            Holder<EnvironmentProvider> below
    ) {
        this.lightLayer = lightLayer;
        this.applyAmbientDarkness = applyAmbientDarkness;
        this.threshold = threshold;
        this.above = above;
        this.below = below;
    }

    /**
     * Builds the current components of the world position based on light level. If the light level of the position is at
     * or above the threshold then uses the {@link #above()} provider. Otherwise, uses the {@link #below()} provider.
     * <p>
     * Filters for sky/block light and ambient darkness if requested.
     *
     * @param level   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder Component map builder to append to
     */
    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        int lightLevel = this.lightLayer
                .map(type -> level.getBrightness(type, pos))
                .orElseGet(() -> level.getMaxLocalRawBrightness(pos));

        if (this.applyAmbientDarkness && this.lightLayer.orElse(null) == LightLayer.SKY) {
            lightLevel -= level.getSkyDarken();
        }

        if (lightLevel >= this.threshold) {
            this.above.value().buildCurrentComponents(level, pos, biome, builder);
        } else {
            this.below.value().buildCurrentComponents(level, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<LightThresholdLightProvider> getType() {
        return EnvironmentProviderTypes.LIGHT_THRESHOLD;
    }

    /**
     * The optional light layer of this provider. If not specified, uses {@link net.minecraft.world.level.LevelReader#getMaxLocalRawBrightness(BlockPos)}
     * to determine light level.
     *
     * @return Returns the light layer of this provider.
     */
    public Optional<LightLayer> lightLayer() {
        return this.lightLayer;
    }

    /**
     * Whether ambient darkness should be applied when using the skylight light type (default: true)
     */
    public boolean applyAmbientDarkness() {
        return this.applyAmbientDarkness;
    }

    /**
     * Light level threshold that determines whether to use {@link #above()} or {@link #below()} when finding the
     * environment components. Must be between 0 and 15 (inclusive).
     */
    public int threshold() {
        return this.threshold;
    }

    /**
     * The provider to use when the light level is at or above the {@link #threshold()}
     */
    public Holder<EnvironmentProvider> above() {
        return this.above;
    }

    /**
     * The provider to use when the light level is below the {@link #threshold()}
     */
    public Holder<EnvironmentProvider> below() {
        return this.below;
    }

    /**
     * Builder class for light threshold providers
     */
    public static final class Builder {
        @Nullable
        private LightLayer lightType = null;
        private boolean applyAmbientDarkness = true;
        private final int threshold;
        private final Holder<EnvironmentProvider> above;
        private final Holder<EnvironmentProvider> below;

        private Builder(int threshold, Holder<EnvironmentProvider> above, Holder<EnvironmentProvider> below) {
            this.threshold = threshold;
            this.above = above;
            this.below = below;
        }

        /**
         * Ignore the ambient darkness of the position when using sky light
         *
         * @return Returns this builder
         */
        @Contract("->this")
        public Builder ignoreAmbientDarkness() {
            this.applyAmbientDarkness = false;
            return this;
        }

        /**
         * Sets a light layer to filter on.
         *
         * @return Returns this builder
         */
        @Contract("_->this")
        public Builder withLightType(LightLayer lightType) {
            this.lightType = lightType;
            return this;
        }

        /**
         * @return Returns a new provider with the parameters of this builder
         */
        @Contract("->new")
        public LightThresholdLightProvider build() {
            return new LightThresholdLightProvider(
                    Optional.ofNullable(this.lightType),
                    this.applyAmbientDarkness,
                    this.threshold,
                    this.above,
                    this.below
            );
        }
    }
}
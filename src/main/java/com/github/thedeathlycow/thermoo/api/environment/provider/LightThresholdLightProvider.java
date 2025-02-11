package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Used to pick between two child providers based on a light level threshold. Can filter for {@link LightType} and apply
 * or ignore {@link World#getAmbientDarkness() ambient darkness} to sky light.
 */
public class LightThresholdLightProvider implements EnvironmentProvider {
    public static final MapCodec<LightThresholdLightProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.stringResolver(
                                    light -> light.name().toLowerCase(),
                                    name -> LightType.valueOf(name.toUpperCase())
                            )
                            .optionalFieldOf("light_type")
                            .forGetter(LightThresholdLightProvider::lightType),
                    Codec.BOOL
                            .optionalFieldOf("apply_ambient_darkness", true)
                            .forGetter(LightThresholdLightProvider::applyAmbientDarkness),
                    Codec.intRange(0, 15)
                            .fieldOf("threshold")
                            .forGetter(LightThresholdLightProvider::threshold),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("above")
                            .forGetter(LightThresholdLightProvider::above),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("below")
                            .forGetter(LightThresholdLightProvider::below)
            ).apply(instance, LightThresholdLightProvider::new)
    );

    private final Optional<LightType> lightType;
    private final boolean applyAmbientDarkness;
    private final int threshold;
    private final RegistryEntry<EnvironmentProvider> above;
    private final RegistryEntry<EnvironmentProvider> below;

    /**
     * Creates a new builder with the mandatory threshold, above, and below fields
     *
     * @param threshold The light level threshold
     * @param above     The provider to use when a positions light level is at or above the {@code threshold}
     * @param below     The provider to use when a positions light level is below the {@code threshold}
     * @return Returns a new builder instance
     */
    @Contract("_,_,_->new")
    public static Builder builder(
            int threshold,
            RegistryEntry<EnvironmentProvider> above,
            RegistryEntry<EnvironmentProvider> below
    ) {
        return new Builder(threshold, above, below);
    }

    private LightThresholdLightProvider(
            Optional<LightType> lightType,
            boolean applyAmbientDarkness,
            int threshold,
            RegistryEntry<EnvironmentProvider> above,
            RegistryEntry<EnvironmentProvider> below
    ) {
        this.lightType = lightType;
        this.applyAmbientDarkness = applyAmbientDarkness;
        this.threshold = threshold;
        this.above = above;
        this.below = below;
    }

    /**
     * Finds the current components of the world position based on light level. If the light level of the position is at
     * or above the threshold then returns the {@link #above()} provider. Otherwise, returns the {@link #below()} provider.
     * <p>
     * Filters for sky/block light and ambient darkness if requested.
     *
     * @param world The world/level being queried
     * @param pos   The position in the world to query
     * @param biome The biome at the position in the world
     * @return Returns one of the child providers based on light conditions
     */
    @Override
    public ComponentMap findCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        int lightLevel = this.lightType
                .map(type -> world.getLightLevel(type, pos))
                .orElseGet(() -> world.getLightLevel(pos));

        if (this.applyAmbientDarkness && this.lightType.orElse(null) == LightType.SKY) {
            lightLevel -= world.getAmbientDarkness();
        }

        return lightLevel >= this.threshold
                ? this.above.value().findCurrentComponents(world, pos, biome)
                : this.below.value().findCurrentComponents(world, pos, biome);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.LIGHT_THRESHOLD;
    }

    /**
     * The optional light type of this provider. If not specified, uses {@link net.minecraft.world.WorldView#getLightLevel(BlockPos)}
     * to determine light level.
     *
     * @return Returns the light type of this provider.
     */
    public Optional<LightType> lightType() {
        return this.lightType;
    }

    /**
     * Whether ambient darkness should be applied when using the skylight light type (default: true)
     */
    public boolean applyAmbientDarkness() {
        return this.applyAmbientDarkness;
    }

    /**
     * Light level threshold that determines whether to use {@link #above()} or {@link #below()} when finding the
     * environment components
     */
    public int threshold() {
        return this.threshold;
    }

    /**
     * The provider to use when the light level is at or above the {@link #threshold()}
     */
    public RegistryEntry<EnvironmentProvider> above() {
        return this.above;
    }

    /**
     * The provider to use when the light level is below the {@link #threshold()}
     */
    public RegistryEntry<EnvironmentProvider> below() {
        return this.below;
    }

    /**
     * Builder class for light threshold providers
     */
    public static class Builder {
        @Nullable
        private LightType lightType = null;
        private boolean applyAmbientDarkness = true;
        private final int threshold;
        private final RegistryEntry<EnvironmentProvider> above;
        private final RegistryEntry<EnvironmentProvider> below;

        private Builder(int threshold, RegistryEntry<EnvironmentProvider> above, RegistryEntry<EnvironmentProvider> below) {
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
         * Sets a light type to filter on.
         *
         * @return Returns this builder
         */
        @Contract("_->this")
        public Builder withLightType(LightType lightType) {
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
package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LightThresholdLightProvider implements EnvironmentProvider {
    public static final MapCodec<LightThresholdLightProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    LightTypeWrapper.CODEC
                            .optionalFieldOf("light_type")
                            .forGetter(LightThresholdLightProvider::lightTypeWrapper),
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

    private final Optional<LightTypeWrapper> lightTypeWrapper;
    private final boolean applyAmbientDarkness;
    private final int threshold;
    private final RegistryEntry<EnvironmentProvider> above;
    private final RegistryEntry<EnvironmentProvider> below;

    @Contract("_,_,_->new")
    public static Builder builder(
            int threshold,
            RegistryEntry<EnvironmentProvider> above,
            RegistryEntry<EnvironmentProvider> below
    ) {
        return new Builder(threshold, above, below);
    }

    private LightThresholdLightProvider(
            Optional<LightTypeWrapper> lightTypeWrapper,
            boolean applyAmbientDarkness,
            int threshold,
            RegistryEntry<EnvironmentProvider> above,
            RegistryEntry<EnvironmentProvider> below
    ) {
        this.lightTypeWrapper = lightTypeWrapper;
        this.applyAmbientDarkness = applyAmbientDarkness;
        this.threshold = threshold;
        this.above = above;
        this.below = below;
    }

    @Override
    public ComponentMap findCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        int lightLevel = this.lightTypeWrapper
                .map(type -> world.getLightLevel(type.lightType(), pos))
                .orElseGet(() -> world.getLightLevel(pos));

        if (this.applyAmbientDarkness && this.lightTypeWrapper.orElse(null) == LightTypeWrapper.SKY) {
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

    public Optional<LightType> lightType() {
        return this.lightTypeWrapper.map(LightTypeWrapper::lightType);
    }

    private Optional<LightTypeWrapper> lightTypeWrapper() {
        return this.lightTypeWrapper;
    }

    public boolean applyAmbientDarkness() {
        return this.applyAmbientDarkness;
    }

    public int threshold() {
        return this.threshold;
    }

    public RegistryEntry<EnvironmentProvider> above() {
        return this.above;
    }

    public RegistryEntry<EnvironmentProvider> below() {
        return this.below;
    }

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

        @Contract("->this")
        public Builder ignoreAmbientDarkness() {
            this.applyAmbientDarkness = false;
            return this;
        }

        @Contract("_->this")
        public Builder withLightType(LightType lightType) {
            this.lightType = lightType;
            return this;
        }

        @Contract("->new")
        public LightThresholdLightProvider build() {
            return new LightThresholdLightProvider(
                    LightTypeWrapper.of(this.lightType),
                    this.applyAmbientDarkness,
                    this.threshold,
                    this.above,
                    this.below
            );
        }
    }

    private enum LightTypeWrapper implements StringIdentifiable {
        BLOCK("block", LightType.BLOCK),
        SKY("sky", LightType.SKY);

        private static final Codec<LightTypeWrapper> CODEC = StringIdentifiable.createCodec(LightTypeWrapper::values);

        private final String name;
        private final LightType lightType;

        LightTypeWrapper(String name, LightType lightType) {
            this.name = name;
            this.lightType = lightType;
        }

        private static Optional<LightTypeWrapper> of(@Nullable LightType base) {
            return switch (base) {
                case BLOCK -> Optional.of(LightTypeWrapper.BLOCK);
                case SKY -> Optional.of(LightTypeWrapper.SKY);
                case null -> Optional.empty();
            };
        }

        @Override
        public String asString() {
            return this.name;
        }

        public LightType lightType() {
            return this.lightType;
        }
    }
}
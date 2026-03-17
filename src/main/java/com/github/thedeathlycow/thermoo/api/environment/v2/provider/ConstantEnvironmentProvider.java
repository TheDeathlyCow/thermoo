package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Contract;

/**
 * Provides constant component values
 */
public final class ConstantEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<ConstantEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnvironmentComponentTypes.COMPONENT_MAP_CODEC
                            .fieldOf("components")
                            .forGetter(ConstantEnvironmentProvider::components)
            ).apply(instance, ConstantEnvironmentProvider::new)
    );

    private final DataComponentMap components;

    private ConstantEnvironmentProvider(DataComponentMap components) {
        this.components = components;
    }

    /**
     * Creates a constant environment provider from a component map builder. The builder is built into a new component
     * map with this method, so modifying the builder after creating the provider will not affect the returned provider.
     *
     * @param builder The builder to create the provider from
     * @return Returns a new replacement environment provider
     */
    @Contract("_->new")
    public static ConstantEnvironmentProvider create(DataComponentMap.Builder builder) {
        return new ConstantEnvironmentProvider(builder.build());
    }

    /**
     * Adds the component map stored in this provider's {@link #components} to the builder. The components supplied by
     * this provider are immutable and never change. If a component type is mapped to a value in the builder and NOT
     * mapped to a value in this provider, then it will be unaffected.
     *
     * @param level   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A component map builder to append to
     */
    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        builder.addAll(this.components);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.CONSTANT;
    }

    /**
     * Gets the component map stored in this provider. This is an {@link EnvironmentComponentTypes environment component}.
     */
    public DataComponentMap components() {
        return this.components;
    }
}
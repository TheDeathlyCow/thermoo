package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;

/**
 * Provides constant values by replacement
 *
 * @see ReduceConstantEnvironmentProvider
 */
public final class ReplaceConstantEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<ReplaceConstantEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnvironmentComponentTypes.COMPONENT_MAP_CODEC
                            .fieldOf("components")
                            .forGetter(ReplaceConstantEnvironmentProvider::components)
            ).apply(instance, ReplaceConstantEnvironmentProvider::new)
    );

    private final ComponentMap components;

    private ReplaceConstantEnvironmentProvider(ComponentMap components) {
        this.components = components;
    }

    /**
     * Creates a replacement environment provider from a component map builder. The builder is built into a new component
     * map with this method, so modifying the builder after creating the provider will not affect the returned provider.
     *
     * @param builder The builder to create the provider from
     * @return Returns a new replacement environment provider
     */
    @Contract("_->new")
    public static ReplaceConstantEnvironmentProvider create(ComponentMap.Builder builder) {
        return new ReplaceConstantEnvironmentProvider(builder.build());
    }

    /**
     * Replaces the components in the builder with the component map stored in this provider's {@link #components}. The
     * components supplied by this provider are immutable and never change. If a component type is mapped to a value in
     * the builder and NOT mapped to a value in this provider, then it will be unaffected.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder Component map builder to append to
     */
    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        builder.replaceAll(this.components);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.REPLACE_CONSTANT;
    }

    /**
     * Gets the component map stored in this provider. This is an {@link EnvironmentComponentTypes environment component}.
     */
    public ComponentMap components() {
        return this.components;
    }
}
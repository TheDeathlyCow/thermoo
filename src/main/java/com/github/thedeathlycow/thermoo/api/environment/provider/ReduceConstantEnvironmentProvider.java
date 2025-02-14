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
 * Provides constant environment values by reduction
 *
 * @see ReplaceConstantEnvironmentProvider
 */
public final class ReduceConstantEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<ReduceConstantEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnvironmentComponentTypes.COMPONENT_MAP_CODEC
                            .fieldOf("components")
                            .forGetter(ReduceConstantEnvironmentProvider::components)
            ).apply(instance, ReduceConstantEnvironmentProvider::new)
    );

    private final ComponentMap components;

    private ReduceConstantEnvironmentProvider(ComponentMap components) {
        this.components = components;
    }

    /**
     * Creates a constant environment provider from a component map builder. The builder is built into a new component
     * map with this method, so modifying the builder after creating the provider will not affect the returned provider.
     *
     * @param builder The builder to create the provider from
     * @return Returns a new constant environment provider
     */
    @Contract("_->new")
    public static ReduceConstantEnvironmentProvider create(ComponentMap.Builder builder) {
        return new ReduceConstantEnvironmentProvider(builder.build());
    }

    /**
     * Builds (by reduction) the component map stored in this provider into the provided builder. The components supplied
     * by this provider are immutable and never change. Note that existing values are
     * {@linkplain ReducibleComponentMapBuilder#addAll(ComponentMap)  reduced}, not overwritten. See {@link ReplaceConstantEnvironmentProvider}
     * if overwritten without replacement is desired.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder Component map builder to append to
     * @see ReplaceConstantEnvironmentProvider
     */
    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        builder.addAll(this.components);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.REDUCE_CONSTANT;
    }

    /**
     * Gets the component map stored in this provider. This is an {@link EnvironmentComponentTypes environment component}.
     */
    public ComponentMap components() {
        return this.components;
    }
}
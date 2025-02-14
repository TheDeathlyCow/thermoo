package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;

/**
 * Provides constant environment values
 */
public final class ConstantEnvironmentProvider implements EnvironmentProvider {

    public static final MapCodec<ConstantEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EnvironmentComponentTypes.COMPONENT_MAP_CODEC
                            .fieldOf("components")
                            .forGetter(ConstantEnvironmentProvider::components)
            ).apply(instance, ConstantEnvironmentProvider::new)
    );

    private final ComponentMap components;

    private ConstantEnvironmentProvider(ComponentMap components) {
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
    public static ConstantEnvironmentProvider create(ComponentMap.Builder builder) {
        return new ConstantEnvironmentProvider(builder.build());
    }

    /**
     * Returns the component map stored in this provider. The components supplied by this provider are immutable and
     * never change.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder Component map builder to append to
     */
    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        builder.addAll(this.components);
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.CONSTANT;
    }

    /**
     * Gets the component map stored in this provider. This is an {@link EnvironmentComponentTypes environment component}.
     */
    public ComponentMap components() {
        return this.components;
    }
}
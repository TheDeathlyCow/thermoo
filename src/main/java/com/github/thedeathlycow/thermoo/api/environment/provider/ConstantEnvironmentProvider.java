package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

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

    public static ConstantEnvironmentProvider create(ComponentMap.Builder builder) {
        return new ConstantEnvironmentProvider(builder.build());
    }

    @Override
    public ComponentMap findCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        return this.components;
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
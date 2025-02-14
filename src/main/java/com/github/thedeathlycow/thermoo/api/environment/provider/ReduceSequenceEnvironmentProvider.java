package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Applies modifiers to a base environment provider from a tag or list of environment providers
 */
public final class ReduceSequenceEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<ReduceSequenceEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryCodecs.entryList(ThermooRegistryKeys.ENVIRONMENT_PROVIDER)
                            .fieldOf("modifiers")
                            .forGetter(ReduceSequenceEnvironmentProvider::modifiers),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("base")
                            .forGetter(ReduceSequenceEnvironmentProvider::base)
            ).apply(instance, ReduceSequenceEnvironmentProvider::new)
    );

    private final RegistryEntryList<EnvironmentProvider> modifiers;
    private final RegistryEntry<EnvironmentProvider> base;

    private ReduceSequenceEnvironmentProvider(
            RegistryEntryList<EnvironmentProvider> modifiers,
            RegistryEntry<EnvironmentProvider> base
    ) {
        this.modifiers = modifiers;
        this.base = base;
    }

    public ReduceSequenceEnvironmentProvider create(
            RegistryEntryList<EnvironmentProvider> modifiers,
            RegistryEntry<EnvironmentProvider> base
    ) {
        return new ReduceSequenceEnvironmentProvider(modifiers, base);
    }

    /**
     * Builds the current components from the {@link #base()} and {@linkplain  ReducibleComponentMapBuilder reduces} the
     * modifiers into it, in the order that the modifiers are specified.
     *
     * @param world   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder Component map builder to append to
     */
    @Override
    public void buildCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome, ReducibleComponentMapBuilder builder) {
        base.value().buildCurrentComponents(world, pos, biome, builder);
        for (RegistryEntry<EnvironmentProvider> modifier : this.modifiers) {
            modifier.value().buildCurrentComponents(world, pos, biome, builder);
        }
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.REDUCE_SEQUENCE;
    }

    /**
     * A list of modifiers that are {@linkplain  ReducibleComponentMapBuilder reduced} into the base. Modifiers are
     * applied in iteration order.
     *
     * @return Returns a registry entry list of providers
     */
    public RegistryEntryList<EnvironmentProvider> modifiers() {
        return modifiers;
    }

    /**
     * The base provider to be modified.
     *
     * @return Returns the provider registry entry
     */
    public RegistryEntry<EnvironmentProvider> base() {
        return base;
    }
}
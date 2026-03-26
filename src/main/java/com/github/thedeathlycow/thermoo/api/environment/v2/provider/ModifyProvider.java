package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentDefinition;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

/**
 * Applies modifiers to a base environment provider from a tag or list of environment providers
 * <p>
 * <strong>Note:</strong> In general, using the {@linkplain EnvironmentDefinition#priority() environment priority} is more
 * flexible than using this provider type.
 */
public final class ModifyProvider implements EnvironmentProvider {
    public static final MapCodec<ModifyProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryCodecs.homogeneousList(ThermooRegistries.ENVIRONMENT_PROVIDER)
                            .fieldOf("modifiers")
                            .forGetter(ModifyProvider::modifiers),
                    EnvironmentProvider.HOLDER_CODEC
                            .fieldOf("base")
                            .forGetter(ModifyProvider::base)
            ).apply(instance, ModifyProvider::new)
    );

    private final HolderSet<EnvironmentProvider> modifiers;
    private final Holder<EnvironmentProvider> base;

    private ModifyProvider(
            HolderSet<EnvironmentProvider> modifiers,
            Holder<EnvironmentProvider> base
    ) {
        this.modifiers = modifiers;
        this.base = base;
    }

    public ModifyProvider create(
            HolderSet<EnvironmentProvider> modifiers,
            Holder<EnvironmentProvider> base
    ) {
        return new ModifyProvider(modifiers, base);
    }

    /**
     * Builds the current components from the {@link #base()} and applies the modifiers to it, in the order that the
     * modifiers are specified.
     *
     * @param level   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder Component map builder to append to
     */
    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        base.value().buildCurrentComponents(level, pos, biome, builder);
        for (Holder<EnvironmentProvider> modifier : this.modifiers) {
            modifier.value().buildCurrentComponents(level, pos, biome, builder);
        }
    }

    @Override
    public MapCodec<ModifyProvider> codec() {
        return CODEC;
    }

    /**
     * A list of modifiers that are applied to the base. Modifiers are applied in iteration order.
     *
     * @return Returns a registry entry list of providers
     */
    public HolderSet<EnvironmentProvider> modifiers() {
        return modifiers;
    }

    /**
     * The base provider to be modified.
     *
     * @return Returns the provider registry entry
     */
    public Holder<EnvironmentProvider> base() {
        return base;
    }
}
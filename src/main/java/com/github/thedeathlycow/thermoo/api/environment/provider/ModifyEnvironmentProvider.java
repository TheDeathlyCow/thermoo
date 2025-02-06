package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.mixin.common.ComponentMapBuilderAccessor;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public final class ModifyEnvironmentProvider implements EnvironmentProvider {

    private final RegistryEntry<EnvironmentProvider> base;
    private final RegistryEntryList<EnvironmentProvider> modifiers;

    public ModifyEnvironmentProvider(RegistryEntry<EnvironmentProvider> base, RegistryEntryList<EnvironmentProvider> modifiers) {
        this.base = base;
        this.modifiers = modifiers;
    }

    @Override
    public ComponentMap findCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ComponentMap result = this.base.value().findCurrentComponents(world, pos, biome);

        for (RegistryEntry<EnvironmentProvider> modifier : this.modifiers) {

        }

        return result;
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return null;
    }
}
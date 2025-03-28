package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.impl.environment.ThermooBiome;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(Biome.class)
public class BiomeMixin implements ThermooBiome {
    @Unique
    private final List<RegistryEntry<EnvironmentProvider>> thermoo$environments = new ArrayList<>();

    @Override
    @Unique
    public List<RegistryEntry<EnvironmentProvider>> thermoo$getEnvironmentProviders() {
        return this.thermoo$environments;
    }

    @Override
    public void thermoo$replaceProviders(Collection<RegistryEntry<EnvironmentProvider>> providers) {
        this.thermoo$environments.clear();
        this.thermoo$environments.addAll(providers);
    }
}
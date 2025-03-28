package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Collection;
import java.util.List;

public interface ThermooBiome {
    List<RegistryEntry<EnvironmentProvider>> thermoo$getEnvironmentProviders();

    void thermoo$replaceProviders(Collection<RegistryEntry<EnvironmentProvider>> providers);
}
package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;

import java.util.Collection;
import java.util.List;

public interface ThermooBiome {
    List<EnvironmentProvider> thermoo$getEnvironmentProviders();

    void thermoo$replaceProviders(Collection<EnvironmentProvider> providers);
}
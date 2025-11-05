package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import net.minecraft.core.Holder;

import java.util.Collection;
import java.util.List;

public interface ThermooBiome {
    List<Holder<EnvironmentProvider>> thermoo$getEnvironmentProviders();

    void thermoo$replaceProviders(Collection<Holder<EnvironmentProvider>> providers);
}
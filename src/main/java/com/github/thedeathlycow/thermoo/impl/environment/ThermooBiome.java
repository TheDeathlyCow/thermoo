package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.Holder;

public interface ThermooBiome {
    List<Holder<EnvironmentProvider>> thermoo$getEnvironmentProviders();

    void thermoo$replaceProviders(Collection<Holder<EnvironmentProvider>> providers);
}
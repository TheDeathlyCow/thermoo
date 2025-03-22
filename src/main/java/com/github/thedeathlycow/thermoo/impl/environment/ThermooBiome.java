package com.github.thedeathlycow.thermoo.impl.environment;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;

import java.util.List;

public interface ThermooBiome {
    List<EnvironmentDefinition> thermoo$getEnvironments();

    void thermoo$addEnvironment(EnvironmentDefinition environment);

    void thermoo$clearEnvironments();
}
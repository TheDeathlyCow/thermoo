package com.github.thedeathlycow.thermoo.impl.platform;

public interface ThermooPlatform {
    /// Gets the current loader of the environment, e.g., Fabric or Neoforge.
    Loader getLoader();
}
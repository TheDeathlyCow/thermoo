package com.github.thedeathlycow.thermoo.api.environment.provider;

public final class EnvironmentProviderTypes {
    public static final EnvironmentProviderType<ConstantEnvironmentProvider> CONSTANT = new EnvironmentProviderType<>(
            ConstantEnvironmentProvider.CODEC
    );

    public static final EnvironmentProviderType<TemperateSeasonEnvironmentProvider> TEMPERATE_SEASONAL = new EnvironmentProviderType<>(
            TemperateSeasonEnvironmentProvider.CODEC
    );

    public static final EnvironmentProviderType<TropicalSeasonEnvironmentProvider> TROPICAL_SEASONAL = new EnvironmentProviderType<>(
            TropicalSeasonEnvironmentProvider.CODEC
    );

    private EnvironmentProviderTypes() {

    }
}
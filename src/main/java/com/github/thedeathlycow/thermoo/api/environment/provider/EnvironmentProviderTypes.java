package com.github.thedeathlycow.thermoo.api.environment.provider;

/**
 * The default {@link EnvironmentProviderType}s provided by Thermoo
 */
public final class EnvironmentProviderTypes {
    /**
     * A constant value environment provider
     *
     * @see ConstantEnvironmentProvider
     */
    public static final EnvironmentProviderType<ConstantEnvironmentProvider> CONSTANT = new EnvironmentProviderType<>(
            ConstantEnvironmentProvider.CODEC
    );

    /**
     * A temperature season environment provider
     *
     * @see TemperateSeasonEnvironmentProvider
     */
    public static final EnvironmentProviderType<TemperateSeasonEnvironmentProvider> TEMPERATE_SEASONAL = new EnvironmentProviderType<>(
            TemperateSeasonEnvironmentProvider.CODEC
    );

    /**
     * A tropical season environment provider
     *
     * @see TropicalSeasonEnvironmentProvider
     */
    public static final EnvironmentProviderType<TropicalSeasonEnvironmentProvider> TROPICAL_SEASONAL = new EnvironmentProviderType<>(
            TropicalSeasonEnvironmentProvider.CODEC
    );

    public static final EnvironmentProviderType<ModifyEnvironmentProvider> MODIFY = new EnvironmentProviderType<>(
            ModifyEnvironmentProvider.CODEC
    );

    private EnvironmentProviderTypes() {

    }
}
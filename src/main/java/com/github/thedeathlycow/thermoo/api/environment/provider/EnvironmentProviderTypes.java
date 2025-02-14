package com.github.thedeathlycow.thermoo.api.environment.provider;

/**
 * The default {@link EnvironmentProviderType}s provided by Thermoo
 */
public final class EnvironmentProviderTypes {
    /**
     * A constant value environment provider that adds values by
     * {@linkplain com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder reduction}
     *
     * @see ReduceConstantEnvironmentProvider
     */
    public static final EnvironmentProviderType<ReduceConstantEnvironmentProvider> REDUCE_CONSTANT = new EnvironmentProviderType<>(ReduceConstantEnvironmentProvider.CODEC);

    /**
     * A constant value environment provider that adds values by replacement
     *
     * @see ReplaceConstantEnvironmentProvider
     */
    public static final EnvironmentProviderType<ReplaceConstantEnvironmentProvider> REPLACE_CONSTANT = new EnvironmentProviderType<>(ReplaceConstantEnvironmentProvider.CODEC);

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

    /**
     * A provider that can reduce a list of modifiers into a base provider through
     * {@link com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder}
     *
     * @see ReduceSequenceEnvironmentProvider
     * @see com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder
     */
    public static final EnvironmentProviderType<ReduceSequenceEnvironmentProvider> REDUCE_SEQUENCE = new EnvironmentProviderType<>(ReduceSequenceEnvironmentProvider.CODEC);

    /**
     * A provider that picks between two child providers based on light level
     *
     * @see LightThresholdLightProvider
     */
    public static final EnvironmentProviderType<LightThresholdLightProvider> LIGHT_THRESHOLD = new EnvironmentProviderType<>(LightThresholdLightProvider.CODEC);

    public static final EnvironmentProviderType<LocalPrecipitationEnvironmentProvider> LOCAL_PRECIPITATION = new EnvironmentProviderType<>(LocalPrecipitationEnvironmentProvider.CODEC);

    private EnvironmentProviderTypes() {

    }
}
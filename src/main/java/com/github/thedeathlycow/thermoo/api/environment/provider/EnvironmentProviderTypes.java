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
     * @see ConstantEnvironmentProvider
     */
    public static final EnvironmentProviderType<ConstantEnvironmentProvider> CONSTANT = new EnvironmentProviderType<>(ConstantEnvironmentProvider.CODEC);

    /**
     * A temperature season environment provider
     *
     * @see TemperateSeasonEnvironmentProvider
     */
    public static final EnvironmentProviderType<TemperateSeasonEnvironmentProvider> TEMPERATE_SEASONAL = new EnvironmentProviderType<>(TemperateSeasonEnvironmentProvider.CODEC);

    /**
     * A tropical season environment provider
     *
     * @see TropicalSeasonEnvironmentProvider
     */
    public static final EnvironmentProviderType<TropicalSeasonEnvironmentProvider> TROPICAL_SEASONAL = new EnvironmentProviderType<>(TropicalSeasonEnvironmentProvider.CODEC);

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

    /**
     * A provider that picks a child provider based on the global weather state (clear, rain, thunder)
     *
     * @see WeatherStateEnvironmentProvider
     */
    public static final EnvironmentProviderType<WeatherStateEnvironmentProvider> WEATHER_STATE = new EnvironmentProviderType<>(WeatherStateEnvironmentProvider.CODEC);

    /**
     * A provider that picks a child provider based on what kind of precipitation a biome receives
     *
     * @see BiomePrecipitationTypeEnvironmentProvider
     */
    public static final EnvironmentProviderType<BiomePrecipitationTypeEnvironmentProvider> PRECIPITATION_TYPE = new EnvironmentProviderType<>(BiomePrecipitationTypeEnvironmentProvider.CODEC);

    /**
     * A leaf-modifier provider that shifts the existing temperature value in a map by some amount
     *
     * @see TemperatureShiftEnvironmentProvider
     */
    public static final EnvironmentProviderType<TemperatureShiftEnvironmentProvider> TEMPERATURE_SHIFT = new EnvironmentProviderType<>(TemperatureShiftEnvironmentProvider.CODEC);

    private EnvironmentProviderTypes() {

    }
}
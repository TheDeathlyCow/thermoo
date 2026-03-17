package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

/**
 * The default {@link EnvironmentProviderType}s provided by Thermoo
 */
public final class EnvironmentProviderTypes {

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
     * A provider that can apply a list of modifiers to a base provider
     *
     * @see ModifyEnvironmentProvider
     */
    public static final EnvironmentProviderType<ModifyEnvironmentProvider> MODIFY = new EnvironmentProviderType<>(ModifyEnvironmentProvider.CODEC);

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

    /**
     * A leaf-modifier provider that applies the Ideal Gas Law to the temperature in a map, using some base pressure.
     *
     * @see SetTemperatureFromPressure
     */
    public static final EnvironmentProviderType<SetTemperatureFromPressure> SET_TEMPERATURE_FROM_PRESSURE = new EnvironmentProviderType<>(SetTemperatureFromPressure.CODEC);

    /**
     * A leaf-modifier provider that reduces atmospheric pressure per metre of altitude.
     *
     * @see SetPressureFromAltitude
     */
    public static final EnvironmentProviderType<SetPressureFromAltitude> SET_PRESSURE_FROM_ALTITUDE = new EnvironmentProviderType<>(SetPressureFromAltitude.CODEC);

    private EnvironmentProviderTypes() {

    }
}
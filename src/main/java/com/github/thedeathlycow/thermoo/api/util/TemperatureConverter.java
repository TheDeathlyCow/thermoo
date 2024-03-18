package com.github.thedeathlycow.thermoo.api.util;

import net.minecraft.util.math.MathHelper;

/**
 * Helper API for conversions between normal temperature units (like Celsius and Fahrenheit) to per-tick temperature
 * point changes.
 */
public class TemperatureConverter {

    public static final double NORMAL_SCALE = 1.0;

    public static final double NORMAL_BASE_SHIFT = 0.0;

    /**
     * Converts a Celsius ambient temperature to a per tick thermoo temperature point change.
     * <p>
     * With a scale of {@value NORMAL_SCALE} and a baseShift of {@value NORMAL_BASE_SHIFT}, this returns a per tick
     * temperature point change based on a linear scale with 10C = -1 temp/tick, 20C = 0 temp/tick, 30C = +1 temp/tick,
     * etc.
     *
     * @param celsiusTemperature The ambient temperature, in Celsius.
     * @param scale              How much to scale the result by. For example, a scale of 2.0 would mean
     *                           25C = +1 temp/tick, and 30C = +2 temp/tick. Normally this is 1.0.
     * @param baseShift          How much to offset the base by. Normally this is 0.
     * @return Returns a per tick temperature point change.
     * @see #temperatureTickToCelsius(int, double, double)
     */
    public static int celsiusToTemperatureTick(double celsiusTemperature, double scale, double baseShift) {
        return MathHelper.floor(scale / 10.0 * (celsiusTemperature - (20.0 + baseShift)));
    }

    /**
     * Converts a per-tick thermoo temperature point change to an ambient temperature in Celsius.
     * <p>
     * Performs the inverse calculation of {@link #celsiusToTemperatureTick(double, double, double)}. So, with a scale
     * of {@value NORMAL_SCALE} and a baseShift of {@value NORMAL_BASE_SHIFT}, this means -1 temp/tick = 10C,
     * 0 temp/tick = 20C, and +1 temp/tick = 30C.
     *
     * @param temperatureTick The thermoo temperature point change per tick value
     * @param scale           How much to scale the result by. For example, a scale of 2.0 would mean
     *                        +1 temp/tick = 25C, and +2 temp/tick = 30C.
     * @return Returns the ambient temperature in Celsius for the given temperature point change.
     * @see #celsiusToTemperatureTick(double, double, double)
     */
    public static double temperatureTickToCelsius(int temperatureTick, double scale, double baseShift) {
        return (10 * temperatureTick) / scale + 20 + baseShift;
    }

    /**
     * Helper method for convert Fahrenheit temperatures to Celsius
     *
     * @param fahrenheitTemperature The temperature in Fahrenheit
     * @return The equivalent temperature in Celsius
     */
    public static double fahrenheitToCelsius(double fahrenheitTemperature) {
        return (fahrenheitTemperature - 32.0) * 5.0 / 9.0;
    }

    /**
     * Helper method for convert Celsius temperatures to Fahrenheit
     *
     * @param celsiusTemperature The temperature in Celsius
     * @return The equivalent temperature in Fahrenheit
     */
    public static double celsiusToFahrenheit(double celsiusTemperature) {
        return (5.0 / 9.0) * celsiusTemperature + 32.0;
    }

    /**
     * Helper method for convert Kelvin temperatures to Celsius
     *
     * @param kelvinTemperature The temperature in Kelvin
     * @return The equivalent temperature in Celsius
     */
    public static double kelvinToCelsius(double kelvinTemperature) {
        return kelvinTemperature - 273.15;
    }

    /**
     * Helper method for convert Celsius temperatures to Kelvin
     *
     * @param celsiusTemperature The temperature in Celsius
     * @return The equivalent temperature in Kelvin
     */
    public static double celsiusToKelvin(double celsiusTemperature) {
        return celsiusTemperature + 273.15;
    }

    /**
     * Helper method for convert Rankine temperatures to Celsius
     *
     * @param rankineTemperature The temperature in Rankine
     * @return The equivalent temperature in Celsius
     */
    public static double rankineToCelsius(double rankineTemperature) {
        double fahrenheitTemperature = rankineTemperature - 459.67;
        return fahrenheitToCelsius(fahrenheitTemperature);
    }

    /**
     * Helper method for convert Celsius temperatures to Rankine
     *
     * @param celsiusTemperature The temperature in Celsius
     * @return The equivalent temperature in Rankine
     */
    public static double celsiusToRankine(double celsiusTemperature) {
        double fahrenheitTemperature = celsiusToFahrenheit(celsiusTemperature);
        return fahrenheitTemperature + 459.67;
    }

    private TemperatureConverter() {

    }
}

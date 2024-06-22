package com.github.thedeathlycow.thermoo.api.util;

import net.minecraft.util.math.MathHelper;

/**
 * Helper API for conversions between normal temperature units (like Celsius and Fahrenheit) to per-tick temperature
 * point changes.
 */
public class TemperatureConverter {

    /**
     * Converts an ambient Celsius temperature value to a per tick Thermoo passive temperature point change.
     * <p>
     * The result is based on a linear scale. For example:
     * <p>
     * 5C - 14C => -1 temp/tick
     * <p>
     * 15C - 24C => 0 temp/tick
     * <p>
     * 25C - 34C => +1 temp/tick
     * <p>
     * etc
     *
     * @param temperatureValue The input temperature value, in Celsius
     * @return An equivalent per-tick temperature point change
     */
    public static int celsiusToTemperatureTick(double temperatureValue) {
        return ambientTemperatureToTemperatureTick(temperatureValue, Settings.DEFAULT);
    }

    /**
     * Converts an ambient temperature value in Celsius, Kelvin, Fahrenheit or Rankine to a per tick Thermoo temperature
     * point change. Using the {@linkplain Settings#DEFAULT default settings}, this converts the temperature value to a
     * temperature point change following a linear scale where 15C-24C equates to 0 temp/tick, and then every +10C adds
     * +1 temp/tick (and vice versa).
     * <p>
     * With the settings, the scale the effect of changes in ambient temperature. For example a scale of 2 will mean that
     * every +5C adds +1 temp/tick. The base shift effects where the 0 base is. For example, a base shift of +5 means that
     * the 20C - 29C will be 0 temp/tick.
     * <p>
     * You can also change the unit to other measurements like Fahrenheit or Kelvin, but the scale is still based in Celsius.
     * So for example with default settings 59F to 75.2F equates to 0 temp/tick, and increases of 18F will add +1 temp/tick.
     *
     * @param temperatureValue The ambient temperature, in Celsius.
     * @param settings         Allows you to adjust the unit, scale, and base of the temperature conversion.
     * @see #temperatureTickToAmbientTemperature(int, Settings)
     */
    public static int ambientTemperatureToTemperatureTick(double temperatureValue, Settings settings) {
        double celsiusTemperature = settings.unit.toCelsius(temperatureValue);

        return MathHelper.floor(settings.scale / 10.0 * (celsiusTemperature - (15.0 + settings.baseShift)));
    }

    /**
     * Converts a per-tick thermoo temperature point change to an ambient temperature in Celsius.
     * <p>
     * Performs the inverse calculation of {@link #celsiusToTemperatureTick(double)}, but returns the median of the input
     * temperature range.
     * <p>
     * So for example:
     * <p>
     * -1 temp/tick => 10C
     * <p>
     * 0 temp/tick => 20C,
     * <p>
     * +1 temp/tick => 30C.
     *
     * @param temperatureTick The thermoo temperature point change per tick value
     * @return Returns the ambient temperature in Celsius for the given temperature point change.
     * @see #ambientTemperatureToTemperatureTick(double, Settings)
     */
    public static double temperatureTickToCelsius(int temperatureTick) {
        return temperatureTickToAmbientTemperature(temperatureTick, Settings.DEFAULT);
    }


    /**
     * Converts a per-tick thermoo temperature point change to an ambient temperature in Celsius.
     * <p>
     * Performs the inverse calculation of {@link #ambientTemperatureToTemperatureTick(double, Settings)}, but returns
     * the median of the range.
     * <p>
     * So for example, 0 temp/tick => 20C, -1 temp/tick => 10C, and +1 temp/tick => 30C.
     *
     * @param temperatureTick The thermoo temperature point change per tick value
     * @param settings        Allows you to adjust the unit, scale, and base of the temperature conversion.
     * @return Returns the ambient temperature in Celsius for the given temperature point change.
     * @see #ambientTemperatureToTemperatureTick(double, Settings)
     */
    public static double temperatureTickToAmbientTemperature(int temperatureTick, Settings settings) {
        double celsiusTemperature = (10 * temperatureTick) / settings.scale + 20.0 + settings.baseShift;
        return settings.unit.fromCelsius(celsiusTemperature);
    }

    public record Settings(
            TemperatureUnit unit,
            double scale,
            double baseShift
    ) {
        public static final Settings DEFAULT = new Settings(TemperatureUnit.CELSIUS, 1.0, 0);
        public static final Settings DEFAULT_CELSIUS = DEFAULT;
        public static final Settings DEFAULT_FAHRENHEIT = new Settings(TemperatureUnit.FAHRENHEIT, 1.0, 0);
        public static final Settings DEFAULT_KELVIN = new Settings(TemperatureUnit.KELVIN, 1.0, 0);
        public static final Settings DEFAULT_RANKINE = new Settings(TemperatureUnit.RANKINE, 1.0, 0);
    }

    private TemperatureConverter() {

    }
}

package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.environment.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

/**
 * An environment provider that applies the <a href="https://en.wikipedia.org/wiki/Ideal_gas_law">Ideal Gas Law</a> to
 * set the current temperature based on atmospheric pressure, using an assumed baseline pressure. The default baseline
 * pressure is {@value AtmosphericPressureComponent#DEFAULT} mbar.
 */
public final class SetTemperatureFromPressure implements EnvironmentProvider {
    public static final MapCodec<SetTemperatureFromPressure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    AtmosphericPressureComponent.CODEC
                            .optionalFieldOf("basePressure", AtmosphericPressureComponent.DEFAULT)
                            .forGetter(SetTemperatureFromPressure::basePressure)
            ).apply(instance, SetTemperatureFromPressure::new)
    );

    private static final TemperatureRecord ABSOLUTE_ZERO = new TemperatureRecord(0, TemperatureUnit.KELVIN);

    private final double basePressure;

    private SetTemperatureFromPressure(double basePressure) {
        this.basePressure = basePressure;
    }

    /**
     * Applying the <a href="https://en.wikipedia.org/wiki/Ideal_gas_law">Ideal Gas Law</a>, sets the temperature
     * component to {@code temperature := (pressure * temperature) / basePressure}.
     * <p>
     * This is based on the assumption that the temperature set in the map currently is derived based on the atmospheric
     * pressure being equal to its default value.
     *
     * @param level   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A component map builder to append to
     */
    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        if (this.basePressure <= 0) {
            builder.set(EnvironmentComponentTypes.TEMPERATURE, ABSOLUTE_ZERO);
        }

        TemperatureRecord baseTemperature = builder.getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT);

        double baseTemperatureK = baseTemperature.valueInUnit(TemperatureUnit.KELVIN);
        double pressure = builder.getOrDefault(EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE, AtmosphericPressureComponent.DEFAULT);

        // based on ideal gas law
        double adjustedTemperatureK = (pressure * baseTemperatureK) / this.basePressure;

        if (adjustedTemperatureK < 0) {
            adjustedTemperatureK = 0;
        }

        builder.set(
                EnvironmentComponentTypes.TEMPERATURE,
                new TemperatureRecord(adjustedTemperatureK, TemperatureUnit.KELVIN)
                        .convertToUnit(baseTemperature.unit())
        );
    }

    @Override
    public EnvironmentProviderType<SetTemperatureFromPressure> getType() {
        return EnvironmentProviderTypes.SET_TEMPERATURE_FROM_PRESSURE;
    }

    /**
     * @return The base pressure in millibars.
     */
    public double basePressure() {
        return this.basePressure;
    }

    /**
     * Creates a new instance of this component using a base pressure of {@value AtmosphericPressureComponent#DEFAULT}
     * mbar.
     */
    public static SetTemperatureFromPressure create() {
        return new SetTemperatureFromPressure(AtmosphericPressureComponent.DEFAULT);
    }

    /**
     * Creates a new instance of this component using a given base pressure.
     *
     * @param basePressure The base pressure, in millibars. May not be negative.
     */
    public static SetTemperatureFromPressure create(double basePressure) {
        if (basePressure < 0) {
            throw new IllegalArgumentException("Pressure cannot be less than 0!");
        }

        return new SetTemperatureFromPressure(basePressure);
    }
}
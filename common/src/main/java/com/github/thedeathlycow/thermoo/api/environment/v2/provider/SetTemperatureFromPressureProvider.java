/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.environment.v2.attribute.ThermooEnvironmentAttributes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

/**
 * An environment provider that applies the <a href="https://en.wikipedia.org/wiki/Ideal_gas_law">Ideal Gas Law</a> to
 * set the current temperature based on atmospheric pressure, using an assumed baseline pressure. If no baseline
 * pressure is provided, then it will use {@link ThermooEnvironmentAttributes#ATMOSPHERIC_PRESSURE} to get a baseline.
 */
public final class SetTemperatureFromPressureProvider implements EnvironmentProvider {
    public static final MapCodec<SetTemperatureFromPressureProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    AtmosphericPressureComponent.CODEC
                            .optionalFieldOf("base_pressure")
                            .forGetter(SetTemperatureFromPressureProvider::basePressure)
            ).apply(instance, SetTemperatureFromPressureProvider::new)
    );

    private static final TemperatureRecord ABSOLUTE_ZERO = new TemperatureRecord(0, TemperatureUnit.KELVIN);

    private final Optional<Double> basePressure;

    private SetTemperatureFromPressureProvider(Optional<Double> basePressure) {
        this.basePressure = basePressure;
    }

    /**
     * Creates a new instance of this component which uses {@linkplain ThermooEnvironmentAttributes#ATMOSPHERIC_PRESSURE environment attributes}
     * to get the baseline pressure.
     */
    public static SetTemperatureFromPressureProvider create() {
        return new SetTemperatureFromPressureProvider(Optional.empty());
    }

    /**
     * Creates a new instance of this component using a given base pressure.
     *
     * @param basePressure The base pressure, in millibars. May not be negative.
     */
    public static SetTemperatureFromPressureProvider create(double basePressure) {
        if (basePressure < 0) {
            throw new IllegalArgumentException("Pressure cannot be less than 0!");
        }

        return new SetTemperatureFromPressureProvider(Optional.of(basePressure));
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
        TemperatureRecord baseTemperature = builder.thermoo$getOrAdd(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT);

        double seaLevelPressure = this.basePressure.orElseGet(() -> {
            return level.environmentAttributes().getValue(ThermooEnvironmentAttributes.ATMOSPHERIC_PRESSURE, pos);
        });

        if (seaLevelPressure <= 0) {
            builder.set(EnvironmentComponentTypes.TEMPERATURE, ABSOLUTE_ZERO.convertToUnit(baseTemperature.unit()));
            return;
        }

        double baseTemperatureK = baseTemperature.valueInUnit(TemperatureUnit.KELVIN);
        double pressure = builder.thermoo$getOrAdd(EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE, AtmosphericPressureComponent.DEFAULT);

        // based on ideal gas law
        double adjustedTemperatureK = (pressure * baseTemperatureK) / seaLevelPressure;

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
    public MapCodec<SetTemperatureFromPressureProvider> codec() {
        return CODEC;
    }

    /**
     * @return The base pressure in millibars.
     */
    public Optional<Double> basePressure() {
        return this.basePressure;
    }
}
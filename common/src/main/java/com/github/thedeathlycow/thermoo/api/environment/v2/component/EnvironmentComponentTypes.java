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

package com.github.thedeathlycow.thermoo.api.environment.v2.component;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooBuiltInRegistries;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.UnaryOperator;

/**
 * Stores the codec and component type keys for Thermoo's environment component map.
 * <p>
 * Mods may define their own component types in their own classes, they only need be registered to
 * {@link ThermooBuiltInRegistries#ENVIRONMENT_COMPONENT_TYPE}.
 */
public final class EnvironmentComponentTypes {
    public static final Codec<DataComponentType<?>> COMPONENT_TYPE_CODEC = Codec.lazyInitialized(
            ThermooBuiltInRegistries.ENVIRONMENT_COMPONENT_TYPE::byNameCodec
    );
    public static final Codec<DataComponentMap> COMPONENT_MAP_CODEC = DataComponentMap.makeCodec(COMPONENT_TYPE_CODEC);

    /**
     * Stores a temperature reading in {@link TemperatureUnit a unit} such as
     * Celsius, Fahrenheit, Kelvin, or Rankine.
     *
     * @see TemperatureRecordComponent
     */
    public static final DataComponentType<TemperatureRecord> TEMPERATURE = register(
            "temperature",
            builder -> builder.persistent(TemperatureRecordComponent.CODEC)
    );

    /**
     * Stores relative humidity on a 0-1 percentage scale.
     * <p>
     * Relative humidity is defined as "the ratio of how much water vapour is in the air to how much water vapour the
     * air could potentially contain" <a href="https://en.m.wikipedia.org/wiki/Humidity#Relative_humidity">[1]</a> and
     * is expressed here on a 0-1 scale.
     *
     * @see RelativeHumidityComponent
     */
    public static final DataComponentType<Double> RELATIVE_HUMIDITY = register(
            "relative_humidity",
            builder -> builder.persistent(RelativeHumidityComponent.CODEC)
    );

    /**
     * Stores atmospheric pressure in millibars.
     *
     * @see AtmosphericPressureComponent
     */
    public static final DataComponentType<Double> ATMOSPHERIC_PRESSURE = register(
            "atmospheric_pressure",
            builder -> builder.persistent(AtmosphericPressureComponent.CODEC)
    );

    @ApiStatus.Internal
    public static void initialize() {
        Thermoo.LOGGER.debug("Initialized Thermoo Environment Component types");
    }

    private static <T> DataComponentType<T> register(
            String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOperator
    ) {
        return Registry.register(
                ThermooBuiltInRegistries.ENVIRONMENT_COMPONENT_TYPE,
                Thermoo.id(name),
                builderOperator.apply(DataComponentType.builder())
                        .build()
        );
    }

    private EnvironmentComponentTypes() {
    }
}
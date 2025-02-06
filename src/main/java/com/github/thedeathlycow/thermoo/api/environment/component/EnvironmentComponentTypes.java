package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registry;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Stores the codec and default component type keys for Thermoo's environment component map.
 * <p>
 * Mods may define their own component types in their own classes, they only need be registered to
 * {@link ThermooRegistries#ENVIRONMENT_COMPONENT_TYPE}.
 */
public final class EnvironmentComponentTypes {
    public static final Codec<ComponentType<?>> COMPONENT_TYPE_CODEC = Codec.lazyInitialized(
            ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE::getCodec
    );
    public static final Codec<ComponentMap> COMPONENT_MAP_CODEC = ComponentMap.createCodec(COMPONENT_TYPE_CODEC);

    public static final TemperatureRecord DEFAULT_TEMPERATURE = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
    public static final double DEFAULT_RELATIVE_HUMIDITY = 0.5;

    /**
     * Stores a temperature record in {@link com.github.thedeathlycow.thermoo.api.util.TemperatureUnit a unit} such as
     * Celsius, Fahrenheit, Kelvin, or Rankine.
     */
    public static final ComponentType<TemperatureRecord> TEMPERATURE = register(
            "temperature",
            builder -> builder.codec(TemperatureRecord.CODEC)
    );

    public static final ComponentType<List<TemperatureShiftComponentType>> TEMPERATURE_SHIFT = register(
            "temperature_shift",
            builder -> builder.codec(TemperatureShiftComponentType.CODEC)
    );


    /**
     * Stores relative humidity on a 0-1 percentage scale.
     * <p>
     * Relative humidity is defined as "the ratio of how much water vapour is in the air to how much water vapour the
     * air could potentially contain" <a href="https://en.m.wikipedia.org/wiki/Humidity#Relative_humidity">[1]</a> and
     * is expressed here on a 0-1 scale.
     */
    public static final ComponentType<Double> RELATIVE_HUMIDITY = register(
            "relative_humidity",
            builder -> builder.codec(Codec.doubleRange(0, 1))
    );

    private static <T> ComponentType<T> register(
            String name,
            UnaryOperator<ComponentType.Builder<T>> builderOperator
    ) {
        return Registry.register(
                ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE,
                Thermoo.id(name),
                builderOperator.apply(ComponentType.builder())
                        .build()
        );
    }

    private EnvironmentComponentTypes() {
    }
}
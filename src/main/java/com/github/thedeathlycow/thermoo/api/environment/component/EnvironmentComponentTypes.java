package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registry;

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

    /**
     * Stores a temperature reading in {@link com.github.thedeathlycow.thermoo.api.util.TemperatureUnit a unit} such as
     * Celsius, Fahrenheit, Kelvin, or Rankine.
     */
    public static final ComponentType<TemperatureRecord> TEMPERATURE = register(
            "temperature",
            builder -> builder.codec(TemperatureReadingComponent.CODEC)
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
            builder -> builder.codec(RelativeHumidityComponent.CODEC)
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
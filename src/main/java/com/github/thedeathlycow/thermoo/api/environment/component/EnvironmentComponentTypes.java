package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;

/**
 * Stores the codec and component type keys for Thermoo's environment component map.
 * <p>
 * Mods may define their own component types in their own classes, they only need be registered to
 * {@link ThermooRegistries#ENVIRONMENT_COMPONENT_TYPE}.
 */
public final class EnvironmentComponentTypes {
    public static final Codec<DataComponentType<?>> COMPONENT_TYPE_CODEC = Codec.lazyInitialized(
            ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE::byNameCodec
    );
    public static final Codec<DataComponentMap> COMPONENT_MAP_CODEC = DataComponentMap.makeCodec(COMPONENT_TYPE_CODEC);

    /**
     * Stores a temperature reading in {@link com.github.thedeathlycow.thermoo.api.util.TemperatureUnit a unit} such as
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

    private static <T> DataComponentType<T> register(
            String name,
            UnaryOperator<DataComponentType.Builder<T>> builderOperator
    ) {
        return Registry.register(
                ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE,
                Thermoo.id(name),
                builderOperator.apply(DataComponentType.builder())
                        .build()
        );
    }

    private EnvironmentComponentTypes() {
    }
}
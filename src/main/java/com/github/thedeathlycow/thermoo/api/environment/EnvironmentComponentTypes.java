package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public final class EnvironmentComponentTypes {
    public static final Codec<ComponentType<?>> COMPONENT_TYPE_CODEC = Codec.lazyInitialized(
            ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE::getCodec
    );
    public static final Codec<ComponentMap> COMPONENT_MAP_CODEC = ComponentMap.createCodec(COMPONENT_TYPE_CODEC);

    public static final ComponentType<TemperatureRecord> TEMPERATURE = register(
            "temperature",
            builder -> builder.codec(TemperatureRecord.CODEC)
    );

    public static final ComponentType<Double> RELATIVE_HUMIDITY = register(
            "temperature",
            builder -> builder.codec(Codec.doubleRange(0, 1.0))
    );

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
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
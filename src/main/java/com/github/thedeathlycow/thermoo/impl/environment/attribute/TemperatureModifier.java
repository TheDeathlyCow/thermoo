package com.github.thedeathlycow.thermoo.impl.environment.attribute;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.attribute.modifier.AttributeModifier;

import java.util.Map;

public interface TemperatureModifier<Argument> extends AttributeModifier<TemperatureRecord, Argument> {
    Simple ADD = TemperatureRecord::sum;
    Simple SUBTRACT = (a, b) -> a.sum(new TemperatureRecord(-b.value(), b.unit()));
    Simple MINIMUM = (a, b) -> a.compareTo(b) < 0 ? a : b;
    Simple MAXIMUM = (a, b) -> a.compareTo(b) < 0 ? b : a;

    Map<AttributeModifier.OperationId, AttributeModifier<TemperatureRecord, ?>> TEMPERATURE_RECORD_LIBRARY = Map.of(
            AttributeModifier.OperationId.ADD,
            ADD,
            AttributeModifier.OperationId.SUBTRACT,
            SUBTRACT,
            AttributeModifier.OperationId.MINIMUM,
            MINIMUM,
            AttributeModifier.OperationId.MAXIMUM,
            MAXIMUM
    );

    static TemperatureRecord lerp(float delta, TemperatureRecord a, TemperatureRecord b) {
        double interpolated = Mth.lerp(delta, a.value(), b.valueInUnit(a.unit()));
        return new TemperatureRecord(interpolated, a.unit());
    }

    @FunctionalInterface
    interface Simple extends TemperatureModifier<TemperatureRecord> {
        @Override
        default Codec<TemperatureRecord> argumentCodec(EnvironmentAttribute<TemperatureRecord> attribute) {
            return TemperatureRecord.CODEC;
        }

        @Override
        default LerpFunction<TemperatureRecord> argumentKeyframeLerp(EnvironmentAttribute<TemperatureRecord> attribute) {
            return TemperatureModifier::lerp;
        }
    }
}
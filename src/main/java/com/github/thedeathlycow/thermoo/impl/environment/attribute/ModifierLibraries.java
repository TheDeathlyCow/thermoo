package com.github.thedeathlycow.thermoo.impl.environment.attribute;

import com.github.thedeathlycow.thermoo.api.util.v1.TemperatureRecord;
import net.minecraft.world.attribute.modifier.AttributeModifier;

import java.util.Map;

public final class ModifierLibraries {
    public static final Map<AttributeModifier.OperationId, AttributeModifier<Double, ?>> DOUBLE = Map.of(
            AttributeModifier.OperationId.ALPHA_BLEND,
            DoubleModifier.ALPHA_BLEND,
            AttributeModifier.OperationId.ADD,
            DoubleModifier.ADD,
            AttributeModifier.OperationId.SUBTRACT,
            DoubleModifier.SUBTRACT,
            AttributeModifier.OperationId.MULTIPLY,
            DoubleModifier.MULTIPLY,
            AttributeModifier.OperationId.MINIMUM,
            DoubleModifier.MINIMUM,
            AttributeModifier.OperationId.MAXIMUM,
            DoubleModifier.MAXIMUM
    );

    public static final Map<AttributeModifier.OperationId, AttributeModifier<TemperatureRecord, ?>> TEMPERATURE_RECORD = Map.of(
            AttributeModifier.OperationId.ADD,
            TemperatureModifier.ADD,
            AttributeModifier.OperationId.SUBTRACT,
            TemperatureModifier.SUBTRACT,
            AttributeModifier.OperationId.MINIMUM,
            TemperatureModifier.MINIMUM,
            AttributeModifier.OperationId.MAXIMUM,
            TemperatureModifier.MAXIMUM
    );

    private ModifierLibraries() {

    }
}
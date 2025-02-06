package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;

import java.util.Collection;

public final class TemperatureRecordComponent implements MergeableComponent<TemperatureRecordComponent> {
    public static final Codec<TemperatureRecordComponent> CODEC = TemperatureRecord.CODEC
            .xmap(TemperatureRecordComponent::new, TemperatureRecordComponent::temperatureRecord);
    public static final TemperatureRecord ROOM_TEMPERATURE = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
    public static final TemperatureRecordComponent DEFAULT = new TemperatureRecordComponent(ROOM_TEMPERATURE);

    private final TemperatureRecord temperatureRecord;

    public TemperatureRecordComponent(TemperatureRecord temperatureRecord) {
        this.temperatureRecord = temperatureRecord;
    }

    public TemperatureRecord temperatureRecord() {
        return this.temperatureRecord;
    }

    @Override
    public TemperatureRecordComponent mergeWith(Collection<ComponentMap> modifiers) {
        TemperatureRecord total = this.temperatureRecord;
        int count = 0;

        for (ComponentMap modifier : modifiers) {
            TemperatureRecordComponent component = modifier.get(EnvironmentComponentTypes.TEMPERATURE);
            if (component != null) {
                total = total.sum(component.temperatureRecord());
                count++;
            }
        }

        return count > 0
                ? new TemperatureRecordComponent(new TemperatureRecord(total.value() / count, total.unit()))
                : DEFAULT;
    }
}
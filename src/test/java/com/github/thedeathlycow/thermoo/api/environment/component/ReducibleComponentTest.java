package com.github.thedeathlycow.thermoo.api.environment.component;

import com.github.thedeathlycow.thermoo.ThermooTest;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.minecraft.component.ComponentMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReducibleComponentTest {
    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();
    }

    @Test
    void twoTemperatureComponentMaps_onMerge_mergeSucessfully() {
        var t1 = new TemperatureRecordComponent(new TemperatureRecord(20, TemperatureUnit.CELSIUS));
        var t2 = new TemperatureRecordComponent(new TemperatureRecord(10, TemperatureUnit.CELSIUS));

        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t1)
                .add(EnvironmentComponentTypes.TEMPERATURE, t2)
                .build();

        TemperatureRecordComponent mergedComponent = m1.get(EnvironmentComponentTypes.TEMPERATURE);

        Assertions.assertNotNull(mergedComponent);
        Assertions.assertEquals(30, mergedComponent.value().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, mergedComponent.value().unit());
    }

    @Test
    void twoHumidityComponentMaps_onMerge_secondOverwritesFirst() {
        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.1)
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.2)
                .build();

        Double mergedComponent = m1.get(EnvironmentComponentTypes.RELATIVE_HUMIDITY);

        Assertions.assertNotNull(mergedComponent);
        Assertions.assertEquals(0.2, mergedComponent, 1e-4);
    }

    @Test
    void temperatureMergedWhileHumidityKept() {
        var t1 = new TemperatureRecordComponent(new TemperatureRecord(20, TemperatureUnit.CELSIUS));
        var t2 = new TemperatureRecordComponent(new TemperatureRecord(10, TemperatureUnit.CELSIUS));

        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.5)
                .add(EnvironmentComponentTypes.TEMPERATURE, t1)
                .add(EnvironmentComponentTypes.TEMPERATURE, t2)
                .build();


        TemperatureRecordComponent mergedTemperature = m1.get(EnvironmentComponentTypes.TEMPERATURE);

        Assertions.assertNotNull(mergedTemperature);
        Assertions.assertEquals(30, mergedTemperature.value().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, mergedTemperature.value().unit());

        Double mergedHumidity = m1.get(EnvironmentComponentTypes.RELATIVE_HUMIDITY);

        Assertions.assertNotNull(mergedHumidity);
        Assertions.assertEquals(0.5, mergedHumidity, 1e-4);
    }

    @Test
    void temperatureMergedWhileHumidityAdded() {
        var t1 = new TemperatureRecordComponent(new TemperatureRecord(20, TemperatureUnit.CELSIUS));
        var t2 = new TemperatureRecordComponent(new TemperatureRecord(10, TemperatureUnit.CELSIUS));

        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t1)
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.5)
                .add(EnvironmentComponentTypes.TEMPERATURE, t2)
                .build();

        TemperatureRecordComponent mergedTemperature = m1.get(EnvironmentComponentTypes.TEMPERATURE);

        Assertions.assertNotNull(mergedTemperature);
        Assertions.assertEquals(30, mergedTemperature.value().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, mergedTemperature.value().unit());

        Double mergedHumidity = m1.get(EnvironmentComponentTypes.RELATIVE_HUMIDITY);

        Assertions.assertNotNull(mergedHumidity);
        Assertions.assertEquals(0.5, mergedHumidity, 1e-4);
    }
}
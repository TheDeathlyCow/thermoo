package com.github.thedeathlycow.thermoo.api.util.component;

import com.github.thedeathlycow.thermoo.ThermooTest;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.MergedComponentMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class ReducibleComponentMapBuilderTest {
    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();
    }

    @Test
    void changedComponent_onMerge_changesRemain() {
        var t1 = new TemperatureRecordComponent(new TemperatureRecord(20, TemperatureUnit.CELSIUS));
        var t2 = new TemperatureRecordComponent(new TemperatureRecord(10, TemperatureUnit.CELSIUS));
        var t3 = new TemperatureRecordComponent(new TemperatureRecord(-5, TemperatureUnit.CELSIUS));

        MergedComponentMap m1 = new MergedComponentMap(
                ComponentMap.builder()
                        .add(EnvironmentComponentTypes.TEMPERATURE, t1)
                        .build()
        );

        TemperatureRecordComponent baseTemperature = m1.get(EnvironmentComponentTypes.TEMPERATURE);
        Assertions.assertNotNull(baseTemperature);
        Assertions.assertEquals(20, baseTemperature.temperature().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, baseTemperature.temperature().unit());

        m1.set(EnvironmentComponentTypes.TEMPERATURE, t2);

        TemperatureRecordComponent changedTemperature = m1.get(EnvironmentComponentTypes.TEMPERATURE);
        Assertions.assertNotNull(changedTemperature);
        Assertions.assertEquals(10, changedTemperature.temperature().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, changedTemperature.temperature().unit());

        ComponentMap m2 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t3)
                .build();

        ComponentMap merged = ReducibleComponentMapBuilder.create()
                .addAll(m1)
                .addAll(m2)
                .build();

        TemperatureRecordComponent mergedTemperature = merged.get(EnvironmentComponentTypes.TEMPERATURE);

        Assertions.assertNotNull(mergedTemperature);
        Assertions.assertEquals(5.0, mergedTemperature.temperature().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, mergedTemperature.temperature().unit());
    }

    @Test
    void twoTemperatureComponentMaps_onMerge_mergeSucessfully() {
        var t1 = new TemperatureRecordComponent(new TemperatureRecord(20, TemperatureUnit.CELSIUS));
        var t2 = new TemperatureRecordComponent(new TemperatureRecord(10, TemperatureUnit.CELSIUS));

        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t1)
                .build();

        ComponentMap m2 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t2)
                .build();

        ComponentMap merged = ReducibleComponentMapBuilder.create()
                .addAll(m1)
                .addAll(m2)
                .build();

        TemperatureRecordComponent mergedTemperature = merged.get(EnvironmentComponentTypes.TEMPERATURE);

        Assertions.assertNotNull(mergedTemperature);
        Assertions.assertEquals(30, mergedTemperature.temperature().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, mergedTemperature.temperature().unit());
    }

    @Test
    void twoHumidityComponentMaps_onMerge_secondOverwritesFirst() {
        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.1)
                .build();

        ComponentMap m2 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.2)
                .build();

        ComponentMap merged = ReducibleComponentMapBuilder.create()
                .addAll(m1)
                .addAll(m2)
                .build();

        Double mergedHumidity = merged.get(EnvironmentComponentTypes.RELATIVE_HUMIDITY);

        Assertions.assertNotNull(mergedHumidity);
        Assertions.assertEquals(0.2, mergedHumidity, 1e-3);
    }

    @Test
    void temperatureMergedWhileHumidityKept() {
        var t1 = new TemperatureRecordComponent(new TemperatureRecord(20, TemperatureUnit.CELSIUS));
        var t2 = new TemperatureRecordComponent(new TemperatureRecord(10, TemperatureUnit.CELSIUS));

        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t1)
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.1)
                .build();

        ComponentMap m2 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t2)
                .build();

        ComponentMap merged = ReducibleComponentMapBuilder.create()
                .addAll(m1)
                .addAll(m2)
                .build();

        TemperatureRecordComponent mergedTemperature = merged.get(EnvironmentComponentTypes.TEMPERATURE);

        Assertions.assertNotNull(mergedTemperature);
        Assertions.assertEquals(30, mergedTemperature.temperature().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, mergedTemperature.temperature().unit());

        Double mergedHumidity = merged.get(EnvironmentComponentTypes.RELATIVE_HUMIDITY);

        Assertions.assertNotNull(mergedHumidity);
        Assertions.assertEquals(0.1, mergedHumidity, 1e-4);
    }

    @Test
    void temperatureMergedWhileHumidityAdded() {
        var t1 = new TemperatureRecordComponent(new TemperatureRecord(20, TemperatureUnit.CELSIUS));
        var t2 = new TemperatureRecordComponent(new TemperatureRecord(10, TemperatureUnit.CELSIUS));

        ComponentMap m1 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t1)
                .build();

        ComponentMap m2 = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, t2)
                .add(EnvironmentComponentTypes.RELATIVE_HUMIDITY, 0.2)
                .build();

        ComponentMap merged = ReducibleComponentMapBuilder.create()
                .addAll(m1)
                .addAll(m2)
                .build();

        TemperatureRecordComponent mergedTemperature = merged.get(EnvironmentComponentTypes.TEMPERATURE);

        Assertions.assertNotNull(mergedTemperature);
        Assertions.assertEquals(30, mergedTemperature.temperature().value(), 1e-3);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, mergedTemperature.temperature().unit());

        Double mergedHumidity = merged.get(EnvironmentComponentTypes.RELATIVE_HUMIDITY);

        Assertions.assertNotNull(mergedHumidity);
        Assertions.assertEquals(0.2, mergedHumidity, 1e-4);
    }
}
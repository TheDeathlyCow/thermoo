package com.github.thedeathlycow.thermoo.testmod.tests.util.component;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.component.ComponentMap;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;

@SuppressWarnings("unused")
public class ReducibleMapComponentBuilderTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void two_reducible_components_reduce_each_other(TestContext context) {
        ComponentMap map = ReducibleComponentMapBuilder.create()
                .add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(20))
                .add(TestReducibleDoubleComponent.KEY, new TestReducibleDoubleComponent(15))
                .add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(10))
                .add(TestReducibleDoubleComponent.KEY, new TestReducibleDoubleComponent(5))
                .build();

        TemperatureRecordComponent temperature = map.get(EnvironmentComponentTypes.TEMPERATURE);
        context.assertFalse(temperature == null, "Temperature component missing");

        TestReducibleDoubleComponent doubleComponent = map.get(TestReducibleDoubleComponent.KEY);
        context.assertFalse(doubleComponent == null, "Double component missing");

        final TemperatureRecord expectedTemp = new TemperatureRecord(30);
        context.assertTrue(
                temperature.temperature().equals(expectedTemp, 1e-2),
                "Temperature was expected to be " + expectedTemp + " but was " + temperature.temperature()
        );

        final double expectedDouble = 20;
        context.assertTrue(
                Math.abs(doubleComponent.value() - expectedDouble) <= 1e-2,
                "Double was expected to be " + expectedDouble + " but was " + doubleComponent.value()
        );

        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void two_reducible_components_from_maps_reduce_each_other(TestContext context) {
        ComponentMap base = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(20))
                .add(TestReducibleDoubleComponent.KEY, new TestReducibleDoubleComponent(15))
                .build();

        ComponentMap changes = ComponentMap.builder()
                .add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(10))
                .add(TestReducibleDoubleComponent.KEY, new TestReducibleDoubleComponent(5))
                .build();

        ComponentMap reduced = ReducibleComponentMapBuilder.create()
                .addAll(base)
                .addAll(changes)
                .build();

        TemperatureRecordComponent temperature = reduced.get(EnvironmentComponentTypes.TEMPERATURE);
        context.assertFalse(temperature == null, "Temperature component missing");

        TestReducibleDoubleComponent doubleComponent = reduced.get(TestReducibleDoubleComponent.KEY);
        context.assertFalse(doubleComponent == null, "Double component missing");

        final TemperatureRecord expectedTemp = new TemperatureRecord(30);
        context.assertTrue(
                temperature.temperature().equals(expectedTemp, 1e-2),
                "Temperature was expected to be " + expectedTemp + " but was " + temperature.temperature()
        );

        final double expectedDouble = 20;
        context.assertTrue(
                Math.abs(doubleComponent.value() - expectedDouble) <= 1e-2,
                "Double was expected to be " + expectedDouble + " but was " + doubleComponent.value()
        );

        context.complete();
    }
}
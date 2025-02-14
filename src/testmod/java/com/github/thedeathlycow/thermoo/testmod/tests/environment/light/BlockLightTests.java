package com.github.thedeathlycow.thermoo.testmod.tests.environment.light;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.ConstantEnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.environment.provider.LightThresholdLightProvider;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.github.thedeathlycow.thermoo.testmod.tests.environment.EnvironmentTestHelper;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockLightTests {
    private static final Supplier<EnvironmentProvider> PROVIDER = () -> LightThresholdLightProvider.builder(
            8,
            RegistryEntry.of(
                    ConstantEnvironmentProvider.create(
                            ComponentMap.builder()
                                    .add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(30))
                    )
            ),
            RegistryEntry.of(
                    ConstantEnvironmentProvider.create(
                            ComponentMap.builder()
                                    .add(EnvironmentComponentTypes.TEMPERATURE, new TemperatureRecordComponent(10))
                    )
            )
    ).withLightType(LightType.BLOCK).build();

    @GameTest(
            templateName = "thermoo-test:block_light_level_9_test",
            skyAccess = true
    )
    public void light_above_is_30c(TestContext context) {
        context.waitAndRun(10L, () -> {
            BlockPos center = new BlockPos(2, 1, 2);
            BlockPos centerAbsolute = context.getAbsolutePos(center);

            LightTestHelper.expectLightLevel(context, center, LightType.BLOCK, 9);

            TemperatureRecordComponent temperature = EnvironmentTestHelper.getTemperature(context, center, PROVIDER.get());

            context.assertFalse(temperature == null, "Temperature missing from provider");
            EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature.temperature().valueInUnit(TemperatureUnit.CELSIUS));

            context.complete();
        });
    }

    @GameTest(
            templateName = "thermoo-test:block_light_level_9_test",
            skyAccess = true
    )
    public void light_below_is_10c(TestContext context) {
        context.waitAndRun(10L, () -> {
            BlockPos corner = new BlockPos(1, 1, 1);
            BlockPos cornerAbsolute = context.getAbsolutePos(corner);

            LightTestHelper.expectLightLevel(context, corner, LightType.BLOCK, 7);

            TemperatureRecordComponent temperature = EnvironmentTestHelper.getTemperature(context, corner, PROVIDER.get());

            context.assertFalse(temperature == null, "Temperature missing from provider");
            EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, temperature.temperature().valueInUnit(TemperatureUnit.CELSIUS));

            context.complete();
        });
    }

    @GameTest(
            templateName = "thermoo-test:block_light_level_9_test",
            skyAccess = true
    )
    public void light_at_threshold_is_30c(TestContext context) {
        context.waitAndRun(10L, () -> {
            BlockPos centerOffset = new BlockPos(2, 1, 3);
            BlockPos centerOffsetAbsolute = context.getAbsolutePos(centerOffset);

            LightTestHelper.expectLightLevel(context, centerOffset, LightType.BLOCK, 8);

            TemperatureRecordComponent temperature = EnvironmentTestHelper.getTemperature(context, centerOffset, PROVIDER.get());

            context.assertFalse(temperature == null, "Temperature missing from provider");
            EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature.temperature().valueInUnit(TemperatureUnit.CELSIUS));

            context.complete();
        });
    }
}
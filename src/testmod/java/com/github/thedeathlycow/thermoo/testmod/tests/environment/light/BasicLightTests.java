package com.github.thedeathlycow.thermoo.testmod.tests.environment.light;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.testmod.tests.environment.EnvironmentTestHelper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
public class BasicLightTests {
    @GameTest(
            templateName = "thermoo-test:light_level_12_test",
            skyAccess = true
    )
    public void light_below_is_10c(TestContext context) {
        context.waitAndRun(10L, () -> {
            BlockPos center = new BlockPos(2, 1, 2);
            BlockPos centerAbsolute = context.getAbsolutePos(center);

            context.assertEquals(context.getWorld().getLightLevel(centerAbsolute), 12, "light level");

            RegistryEntry<EnvironmentProvider> provider = EnvironmentTestHelper.getEnvironmentProvider(context, "light/basic_light");
            TemperatureRecordComponent temperature = provider.value().findCurrentComponents(
                    context.getWorld(),
                    centerAbsolute,
                    context.getWorld().getBiome(centerAbsolute)
            ).get(EnvironmentComponentTypes.TEMPERATURE);

            context.assertFalse(temperature == null, "Temperature missing from provider");
            EnvironmentTestHelper.assertTemperatureEquals(context, 10.0, temperature.temperature().valueInUnit(TemperatureUnit.CELSIUS));

            context.complete();
        });
    }

    @GameTest(
            templateName = "thermoo-test:light_level_12_test",
            skyAccess = true
    )
    public void light_above_is_30c(TestContext context) {
        context.waitAndRun(10L, () -> {
            BlockPos corner = new BlockPos(0, 1, 0);
            BlockPos cornerAbsolute = context.getAbsolutePos(corner);

            context.assertEquals(context.getWorld().getLightLevel(cornerAbsolute), 14, "light level");

            RegistryEntry<EnvironmentProvider> provider = EnvironmentTestHelper.getEnvironmentProvider(context, "light/basic_light");
            TemperatureRecordComponent temperature = provider.value().findCurrentComponents(
                    context.getWorld(),
                    cornerAbsolute,
                    context.getWorld().getBiome(cornerAbsolute)
            ).get(EnvironmentComponentTypes.TEMPERATURE);

            context.assertFalse(temperature == null, "Temperature missing from provider");
            EnvironmentTestHelper.assertTemperatureEquals(context, 30, temperature.temperature().valueInUnit(TemperatureUnit.CELSIUS));

            context.complete();
        });
    }

    @GameTest(
            templateName = "thermoo-test:light_level_12_test",
            skyAccess = true
    )
    public void light_at_threshold_is_30c(TestContext context) {
        context.waitAndRun(10L, () -> {
            BlockPos centerOffset = new BlockPos(2, 1, 3);
            BlockPos centerOffsetAbsolute = context.getAbsolutePos(centerOffset);

            context.assertEquals(context.getWorld().getLightLevel(centerOffsetAbsolute), 13, "light level");

            RegistryEntry<EnvironmentProvider> provider = EnvironmentTestHelper.getEnvironmentProvider(context, "light/basic_light");
            TemperatureRecordComponent temperature = provider.value().findCurrentComponents(
                    context.getWorld(),
                    centerOffsetAbsolute,
                    context.getWorld().getBiome(centerOffsetAbsolute)
            ).get(EnvironmentComponentTypes.TEMPERATURE);

            context.assertFalse(temperature == null, "Temperature missing from provider");
            EnvironmentTestHelper.assertTemperatureEquals(context, 30.0, temperature.temperature().valueInUnit(TemperatureUnit.CELSIUS));

            context.complete();
        });
    }
}
package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.gametest.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class ReferenceConstantBirchForestTests implements CustomTestMethodInvoker {
    private static final String ENVIRONMENT = ThermooTestMod.MODID + ":no_seasons";

    @GameTest(environment = ENVIRONMENT)
    public void birch_forest_temperature_is_22c(TestContext context) {
        World world = context.getWorld();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.BIRCH_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(context, 22.0, temperature);

        context.complete();
    }

    @GameTest(environment = ENVIRONMENT)
    public void birch_forest_humidity_is_52pc(TestContext context) {
        World world = context.getWorld();

        double humidity = EnvironmentTestHelper.getBiomeHumidity(context, world, BiomeKeys.BIRCH_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(context, 0.52, humidity);

        context.complete();
    }

    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        EnvironmentTestHelper.expectTemperatureSeason(context, null);
        EnvironmentTestHelper.expectTropicalSeason(context, null);
        method.invoke(this, context);
    }
}
package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class ReferenceConstantBirchForestTests implements CustomTestMethodInvoker {

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void birch_forest_temperature_is_22c(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double temperature = EnvironmentTestHelper.getBiomeTemperature(helper, level, Biomes.BIRCH_FOREST);
        EnvironmentTestHelper.assertTemperatureEquals(helper, 22.0, temperature);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void birch_forest_humidity_is_52pc(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double humidity = EnvironmentTestHelper.getBiomeHumidity(helper, level, Biomes.BIRCH_FOREST);
        EnvironmentTestHelper.assertHumidityEquals(helper, 0.52, humidity);

        helper.succeed();
    }

    @Override
    public void invokeTestMethod(GameTestHelper helper, Method method) throws ReflectiveOperationException {
        EnvironmentTestHelper.expectTemperateSeason(helper, null);
        EnvironmentTestHelper.expectTropicalSeason(helper, null);
        method.invoke(this, helper);
    }
}
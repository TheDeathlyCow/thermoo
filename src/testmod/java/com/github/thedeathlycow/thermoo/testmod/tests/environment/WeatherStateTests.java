package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class WeatherStateTests {
    @BeforeBatch(batch = "snowyTaiga_rainy")
    public void setRainyForRainy(ServerLevel world) {
        world.setWeatherParameters(0, 20000000, true, false);
        world.setRainLevel(1f);
    }

    @AfterBatch(batch = "snowyTaiga_rainy")
    public void setClearForRainy(ServerLevel world) {
        world.setWeatherParameters(20000000, 0, false, false);
        world.setRainLevel(0f);
        world.setThunderLevel(0f);
    }

    @BeforeBatch(batch = "snowyTaiga_sunny")
    public void setClearForSunny(ServerLevel world) {
        world.setWeatherParameters(20000000, 0, false, false);
        world.setRainLevel(0f);
    }

    @BeforeBatch(batch = "snowyTaiga_thundering")
    public void setRainyForThundering(ServerLevel world) {
        world.setWeatherParameters(0, 20000000, true, true);
        world.setRainLevel(1f);
        world.setThunderLevel(1f);
    }

    @AfterBatch(batch = "snowyTaiga_thundering")
    public void setClearForThundering(ServerLevel world) {
        world.setWeatherParameters(20000000, 0, false, false);
        world.setRainLevel(0f);
        world.setThunderLevel(0f);
    }

    @GameTest(
            template = FabricGameTest.EMPTY_STRUCTURE,
            batch = "snowyTaiga_sunny"
    )
    public void snowy_taiga_during_sunny_is_neg5c(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.SNOWY_TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

        context.succeed();
    }

    @GameTest(
            template = FabricGameTest.EMPTY_STRUCTURE,
            batch = "snowyTaiga_rainy"
    )
    public void snowy_taiga_during_rainy_is_neg10c(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.SNOWY_TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(context, -10.0, temperature);

        context.succeed();
    }

    @GameTest(
            template = FabricGameTest.EMPTY_STRUCTURE,
            batch = "snowyTaiga_thundering"
    )
    public void snowy_taiga_during_thunder_is_neg15c(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.SNOWY_TAIGA);
        EnvironmentTestHelper.assertTemperatureEquals(context, -15.0, temperature);

        context.succeed();
    }
}
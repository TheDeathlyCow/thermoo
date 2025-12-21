package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@SuppressWarnings("unused")
@GameTestHolder(ThermooTestMod.MODID)
@PrefixGameTestTemplate(false)
public class PrecipitationTypeTests {
    @BeforeBatch(batch = "snowyPlains_rainy")
    public void setRainyForRainy(ServerLevel world) {
        world.setWeatherParameters(0, 20000000, false, false);
        world.setRainLevel(1f);
    }

    @AfterBatch(batch = "snowyPlains_rainy")
    public void setClearForRainy(ServerLevel world) {
        world.setWeatherParameters(20000000, 0, false, false);
        world.setRainLevel(0f);
    }

    @BeforeBatch(batch = "snowyPlains_sunny")
    public void setClearForSunny(ServerLevel world) {
        world.setWeatherParameters(20000000, 0, false, false);
        world.setRainLevel(0f);
    }

    @GameTest(
            template = ThermooTestMod.EMPTY_STRUCTURE,
            batch = "snowyPlains_rainy"
    )
    public void snowy_plains_has_snowy_temperature_when_not_raining(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.SNOWY_PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

        context.succeed();
    }

    @GameTest(
            template = ThermooTestMod.EMPTY_STRUCTURE,
            batch = "snowyPlains_sunny"
    )
    public void snowy_plains_has_snowy_temperature_when_raining(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        EnvironmentTestHelper.setSeasons(context, null, null);

        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, Biomes.SNOWY_PLAINS);
        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);

        context.succeed();
    }
}
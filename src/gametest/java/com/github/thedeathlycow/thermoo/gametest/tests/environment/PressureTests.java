package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.AtmosphericPressureComponent;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;

@SuppressWarnings("unused")
public class PressureTests  {
    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_default_at_sea_level(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getSeaLevel(), Biomes.JAGGED_PEAKS);
        EnvironmentTestHelper.assertPressureEquals(helper, AtmosphericPressureComponent.DEFAULT, pressure);

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_low_pressure_at_max_altitude(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getMaxY(), Biomes.JAGGED_PEAKS);

        helper.assertTrue(
                pressure < AtmosphericPressureComponent.DEFAULT,
                Component.literal("Measured pressure " + pressure + " mbar was not less than " + AtmosphericPressureComponent.DEFAULT + " mbar")
        );

        helper.succeed();
    }

    @GameTest(environment = EnvironmentTestHelper.NO_SEASONS)
    public void jagged_peaks_is_high_pressure_at_min_altitude(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        double pressure = EnvironmentTestHelper.getBiomePressure(helper, level, level.getMinY(), Biomes.JAGGED_PEAKS);

        helper.assertTrue(
                pressure > AtmosphericPressureComponent.DEFAULT,
                Component.literal("Measured pressure " + pressure + " mbar was not greater than " + AtmosphericPressureComponent.DEFAULT + " mbar")
        );

        helper.succeed();
    }
}
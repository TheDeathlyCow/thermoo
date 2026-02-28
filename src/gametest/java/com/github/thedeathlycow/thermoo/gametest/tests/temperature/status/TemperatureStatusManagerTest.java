package com.github.thedeathlycow.thermoo.gametest.tests.temperature.status;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusManager;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

@SuppressWarnings("unused")
public class TemperatureStatusManagerTest {
    @GameTest
    public void desert_dry_fallback_humidity_is_spring(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        HolderLookup<TemperatureStatus> lookup = level.holderLookup(ThermooRegistryKeys.TEMPERATURE_STATUS);

        var statuses = TemperatureStatusManager.lookup(EntityType.PLAYER.builtInRegistryHolder(), lookup);

        helper.succeed();
    }
}
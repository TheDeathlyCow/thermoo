package com.github.thedeathlycow.thermoo.gametest.init.tick;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import dev.yumi.commons.TriState;
import net.minecraft.world.level.gamerules.GameRule;

public final class TestEnvironmentChanges {
    /**
     * Gamerule to enable/disable environment changes for testing purposes
     */
    public static final GameRule<Boolean> APPLY_ENVIRONMENT_CHANGES = ThermooServices.GAME_RULES.forBoolean(
            ThermooTestMod.id("apply_environment_changes"),
            true
    );

    private static final TemperatureRecord COLD_TEMPERATURE = new TemperatureRecord(5, TemperatureUnit.CELSIUS);
    private static final TemperatureRecord WARM_TEMPERATURE = new TemperatureRecord(25, TemperatureUnit.CELSIUS);

    public static void initialize() {
        ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_UPDATE.register(context -> TriState.from(context.level().getGameRules().get(APPLY_ENVIRONMENT_CHANGES)));

        ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.register(context -> {
            TemperatureRecord temperature = context.components()
                    .getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT);

            if (temperature.compareTo(COLD_TEMPERATURE) < 0) {
                return -2;
            } else if (temperature.compareTo(WARM_TEMPERATURE) > 0) {
                return 2;
            } else {
                return 0;
            }
        });
    }

    private TestEnvironmentChanges() {

    }
}
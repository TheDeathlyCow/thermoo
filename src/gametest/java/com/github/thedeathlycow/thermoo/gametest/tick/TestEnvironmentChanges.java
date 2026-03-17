package com.github.thedeathlycow.thermoo.gametest.tick;

import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.v2.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.gametest.ThermooTestMod;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.NotNull;

public final class TestEnvironmentChanges {
    /**
     * Gamerule to enable/disable environment changes for testing purposes
     */
    public static final GameRule<@NotNull Boolean> APPLY_ENVIRONMENT_CHANGES =
            GameRuleBuilder.forBoolean(true)
                            .buildAndRegister(ThermooTestMod.id("apply_environment_changes"));

    private static final TemperatureRecord COLD_TEMPERATURE = new TemperatureRecord(5, TemperatureUnit.CELSIUS);
    private static final TemperatureRecord WARM_TEMPERATURE = new TemperatureRecord(25, TemperatureUnit.CELSIUS);

    public static void initialize() {
        ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_UPDATE.register(context -> TriState.of(context.level().getGameRules().get(APPLY_ENVIRONMENT_CHANGES)));

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
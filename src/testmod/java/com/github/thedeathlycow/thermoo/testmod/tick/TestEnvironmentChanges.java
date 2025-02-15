package com.github.thedeathlycow.thermoo.testmod.tick;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.GameRules;

public final class TestEnvironmentChanges {
    /**
     * Gamerule to enable/disable environment changes for testing purposes
     */
    public static final GameRules.Key<GameRules.BooleanRule> APPLY_ENVIRONMENT_CHANGES =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".applyEnvironmentChanges",
                    GameRules.Category.MISC,
                    GameRuleFactory.createBooleanRule(true)
            );

    private static final TemperatureRecord COLD_TEMPERATURE = new TemperatureRecord(5, TemperatureUnit.CELSIUS);
    private static final TemperatureRecord WARM_TEMPERATURE = new TemperatureRecord(25, TemperatureUnit.CELSIUS);

    public static void initialize() {
        ServerPlayerEnvironmentTickEvents.ALLOW_TEMPERATURE_UPDATE.register(context -> TriState.of(context.world().getGameRules().get(APPLY_ENVIRONMENT_CHANGES).get()));

        ServerPlayerEnvironmentTickEvents.GET_TEMPERATURE_CHANGE.register(context -> {
            TemperatureRecordComponent temperature = context.components()
                    .getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT);

            if (temperature.temperature().compareTo(COLD_TEMPERATURE) < 0) {
                return -2;
            } else if (temperature.temperature().compareTo(WARM_TEMPERATURE) > 0) {
                return 2;
            } else {
                return 0;
            }
        });
    }

    private TestEnvironmentChanges() {

    }
}
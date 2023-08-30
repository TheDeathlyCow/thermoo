package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.testmod.config.ThermooConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ThermooTestMod implements ModInitializer {


    /**
     * Gamerule to enable/disable passive changes for testing purposes
     */
    public static final GameRules.Key<GameRules.BooleanRule> APPLY_PASSIVE_CHANGES =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".applyPassiveChanges",
                    GameRules.Category.MISC,
                    GameRuleFactory.createBooleanRule(true)
            );

    private static final ThermooConfig config = new ThermooConfig();

    @Override
    public void onInitialize() {
        PlayerEnvironmentEvents.CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE
                .register(
                        (change, player) -> player.getWorld().getGameRules().getBoolean(APPLY_PASSIVE_CHANGES)
                );
        EnvironmentControllerInitializeEvent.EVENT.register(TestmodController::new);
    }

    public static ThermooConfig getConfig() {
        return config;
    }

}

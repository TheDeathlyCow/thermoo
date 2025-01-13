package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.testmod.config.ThermooConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.world.ServerWorld;
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
                        (change, player) -> {
                            if (player.getWorld() instanceof ServerWorld serverWorld) {
                                boolean applyPassiveChanges = serverWorld.getGameRules().getBoolean(APPLY_PASSIVE_CHANGES);
                                return TriState.of(applyPassiveChanges);
                            }
                            return TriState.DEFAULT;
                        }
                );
        EnvironmentControllerInitializeEvent.EVENT.register(TestmodController::new);

        ThermooAttributes.baseValueEvent(ThermooAttributes.MIN_TEMPERATURE).register((entity, baseValue) -> 40);
        ThermooAttributes.baseValueEvent(ThermooAttributes.MAX_TEMPERATURE).register((entity, baseValue) -> 40);
    }

    public static ThermooConfig getConfig() {
        return config;
    }

}

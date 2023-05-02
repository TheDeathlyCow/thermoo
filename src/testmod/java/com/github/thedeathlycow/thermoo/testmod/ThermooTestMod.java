package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.event.*;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.biome.Biome;

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

    @Override
    public void onInitialize() {
        PlayerEnvironmentEvents.TICK_BIOME_TEMPERATURE_CHANGE.register(this::applyTemperatureChanges);
        LivingEntityEnvironmentEvents.TICK_IN_HEATED_LOCATION.register(this::tickHeatSources);
        LivingEntityEnvironmentEvents.TICK_HEAT_EFFECTS.register(this::tickHeatEffects);
        LivingEntityEnvironmentEvents.TICK_IN_WET_LOCATION.register(this::tickWetChange);
        LivingEntityTemperatureEvents.ON_STEPPED_ON_HOT_FLOOR.register((e, s, t) -> true);
    }

    public void applyTemperatureChanges(
            EnvironmentController controller,
            PlayerEntity player,
            Biome biome,
            InitialTemperatureChangeResult result
    ) {
        if (player.getWorld().getGameRules().getBoolean(APPLY_PASSIVE_CHANGES)) {
            result.applyInitialChange();
        }
    }

    public void tickHeatSources(
            EnvironmentController controller,
            LivingEntity entity,
            InitialTemperatureChangeResult result
    ) {
        if (entity.getWorld().getGameRules().getBoolean(APPLY_PASSIVE_CHANGES)) {
            result.applyInitialChange();
        }
    }

    public void tickHeatEffects(
            EnvironmentController controller,
            LivingEntity entity,
            InitialTemperatureChangeResult result
    ) {
        if (entity.getWorld().getGameRules().getBoolean(APPLY_PASSIVE_CHANGES)) {
            result.applyInitialChange();
        }
    }

    public void tickWetChange(
            EnvironmentController controller,
            LivingEntity entity,
            InitialSoakChangeResult result
    ) {
        if (entity.getWorld().getGameRules().getBoolean(APPLY_PASSIVE_CHANGES)) {
            result.applyInitialChange();
        }
    }


}

package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonEvents;
import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.testmod.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.testmod.tests.util.component.TestReducibleDoubleComponent;
import com.github.thedeathlycow.thermoo.testmod.tick.TestEnvironmentChanges;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.Optional;

public class ThermooTestMod implements ModInitializer {
    public static final GameRules.Key<GameRules.IntRule> CURRENT_SEASON =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".setTestSeason",
                    GameRules.Category.MISC,
                    GameRuleFactory.createIntRule(0, 0, 4)
            );

    public static final GameRules.Key<GameRules.IntRule> CURRENT_TROPICAL_SEASON =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".setTestTropicalSeason",
                    GameRules.Category.MISC,
                    GameRuleFactory.createIntRule(0, 0, 2)
            );

    private static final ThermooConfig config = new ThermooConfig();

    @Override
    public void onInitialize() {
        EnvironmentControllerInitializeEvent.EVENT.register(TestmodController::new);

        ThermooAttributes.baseValueEvent(ThermooAttributes.MIN_TEMPERATURE).register((entity, baseValue) -> 40);
        ThermooAttributes.baseValueEvent(ThermooAttributes.MAX_TEMPERATURE).register((entity, baseValue) -> 40);

        TestEnvironmentChanges.initialize();

        ThermooSeasonEvents.GET_CURRENT_SEASON.register(
                world -> Optional.ofNullable(switch (world.getServer().getGameRules().getInt(CURRENT_SEASON)) {
                    case 1 -> ThermooSeason.SPRING;
                    case 2 -> ThermooSeason.SUMMER;
                    case 3 -> ThermooSeason.AUTUMN;
                    case 4 -> ThermooSeason.WINTER;
                    default -> null;
                })
        );

        ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.register(
                (world, pos) -> Optional.ofNullable(switch (world.getServer().getGameRules().getInt(CURRENT_TROPICAL_SEASON)) {
                    case 1 -> ThermooSeason.TROPICAL_WET;
                    case 2 -> ThermooSeason.TROPICAL_DRY;
                    default -> null;
                })
        );

        Registry.register(
                ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE,
                Identifier.of("thermoo-test", "test_reducible_double"),
                TestReducibleDoubleComponent.KEY
        );
    }

    public static ThermooConfig getConfig() {
        return config;
    }

}

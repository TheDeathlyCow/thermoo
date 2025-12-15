package com.github.thedeathlycow.thermoo.gametest;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonEvents;
import com.github.thedeathlycow.thermoo.gametest.tests.item.ModifyItemAttributeModifiersTest;
import com.github.thedeathlycow.thermoo.gametest.tick.TestEnvironmentChanges;
import com.github.thedeathlycow.thermoo.gametest.tick.TestSoakableChanges;
import com.github.thedeathlycow.thermoo.gametest.tick.TestTemperatureChanges;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameRules;

import java.util.Optional;

public class ThermooTestMod implements ModInitializer {
    public static final String MODID = Thermoo.MODID + "-test";
    public static final GameRules.Key<GameRules.IntegerValue> CURRENT_SEASON =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".setTestSeason",
                    GameRules.Category.MISC,
                    GameRuleFactory.createIntRule(0, 0, 4)
            );

    public static final GameRules.Key<GameRules.IntegerValue> CURRENT_TROPICAL_SEASON =
            GameRuleRegistry.register(
                    Thermoo.MODID + ".setTestTropicalSeason",
                    GameRules.Category.MISC,
                    GameRuleFactory.createIntRule(0, 0, 2)
            );

    @Override
    public void onInitialize() {
        ThermooAttributes.baseValueEvent(ThermooAttributes.MIN_TEMPERATURE).register((entity, baseValue) -> 40);
        ThermooAttributes.baseValueEvent(ThermooAttributes.MAX_TEMPERATURE).register((entity, baseValue) -> 40);

        TestTemperatureChanges.initialize();
        TestSoakableChanges.initialize();
        TestEnvironmentChanges.initialize();
        ModifyItemAttributeModifiersTest.initialize();

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
    }

    public static Identifier location(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
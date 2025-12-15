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
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ThermooTestMod implements ModInitializer {
    public static final String MODID = Thermoo.MODID + "-test";
    public static final GameRule<@NotNull ThermooSeason> CURRENT_SEASON =
            GameRuleBuilder.forEnum(ThermooSeason.SPRING)
                            .buildAndRegister(id("setTestSeason"));

    public static final GameRule<@NotNull ThermooSeason> CURRENT_TROPICAL_SEASON =
            GameRuleBuilder.forEnum(ThermooSeason.TROPICAL_DRY)
                    .buildAndRegister(id("setTestTropicalSeason"));

    @Override
    public void onInitialize() {
        ThermooAttributes.baseValueEvent(ThermooAttributes.MIN_TEMPERATURE).register((entity, baseValue) -> 40);
        ThermooAttributes.baseValueEvent(ThermooAttributes.MAX_TEMPERATURE).register((entity, baseValue) -> 40);

        TestTemperatureChanges.initialize();
        TestSoakableChanges.initialize();
        TestEnvironmentChanges.initialize();
        ModifyItemAttributeModifiersTest.initialize();

        ThermooSeasonEvents.GET_CURRENT_SEASON.register(
                level -> {
                    if (level instanceof ServerLevel serverLevel) {
                        return Optional.of(serverLevel.getGameRules().get(CURRENT_SEASON));
                    } else {
                        return Optional.empty();
                    }
                }
        );

        ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.register(
                (level, pos) -> {
                    if (level instanceof ServerLevel serverLevel) {
                        return Optional.of(serverLevel.getGameRules().get(CURRENT_TROPICAL_SEASON));
                    } else {
                        return Optional.empty();
                    }
                }
        );
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
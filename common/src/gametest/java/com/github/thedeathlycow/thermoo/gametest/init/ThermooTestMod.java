package com.github.thedeathlycow.thermoo.gametest.init;

import com.github.thedeathlycow.thermoo.api.core.v2.ThermooCodecs;
import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonEvents;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.gametest.tests.item.ModifyItemAttributeModifiersTest;
import com.github.thedeathlycow.thermoo.gametest.init.tick.TestEnvironmentChanges;
import com.github.thedeathlycow.thermoo.gametest.init.tick.TestSoakableChanges;
import com.github.thedeathlycow.thermoo.gametest.init.tick.TestTemperatureChanges;
import com.github.thedeathlycow.thermoo.gametest.util.ThermooGameTestModInitializer;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.Optional;

public final class ThermooTestMod {
    public static final String MODID = Thermoo.MODID + "-test";

    public static final GameRule<Boolean> ENABLE_SEASONS = ThermooServices.GAME_RULES.forBoolean(id("enable_seasons"), false);

    public static final GameRule<Boolean> ENABLE_TROPICAL_SEASONS = ThermooServices.GAME_RULES.forBoolean(id("enable_tropical_seasons"), false);

    public static final GameRule<TemperateSeason> CURRENT_SEASON = ThermooServices.GAME_RULES.forEnum(
            id("set_test_season"),
            TemperateSeason.SPRING,
            ThermooCodecs.createEnumCodec(TemperateSeason.class)
    );

    public static final GameRule<TropicalSeason> CURRENT_TROPICAL_SEASON = ThermooServices.GAME_RULES.forEnum(
            id("set_test_tropical_season"),
            TropicalSeason.MILD,
            ThermooCodecs.createEnumCodec(TropicalSeason.class)
    );

    public static void onInitialize() {
        ThermooGameTestModInitializer.onInitialize();
        ThermooAttributes.baseValueEvent(ThermooAttributes.MIN_TEMPERATURE).register((_, _) -> 40);
        ThermooAttributes.baseValueEvent(ThermooAttributes.MAX_TEMPERATURE).register((_, _) -> 40);

        TestTemperatureChanges.initialize();
        TestSoakableChanges.initialize();
        TestEnvironmentChanges.initialize();
        ModifyItemAttributeModifiersTest.initialize();

        ThermooSeasonEvents.GET_CURRENT_SEASON.register(
                (level, pos) -> {
                    if (level instanceof ServerLevel serverLevel) {
                        GameRules rules = serverLevel.getGameRules();

                        return rules.get(ENABLE_SEASONS)
                                ? Optional.of(serverLevel.getGameRules().get(CURRENT_SEASON).createState())
                                : Optional.empty();
                    } else {
                        return Optional.empty();
                    }
                }
        );

        ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.register(
                (level, pos) -> {
                    if (level instanceof ServerLevel serverLevel) {
                        GameRules rules = serverLevel.getGameRules();

                        return rules.get(ENABLE_TROPICAL_SEASONS)
                                ? Optional.of(serverLevel.getGameRules().get(CURRENT_TROPICAL_SEASON).createState())
                                : Optional.empty();
                    } else {
                        return Optional.empty();
                    }
                }
        );
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    private ThermooTestMod() {

    }
}
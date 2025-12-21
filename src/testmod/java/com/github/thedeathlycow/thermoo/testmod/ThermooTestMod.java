package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialEvents;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonEvents;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.testmod.tests.item.ModifyItemAttributeModifiersTest;
import com.github.thedeathlycow.thermoo.testmod.tick.TestEnvironmentChanges;
import com.github.thedeathlycow.thermoo.testmod.tick.TestSoakableChanges;
import com.github.thedeathlycow.thermoo.testmod.tick.TestTemperatureChanges;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.common.Mod;
import org.sinytra.fabric.gametest_api.generated.GeneratedEntryPoint;

import java.util.Optional;

@Mod(ThermooTestMod.MODID)
public class ThermooTestMod {
    public static final String MODID = Thermoo.MODID + "_test";
    public static final String EMPTY_STRUCTURE = "empty";
    public static final String EMPTY_STRUCTURE_MODID = GeneratedEntryPoint.MOD_ID;

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

    public ThermooTestMod() {
        ArmorMaterialEvents.GET_FROST_RESISTANCE.register(ArmorMaterialListener.COLD);
        ArmorMaterialEvents.GET_HEAT_RESISTANCE.register(ArmorMaterialListener.HEAT);
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

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}

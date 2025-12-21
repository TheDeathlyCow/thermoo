package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.gametest.GameTestHolder;
import org.jetbrains.annotations.Nullable;

@GameTestHolder(ThermooTestMod.MODID)
public final class EnvironmentTestHelper {
    public static void assertTemperatureEquals(GameTestHelper context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-2,
                "Expected temperature was " + expected + "°C but was actually " + actual + "°C"
        );
    }

    public static void assertHumidityEquals(GameTestHelper context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-2,
                "Expected humidity was " + expected + "% but was actually " + actual + "%"
        );
    }

    public static TemperatureRecord getTemperature(GameTestHelper context, BlockPos pos, EnvironmentProvider provider) {
        BlockPos absolute = context.absolutePos(pos);

        DataComponentMap.Builder builder = DataComponentMap.builder();
        provider.buildCurrentComponents(
                context.getLevel(),
                absolute,
                context.getLevel().getBiome(absolute),
                builder
        );
        TemperatureRecord component = builder.build().get(EnvironmentComponentTypes.TEMPERATURE);
        context.assertFalse(component == null, "Temperature is missing");
        return component;
    }

    public static double getBiomeTemperature(GameTestHelper context, Level world, ResourceKey<Biome> biomeKey) {
        Holder<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.registryAccess(), biomeKey);
        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
                        world,
                        context.absolutePos(BlockPos.ZERO),
                        plains
                ).getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT)
                .valueInUnit(TemperatureUnit.CELSIUS);
    }

    public static double getBiomeHumidity(GameTestHelper context, Level world, ResourceKey<Biome> biomeKey) {
        Holder<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.registryAccess(), biomeKey);
        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
                world,
                context.absolutePos(BlockPos.ZERO),
                plains
        ).getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
    }

    public static Holder<Biome> getBiomeEntry(RegistryAccess manager, ResourceKey<Biome> biomeKey) {
        return manager.registryOrThrow(Registries.BIOME)
                .getHolder(biomeKey.location())
                .orElseThrow();
    }

    public static void setSeasons(GameTestHelper context, @Nullable ThermooSeason temperateSeason, @Nullable ThermooSeason tropicalSeason) {
        MinecraftServer server = context.getLevel().getServer();
        GameRules.IntegerValue temperateSeasonRule = server.getGameRules().getRule(ThermooTestMod.CURRENT_SEASON);
        int temperateSeasonValue = switch (temperateSeason) {
            case SPRING -> 1;
            case SUMMER -> 2;
            case AUTUMN -> 3;
            case WINTER -> 4;
            case null, default -> 0;
        };
        temperateSeasonRule.set(temperateSeasonValue, server);

        GameRules.IntegerValue tropicalSeasonRule = server.getGameRules().getRule(ThermooTestMod.CURRENT_TROPICAL_SEASON);
        int tropicalSeasonValue = switch (tropicalSeason) {
            case TROPICAL_WET -> 1;
            case TROPICAL_DRY -> 2;
            case null, default -> 0;
        };
        tropicalSeasonRule.set(tropicalSeasonValue, server);

        ThermooSeason newTemperateSeason = ThermooSeason.getCurrentSeason(context.getLevel()).orElse(null);
        context.assertTrue(
                newTemperateSeason == temperateSeason,
                "Expected temperate season to be " + temperateSeason + " but was " + newTemperateSeason
        );

        ThermooSeason newTropicalSeason = ThermooSeason.getCurrentTropicalSeason(context.getLevel(), BlockPos.ZERO)
                .orElse(null);
        context.assertTrue(
                newTropicalSeason == tropicalSeason,
                "Expected tropical season to be " + tropicalSeason + " but was " + newTropicalSeason
        );
    }

    private EnvironmentTestHelper() {

    }
}
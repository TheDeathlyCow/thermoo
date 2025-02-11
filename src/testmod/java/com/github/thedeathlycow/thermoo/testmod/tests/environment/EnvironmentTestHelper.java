package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public final class EnvironmentTestHelper {
    public static RegistryEntry<EnvironmentProvider> getEnvironmentProvider(TestContext context, String id) {
        return context.getWorld()
                .getRegistryManager()
                .getOrThrow(ThermooRegistryKeys.ENVIRONMENT_PROVIDER)
                .getOrThrow(RegistryKey.of(
                        ThermooRegistryKeys.ENVIRONMENT_PROVIDER,
                        Identifier.of("thermoo-test", id)
                ));
    }

    public static void assertTemperatureEquals(TestContext context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-2,
                "Expected temperature was " + expected + "*C but was actually " + actual + "*C"
        );
    }

    public static void assertHumidityEquals(TestContext context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-2,
                "Expected humidity was " + expected + "% but was actually " + actual + "%"
        );
    }

    public static double getBiomeTemperature(TestContext context, World world, RegistryKey<Biome> biomeKey) {
        RegistryEntry<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.getRegistryManager(), biomeKey);
        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
                        world,
                        context.getAbsolutePos(BlockPos.ORIGIN),
                        plains
                ).getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT)
                .temperature()
                .valueInUnit(TemperatureUnit.CELSIUS);
    }

    public static double getBiomeHumidity(TestContext context, World world, RegistryKey<Biome> biomeKey) {
        RegistryEntry<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.getRegistryManager(), biomeKey);
        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
                world,
                context.getAbsolutePos(BlockPos.ORIGIN),
                plains
        ).getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
    }

    public static RegistryEntry<Biome> getBiomeEntry(DynamicRegistryManager manager, RegistryKey<Biome> biomeKey) {
        return manager.getOrThrow(RegistryKeys.BIOME)
                .getEntry(biomeKey.getValue())
                .orElseThrow();
    }

    public static void setSeasons(TestContext context, @Nullable ThermooSeason temperateSeason, @Nullable ThermooSeason tropicalSeason) {
        MinecraftServer server = context.getWorld().getServer();
        GameRules.IntRule temperateSeasonRule = server.getGameRules().get(ThermooTestMod.CURRENT_SEASON);
        int temperateSeasonValue = switch (temperateSeason) {
            case SPRING -> 1;
            case SUMMER -> 2;
            case AUTUMN -> 3;
            case WINTER -> 4;
            case null, default -> 0;
        };
        temperateSeasonRule.set(temperateSeasonValue, server);

        GameRules.IntRule tropicalSeasonRule = server.getGameRules().get(ThermooTestMod.CURRENT_TROPICAL_SEASON);
        int tropicalSeasonValue = switch (tropicalSeason) {
            case TROPICAL_WET -> 1;
            case TROPICAL_DRY -> 2;
            case null, default -> 0;
        };
        tropicalSeasonRule.set(tropicalSeasonValue, server);

        ThermooSeason newTemperateSeason = ThermooSeason.getCurrentSeason(context.getWorld()).orElse(null);
        context.assertTrue(
                newTemperateSeason == temperateSeason,
                "Expected temperate season to be " + temperateSeason + " but was " + newTemperateSeason
        );

        ThermooSeason newTropicalSeason = ThermooSeason.getCurrentTropicalSeason(context.getWorld(), BlockPos.ORIGIN)
                .orElse(null);
        context.assertTrue(
                newTropicalSeason == tropicalSeason,
                "Expected tropical season to be " + tropicalSeason + " but was " + newTropicalSeason
        );
    }

    private EnvironmentTestHelper() {

    }
}
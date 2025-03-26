package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.util.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.gametest.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public final class EnvironmentTestHelper {
    public static final String NO_SEASONS = ThermooTestMod.MODID + ":seasonal/no_seasons";
    public static final String SPRING = ThermooTestMod.MODID + ":seasonal/temperate_spring";
    public static final String AUTUMN = ThermooTestMod.MODID + ":seasonal/temperate_autumn";
    public static final String WINTER = ThermooTestMod.MODID + ":seasonal/temperate_winter";
    public static final String SUMMER = ThermooTestMod.MODID + ":seasonal/temperate_summer";
    public static final String DRY = ThermooTestMod.MODID + ":seasonal/tropical_dry";
    public static final String DRY_SPRING = ThermooTestMod.MODID + ":seasonal/dry_spring";
    public static final String DRY_SUMMER = ThermooTestMod.MODID + ":seasonal/dry_summer";
    public static final String DRY_AUTUMN = ThermooTestMod.MODID + ":seasonal/dry_autumn";
    public static final String DRY_WINTER = ThermooTestMod.MODID + ":seasonal/dry_winter";

    public static final String WET = ThermooTestMod.MODID + ":seasonal/tropical_wet";
    public static final String WET_SPRING = ThermooTestMod.MODID + ":seasonal/wet_spring";
    public static final String WET_SUMMER = ThermooTestMod.MODID + ":seasonal/wet_summer";
    public static final String WET_AUTUMN = ThermooTestMod.MODID + ":seasonal/wet_autumn";
    public static final String WET_WINTER = ThermooTestMod.MODID + ":seasonal/wet_winter";

    public static final String CLEAR_WEATHER = ThermooTestMod.MODID + ":weather/clear";
    public static final String RAINY_WEATHER = ThermooTestMod.MODID + ":weather/rainy";
    public static final String THUNDER_WEATHER = ThermooTestMod.MODID + ":weather/thunder";

    public static void assertTemperatureEquals(TestContext context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-2,
                Text.literal("Expected temperature was " + expected + "°C but was actually " + actual + "°C")
        );
    }

    public static void assertHumidityEquals(TestContext context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-2,
                Text.literal("Expected humidity was " + expected + "% but was actually " + actual + "%")
        );
    }

    public static TemperatureRecord getTemperature(TestContext context, BlockPos pos, EnvironmentProvider provider) {
        BlockPos absolute = context.getAbsolutePos(pos);

        ComponentMap.Builder builder = ComponentMap.builder();
        provider.buildCurrentComponents(
                context.getWorld(),
                absolute,
                context.getWorld().getBiome(absolute),
                builder
        );
        TemperatureRecord component = builder.build().get(EnvironmentComponentTypes.TEMPERATURE);
        context.assertFalse(component == null, Text.literal("Temperature is missing"));
        return component;
    }

    public static double getBiomeTemperature(TestContext context, World world, RegistryKey<Biome> biomeKey) {
        RegistryEntry<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.getRegistryManager(), biomeKey);
        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
                        world,
                        context.getAbsolutePos(BlockPos.ORIGIN),
                        plains
                ).getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT)
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

    public static void expectTemperateSeason(TestContext context, @Nullable ThermooSeason season) {
        ThermooSeason newTemperateSeason = ThermooSeason.getCurrentSeason(context.getWorld()).orElse(null);
        context.assertTrue(
                newTemperateSeason == season,
                Text.literal("Expected temperate season to be " + season + " but was " + newTemperateSeason)
        );
    }

    public static void expectTropicalSeason(TestContext context, @Nullable ThermooSeason season) {
        BlockPos pos = context.getAbsolutePos(BlockPos.ORIGIN);
        ThermooSeason newTropicalSeason = ThermooSeason.getCurrentTropicalSeason(context.getWorld(), pos).orElse(null);

        context.assertTrue(
                newTropicalSeason == season,
                Text.literal("Expected tropical season at " + pos + " to be " + season + " but was " + newTropicalSeason)
        );
    }

    private EnvironmentTestHelper() {

    }
}
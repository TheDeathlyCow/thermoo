/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.environment.v2.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.RelativeHumidityComponent;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.environment.v2.provider.EnvironmentProvider;
import com.github.thedeathlycow.thermoo.api.season.v2.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.v2.ThermooSeasonState;
import com.github.thedeathlycow.thermoo.api.season.v2.TropicalSeason;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureRecord;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureUnit;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
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

    public static void assertTemperatureEquals(GameTestHelper context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-4,
                Component.literal("Expected temperature was " + expected + "°C but was actually " + actual + "°C")
        );
    }

    public static void assertHumidityEquals(GameTestHelper context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-4,
                Component.literal("Expected humidity was " + expected * 100 + "% but was actually " + actual * 100 + "%")
        );
    }

    public static void assertPressureEquals(GameTestHelper context, double expected, double actual) {
        context.assertTrue(
                Math.abs(actual - expected) <= 1e-4,
                Component.literal("Expected pressure was " + expected + " mbar but was actually " + actual + " mbar")
        );
    }

    public static TemperatureRecord getTemperature(GameTestHelper helper, BlockPos pos, EnvironmentProvider provider) {
        BlockPos absolute = helper.absolutePos(pos);

        DataComponentMap.Builder builder = DataComponentMap.builder();
        provider.buildCurrentComponents(
                helper.getLevel(),
                absolute,
                helper.getLevel().getBiome(absolute),
                builder
        );
        TemperatureRecord component = builder.build().get(EnvironmentComponentTypes.TEMPERATURE);
        helper.assertFalse(component == null, Component.literal("Temperature is missing"));
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

    public static double getBiomeHumidity(GameTestHelper context, Level level, ResourceKey<Biome> biomeKey) {
        Holder<Biome> plains = EnvironmentTestHelper.getBiomeEntry(level.registryAccess(), biomeKey);
        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
                level,
                context.absolutePos(BlockPos.ZERO),
                plains
        ).getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
    }

    public static double getBiomePressure(GameTestHelper context, Level world, int y, ResourceKey<Biome> biomeKey) {
        Holder<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.registryAccess(), biomeKey);
        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
                world,
                context.absolutePos(BlockPos.ZERO).atY(y),
                plains
        ).getOrDefault(EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE, AtmosphericPressureComponent.DEFAULT);
    }

    public static Holder<Biome> getBiomeEntry(RegistryAccess access, ResourceKey<Biome> biomeKey) {
        return access.lookupOrThrow(Registries.BIOME)
                .get(biomeKey)
                .orElseThrow();
    }

    public static void expectTemperateSeason(GameTestHelper context, @Nullable TemperateSeason season) {
        BlockPos pos = context.absolutePos(BlockPos.ZERO);
        TemperateSeason newTemperateSeason = TemperateSeason.getCurrentState(context.getLevel(), pos)
                .map(ThermooSeasonState::season)
                .orElse(null);
        context.assertTrue(
                newTemperateSeason == season,
                Component.literal("Expected temperate season to be " + season + " but was " + newTemperateSeason)
        );
    }

    public static void expectTropicalSeason(GameTestHelper context, @Nullable TropicalSeason season) {
        BlockPos pos = context.absolutePos(BlockPos.ZERO);
        TropicalSeason newTropicalSeason = TropicalSeason.getCurrentState(context.getLevel(), pos)
                .map(ThermooSeasonState::season)
                .orElse(null);

        context.assertTrue(
                newTropicalSeason == season,
                Component.literal("Expected tropical season at " + pos + " to be " + season + " but was " + newTropicalSeason)
        );
    }

    private EnvironmentTestHelper() {

    }
}
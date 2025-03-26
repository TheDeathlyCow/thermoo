package com.github.thedeathlycow.thermoo.gametest.tests.environment;

public final class EnvironmentTestHelper {
//    public static void assertTemperatureEquals(TestContext context, double expected, double actual) {
//        context.assertTrue(
//                Math.abs(actual - expected) <= 1e-2,
//                "Expected temperature was " + expected + "°C but was actually " + actual + "°C"
//        );
//    }
//
//    public static void assertHumidityEquals(TestContext context, double expected, double actual) {
//        context.assertTrue(
//                Math.abs(actual - expected) <= 1e-2,
//                "Expected humidity was " + expected + "% but was actually " + actual + "%"
//        );
//    }
//
//    public static TemperatureRecord getTemperature(TestContext context, BlockPos pos, EnvironmentProvider provider) {
//        BlockPos absolute = context.getAbsolutePos(pos);
//
//        ComponentMap.Builder builder = ComponentMap.builder();
//        provider.buildCurrentComponents(
//                context.getWorld(),
//                absolute,
//                context.getWorld().getBiome(absolute),
//                builder
//        );
//        TemperatureRecord component = builder.build().get(EnvironmentComponentTypes.TEMPERATURE);
//        context.assertFalse(component == null, "Temperature is missing");
//        return component;
//    }
//
//    public static double getBiomeTemperature(TestContext context, World world, RegistryKey<Biome> biomeKey) {
//        RegistryEntry<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.getRegistryManager(), biomeKey);
//        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
//                        world,
//                        context.getAbsolutePos(BlockPos.ORIGIN),
//                        plains
//                ).getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT)
//                .valueInUnit(TemperatureUnit.CELSIUS);
//    }
//
//    public static double getBiomeHumidity(TestContext context, World world, RegistryKey<Biome> biomeKey) {
//        RegistryEntry<Biome> plains = EnvironmentTestHelper.getBiomeEntry(world.getRegistryManager(), biomeKey);
//        return EnvironmentLookupImpl.INSTANCE.findEnvironmentComponentsForBiome(
//                world,
//                context.getAbsolutePos(BlockPos.ORIGIN),
//                plains
//        ).getOrDefault(EnvironmentComponentTypes.RELATIVE_HUMIDITY, RelativeHumidityComponent.DEFAULT);
//    }
//
//    public static RegistryEntry<Biome> getBiomeEntry(DynamicRegistryManager manager, RegistryKey<Biome> biomeKey) {
//        return manager.getOrThrow(RegistryKeys.BIOME)
//                .getEntry(biomeKey.getValue())
//                .orElseThrow();
//    }
//
//    public static void setSeasons(TestContext context, @Nullable ThermooSeason temperateSeason, @Nullable ThermooSeason tropicalSeason) {
//        MinecraftServer server = context.getWorld().getServer();
//        GameRules.IntRule temperateSeasonRule = server.getGameRules().get(Thermoogametest.CURRENT_SEASON);
//        int temperateSeasonValue = switch (temperateSeason) {
//            case SPRING -> 1;
//            case SUMMER -> 2;
//            case AUTUMN -> 3;
//            case WINTER -> 4;
//            case null, default -> 0;
//        };
//        temperateSeasonRule.set(temperateSeasonValue, server);
//
//        GameRules.IntRule tropicalSeasonRule = server.getGameRules().get(Thermoogametest.CURRENT_TROPICAL_SEASON);
//        int tropicalSeasonValue = switch (tropicalSeason) {
//            case TROPICAL_WET -> 1;
//            case TROPICAL_DRY -> 2;
//            case null, default -> 0;
//        };
//        tropicalSeasonRule.set(tropicalSeasonValue, server);
//
//        ThermooSeason newTemperateSeason = ThermooSeason.getCurrentSeason(context.getWorld()).orElse(null);
//        context.assertTrue(
//                newTemperateSeason == temperateSeason,
//                "Expected temperate season to be " + temperateSeason + " but was " + newTemperateSeason
//        );
//
//        ThermooSeason newTropicalSeason = ThermooSeason.getCurrentTropicalSeason(context.getWorld(), BlockPos.ORIGIN)
//                .orElse(null);
//        context.assertTrue(
//                newTropicalSeason == tropicalSeason,
//                "Expected tropical season to be " + tropicalSeason + " but was " + newTropicalSeason
//        );
//    }
//
//    private EnvironmentTestHelper() {
//
//    }
}
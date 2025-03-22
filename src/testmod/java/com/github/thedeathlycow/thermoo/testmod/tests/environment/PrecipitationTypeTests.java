package com.github.thedeathlycow.thermoo.testmod.tests.environment;

@SuppressWarnings("unused")
public class PrecipitationTypeTests {
//    @BeforeBatch(batchId = "snowyPlains_rainy")
//    public void setRainyForRainy(ServerWorld world) {
//        world.setWeather(0, 20000000, false, false);
//        world.setRainGradient(1f);
//    }
//
//    @AfterBatch(batchId = "snowyPlains_rainy")
//    public void setClearForRainy(ServerWorld world) {
//        world.setWeather(20000000, 0, false, false);
//        world.setRainGradient(0f);
//    }
//
//    @BeforeBatch(batchId = "snowyPlains_sunny")
//    public void setClearForSunny(ServerWorld world) {
//        world.setWeather(20000000, 0, false, false);
//        world.setRainGradient(0f);
//    }
//
//    @GameTest(
//            templateName = FabricGameTest.EMPTY_STRUCTURE,
//            batchId = "snowyPlains_rainy"
//    )
//    public void snowy_plains_has_snowy_temperature_when_not_raining(TestContext context) {
//        ServerWorld world = context.getWorld();
//        EnvironmentTestHelper.setSeasons(context, null, null);
//
//        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_PLAINS);
//        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);
//
//        context.complete();
//    }
//
//    @GameTest(
//            templateName = FabricGameTest.EMPTY_STRUCTURE,
//            batchId = "snowyPlains_sunny"
//    )
//    public void snowy_plains_has_snowy_temperature_when_raining(TestContext context) {
//        ServerWorld world = context.getWorld();
//        EnvironmentTestHelper.setSeasons(context, null, null);
//
//        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_PLAINS);
//        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);
//
//        context.complete();
//    }
}
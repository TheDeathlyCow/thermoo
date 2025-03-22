package com.github.thedeathlycow.thermoo.testmod.tests.environment;

@SuppressWarnings("unused")
public class WeatherStateTests {
//    @BeforeBatch(batchId = "snowyTaiga_rainy")
//    public void setRainyForRainy(ServerWorld world) {
//        world.setWeather(0, 20000000, true, false);
//        world.setRainGradient(1f);
//    }
//
//    @AfterBatch(batchId = "snowyTaiga_rainy")
//    public void setClearForRainy(ServerWorld world) {
//        world.setWeather(20000000, 0, false, false);
//        world.setRainGradient(0f);
//        world.setThunderGradient(0f);
//    }
//
//    @BeforeBatch(batchId = "snowyTaiga_sunny")
//    public void setClearForSunny(ServerWorld world) {
//        world.setWeather(20000000, 0, false, false);
//        world.setRainGradient(0f);
//    }
//
//    @BeforeBatch(batchId = "snowyTaiga_thundering")
//    public void setRainyForThundering(ServerWorld world) {
//        world.setWeather(0, 20000000, true, true);
//        world.setRainGradient(1f);
//        world.setThunderGradient(1f);
//    }
//
//    @AfterBatch(batchId = "snowyTaiga_thundering")
//    public void setClearForThundering(ServerWorld world) {
//        world.setWeather(20000000, 0, false, false);
//        world.setRainGradient(0f);
//        world.setThunderGradient(0f);
//    }
//
//    @GameTest(
//            templateName = FabricGameTest.EMPTY_STRUCTURE,
//            batchId = "snowyTaiga_sunny"
//    )
//    public void snowy_taiga_during_sunny_is_neg5c(TestContext context) {
//        ServerWorld world = context.getWorld();
//        EnvironmentTestHelper.setSeasons(context, null, null);
//
//        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
//        EnvironmentTestHelper.assertTemperatureEquals(context, -5.0, temperature);
//
//        context.complete();
//    }
//
//    @GameTest(
//            templateName = FabricGameTest.EMPTY_STRUCTURE,
//            batchId = "snowyTaiga_rainy"
//    )
//    public void snowy_taiga_during_rainy_is_neg10c(TestContext context) {
//        ServerWorld world = context.getWorld();
//        EnvironmentTestHelper.setSeasons(context, null, null);
//
//        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
//        EnvironmentTestHelper.assertTemperatureEquals(context, -10.0, temperature);
//
//        context.complete();
//    }
//
//    @GameTest(
//            templateName = FabricGameTest.EMPTY_STRUCTURE,
//            batchId = "snowyTaiga_thundering"
//    )
//    public void snowy_taiga_during_thunder_is_neg15c(TestContext context) {
//        ServerWorld world = context.getWorld();
//        EnvironmentTestHelper.setSeasons(context, null, null);
//
//        double temperature = EnvironmentTestHelper.getBiomeTemperature(context, world, BiomeKeys.SNOWY_TAIGA);
//        EnvironmentTestHelper.assertTemperatureEquals(context, -15.0, temperature);
//
//        context.complete();
//    }
}
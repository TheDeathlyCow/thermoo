package com.github.thedeathlycow.thermoo.gametest.client;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.screenshot.TestScreenshotComparisonOptions;

public class HealthBarTests implements FabricClientGameTest {
    private static final String EMPTY = "empty_temperature_bar";
    private static final String HALF_HEART = "half_heart";
    private static final String FULL_HEART = "full_heart";
    private static final String HALF_FULL = "half_full_temperature_bar";
    private static final String FULL = "full_temperature_bar";


    @Override
    public void runTest(ClientGameTestContext context) {
        context.getInput().resizeWindow(2048, 1024); // Multiple of 256 to not squish the pixels of 256x overlays.
        context.runOnClient(client -> {
            client.options.hudHidden = false;
            client.options.getGuiScale().setValue(2);
        });

        try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
            singleplayer.getClientWorld().waitForChunksRender();

            final int maxTemperature = 5600;

            testPlayerTemperatureBar(singleplayer, context, 0, EMPTY);
            testPlayerTemperatureBar(singleplayer, context, 1, EMPTY);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 20, HALF_HEART);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 10, FULL_HEART);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 10 + 1, FULL_HEART);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 2 - 1, HALF_FULL);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature, FULL);
        }
    }

    private static void testPlayerTemperatureBar(
            TestSingleplayerContext singleplayer,
            ClientGameTestContext context,
            int temperature,
            String template
    ) {
        singleplayer.getServer().runCommand("/gamerule fireDamage false"); // hack to prevent damage from affecting health underneath temperature display
        singleplayer.getServer().runCommand("/thermoo temperature set @p " + temperature);
        singleplayer.getServer().runCommand("/effect clear @p"); // hack to remove the poison temperature effect

        context.waitTicks(1); // wait to allow for temperature to update
        context.assertScreenshotEquals(
                TestScreenshotComparisonOptions.of(template)
                        .withRegion(842, 944, 162, 20)
                        .save()
        );
    }
}
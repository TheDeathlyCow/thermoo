package com.github.thedeathlycow.thermoo.gametest.client;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.screenshot.TestScreenshotComparisonOptions;

public class HealthBarTests implements FabricClientGameTest {
    static final String EMPTY = "player_empty";
    static final String TWENTIETH = "player_twentieth";
    static final String TENTH = "player_tenth";
    static final String HALF = "player_half";
    static final String NINETIETH = "player_ninetieth";
    static final String NINETY_FIFTH = "player_ninety_fifth";
    static final String FULL = "player_full";
    static final String TWO_BARS = "_two_bars";

    private int offset = 0;

    @Override
    public void runTest(ClientGameTestContext context) {
        context.getInput().resizeWindow(2048, 1024); // Multiple of 256 to not squish the pixels of 256x overlays.
        context.runOnClient(client -> {
            client.options.hideGui = false;
            client.options.guiScale().set(2);
        });

        try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
            singleplayer.getServer().runCommand("/gamerule fireDamage false"); // hack to prevent damage from affecting health underneath temperature display
            singleplayer.getClientWorld().waitForChunksRender();
            offset = 0;

            final int maxTemperature = 5600;

            testPlayerTemperatureBar(singleplayer, context, 0, EMPTY);
            testPlayerTemperatureBar(singleplayer, context, 1, EMPTY);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 20, TWENTIETH);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 10, TENTH);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 10 + 1, TENTH);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 2, HALF);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature * 9 / 10, NINETIETH);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature * 19 / 20, NINETY_FIFTH);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature, FULL);

            singleplayer.getServer().runCommand("/attribute @p max_health base set 40");
            singleplayer.getServer().runCommand("/effect give @p minecraft:instant_health 4 100 true");
            context.waitTicks(20); // wait to allow for health bar update
            offset = 20;

            testPlayerTemperatureBar(singleplayer, context, 0, EMPTY + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, 1, EMPTY + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 20, TWENTIETH + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 10, TENTH + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 10 + 1, TENTH + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature / 2, HALF + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature * 9 / 10, NINETIETH + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature * 19 / 20, NINETY_FIFTH + TWO_BARS);
            testPlayerTemperatureBar(singleplayer, context, maxTemperature, FULL + TWO_BARS);
        }
    }

    private void testPlayerTemperatureBar(
            TestSingleplayerContext singleplayer,
            ClientGameTestContext context,
            int temperature,
            String template
    ) {
        singleplayer.getServer().runCommand("/thermoo temperature set @p " + temperature);
        singleplayer.getServer().runCommand("/effect clear @p"); // hack to remove the poison temperature effect

        context.waitTicks(1); // wait to allow for temperature to update
        context.assertScreenshotEquals(
                TestScreenshotComparisonOptions.of(template)
                        .withRegion(842, 944 - offset, 162, 20 + offset)
                        .save()
        );
        singleplayer.getServer().runCommand("/thermoo temperature get @p");
    }
}
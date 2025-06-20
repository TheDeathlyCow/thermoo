package com.github.thedeathlycow.thermoo.gametest.client;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.fabric.api.client.gametest.v1.screenshot.TestScreenshotComparisonOptions;

public class MountHealthBarTests implements FabricClientGameTest {
    static final String EMPTY = "mount_empty";
    static final String FIFTH = "mount_fifth";
    static final String TENTH = "mount_tenth";
    static final String HALF = "mount_half";
    static final String FULL = "mount_full";
    static final String TWO_BARS = "_two_bars";
    static final String THREE_BARS = "_three_bars";
    static final String FOUR_BARS = "_four_bars";
    private int offset = 0;

    @Override
    public void runTest(ClientGameTestContext context) {
        context.getInput().resizeWindow(2048, 1024); // Multiple of 256 to not squish the pixels of 256x overlays.
        context.runOnClient(client -> {
            client.options.hudHidden = false;
            client.options.getGuiScale().setValue(2);
        });

        try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
            singleplayer.getClientWorld().waitForChunksRender();
            offset = 0;

            final int maxTemperature = 5600;

            singleplayer.getServer().runCommand("/execute at @p summon pig run tag @s add pig");
            singleplayer.getServer().runCommand("/data merge entity @n[tag=pig] {NoAI:true}");
            singleplayer.getServer().runCommand("/ride @p mount @n[tag=pig]");

            testMountTemperatureBar(singleplayer, context, 0, EMPTY);
            testMountTemperatureBar(singleplayer, context, 1, EMPTY);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10, TENTH);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10 + 1, TENTH);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 5, FIFTH);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 2, HALF);
            testMountTemperatureBar(singleplayer, context, maxTemperature, FULL);

            singleplayer.getServer().runCommand("/attribute @n[tag=pig] max_health base set 40");
            singleplayer.getServer().runCommand("/effect give @n[tag=pig] minecraft:instant_health 4 100 true");
            context.waitTicks(20); // wait to allow for health bar update
            offset = 20;

            testMountTemperatureBar(singleplayer, context, 0, EMPTY + TWO_BARS);
            testMountTemperatureBar(singleplayer, context, 1, EMPTY + TWO_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10, TENTH + TWO_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10 + 1, TENTH + TWO_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 5, FIFTH + TWO_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 2, HALF + TWO_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature, FULL + TWO_BARS);

            singleplayer.getServer().runCommand("/attribute @n[tag=pig] max_health base set 60");
            singleplayer.getServer().runCommand("/effect give @n[tag=pig] minecraft:instant_health 4 100 true");
            context.waitTicks(20); // wait to allow for health bar update
            offset = 40;

            testMountTemperatureBar(singleplayer, context, 0, EMPTY + THREE_BARS);
            testMountTemperatureBar(singleplayer, context, 1, EMPTY + THREE_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10, TENTH + THREE_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10 + 1, TENTH + THREE_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 5, FIFTH + THREE_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 2, HALF + THREE_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature, FULL + THREE_BARS);

            singleplayer.getServer().runCommand("/attribute @n[tag=pig] max_health base set 80");
            singleplayer.getServer().runCommand("/effect give @n[tag=pig] minecraft:instant_health 4 100 true");
            context.waitTicks(20); // wait to allow for health bar update
            offset = 60;

            testMountTemperatureBar(singleplayer, context, 0, EMPTY + FOUR_BARS);
            testMountTemperatureBar(singleplayer, context, 1, EMPTY + FOUR_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10, TENTH + FOUR_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 10 + 1, TENTH + FOUR_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 5, FIFTH + FOUR_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature / 2, HALF + FOUR_BARS);
            testMountTemperatureBar(singleplayer, context, maxTemperature, FULL + FOUR_BARS);
        }
    }

    private void testMountTemperatureBar(
            TestSingleplayerContext singleplayer,
            ClientGameTestContext context,
            int temperature,
            String template
    ) {
        singleplayer.getServer().runCommand("/thermoo temperature set @n[tag=pig] " + temperature);

        context.waitTicks(1); // wait to allow for temperature to update
        context.assertScreenshotEquals(
                TestScreenshotComparisonOptions.of(template)
                        .withRegion(1044, 944 - offset, 162, 20 + offset)
                        .save()
        );
    }
}
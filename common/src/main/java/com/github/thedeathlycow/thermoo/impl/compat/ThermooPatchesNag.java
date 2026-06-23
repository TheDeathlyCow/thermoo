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

package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooClientTickEvents;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.YumiMods;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ThermooPatchesNag implements ThermooClientTickEvents.EndTick {
    private static final Logger LOGGER = LoggerFactory.getLogger(Thermoo.MODID + "-patch-nag");

    private static final Style LINK_STYLE = Style.EMPTY
            .withUnderlined(true)
            .withColor(ChatFormatting.GREEN)
            .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://www.modrinth.com/mod/thermoo-patches")));

    private static final ThermooPatchesNag INSTANCE = new ThermooPatchesNag();

    private final List<ModContainer> patchAvailableMods = Collections.synchronizedList(new ArrayList<>());

    private boolean naggedPlayer = false;

    @Nullable
    private Component nagMessage = null;

    public static void initialize(ThermooConfig config) {
        if (enableNag(config)) {
            INSTANCE.fetchAsync(config);
            ThermooClientTickEvents.END_CLIENT_TICK.register(INSTANCE);
        }
    }

    private static boolean enableNag(ThermooConfig config) {
        return !YumiMods.get().isModLoaded("thermoo-patches") && config.enableThermooPatchesNag();
    }

    @Override
    public void onEndTick(Minecraft client) {
        if (this.naggedPlayer) {
            return;
        }

        Component nagMessageText = this.getNagMessage();
        LocalPlayer player = client.player;

        if (nagMessageText != null && player != null) {
            this.naggedPlayer = true;
            player.sendSystemMessage(nagMessageText);
            LOGGER.warn(nagMessageText.getString());
        }
    }

    private void fetchAsync(ThermooConfig config) {
        Thread.ofVirtual().start(() -> {
            try {
                this.fetch(config);
            } catch (Exception e) {
                LOGGER.error("Error fetching Thermoo Patches patch list", e);
            }
        });
    }

    private void fetch(ThermooConfig config) {
        LOGGER.info("Fetching Thermoo Patches patch list data...");
        List<ModContainer> foundMods;

        try (HttpClient client = HttpClient.newHttpClient()) {
            PatchList patches = PatchListService.fetchPatchList(client, config.thermooPatchesPatchListUrl());
            foundMods = patches.getPatchAvailableMods(YumiMods.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!foundMods.isEmpty()) {
            LOGGER.warn("Thermoo Patches is recommended for this mod set!");
            this.patchAvailableMods.addAll(foundMods);
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Your mod set is patched with Thermoo Patches, great job!");
        }
    }

    @Nullable
    private Component getNagMessage() {
        if (!this.patchAvailableMods.isEmpty()) {
            this.nagMessage = this.createNagMessage();
        }

        return this.nagMessage;
    }

    private Component createNagMessage() {
        MutableComponent message = Component.literal("").withStyle(ChatFormatting.DARK_GREEN);

        message.append("\n");
        message.append(Component.translatable("text.thermoo.thermoo-patches-nag.body"));
        message.append("\n\n");

        synchronized (this.patchAvailableMods) {
            patchAvailableMods.forEach(mod -> {
                Component modEntry = Component.translatable(
                        "text.thermoo.thermoo-patches-nag.item",
                        mod.getName(),
                        mod.id()
                ).withStyle(ChatFormatting.YELLOW);

                message.append(modEntry);
                message.append("\n");
            });
        }

        message.append("\n");
        message.append(Component.translatable("text.thermoo.thermoo-patches-nag.footer"));
        message.append(
                Component.literal("\nhttps://www.modrinth.com/mod/thermoo-patches\n\n")
                        .setStyle(LINK_STYLE)
        );
        message.append(Component.translatable("text.thermoo.thermoo-patches-nag.disable"));

        return message;
    }

    private ThermooPatchesNag() {

    }
}
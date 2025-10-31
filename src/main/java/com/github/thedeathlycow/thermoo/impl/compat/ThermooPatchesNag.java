package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModMetadata;
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

public class ThermooPatchesNag implements ClientTickEvents.EndTick {
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
            ClientTickEvents.END_CLIENT_TICK.register(INSTANCE);
        }
    }

    private static boolean enableNag(ThermooConfig config) {
        return !FabricLoader.getInstance().isModLoaded("thermoo-patches") && config.enableThermooPatchesNag();
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
            player.displayClientMessage(nagMessageText, false);
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
        List<ModContainer> patchAvailableMods;

        try (HttpClient client = HttpClient.newHttpClient()) {
            PatchList patches = PatchListService.fetchPatchList(client, config.thermooPatchesPatchListUrl());
            patchAvailableMods = patches.getPatchAvailableMods(FabricLoader.getInstance());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException | VersionParsingException e) {
            throw new RuntimeException(e);
        }

        if (!patchAvailableMods.isEmpty()) {
            LOGGER.warn("Thermoo Patches is recommended for this mod set!");
            this.patchAvailableMods.addAll(patchAvailableMods);
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
                ModMetadata metadata = mod.getMetadata();
                Component modEntry = Component.translatable(
                        "text.thermoo.thermoo-patches-nag.item",
                        metadata.getName(),
                        metadata.getId()
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
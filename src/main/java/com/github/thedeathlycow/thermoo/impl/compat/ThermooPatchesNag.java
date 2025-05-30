package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ThermooPatchesNag implements ServerPlayConnectionEvents.Join, ServerTickEvents.EndTick {
    private static final Logger LOGGER = LoggerFactory.getLogger(Thermoo.MODID + "-patch-nag");

    private static final Style HEADER_STYLE = Style.EMPTY
            .withBold(true)
            .withColor(Formatting.YELLOW);

    private static final Style LINK_STYLE = Style.EMPTY
            .withUnderline(true)
            .withColor(Formatting.GREEN)
            .withClickEvent(new ClickEvent.OpenUrl(URI.create("https://www.modrinth.com/mod/thermoo-patches")));

    private static final ThermooPatchesNag INSTANCE = new ThermooPatchesNag();

    private final AtomicReference<Text> nagMessage = new AtomicReference<>(null);

    private final List<ServerPlayerEntity> playersToBroadcast = new ArrayList<>();

    public static void initialize(ThermooConfig config) {
        if (enableNag(config)) {
            INSTANCE.queueFetch(config);
            ServerPlayConnectionEvents.JOIN.register(INSTANCE);
            ServerTickEvents.END_SERVER_TICK.register(INSTANCE);
        }
    }

    private static boolean enableNag(ThermooConfig config) {
        return !FabricLoader.getInstance().isModLoaded("thermoo-patches") && config.enableThermooPatchesNag();
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        if (server.isSingleplayer() || handler.getPlayer().getPermissionLevel() >= server.getOpPermissionLevel()) {
            this.playersToBroadcast.add(handler.getPlayer());
        }
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        if (!this.playersToBroadcast.isEmpty() && this.nagMessage.get() != null) {
            Iterator<ServerPlayerEntity> players = this.playersToBroadcast.iterator();
            while (players.hasNext()) {
                ServerPlayerEntity player = players.next();
                player.sendMessage(this.nagMessage.get());
                players.remove();

                // also nag the server for good measure
                LOGGER.warn(this.nagMessage.get().getString());
            }
        }
    }

    private void queueFetch(ThermooConfig config) {
        Thread.ofVirtual().start(() -> {
            try {
                this.fetch(config);
            } catch (Exception e) {
                LOGGER.error("Error fetching Thermoo Patches patch list", e);
            }
        });
    }

    private void fetch(ThermooConfig config) {
        List<ModContainer> patchAvailableMods;

        try (HttpClient client = HttpClient.newHttpClient()) {
            PatchList patches = PatchListService.fetchPatchList(client, config.thermooPatchesPatchListUrl());
            patchAvailableMods = patches.getPatchAvailableMods();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException | VersionParsingException e) {
            throw new RuntimeException(e);
        }

        if (!patchAvailableMods.isEmpty()) {
            LOGGER.warn("Thermoo Patches is recommended for this mod set!");
            this.nagMessage.set(this.createNagMessage(patchAvailableMods));
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Your mod set is patched with Thermoo Patches, great job!");
        }
    }

    private Text createNagMessage(List<ModContainer> patchAvailableMods) {
        MutableText message = Text.literal("").formatted(Formatting.DARK_GREEN);

        message.append(Text.translatable("text.thermoo.thermoo-patches-nag.header").setStyle(HEADER_STYLE));

        message.append("\n");
        message.append(Text.translatable("text.thermoo.thermoo-patches-nag.body"));
        message.append("\n\n");

        patchAvailableMods.forEach(mod -> {
            ModMetadata metadata = mod.getMetadata();
            Text modEntry = Text.translatable(
                    "text.thermoo.thermoo-patches-nag.item",
                    metadata.getName(),
                    metadata.getId()
            ).formatted(Formatting.YELLOW);

            message.append(modEntry);
            message.append("\n");
        });

        message.append("\n");
        message.append(Text.translatable("text.thermoo.thermoo-patches-nag.footer"));
        message.append(
                Text.literal("\nhttps://www.modrinth.com/mod/thermoo-patches\n")
                        .setStyle(LINK_STYLE)
        );
        message.append(Text.translatable("text.thermoo.thermoo-patches-nag.disable"));

        return message;
    }

    private ThermooPatchesNag() {

    }
}
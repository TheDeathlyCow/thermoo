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

package com.github.thedeathlycow.thermoo.impl.neoforge.mod;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.neoforge.platform.ThermooRegistriesImpl;
import com.github.thedeathlycow.thermoo.impl.neoforge.registry.ThermooAttachments;
import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooCommandRegistrationCallback;
import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooServerLifecycleEvents;
import dev.yumi.mc.core.api.YumiMods;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/// Primary entry point logic is handled via a Fabric-like entrypoint in [com.github.thedeathlycow.thermoo.impl.neoforge.ThermooNeoforge]
@Mod(Thermoo.MODID)
public class ThermooNeoforgeMod {
    public ThermooNeoforgeMod(IEventBus modBus) {
        ThermooAttachments.initialize(modBus);
        modBus.addListener(ThermooNeoforgeMod::registerDynamicRegistries);
        NeoForge.EVENT_BUS.addListener(ThermooNeoforgeMod::onServerStarted);
        NeoForge.EVENT_BUS.addListener(ThermooNeoforgeMod::onStartStopped);
        NeoForge.EVENT_BUS.addListener(ThermooNeoforgeMod::onCommandRegistered);

        if (YumiMods.get().isDevelopmentEnvironment()) {
            Thermoo.LOGGER.info("Thermoo Neoforge @Mod class initialized");
        }
    }

    private static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
        for (ThermooRegistriesImpl.DynamicRegistry<?> registry : ThermooRegistriesImpl.DYNAMIC_REGISTRIES) {
            registry.onNewRegistry(event);
        }
    }

    private static void onServerStarted(ServerStartedEvent event) {
        ThermooServerLifecycleEvents.SERVER_STARTED.invoker().onServerStarted(event.getServer());
    }

    private static void onStartStopped(ServerStoppedEvent event) {
        ThermooServerLifecycleEvents.SERVER_STOPPED.invoker().onServerStopped(event.getServer());
    }

    private static void onCommandRegistered(RegisterCommandsEvent event) {
        ThermooCommandRegistrationCallback.EVENT.invoker().register(
                event.getDispatcher(),
                event.getBuildContext(),
                event.getCommandSelection()
        );
    }
}
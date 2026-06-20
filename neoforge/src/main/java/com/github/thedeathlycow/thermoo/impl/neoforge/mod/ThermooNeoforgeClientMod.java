package com.github.thedeathlycow.thermoo.impl.neoforge.mod;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooClientTickEvents;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

/// Primary entry point logic is handled via a Fabric-like entrypoint in [com.github.thedeathlycow.thermoo.impl.neoforge.ThermooNeoforgeClient]
@Mod(value = Thermoo.MODID, dist = Dist.CLIENT)
public class ThermooNeoforgeClientMod {
    public ThermooNeoforgeClientMod(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(ThermooNeoforgeClientMod::onClientTick);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        ThermooClientTickEvents.END_CLIENT_TICK.invoker().onEndTick(Minecraft.getInstance());
    }
}
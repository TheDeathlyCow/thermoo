package com.github.thedeathlycow.thermoo.impl.neoforge.mod;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.neoforge.platform.ThermooRegistriesImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/// Primary entry point logic is handled via a Fabric-like entrypoint in [com.github.thedeathlycow.thermoo.impl.neoforge.ThermooNeoforge]
@Mod(Thermoo.MODID)
public class ThermooNeoforgeMod {
    public ThermooNeoforgeMod(IEventBus modBus) {
        modBus.addListener(ThermooNeoforgeMod::registerDynamicRegistries);
    }

    private static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
        for (ThermooRegistriesImpl.DynamicRegistry<?> registry : ThermooRegistriesImpl.DYNAMIC_REGISTRIES) {
            registry.onNewRegistry(event);
        }
    }
}
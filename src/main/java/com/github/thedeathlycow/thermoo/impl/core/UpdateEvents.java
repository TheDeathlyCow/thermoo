package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v1.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.core.v1.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

import java.util.IdentityHashMap;
import java.util.Map;

public record UpdateEvents(
        Event<LivingEntityTemperatureTickEvents.GetTemperatureChange> event
) {
    private static final Map<ResourceKey<TemperatureSource>, UpdateEvents> EVENT_REGISTRY = new IdentityHashMap<>();

    public static void invokeAllWithContext(
            EnvironmentTickContext<? extends LivingEntity> context,
            HolderLookup<TemperatureSource> lookup
    ) {
        for (TemperatureChange ctx : ((ThermooServerLevel) context.level()).thermoo$tickingTemperatureSources()) {
            ResourceKey<TemperatureSource> key = ctx.source().unwrapKey().orElse(null);

            if (key != null && context.affected().tickCount % ctx.source().value().tickInterval() == 0) {
                UpdateEvents events = EVENT_REGISTRY.get(key);
                int tempChange = events.event.invoker().addTemperature(context);

                if (tempChange != 0) {
                    context.affected().thermoo$addTemperature(tempChange, ctx);
                }
            }
        }
    }

    public static UpdateEvents getOrCreate(ResourceKey<TemperatureSource> key) {
        return EVENT_REGISTRY.computeIfAbsent(
                key,
                _ -> new UpdateEvents(createGetChange())
        );
    }

    public static boolean hasRegisteredEvents(ResourceKey<TemperatureSource> key) {
        return EVENT_REGISTRY.containsKey(key);
    }

    private static Event<LivingEntityTemperatureTickEvents.GetTemperatureChange> createGetChange() {
        return EventFactory.createArrayBacked(
                LivingEntityTemperatureTickEvents.GetTemperatureChange.class,
                listeners -> context -> {
                    int total = 0;
                    for (LivingEntityTemperatureTickEvents.GetTemperatureChange listener : listeners) {
                        total += listener.addTemperature(context);
                    }
                    return total;
                }
        );
    }
}
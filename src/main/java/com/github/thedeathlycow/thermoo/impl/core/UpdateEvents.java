package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v1.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.core.v1.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSource;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

import java.util.IdentityHashMap;
import java.util.Map;

public record UpdateEvents(
        Event<LivingEntityTemperatureTickEvents.AllowTemperatureUpdate> allowUpdate,
        Event<LivingEntityTemperatureTickEvents.GetTemperatureChange> getChange,
        Event<LivingEntityTemperatureTickEvents.AllowTemperatureChange> allowChange
) {
    private static final Map<ResourceKey<TemperatureSource>, UpdateEvents> EVENT_REGISTRY = new IdentityHashMap<>();

    public static void invokeAllWithContext(
            EnvironmentTickContext<? extends LivingEntity> context,
            HolderLookup<TemperatureSource> lookup
    ) {
        lookup.listElements().forEach(holder -> {
                    UpdateEvents events = EVENT_REGISTRY.get(holder.key());
                    if (events.allowUpdate.invoker().allowUpdate(context) == TriState.FALSE) {
                        return;
                    }

                    int tempChange = events.getChange.invoker().addTemperature(context);
                    if (tempChange != 0 && events.allowChange.invoker().allowChange(context, tempChange) != TriState.FALSE) {
                        // TODO: replace heating mods with sources
//                        context.affected().thermoo$addTemperature(tempChange, holder);
                    }
                }
        );
    }

    public static UpdateEvents getOrCreate(ResourceKey<TemperatureSource> key) {
        return EVENT_REGISTRY.computeIfAbsent(
                key,
                _ -> new UpdateEvents(createAllowUpdate(), createGetChange(), createAllowChange())
        );
    }

    private static Event<LivingEntityTemperatureTickEvents.AllowTemperatureUpdate> createAllowUpdate() {
        return EventFactory.createArrayBacked(
                LivingEntityTemperatureTickEvents.AllowTemperatureUpdate.class,
                listeners -> context -> {
                    for (LivingEntityTemperatureTickEvents.AllowTemperatureUpdate listener : listeners) {
                        TriState result = listener.allowUpdate(context);
                        if (result != TriState.DEFAULT) {
                            return result;
                        }
                    }
                    return TriState.DEFAULT;
                }
        );
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

    private static Event<LivingEntityTemperatureTickEvents.AllowTemperatureChange> createAllowChange() {
        return EventFactory.createArrayBacked(
                LivingEntityTemperatureTickEvents.AllowTemperatureChange.class,
                listeners -> (context, temperatureChange) -> {
                    for (LivingEntityTemperatureTickEvents.AllowTemperatureChange listener : listeners) {
                        TriState result = listener.allowChange(context, temperatureChange);
                        if (result != TriState.DEFAULT) {
                            return result;
                        }
                    }
                    return TriState.DEFAULT;
                }
        );
    }
}
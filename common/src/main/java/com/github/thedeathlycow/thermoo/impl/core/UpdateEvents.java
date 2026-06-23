/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
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

package com.github.thedeathlycow.thermoo.impl.core;

import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v2.event.EnvironmentTickContext;
import com.github.thedeathlycow.thermoo.api.core.v2.event.LivingEntityTemperatureTickEvents;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSource;
import com.github.thedeathlycow.thermoo.api.core.v2.source.TemperatureSources;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.google.common.base.Preconditions;
import dev.yumi.commons.event.Event;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public record UpdateEvents(
        Event<Identifier, LivingEntityTemperatureTickEvents.GetTemperatureChange> event
) {
    private static final Set<ResourceKey<TemperatureSource>> MAY_NOT_TICK = Set.of(TemperatureSources.ABSOLUTE, TemperatureSources.ENVIRONMENT);
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
        Preconditions.checkArgument(!MAY_NOT_TICK.contains(key), "The temperature source " + key.identifier() + " is not allowed to be ticked through this event.");

        return EVENT_REGISTRY.computeIfAbsent(
                key,
                _ -> new UpdateEvents(createGetChange())
        );
    }

    public static boolean hasRegisteredEvents(ResourceKey<TemperatureSource> key) {
        return EVENT_REGISTRY.containsKey(key);
    }

    private static Event<Identifier, LivingEntityTemperatureTickEvents.GetTemperatureChange> createGetChange() {
        return Thermoo.EVENT_MANAGER.create(
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
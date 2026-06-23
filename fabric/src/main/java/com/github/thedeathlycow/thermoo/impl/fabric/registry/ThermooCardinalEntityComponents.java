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

package com.github.thedeathlycow.thermoo.impl.fabric.registry;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.fabric.cca.SyncedIntCardinalComponent;
import com.github.thedeathlycow.thermoo.impl.fabric.cca.TemperatureStatusSettingsComponentImpl;
import com.github.thedeathlycow.thermoo.impl.fabric.cca.TemperatureStatusSystemImpl;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class ThermooCardinalEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<SyncedIntCardinalComponent> TEMPERATURE = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature"),
            SyncedIntCardinalComponent.class
    );

    public static final ComponentKey<SyncedIntCardinalComponent> WETNESS = ComponentRegistry.getOrCreate(
            Thermoo.id("wetness"),
            SyncedIntCardinalComponent.class
    );

    public static final ComponentKey<TemperatureStatusSettingsComponentImpl> TEMPERATURE_STATUS_SETTINGS = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature_effects"), // for backwards compatibility, the old id is still used
            TemperatureStatusSettingsComponentImpl.class
    );

    public static final ComponentKey<TemperatureStatusSystemImpl> TEMPERATURE_STATUS_SYSTEM = ComponentRegistry.getOrCreate(
            Thermoo.id("temperature_status_system"), // for backwards compatibility, the old id is still used
            TemperatureStatusSystemImpl.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE,
                SyncedIntCardinalComponent::new
        );
        registry.registerFor(
                LivingEntity.class,
                WETNESS,
                SyncedIntCardinalComponent::new
        );
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE_STATUS_SETTINGS,
                TemperatureStatusSettingsComponentImpl::new
        );
        registry.registerFor(
                LivingEntity.class,
                TEMPERATURE_STATUS_SYSTEM,
                TemperatureStatusSystemImpl::new
        );
    }
}
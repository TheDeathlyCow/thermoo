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

package com.github.thedeathlycow.thermoo.impl.fabric.compat.polymer;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.attribute.AttributeData;
import com.github.thedeathlycow.thermoo.impl.compat.init.DependentModInitializer;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import eu.pb4.polymer.core.api.utils.PolymerUtils;
import eu.pb4.polymer.rsm.api.RegistrySyncUtils;
import net.minecraft.core.registries.BuiltInRegistries;


public class ThermooPolymerPatch implements DependentModInitializer {
    @Override
    public void onInitialize() {
        if (Thermoo.getConfig().enablePolymerPatch()) {
            polymerizeAttributes();
            polymerizeArgumentTypes();
            PolymerUtils.markAsServerOnlyRegistry(ThermooRegistries.TEMPERATURE_STATUS);
            PolymerUtils.markAsServerOnlyRegistry(ThermooRegistries.TEMPERATURE_SOURCE);

            Thermoo.LOGGER.info("Patched Thermoo for server-side with Polymer!");
        }
    }

    private static void polymerizeArgumentTypes() {
        RegistrySyncUtils.setServerEntry(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Thermoo.TEMPERATURE_UNIT_ARG_SERIALIZER);
    }

    private static void polymerizeAttributes() {
        for (AttributeData data : AttributeData.values()) {
            PolymerEntityUtils.registerAttribute(data.attribute());
        }
    }

    @Override
    public String[] getRequiredModIds() {
        return new String[]{"polymer-core", "polymer-registry-sync-manipulator"};
    }
}
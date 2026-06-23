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

package com.github.thedeathlycow.thermoo.impl.attribute;

import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * Helper methods for attribute related functions
 */
public class AttributeHelper {

    private static final String PREFIX = "thermoo:generic.";

    public static String fixPrefixedAttributeIds(String id) {
        String normalizedID = NamespacedSchema.ensureNamespaced(id);

        String normalizedPrefix = NamespacedSchema.ensureNamespaced(PREFIX);
        if (normalizedID.startsWith(normalizedPrefix)) {
            return "thermoo:" + normalizedID.substring(normalizedPrefix.length());
        }

        return id;
    }

    public static void applyValueAsModifier(
            LivingEntity entity,
            AttributeData attribute,
            double value
    ) {
        var modifier = new AttributeModifier(
                attribute.location(),
                value,
                AttributeModifier.Operation.ADD_VALUE
        );

        AttributeInstance attributeInstance = entity.getAttribute(attribute.attribute());

        if (attributeInstance == null) {
            throw new IllegalStateException("Attribute not found on " + entity.getType() + ": " + attribute);
        }

        attributeInstance.addTransientModifier(modifier);
    }

    private AttributeHelper() {

    }

}

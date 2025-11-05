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
                attribute.id(),
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

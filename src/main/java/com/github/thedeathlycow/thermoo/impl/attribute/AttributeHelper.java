package com.github.thedeathlycow.thermoo.impl.attribute;

import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class AttributeHelper {

    private static final String PREFIX = "thermoo:generic.";

    public static String fixPrefixedAttributeIds(String id) {
        String normalizedID = IdentifierNormalizingSchema.normalize(id);

        String normalizedPrefix = IdentifierNormalizingSchema.normalize(PREFIX);
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
        var modifier = new EntityAttributeModifier(
                attribute.id(),
                value,
                EntityAttributeModifier.Operation.ADD_VALUE
        );

        EntityAttributeInstance attributeInstance = entity.getAttributeInstance(attribute.attribute());

        if (attributeInstance == null) {
            throw new IllegalStateException("Attribute not found on " + entity.getType() + ": " + attribute);
        }

        attributeInstance.addTemporaryModifier(modifier);
    }

    private AttributeHelper() {

    }

}

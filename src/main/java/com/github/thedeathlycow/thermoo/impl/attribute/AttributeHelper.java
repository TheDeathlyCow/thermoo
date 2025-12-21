package com.github.thedeathlycow.thermoo.impl.attribute;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Helper methods for attribute related functions
 */
public class AttributeHelper {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(
            Registries.ATTRIBUTE,
            Thermoo.MODID
    );

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

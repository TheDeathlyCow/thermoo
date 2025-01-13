package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import net.minecraft.datafixer.schema.IdentifierNormalizingSchema;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;

public class AttributeHelper {

    public static final List<AttributeData> THERMOO_ATTRIBUTES = List.of(
            new AttributeData(
                    Thermoo.id("base_min_temperature"),
                    ThermooAttributes.MIN_TEMPERATURE
            ),
            new AttributeData(
                    Thermoo.id("base_max_temperature"),
                    ThermooAttributes.MAX_TEMPERATURE
            ),
            new AttributeData(
                    Thermoo.id("base_heat_resistance"),
                    ThermooAttributes.HEAT_RESISTANCE
            ),
            new AttributeData(
                    Thermoo.id("base_frost_resistance"),
                    ThermooAttributes.FROST_RESISTANCE
            )
    );

    private static final String PREFIX = "thermoo:generic.";

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

    public static String fixPrefixedAttributeIds(String id) {
        String normalizedID = IdentifierNormalizingSchema.normalize(id);

        String normalizedPrefix = IdentifierNormalizingSchema.normalize(PREFIX);
        if (normalizedID.startsWith(normalizedPrefix)) {
            return "thermoo:" + normalizedID.substring(normalizedPrefix.length());
        }

        return id;
    }

    public record AttributeData(Identifier id, RegistryEntry<EntityAttribute> attribute) {

    }

    private AttributeHelper() {

    }

}

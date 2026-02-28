package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;

/**
 * A temperature effect that applies an attribute modifier to a victim.
 * <p>
 * Includes options to increase the modifier in strength with respect to the target's
 * {@linkplain TemperatureAware#thermoo$getTemperatureScale() current temperature scale}.
 */
public final class AttributeModifierEffect implements TemperatureEffectV2 {
    public static final MapCodec<AttributeModifierEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                            .fieldOf("attribute_type")
                            .forGetter(AttributeModifierEffect::attribute),
                    Codec.DOUBLE
                            .fieldOf("value")
                            .forGetter(AttributeModifierEffect::value),
                    Identifier.CODEC
                            .fieldOf("id")
                            .forGetter(AttributeModifierEffect::id),
                    AttributeModifier.Operation.CODEC
                            .fieldOf("operation")
                            .forGetter(AttributeModifierEffect::operation),
                    Codec.BOOL
                            .fieldOf("scale_with_temperature")
                            .forGetter(AttributeModifierEffect::scaleWithTemperature)
            ).apply(instance, AttributeModifierEffect::new)
    );

    private final Holder<Attribute> attribute;
    private final double value;
    private final Identifier id;
    private final AttributeModifier.Operation operation;
    private final boolean scaleWithTemperature;

    private AttributeModifierEffect(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation,
            boolean scaleWithTemperature
    ) {
        this.value = value;
        this.attribute = attribute;
        this.id = id;
        this.operation = operation;
        this.scaleWithTemperature = scaleWithTemperature;
    }

    private static AttributeModifierEffect createChecked(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation,
            boolean scaleWithTemperature
    ) {
        Preconditions.checkNotNull(attribute, "Attribute may not be null");
        Preconditions.checkArgument(Double.isFinite(value), "Value must be finite");
        Preconditions.checkNotNull(id, "ID may not be null");
        Preconditions.checkNotNull(operation, "Operation may not be null");

        return new AttributeModifierEffect(attribute, value, id, operation, scaleWithTemperature);
    }

    public static AttributeModifierEffect create(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation
    ) {
        return createChecked(attribute, value, id, operation, false);
    }

    public static AttributeModifierEffect create(Holder<Attribute> attribute, AttributeModifier modifier) {
        return create(attribute, modifier.amount(), modifier.id(), modifier.operation());
    }

    public static AttributeModifierEffect createScaled(
            Holder<Attribute> attribute,
            double value,
            Identifier id,
            AttributeModifier.Operation operation
    ) {
        return createChecked(attribute, value, id, operation, true);
    }

    public static AttributeModifierEffect createScaled(Holder<Attribute> attribute, AttributeModifier modifier) {
        return createScaled(attribute, modifier.amount(), modifier.id(), modifier.operation());
    }

    @Override
    public boolean apply(LivingEntity victim, Level level) {
        AttributeInstance attrInstance = victim.getAttribute(this.attribute);

        if (attrInstance != null && !attrInstance.hasModifier(this.id)) {
            attrInstance.addTransientModifier(new AttributeModifier(this.id, this.value, this.operation));
            return true;
        }

        return false;
    }

    @Override
    public void remove(LivingEntity victim, Level level) {
        AttributeInstance attributeInstance = victim.getAttribute(this.attribute);

        if (attributeInstance != null) {
            attributeInstance.removeModifier(this.id);
        }
    }

    @Override
    public MapCodec<AttributeModifierEffect> codec() {
        return CODEC;
    }

    public double value() {
        return value;
    }

    public Holder<Attribute> attribute() {
        return attribute;
    }

    public Identifier id() {
        return id;
    }

    public AttributeModifier.Operation operation() {
        return operation;
    }

    public boolean scaleWithTemperature() {
        return scaleWithTemperature;
    }
}
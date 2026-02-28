package com.github.thedeathlycow.thermoo.api.temperature.effect.v2.variants;

import com.github.thedeathlycow.thermoo.api.temperature.effect.v2.TemperatureEffectV2;
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
import org.jetbrains.annotations.Range;

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
                            .forGetter(AttributeModifierEffect::operation)
            ).apply(instance, AttributeModifierEffect::new)
    );

    private final Holder<Attribute> attribute;
    private final double value;
    private final Identifier id;
    private final AttributeModifier.Operation operation;

    private AttributeModifierEffect(Holder<Attribute> attribute, double value, Identifier id, AttributeModifier.Operation operation) {
        this.value = value;
        this.attribute = attribute;
        this.id = id;
        this.operation = operation;
    }

    public static AttributeModifierEffect create(Holder<Attribute> attribute, double value, Identifier id, AttributeModifier.Operation operation) {
        return new AttributeModifierEffect(attribute, value, id, operation);
    }

    public static AttributeModifierEffect fromAttributeAndModifier(Holder<Attribute> attribute, AttributeModifier modifier) {
        return create(attribute, modifier.amount(), modifier.id(), modifier.operation());
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
    @Range(from = 1, to = Integer.MAX_VALUE)
    public int interval() {
        return TemperatureEffectV2.DEFAULT_INTERVAL;
    }

    @Override
    public MapCodec<? extends AttributeModifierEffect> codec() {
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
}
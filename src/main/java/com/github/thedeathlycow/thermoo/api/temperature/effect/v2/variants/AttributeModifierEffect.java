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

public final class AttributeModifierEffect implements TemperatureEffectV2 {
    public static final MapCodec<AttributeModifierEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT
                            .fieldOf("value")
                            .forGetter(AttributeModifierEffect::value),
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                            .fieldOf("attribute_type")
                            .forGetter(AttributeModifierEffect::attribute),
                    Identifier.CODEC
                            .fieldOf("id")
                            .forGetter(AttributeModifierEffect::id),
                    AttributeModifier.Operation.CODEC
                            .fieldOf("operation")
                            .forGetter(AttributeModifierEffect::operation)
            ).apply(instance, AttributeModifierEffect::new)
    );

    private final float value;
    private final Holder<Attribute> attribute;
    private final Identifier id;
    private final AttributeModifier.Operation operation;

    private AttributeModifierEffect(float value, Holder<Attribute> attribute, Identifier id, AttributeModifier.Operation operation) {
        this.value = value;
        this.attribute = attribute;
        this.id = id;
        this.operation = operation;
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
    public MapCodec<? extends AttributeModifierEffect> codec() {
        return CODEC;
    }

    public float value() {
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
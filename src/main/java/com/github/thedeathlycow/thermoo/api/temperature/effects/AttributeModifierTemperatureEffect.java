package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

/**
 * A temperature effect that applies an attribute modifier to a victim.
 * <p>
 * Includes options to increase the modifier in strength with respect to the target's
 * {@linkplain TemperatureAware#thermoo$getTemperatureScale() current temperature scale}.
 */
public final class AttributeModifierTemperatureEffect extends TemperatureEffect<AttributeModifierTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT
                            .fieldOf("value")
                            .forGetter(Config::value),
                    Registries.ATTRIBUTE.getEntryCodec()
                            .fieldOf("attribute_type")
                            .forGetter(Config::attribute),
                    Identifier.CODEC
                            .fieldOf("id")
                            .forGetter(Config::id),
                    EntityAttributeModifier.Operation.CODEC
                            .fieldOf("operation")
                            .forGetter(Config::operation)
            ).apply(instance, Config::new)
    );

    public AttributeModifierTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        EntityAttributeInstance attrInstance = victim.getAttributeInstance(config.attribute);
        if (attrInstance != null && !attrInstance.hasModifier(config.id)) {
            attrInstance.addTemporaryModifier(
                    new EntityAttributeModifier(
                            config.id,
                            config.value,
                            config.operation
                    )
            );
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        // only apply when the entity has this attribute
        EntityAttributeInstance attrInstance = victim.getAttributeInstance(config.attribute);
        return attrInstance != null;
    }

    @Override
    public void remove(LivingEntity victim, ServerWorld serverWorld, Config config) {
        super.remove(victim, serverWorld, config);
        EntityAttributeInstance attributeInstance = victim.getAttributeInstance(config.attribute);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(config.id);
        }
    }


    public record Config(
            float value,
            RegistryEntry<EntityAttribute> attribute,
            Identifier id,
            EntityAttributeModifier.Operation operation
    ) {
    }

}

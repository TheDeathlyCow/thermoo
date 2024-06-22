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
import net.minecraft.util.Uuids;

import java.util.UUID;

/**
 * A temperature effect that applies an attribute modifier to a victim that increases in strength with respect to the
 * current temperature scale, as computed by {@link TemperatureAware#thermoo$getTemperatureScale()}
 */
public class ScalingAttributeModifierTemperatureEffect extends TemperatureEffect<ScalingAttributeModifierTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT
                            .fieldOf("scale")
                            .orElse(1f)
                            .forGetter(Config::scale),
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

    public ScalingAttributeModifierTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        EntityAttributeInstance attrInstance = victim.getAttributeInstance(config.attribute);
        if (attrInstance == null) {
            return;
        }

        // add the modifier back with greater strength
        double amount = config.scale * victim.thermoo$getTemperatureScale();

        attrInstance.addTemporaryModifier(
                new EntityAttributeModifier(
                        config.id,
                        amount,
                        config.operation
                )
        );
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        // this effect will always apply as it scales with the temperature
        EntityAttributeInstance attrInstance = victim.getAttributeInstance(config.attribute);

        if (attrInstance == null) {
            return false;
        }

        EntityAttributeModifier modifier = attrInstance.getModifier(config.id);
        if (modifier == null) {
            return true;
        }

        double newAmount = config.scale * victim.thermoo$getTemperatureScale();
        double currentValue = modifier.value();

        boolean shouldApply = newAmount != currentValue;

        if (shouldApply) {
            // remove the modifier - even if the other predicate tests fail
            attrInstance.removeModifier(config.id);
        }

        return shouldApply;
    }

    public record Config(
            float scale,
            RegistryEntry<EntityAttribute> attribute,
            Identifier id,
            EntityAttributeModifier.Operation operation
    ) {
    }

}

package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


/**
 * A temperature effect that applies an attribute modifier to a victim that increases in strength with respect to the
 * current temperature scale, as computed by {@link TemperatureAware#thermoo$getTemperatureScale()}
 */
public final class ScalingAttributeModifierTemperatureEffect extends TemperatureEffect<ScalingAttributeModifierTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.FLOAT
                            .fieldOf("scale")
                            .orElse(1f)
                            .forGetter(Config::scale),
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
                            .fieldOf("attribute_type")
                            .forGetter(Config::attribute),
                    Identifier.CODEC
                            .fieldOf("id")
                            .forGetter(Config::id),
                    AttributeModifier.Operation.CODEC
                            .fieldOf("operation")
                            .forGetter(Config::operation)
            ).apply(instance, Config::new)
    );

    public ScalingAttributeModifierTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerLevel serverLevel, Config config) {
        AttributeInstance attrInstance = victim.getAttribute(config.attribute);
        if (attrInstance == null) {
            return;
        }

        // add the modifier back with greater strength
        double amount = config.scale * victim.thermoo$getTemperatureScale();

        attrInstance.addTransientModifier(
                new AttributeModifier(
                        config.id,
                        amount,
                        config.operation
                )
        );
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        // this effect will always apply as it scales with the temperature
        AttributeInstance attrInstance = victim.getAttribute(config.attribute);

        if (attrInstance == null) {
            return false;
        }

        AttributeModifier modifier = attrInstance.getModifier(config.id);
        if (modifier == null) {
            return true;
        }

        double newAmount = config.scale * victim.thermoo$getTemperatureScale();
        double currentValue = modifier.amount();

        boolean shouldApply = newAmount != currentValue;

        if (shouldApply) {
            // remove the modifier - even if the other predicate tests fail
            attrInstance.removeModifier(config.id);
        }

        return shouldApply;
    }

    public record Config(
            float scale,
            Holder<Attribute> attribute,
            Identifier id,
            AttributeModifier.Operation operation
    ) {
        /**
         * @return Returns the value of {@link #id}
         * @deprecated This field was named based on old name in Mojmap. Use {@link #id} to better conform to the new name.
         */
        @Deprecated(since = "9.0.0", forRemoval = true)
        public Identifier location() {
            return id;
        }
    }
}

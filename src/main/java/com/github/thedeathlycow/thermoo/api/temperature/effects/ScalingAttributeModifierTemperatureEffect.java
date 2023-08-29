package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.google.gson.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.UUID;

/**
 * A temperature effect that applies an attribute modifier to a victim that increases in strength with respect to the
 * current temperature scale, as computed by {@link TemperatureAware#thermoo$getTemperatureScale()}
 */
public class ScalingAttributeModifierTemperatureEffect extends TemperatureEffect<ScalingAttributeModifierTemperatureEffect.Config> {

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        EntityAttributeInstance attrInstance = victim.getAttributeInstance(config.attribute);
        if (attrInstance == null) {
            return;
        }

        // remove the existing modifier

        // add the modifier back with greater strength
        double amount = config.scale * victim.thermoo$getTemperatureScale();

        attrInstance.addTemporaryModifier(
                new EntityAttributeModifier(
                        config.uuid,
                        config.name,
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

        EntityAttributeModifier modifier = attrInstance.getModifier(config.uuid);
        if (modifier == null) {
            return true;
        }

        double newAmount = config.scale * victim.thermoo$getTemperatureScale();
        double currentValue = modifier.getValue();

        boolean shouldApply = newAmount != currentValue;

        if (shouldApply) {
            // remove the modifier - even if the other predicate tests fail
            attrInstance.removeModifier(config.uuid);
        }

        return shouldApply;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
        return Config.fromJson(json);
    }

    public record Config(
            float scale,
            EntityAttribute attribute,
            UUID uuid,
            String name,
            EntityAttributeModifier.Operation operation
    ) {

        public static Config fromJson(JsonElement jsonElement) throws JsonSyntaxException {

            //// init defaults ////
            float scale = 1.0f;
            String name = "";

            JsonObject json = jsonElement.getAsJsonObject();

            //// overwrite defaults if present ////
            if (json.has("scale")) {
                scale = json.get("scale").getAsFloat();
            }

            if (json.has("name")) {
                name = json.get("name").getAsString();
            }

            //// grab required values ////

            UUID id = UUID.fromString(json.get("modifier_uuid").getAsString());

            Identifier attrID = new Identifier(json.get("attribute_type").getAsString());
            EntityAttribute attribute = Registries.ATTRIBUTE.get(attrID);

            if (attribute == null) {
                throw new JsonParseException("Unknown attribute: " + attrID);
            }

            EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.valueOf(
                    json.get("operation").getAsString().toUpperCase()
            );

            return new Config(scale, attribute, id, name, operation);
        }
    }

}

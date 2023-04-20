package com.github.thedeathlycow.thermoo.api.temperature.effects;


import com.google.gson.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Applies {@link StatusEffect}s to {@link LivingEntity}s if their temperature scale is within a given range.
 * <p>
 * The type, duration, and intensity can all be configured of each status effect can be configured. May specify 1 or more
 * effects to all be applied at once.
 */
public class StatusEffectTemperatureEffect extends TemperatureEffect<StatusEffectTemperatureEffect.Config> {

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        for (Config.ConfigEffect effect : config.effects) {

        }
    }

    private void addEffect(LivingEntity victim, Config.ConfigEffect effect) {
        StatusEffectInstance existingEffect = victim.getStatusEffect(effect.type);
        if (existingEffect != null) {
            if (existingEffect.getAmplifier() == effect.amplifier && existingEffect.getDuration() > effect.duration / 2) {
                return;
            }
        }

        victim.addStatusEffect(
                new StatusEffectInstance(
                        effect.type,
                        effect.duration,
                        effect.amplifier,
                        true, true
                ),
                null
        );
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        // only try to apply every 5 ticks
        return victim.age % 5 == 0;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
        return Config.fromJson(json);
    }

    public record Config(
            Collection<ConfigEffect> effects
    ) {

        protected record ConfigEffect(
                StatusEffect type,
                int duration,
                int amplifier
        ) {
        }

        public static Config fromJson(JsonElement json) throws JsonSyntaxException {
            JsonObject object = json.getAsJsonObject();

            // get effects
            JsonElement effectsJson = object.get("effects");

            Collection<ConfigEffect> effects;
            if (effectsJson.isJsonArray()) {
                effects = new ArrayList<>();
                for (var effect : effectsJson.getAsJsonArray()) {
                    effects.add(deserializeEffect(effect));
                }
            } else {
                effects = Collections.singleton(deserializeEffect(effectsJson));
            }

            return new Config(effects);
        }

        private static ConfigEffect deserializeEffect(JsonElement json) throws JsonSyntaxException {
            JsonObject object = json.getAsJsonObject();

            // get numbers
            int amplifier = object.get("amplifier").getAsInt();

            // duration defaults to 20
            int duration = 20;
            if (object.has("duration")) {
                duration = object.get("duration").getAsInt();
            }

            // get effect
            Identifier effectID = new Identifier(object.get("effect").getAsString());
            StatusEffect effect = Registries.STATUS_EFFECT.get(effectID);
            if (effect == null) {
                throw new JsonParseException("Unknown status effect: " + effectID);
            }

            return new ConfigEffect(effect, duration, amplifier);
        }
    }


}

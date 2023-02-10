package com.github.thedeathlycow.thermoo.api.temperature.effects;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Applies {@link StatusEffect}s to {@link LivingEntity}s if their temperature scale is within a given range.
 * <p>
 * The status effect type, duration, and intensity can all be configured, as well as the temperature scale at which
 * the effect will apply.
 */
public class StatusEffectTemperatureEffect extends TemperatureEffect<StatusEffectTemperatureEffect.Config> {

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        victim.addStatusEffect(createEffectInstance(config), null);
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {

        if (config.temperatureScaleRange.test(victim.thermoo$getTemperatureScale())) {
            StatusEffectInstance currentEffectInstance = victim.getStatusEffect(config.effect());

            // can apply if effect is not present
            if (currentEffectInstance == null) {
                return true;
            }

            // always update effects
            if (currentEffectInstance.getAmplifier() < config.amplifier()) {
                return true;
            }

            // apply if effect is closeish to running out (done to prevent timer spam)
            return currentEffectInstance.getDuration() < (config.duration / 2);
        }

        return false;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
        return Config.fromJson(json);
    }

    private static StatusEffectInstance createEffectInstance(Config config) {
        return createEffectInstance(config, true, true);
    }

    private static StatusEffectInstance createEffectInstance(Config config, boolean ambient, boolean visible) {
        return new StatusEffectInstance(config.effect(), config.duration(), config.amplifier(), ambient, visible);
    }

    public record Config(
            NumberRange.FloatRange temperatureScaleRange,
            StatusEffect effect,
            int duration,
            int amplifier
    ) {
        public static Config fromJson(JsonElement json) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();

            // get numbers
            NumberRange.FloatRange temperatureScaleRange = NumberRange.FloatRange.fromJson(object.get("temperature_scale_range"));
            int amplifier = object.get("amplifier").getAsInt();

            // duration defaults to 20
            int duration = 20;
            if (object.has("duration")) {
                duration = object.get("duration").getAsInt();
            }

            // get effect
            Identifier effectID = new Identifier(object.get("effect").getAsString());
            StatusEffect effect = Registry.STATUS_EFFECT.get(effectID);
            if (effect == null) {
                throw new JsonParseException("Unknown status effect: " + effectID);
            }

            return new Config(temperatureScaleRange, effect, duration, amplifier);
        }
    }

}

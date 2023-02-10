package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;

/**
 * Applies damage to {@link net.minecraft.entity.LivingEntity}s when their temperature scale is within a given range.
 * The amount and interval of the damage pulses, but the damage source cannot be at this time. Instead, damage sources are
 * hard coded into a specific type (such as {@code thermoo:freeze_damage_legacy}).
 * <p>
 * In 1.19.4, this will be replaced with the new damage source data system. As such, it is called "Legacy".
 */
public class LegacyDamageTemperatureEffect extends TemperatureEffect<LegacyDamageTemperatureEffect.Config> {


    private final DamageSource damageSource;

    public LegacyDamageTemperatureEffect(DamageSource damageSource) {
        this.damageSource = damageSource;
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        if (!victim.world.isClient) {
            victim.damage(this.damageSource, config.amount);
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.age % config.damageInterval == 0 && config.progressThreshold.test(victim.getFreezingScale());
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
        return Config.fromJson(json, context);
    }

    public record Config(
            NumberRange.FloatRange progressThreshold,
            float amount,
            int damageInterval
    ) {

        public static Config fromJson(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();

            // get progress range
            NumberRange.FloatRange progressThreshold = NumberRange.FloatRange.fromJson(object.get("progress_threshold"));

            float amount = object.get("amount").getAsFloat();

            int damageInterval = object.get("damage_interval").getAsInt();

            return new Config(progressThreshold, amount, damageInterval);
        }
    }

}


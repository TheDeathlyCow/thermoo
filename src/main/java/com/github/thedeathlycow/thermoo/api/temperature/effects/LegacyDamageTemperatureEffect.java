package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;

import java.util.function.Function;

/**
 * Applies damage to {@link net.minecraft.entity.LivingEntity}s when their temperature scale is within a given range.
 * The amount and interval of the damage pulses, but the damage source cannot be at this time. Instead, damage sources are
 * hard coded into a specific type (such as {@code thermoo:freeze_damage_legacy}).
 *
 * @deprecated For 1.19.4+, use {@link DamageTemperatureEffect} instead
 */
@Deprecated
public class LegacyDamageTemperatureEffect extends TemperatureEffect<LegacyDamageTemperatureEffect.Config> {


    private final Function<ServerWorld, DamageSource> damageSourceSupplier;

    public LegacyDamageTemperatureEffect(Function<ServerWorld, DamageSource> damageSourceSupplier) {
        this.damageSourceSupplier = damageSourceSupplier;
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        victim.damage(this.damageSourceSupplier.apply(serverWorld), config.amount);
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.age % config.damageInterval == 0;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
        return Config.fromJson(json, context);
    }

    public record Config(
            float amount,
            int damageInterval
    ) {

        public static Config fromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
            JsonObject object = json.getAsJsonObject();

            float amount = object.get("amount").getAsFloat();

            int damageInterval = object.get("damage_interval").getAsInt();

            return new Config(amount, damageInterval);
        }
    }

}


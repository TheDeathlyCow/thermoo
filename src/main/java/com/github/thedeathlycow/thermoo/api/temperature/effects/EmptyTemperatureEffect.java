package com.github.thedeathlycow.thermoo.api.temperature.effects;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Represents an 'empty' temperature effect that is never applied and does nothing.
 * <p>
 * Primarily used for overriding lower-priority effects of the same identifier in order to disable them.
 */
public final class EmptyTemperatureEffect extends TemperatureEffect<EmptyTemperatureEffect.Config> {

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        // does nothing
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return false;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
        return Config.INSTANCE;
    }

    public static final class Config {

        public static final Config INSTANCE = new Config();

        private Config() {

        }
    }

}
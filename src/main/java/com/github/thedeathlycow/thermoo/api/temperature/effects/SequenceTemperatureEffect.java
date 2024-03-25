package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.google.gson.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies multiple child temperature effects at once. Useful for when you want to apply several different temperature
 * effects under the same base set of conditions, without the overhead of checking those conditions multiple times.
 */
public class SequenceTemperatureEffect extends TemperatureEffect<SequenceTemperatureEffect.Config> {

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        for (ConfiguredTemperatureEffect<?> child : config.children()) {
            EntityType<?> childType = child.getEntityType();
            if (childType == null || victim.getType() == childType) {
                child.applyIfPossible(victim);
            }
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return true;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
        return Config.fromJson(json, context);
    }

    public record Config(List<ConfiguredTemperatureEffect<?>> children) {

        public static Config fromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
            JsonObject object = json.getAsJsonObject();

            JsonArray jsonChildren = object.get("children").getAsJsonArray();
            List<ConfiguredTemperatureEffect<?>> children = new ArrayList<>(jsonChildren.size());
            for (JsonElement jsonChild : jsonChildren) {
                ConfiguredTemperatureEffect<?> child = context.deserialize(jsonChild, ConfiguredTemperatureEffect.class);
                children.add(child);
            }

            return new Config(children);
        }

    }

}

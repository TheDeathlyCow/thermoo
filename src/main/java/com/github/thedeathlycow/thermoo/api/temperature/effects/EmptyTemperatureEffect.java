package com.github.thedeathlycow.thermoo.api.temperature.effects;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.function.Supplier;

/**
 * Represents an 'empty' temperature effect that is never applied and does nothing.
 * <p>
 * Primarily used for overriding lower-priority effects of the same identifier in order to disable them.
 */
public final class EmptyTemperatureEffect extends TemperatureEffect<EmptyTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = Codec.unit(() -> Config.INSTANCE);

    public EmptyTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        // does nothing
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return false;
    }

    public static final class Config {

        public static final Config INSTANCE = new Config();

        private Config() {

        }
    }

}
package com.github.thedeathlycow.thermoo.api.temperature.effects;


import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

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
    public void apply(LivingEntity victim, ServerLevel serverLevel, Config config) {
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
package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

/**
 * Applies multiple child temperature effects at once. Useful for when you want to apply several different temperature
 * effects under the same base set of conditions, without the overhead of checking those conditions multiple times.
 */
public class SequenceTemperatureEffect extends TemperatureEffect<SequenceTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(ConfiguredTemperatureEffect.CODEC)
                            .fieldOf("children")
                            .forGetter(Config::children)
            ).apply(instance, Config::new)
    );

    public SequenceTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        for (ConfiguredTemperatureEffect<?> child : config.children()) {
            EntityType<?> childType = child.entityType().orElse(null);
            if (childType == null || victim.getType() == childType) {
                child.applyIfPossible(victim);
            }
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return true;
    }

    public record Config(List<ConfiguredTemperatureEffect<?>> children) {

    }

}

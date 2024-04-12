package com.github.thedeathlycow.thermoo.api.temperature.effects;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;

import java.util.Collection;
import java.util.List;

/**
 * Applies {@link StatusEffect}s to {@link LivingEntity}s if their temperature scale is within a given range.
 * <p>
 * The type, duration, and intensity can all be configured of each status effect can be configured. May specify 1 or more
 * effects to all be applied at once.
 */
public class StatusEffectTemperatureEffect extends TemperatureEffect<StatusEffectTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(Config.ConfigEffect.CODEC)
                            .fieldOf("effects")
                            .forGetter(Config::effects)
            ).apply(instance, Config::new)
    );


    public StatusEffectTemperatureEffect(Codec<Config> configCodec) {
        super(configCodec);
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {
        for (Config.ConfigEffect effect : config.effects) {
            this.addEffect(victim, effect);
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

    public record Config(
            List<ConfigEffect> effects
    ) {
        protected record ConfigEffect(
                StatusEffect type,
                int duration,
                int amplifier
        ) {

            public static final Codec<ConfigEffect> CODEC = RecordCodecBuilder.create(
                    instance -> {
                        return instance.group(
                                Registries.STATUS_EFFECT.getCodec()
                                        .fieldOf("effect")
                                        .forGetter(ConfigEffect::type),
                                Codecs.POSITIVE_INT
                                        .fieldOf("duration")
                                        .orElse(20)
                                        .forGetter(ConfigEffect::duration),
                                Codecs.NONNEGATIVE_INT
                                        .fieldOf("amplifier")
                                        .forGetter(ConfigEffect::amplifier)
                        ).apply(instance, ConfigEffect::new);
                    }
            );
        }
    }


}

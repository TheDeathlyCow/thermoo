package com.github.thedeathlycow.thermoo.api.temperature.effects;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/**
 * Applies {@link MobEffect}s to {@link LivingEntity}s if their temperature scale is within a given range.
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
    public void apply(LivingEntity victim, ServerLevel serverWorld, Config config) {
        for (Config.ConfigEffect effect : config.effects) {
            this.addEffect(victim, effect);
        }
    }

    private void addEffect(LivingEntity victim, Config.ConfigEffect effect) {
        MobEffectInstance existingEffect = victim.getEffect(effect.type);
        if (existingEffect != null) {
            if (existingEffect.getAmplifier() == effect.amplifier && existingEffect.getDuration() > effect.duration / 2) {
                return;
            }
        }

        victim.addEffect(
                new MobEffectInstance(
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
        return victim.tickCount % 5 == 0;
    }

    public record Config(
            List<ConfigEffect> effects
    ) {
        protected record ConfigEffect(
                Holder<MobEffect> type,
                int duration,
                int amplifier
        ) {

            public static final Codec<ConfigEffect> CODEC = RecordCodecBuilder.create(
                    instance -> {
                        return instance.group(
                                BuiltInRegistries.MOB_EFFECT.holderByNameCodec()
                                        .fieldOf("effect")
                                        .forGetter(ConfigEffect::type),
                                ExtraCodecs.POSITIVE_INT
                                        .fieldOf("duration")
                                        .orElse(20)
                                        .forGetter(ConfigEffect::duration),
                                ExtraCodecs.NON_NEGATIVE_INT
                                        .fieldOf("amplifier")
                                        .forGetter(ConfigEffect::amplifier)
                        ).apply(instance, ConfigEffect::new);
                    }
            );
        }
    }
}

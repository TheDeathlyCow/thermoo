package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies {@link MobEffect}s to {@link LivingEntity}s if their temperature scale is within a given range.
 * <p>
 * The type, duration, and intensity can all be configured of each status effect can be configured. May specify 1 or more
 * effects to all be applied at once.
 */
public final class MobEffectTemperatureEffect implements TemperatureEffect {
    public static final MapCodec<MobEffectTemperatureEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ConfiguredMobEffect.CODEC.listOf()
                            .fieldOf("effects")
                            .forGetter(MobEffectTemperatureEffect::effects)
            ).apply(instance, MobEffectTemperatureEffect::new)
    );

    private final List<ConfiguredMobEffect> effects;

    private MobEffectTemperatureEffect(List<ConfiguredMobEffect> effects) {
        this.effects = effects;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ConfiguredMobEffect.Builder effect(Holder<MobEffect> effect) {
        Preconditions.checkNotNull(effect);

        return new ConfiguredMobEffect.Builder(effect);
    }

    @Override
    public boolean apply(LivingEntity victim, Level level) {
        boolean appliedAny = false;

        for (ConfiguredMobEffect effect : this.effects) {
            appliedAny |= this.applyEffect(victim, effect);
        }

        return appliedAny;
    }

    private boolean applyEffect(LivingEntity victim, ConfiguredMobEffect effect) {
        MobEffectInstance existingEffect = victim.getEffect(effect.effect());
        if (existingEffect != null) {
            if (existingEffect.getAmplifier() == effect.amplifier() && existingEffect.getDuration() > effect.duration() / 2) {
                return false;
            }
        }

        return victim.addEffect(
                new MobEffectInstance(
                        effect.effect(),
                        effect.duration(),
                        effect.amplifier(),
                        effect.ambient(),
                        effect.visible(),
                        effect.showIcon()
                ),
                null
        );
    }

    @Override
    public MapCodec<MobEffectTemperatureEffect> codec() {
        return CODEC;
    }

    public List<ConfiguredMobEffect> effects() {
        return effects;
    }

    public static final class Builder {
        private final List<ConfiguredMobEffect> effects = new ArrayList<>();

        private Builder() {

        }

        public Builder addEffect(ConfiguredMobEffect.Builder effect) {
            Preconditions.checkNotNull(effect);
            this.effects.add(effect.build());
            return this;
        }

        public MobEffectTemperatureEffect build() {
            return new MobEffectTemperatureEffect(this.effects);
        }
    }
}
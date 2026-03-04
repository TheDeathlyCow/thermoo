package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Applies a set of {@link MobEffect}s to targets. Note that mob effects are not removed from targets immediately when
 * this effect is removed, they will just simply be allowed to expire.
 */
public final class MobEffectEffect implements TemperatureEffect {
    /**
     * The codec for the object.
     */
    public static final MapCodec<MobEffectEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    TemplateMobEffect.CODEC.listOf()
                            .fieldOf("effects")
                            .forGetter(MobEffectEffect::effects)
            ).apply(instance, MobEffectEffect::new)
    );

    private final List<TemplateMobEffect> effects;

    private MobEffectEffect(List<TemplateMobEffect> effects) {
        this.effects = effects;
    }

    /**
     * Creates a new builder for the effect.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates an effect for {@link Builder#addEffect(TemplateMobEffect.Builder)}
     *
     * @param effect The effect type.
     * @throws NullPointerException if the {@code effect} is {@code null}.
     */
    public static TemplateMobEffect.Builder effect(Holder<MobEffect> effect) {
        Preconditions.checkNotNull(effect);

        return new TemplateMobEffect.Builder(effect);
    }

    /**
     * Applies the mob effects to the target.
     * <p>
     * If the target already has one of the template effects specified here, then it will only be updated if its
     * remaining duration is less than half of the template's duration OR if it has a different amplifier from the template
     * effect.
     *
     * @param target The entity receiving the effect.
     * @param context Additional context for the effect.
     * @return Returns {@code true} if any mob effect was applied, {@code false} otherwise.
     */
    @Override
    public boolean apply(LivingEntity target, TemperatureEffectContext context) {
        boolean appliedAny = false;

        for (TemplateMobEffect effect : this.effects) {
            appliedAny |= this.applyEffect(target, effect);
        }

        return appliedAny;
    }

    private boolean applyEffect(LivingEntity victim, TemplateMobEffect effect) {
        MobEffectInstance existingEffect = victim.getEffect(effect.effect());
        if (existingEffect != null) {
            if (existingEffect.getAmplifier() == effect.amplifier() && existingEffect.getDuration() > effect.duration() / 2) {
                return false;
            }
        }

        return victim.addEffect(effect.toEffectInstance(), null);
    }

    /**
     * @return Returns {@link #CODEC}
     */
    @Override
    public MapCodec<MobEffectEffect> codec() {
        return CODEC;
    }

    /**
     * The list of configured effect templates that this effect will apply.
     */
    public List<TemplateMobEffect> effects() {
        return effects;
    }

    /**
     * Builder for this effect.
     */
    public static final class Builder {
        private final List<TemplateMobEffect> effects = new ArrayList<>();

        private Builder() {

        }

        /**
         * Adds a template effect to this builder.
         *
         * @param effect The effect to add, may not be {@code null}.
         * @return Returns this builder.
         * @throws NullPointerException if the {@code effect} is {@code null}.
         */
        public Builder addEffect(TemplateMobEffect.Builder effect) {
            Preconditions.checkNotNull(effect);
            this.effects.add(effect.build());
            return this;
        }

        /**
         * Adds a collection of template effects to this builder. Any {@code null} entries in the collection will be
         * skipped.
         *
         * @param effects The effects to add, may not be {@code null}.
         * @return Returns this builder.
         * @throws NullPointerException if {@code effects} is {@code null}.
         */
        public Builder addEffects(Collection<TemplateMobEffect.Builder> effects) {
            Preconditions.checkNotNull(effects);

            for (var effectBuilder : effects) {
                if (effectBuilder != null) {
                    this.effects.add(effectBuilder.build());
                }
            }

            return this;
        }

        /**
         * Creates a new mob effect-effect from this builder.
         */
        public MobEffectEffect build() {
            return new MobEffectEffect(this.effects);
        }
    }
}
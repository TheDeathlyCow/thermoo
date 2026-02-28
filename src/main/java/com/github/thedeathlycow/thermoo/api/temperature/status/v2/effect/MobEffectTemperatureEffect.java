package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class MobEffectTemperatureEffect implements TemperatureEffectV2 {
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

    public static final class ConfiguredMobEffect {
        private static final int DEFAULT_DURATION = 20 * 5;

        public static final Codec<ConfiguredMobEffect> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        BuiltInRegistries.MOB_EFFECT.holderByNameCodec()
                                .fieldOf("effect")
                                .forGetter(ConfiguredMobEffect::effect),
                        ExtraCodecs.NON_NEGATIVE_INT
                                .optionalFieldOf("duration", DEFAULT_DURATION)
                                .forGetter(ConfiguredMobEffect::duration),
                        Codec.intRange(0, 255)
                                .optionalFieldOf("amplifier", 0)
                                .forGetter(ConfiguredMobEffect::amplifier),
                        Codec.BOOL
                                .optionalFieldOf("ambient", false)
                                .forGetter(ConfiguredMobEffect::ambient),
                        Codec.BOOL
                                .optionalFieldOf("visible", true)
                                .forGetter(ConfiguredMobEffect::visible),
                        Codec.BOOL
                                .optionalFieldOf("show_icon", true)
                                .forGetter(ConfiguredMobEffect::showIcon)
                ).apply(instance, ConfiguredMobEffect::new)
        );

        private final Holder<MobEffect> effect;
        private final int duration;
        private final int amplifier;
        private final boolean ambient;
        private final boolean visible;
        private final boolean showIcon;

        private ConfiguredMobEffect(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
            this.ambient = ambient;
            this.visible = visible;
            this.showIcon = showIcon;
        }

        public Holder<MobEffect> effect() {
            return effect;
        }

        public int duration() {
            return duration;
        }

        public int amplifier() {
            return amplifier;
        }

        public boolean ambient() {
            return ambient;
        }

        public boolean visible() {
            return visible;
        }

        public boolean showIcon() {
            return showIcon;
        }

        public static final class Builder {
            private final Holder<MobEffect> effect;
            @Nullable
            private Integer duration = null;
            @Nullable
            private Integer amplifier = null;
            private boolean ambient = false;
            private boolean visible = true;
            private boolean showIcon = true;

            private Builder(Holder<MobEffect> effect) {
                this.effect = effect;
            }

            public Builder withDuration(int duration) {
                Preconditions.checkArgument(duration >= 0, "Duration may not be negative");
                Preconditions.checkState(this.duration == null, "Duration already set");

                this.duration = duration;
                return this;
            }

            public Builder withAmplifier(int amplifier) {
                Preconditions.checkArgument(amplifier >= 0 && amplifier <= 255, "Amplifier must be between 0 and 255");
                Preconditions.checkState(this.amplifier == null, "Amplifier already set");

                this.amplifier = amplifier;
                return this;
            }

            public Builder ambient() {
                this.ambient = true;
                return this;
            }

            public Builder invisible() {
                this.visible = false;
                return this;
            }

            public Builder doNotShowIcon() {
                this.showIcon = false;
                return this;
            }

            public ConfiguredMobEffect build() {
                return new ConfiguredMobEffect(
                        this.effect,
                        this.duration != null ? this.duration : DEFAULT_DURATION,
                        this.amplifier != null ? this.amplifier : 0,
                        this.ambient,
                        this.visible,
                        this.showIcon
                );
            }
        }
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
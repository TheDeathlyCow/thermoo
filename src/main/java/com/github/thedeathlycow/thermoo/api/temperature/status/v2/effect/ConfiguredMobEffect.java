package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * A class which stores template data for producing new instances of {@link MobEffectInstance}.
 *
 * @see MobEffectTemperatureEffect
 */
public final class ConfiguredMobEffect {
    private static final int DEFAULT_DURATION = 20 * 5;

    /**
     * The codec for this type.
     */
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

    MobEffectInstance toEffectInstance() {
        return new MobEffectInstance(
                this.effect(),
                this.duration(),
                this.amplifier(),
                this.ambient(),
                this.visible(),
                this.showIcon()
        );
    }

    /**
     * The mob effect type
     */
    public Holder<MobEffect> effect() {
        return effect;
    }

    /**
     * The duration of the effect instance in ticks
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    public int duration() {
        return duration;
    }

    /**
     * The amplifier of the effect instance
     */
    @Range(from = 0, to = 255)
    public int amplifier() {
        return amplifier;
    }

    /**
     * Whether this is an ambient effect
     */
    public boolean ambient() {
        return ambient;
    }

    /**
     * Whether this effect's particles should be visible
     */
    public boolean visible() {
        return visible;
    }

    /**
     * Whether the icon for the effect should be shown on the client
     */
    public boolean showIcon() {
        return showIcon;
    }

    public static final class Builder {
        private final Holder<MobEffect> effect;
        private int duration = DEFAULT_DURATION;
        private int amplifier = 0;
        private boolean ambient = false;
        private boolean visible = true;
        private boolean showIcon = true;

        /**
         * Create new instances of this builder with {@link MobEffectTemperatureEffect#effect(Holder)}.
         */
        Builder(Holder<MobEffect> effect) {
            this.effect = effect;
        }

        /**
         * Sets the duration of the effect.
         *
         * @param duration An int that is greater than or equal to 0.
         * @return Returns this builder.
         * @throws IllegalArgumentException if the duration is outside the specified bound
         */
        public Builder withDuration(int duration) {
            Preconditions.checkArgument(duration >= 0, "Duration may not be negative");
            this.duration = duration;
            return this;
        }

        /**
         * Sets the amplifier of the effect.
         *
         * @param amplifier An int that is between 0 and 255 (inclusive).
         * @return Returns this builder.
         * @throws IllegalArgumentException if the amplifier is outside the specified bound
         */
        public Builder withAmplifier(int amplifier) {
            Preconditions.checkArgument(amplifier >= 0 && amplifier <= 255, "Amplifier must be between 0 and 255");

            this.amplifier = amplifier;
            return this;
        }

        /**
         * Makes this an ambient effect.
         *
         * @return Returns this builder.
         */
        public Builder ambient() {
            this.ambient = true;
            return this;
        }

        /**
         * Makes the particles of this effect invisible.
         *
         * @return Returns this builder.
         */
        public Builder invisible() {
            this.visible = false;
            return this;
        }

        /**
         * Makes this effects icon invisible on the client.
         *
         * @return Returns this builder.
         */
        public Builder doNotShowIcon() {
            this.showIcon = false;
            return this;
        }

        /**
         * Creates a new {@link ConfiguredMobEffect} from this builder.
         */
        public ConfiguredMobEffect build() {
            return new ConfiguredMobEffect(
                    this.effect,
                    this.duration,
                    this.amplifier,
                    this.ambient,
                    this.visible,
                    this.showIcon
            );
        }
    }
}
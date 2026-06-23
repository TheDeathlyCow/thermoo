/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Range;

/**
 * A class which stores template data for producing new instances of {@link MobEffectInstance}.
 *
 * @see MobEffectEffect
 */
public final class TemplateMobEffect {
    private static final int DEFAULT_DURATION = 20 * 5;

    /**
     * The codec for this type.
     */
    public static final Codec<TemplateMobEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BuiltInRegistries.MOB_EFFECT.holderByNameCodec()
                            .fieldOf("effect")
                            .forGetter(TemplateMobEffect::effect),
                    ExtraCodecs.NON_NEGATIVE_INT
                            .optionalFieldOf("duration", DEFAULT_DURATION)
                            .forGetter(TemplateMobEffect::duration),
                    Codec.intRange(0, 255)
                            .optionalFieldOf("amplifier", 0)
                            .forGetter(TemplateMobEffect::amplifier),
                    Codec.BOOL
                            .optionalFieldOf("ambient", false)
                            .forGetter(TemplateMobEffect::ambient),
                    Codec.BOOL
                            .optionalFieldOf("visible", true)
                            .forGetter(TemplateMobEffect::visible),
                    Codec.BOOL
                            .optionalFieldOf("show_icon", true)
                            .forGetter(TemplateMobEffect::showIcon)
            ).apply(instance, TemplateMobEffect::new)
    );

    private final Holder<MobEffect> effect;
    private final int duration;
    private final int amplifier;
    private final boolean ambient;
    private final boolean visible;
    private final boolean showIcon;

    private TemplateMobEffect(Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
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
         * Create new instances of this builder with {@link MobEffectEffect#effect(Holder)}.
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
         * Creates a new {@link TemplateMobEffect} from this builder.
         */
        public TemplateMobEffect build() {
            return new TemplateMobEffect(
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
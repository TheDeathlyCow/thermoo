package com.github.thedeathlycow.thermoo.api.temperature.effect.v2.variants;

import com.github.thedeathlycow.thermoo.api.temperature.effect.v2.TemperatureEffectV2;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Range;

public final class DamageEffect implements TemperatureEffectV2 {
    public static final MapCodec<DamageEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT
                            .fieldOf("amount")
                            .forGetter(DamageEffect::amount),
                    ExtraCodecs.POSITIVE_INT
                            .fieldOf("interval")
                            .forGetter(DamageEffect::interval),
                    ResourceKey.codec(Registries.DAMAGE_TYPE)
                            .fieldOf("damage_type")
                            .forGetter(DamageEffect::damageType)
            ).apply(instance, DamageEffect::new)
    );

    private final float amount;
    private final int interval;
    private final ResourceKey<DamageType> damageType;

    private DamageEffect(float amount, int interval, ResourceKey<DamageType> damageType) {
        this.amount = amount;
        this.interval = interval;
        this.damageType = damageType;
    }

    public static DamageEffect create(float amount, int interval, ResourceKey<DamageType> damageType) {
        Preconditions.checkArgument(amount >= 0f, "Damage amount must be at least 0");
        Preconditions.checkArgument(interval >= 1, "Interval must be at least 1");

        return new DamageEffect(amount, interval, damageType);
    }

    @Override
    public boolean apply(LivingEntity victim, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return victim.hurtServer(serverLevel, serverLevel.damageSources().source(damageType), amount);
        }

        return false;
    }

    @Override
    @Range(from = 1, to = Integer.MAX_VALUE)
    public int interval() {
        return this.interval;
    }

    @Override
    public MapCodec<DamageEffect> codec() {
        return CODEC;
    }

    public float amount() {
        return amount;
    }

    public ResourceKey<DamageType> damageType() {
        return damageType;
    }
}
package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
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

import java.util.Objects;

public final class DamageEffectV2 implements TemperatureEffectV2 {
    public static final MapCodec<DamageEffectV2> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT
                            .fieldOf("amount")
                            .forGetter(DamageEffectV2::amount),
                    ResourceKey.codec(Registries.DAMAGE_TYPE)
                            .fieldOf("damage_type")
                            .forGetter(DamageEffectV2::damageType)
            ).apply(instance, DamageEffectV2::new)
    );

    private final float amount;
    private final ResourceKey<DamageType> damageType;

    private DamageEffectV2(float amount, ResourceKey<DamageType> damageType) {
        this.amount = amount;
        this.damageType = damageType;
    }

    public static DamageEffectV2 create(float amount, ResourceKey<DamageType> damageType) {
        Preconditions.checkArgument(amount >= 0f, "Damage amount must be at least 0");
        Objects.requireNonNull(damageType, "Damage type may not be null");

        return new DamageEffectV2(amount, damageType);
    }

    @Override
    public boolean apply(LivingEntity victim, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return victim.hurtServer(serverLevel, serverLevel.damageSources().source(damageType), amount);
        }

        return false;
    }

    @Override
    public MapCodec<DamageEffectV2> codec() {
        return CODEC;
    }

    public float amount() {
        return amount;
    }

    public ResourceKey<DamageType> damageType() {
        return damageType;
    }
}
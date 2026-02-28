package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Objects;

/**
 * Applies damage to {@link LivingEntity}s.
 * <p>
 * The type and amount of damage can be configured, as well as the damage type. However, the {@link DamageSource}
 * applied only stores the type - the direct source entity, attacker, and position are all {@code null}.
 */
public final class DamageEffect implements TemperatureEffectV2 {
    public static final MapCodec<DamageEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT
                            .fieldOf("amount")
                            .forGetter(DamageEffect::amount),
                    ResourceKey.codec(Registries.DAMAGE_TYPE)
                            .fieldOf("damage_type")
                            .forGetter(DamageEffect::damageType)
            ).apply(instance, DamageEffect::new)
    );

    private final float amount;
    private final ResourceKey<DamageType> damageType;

    private DamageEffect(float amount, ResourceKey<DamageType> damageType) {
        this.amount = amount;
        this.damageType = damageType;
    }

    public static DamageEffect create(float amount, ResourceKey<DamageType> damageType) {
        Preconditions.checkArgument(amount >= 0f, "Damage amount must be at least 0");
        Objects.requireNonNull(damageType, "Damage type may not be null");

        return new DamageEffect(amount, damageType);
    }

    @Override
    public boolean apply(LivingEntity victim, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return victim.hurtServer(serverLevel, serverLevel.damageSources().source(damageType), amount);
        }

        return false;
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
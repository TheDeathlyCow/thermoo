package com.github.thedeathlycow.thermoo.api.temperature.status.v2.effect;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectContext;
import com.github.thedeathlycow.thermoo.mixin.common.accessor.DamageSourcesAccessor;
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
import org.jetbrains.annotations.Nullable;

/**
 * Applies damage to {@link LivingEntity}s.
 * <p>
 * The type and amount of damage can be configured, as well as the damage type. However, the {@link DamageSource}
 * applied only stores the type - the direct source entity, attacker, and position are all {@code null}.
 */
public final class DamageEffect implements TemperatureEffect {
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
    @Nullable
    private DamageSource damageSource = null;

    private DamageEffect(float amount, ResourceKey<DamageType> damageType) {
        this.amount = amount;
        this.damageType = damageType;
    }

    /**
     * Creates a new damage effect for data generation.
     *
     * @param amount     A non-negative finite float.
     * @param damageType The damage type key.
     * @throws IllegalArgumentException if the {@code amount} is negative, infinite, or NaN.
     * @throws NullPointerException     if the {@code damageType} is {@code null}.
     */
    public static DamageEffect create(float amount, ResourceKey<DamageType> damageType) {
        Preconditions.checkArgument(amount >= 0f, "Damage amount must be at least 0");
        Preconditions.checkArgument(Float.isFinite(amount), "Damage amount must be finite");
        Preconditions.checkNotNull(damageType, "Damage type may not be null");

        return new DamageEffect(amount, damageType);
    }

    /**
     * Hurts the target.
     *
     * @param target  The entity receiving the effect.
     * @param context Additional context for the effect.
     * @return Returns {@code true} if executed on the logical server AND the target was successfully hurt.
     */
    @Override
    public boolean apply(LivingEntity target, TemperatureEffectContext context) {
        if (target.level() instanceof ServerLevel serverLevel) {
            return target.hurtServer(serverLevel, this.damageSource(serverLevel, damageType), amount);
        }

        return false;
    }

    private DamageSource damageSource(ServerLevel serverLevel, ResourceKey<DamageType> damageType) {
        if (this.damageSource == null) {
            this.damageSource = ((DamageSourcesAccessor) serverLevel.damageSources()).thermoo_invokeSource(damageType);
        }

        return this.damageSource;
    }

    /**
     * @return Returns {@link #CODEC}
     */
    @Override
    public MapCodec<DamageEffect> codec() {
        return CODEC;
    }

    /**
     * The amount of damage inflicted by the effect.
     *
     * @return Returns a float that is finite and non-negative.
     */
    public float amount() {
        return amount;
    }

    /**
     * The damage type of the effect.
     */
    public ResourceKey<DamageType> damageType() {
        return damageType;
    }
}
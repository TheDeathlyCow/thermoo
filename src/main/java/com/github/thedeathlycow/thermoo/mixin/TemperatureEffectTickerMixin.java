package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.temperature.effects.TemperatureEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class TemperatureEffectTickerMixin extends Entity {

    public TemperatureEffectTickerMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;addPowderSnowSlowIfNeeded()V",
                    shift = At.Shift.AFTER
            )
    )
    private void tickFreezingEffects(CallbackInfo ci) {
        // ticks effects for all living entities

        World world = this.getWorld();
        if (world.isClient) {
            return;
        }

        var profiler = world.getProfiler();
        profiler.push("thermoo.temperature_effects");

        final LivingEntity instance = (LivingEntity) (Object) this;

        for (var effect : TemperatureEffects.getEffectsForEntity(instance)) {
            effect.applyIfPossible(instance);
        }

        for (var effect : TemperatureEffects.getLoadedConfiguredEffects()) {
            effect.applyIfPossible(instance);
        }

        profiler.pop();
    }

}

package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.attribute.AttributeData;
import com.github.thedeathlycow.thermoo.impl.attribute.AttributeHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeMixin {

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void addDefaultBoundsModifiers(EntityType<? extends LivingEntity> type, Level world, CallbackInfo ci) {
        LivingEntity instance = (LivingEntity) (Object) this;

        for (var attribute : AttributeData.values()) {
            double value = attribute.baseValueEvent().invoker().getBaseValue(instance);
            if (value != 0) {
                AttributeHelper.applyValueAsModifier(instance, attribute, value);
            }
        }
    }
}

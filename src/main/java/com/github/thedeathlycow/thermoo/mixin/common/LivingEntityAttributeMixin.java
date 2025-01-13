package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.impl.AttributeHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
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
    private void addDefaultBoundsModifiers(EntityType<? extends LivingEntity> type, World world, CallbackInfo ci) {
        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();
        LivingEntity instance = (LivingEntity) (Object) this;

        for (var attribute : AttributeHelper.THERMOO_ATTRIBUTES) {
            double value = controller.getBaseValueForAttribute(attribute.attribute(), instance);
            if (value != 0) {
                AttributeHelper.applyValueAsModifier(instance, attribute, value);
            }
        }
    }
}

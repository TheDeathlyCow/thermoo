package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeMixin {

    @Shadow
    public abstract @Nullable EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void addDefaultBoundsModifiers(EntityType<? extends LivingEntity> type, World world, CallbackInfo ci) {

        var attributes = new EntityAttribute[]{
                ThermooAttributes.MIN_TEMPERATURE,
                ThermooAttributes.MAX_TEMPERATURE,
                ThermooAttributes.HEAT_RESISTANCE,
                ThermooAttributes.FROST_RESISTANCE
        };
        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();
        LivingEntity instance = (LivingEntity) (Object) this;

        for (var attribute : attributes) {
            double value = controller.getBaseValueForAttribute(attribute, instance);
            if (value != 0) {
                applyValueAsModifier(type, attribute, value);
            }
        }
    }

    private void applyValueAsModifier(EntityType<? extends LivingEntity> type, EntityAttribute attribute, double value) {
        var modifier = new EntityAttributeModifier(
                "",
                value,
                EntityAttributeModifier.Operation.ADDITION
        );

        EntityAttributeInstance attributeInstance = this.getAttributeInstance(attribute);

        if (attributeInstance == null) {
            throw new IllegalStateException("Attribute not found on " + type + ": " + attribute);
        }

        attributeInstance.addTemporaryModifier(modifier);
    }

}

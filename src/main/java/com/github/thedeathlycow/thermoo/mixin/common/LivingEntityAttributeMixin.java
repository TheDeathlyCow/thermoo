package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.impl.AttributeHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeMixin {

    @Shadow
    public abstract @Nullable EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

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
                this.thermoo$applyValueAsModifier(type, attribute, value);
            }
        }
    }

    @Unique
    private void thermoo$applyValueAsModifier(
            EntityType<? extends LivingEntity> type,
            AttributeHelper.IdAttributePair attribute,
            double value
    ) {
        var modifier = new EntityAttributeModifier(
                attribute.id(),
                value,
                EntityAttributeModifier.Operation.ADD_VALUE
        );

        EntityAttributeInstance attributeInstance = this.getAttributeInstance(attribute.attribute());

        if (attributeInstance == null) {
            throw new IllegalStateException("Attribute not found on " + type + ": " + attribute);
        }

        attributeInstance.addTemporaryModifier(modifier);
    }

}

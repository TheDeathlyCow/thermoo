package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.ThermalResistanceType;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {

    @Inject(
            method = "method_56689",
            at = @At(
                    value = "TAIL",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void buildAttributeValues(
            RegistryEntry<ArmorMaterial> armorMaterial,
            ArmorItem.Type type,
            CallbackInfoReturnable<AttributeModifiersComponent> cir,
            int protection, float toughness,
            AttributeModifiersComponent.Builder builder
    ) {
        for (ThermalResistanceType resistanceType : ThermalResistanceType.values()) {
            resistanceType.buildResistance(armorMaterial, type, builder);
        }
    }

}

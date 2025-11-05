package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.impl.ThermalResistanceType;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Debug;
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
                    shift = At.Shift.BEFORE // required to shift to before the builder is actually built
            )
    )
    private static void buildAttributeValues(
            Holder<ArmorMaterial> armorMaterial,
            ArmorItem.Type type,
            CallbackInfoReturnable<ItemAttributeModifiers> cir,
            @Local ItemAttributeModifiers.Builder builder
    ) {
        for (ThermalResistanceType resistanceType : ThermalResistanceType.values()) {
            resistanceType.buildResistance(armorMaterial, type, builder);
        }
    }

}

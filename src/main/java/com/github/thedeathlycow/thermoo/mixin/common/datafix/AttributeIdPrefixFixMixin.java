package com.github.thedeathlycow.thermoo.mixin.common.datafix;

import com.github.thedeathlycow.thermoo.impl.attribute.AttributeHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.datafixer.fix.AttributeIdPrefixFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttributeIdPrefixFix.class)
public class AttributeIdPrefixFixMixin {
    @ModifyReturnValue(
            method = "removePrefix",
            at = @At("TAIL")
    )
    private static String removePrefixForThermoo(String original) {
        return AttributeHelper.fixPrefixedAttributeIds(original);
    }
}
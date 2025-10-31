package com.github.thedeathlycow.thermoo.mixin.common.datafix;

import com.github.thedeathlycow.thermoo.impl.attribute.AttributeHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.datafix.fixes.AttributeIdPrefixFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttributeIdPrefixFix.class)
public class AttributeIdPrefixFixMixin {
    @ModifyReturnValue(
            method = "replaceId",
            at = @At("TAIL")
    )
    private static String removePrefixForThermoo(String original) {
        return AttributeHelper.fixPrefixedAttributeIds(original);
    }
}
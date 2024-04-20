package com.github.thedeathlycow.thermoo.mixin.common.accesor;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityAttributeModifier.class)
public interface EntityAttributeModiferAccessor {

    @Accessor("name")
    String thermoo$name();

}

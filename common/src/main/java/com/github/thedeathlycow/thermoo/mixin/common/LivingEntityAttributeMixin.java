/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

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
    private void addDefaultBoundsModifiers(EntityType<? extends LivingEntity> entityType, Level level, CallbackInfo ci) {
        LivingEntity instance = (LivingEntity) (Object) this;

        for (var attribute : AttributeData.values()) {
            double value = attribute.baseValueEvent().invoker().getBaseValue(instance);
            if (value != 0) {
                AttributeHelper.applyValueAsModifier(instance, attribute, value);
            }
        }
    }
}

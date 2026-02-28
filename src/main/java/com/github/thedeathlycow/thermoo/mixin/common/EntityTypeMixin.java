package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureEffectCache;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(EntityType.class)
public class EntityTypeMixin implements TemperatureEffectCache {
    @Unique
    @Nullable
    private List<Holder.Reference<TemperatureStatus>> thermoo$effects = null;

    @Override
    @Unique
    public void thermoo$setEffects(List<Holder.Reference<TemperatureStatus>> effects) {
        this.thermoo$effects = effects;
    }

    @Override
    @Unique
    public List<Holder.Reference<TemperatureStatus>> thermoo$getEffects() {
        return this.thermoo$effects;
    }
}
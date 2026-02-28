package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffectV2;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusSelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TemperatureStatusImpl(
        @NotNull TemperatureStatusSelector selector,
        int interval,
        @NotNull List<TemperatureEffectV2> effects
) implements TemperatureStatus {
    @Override
    public boolean apply(LivingEntity entity, Level level) {
        if (entity.tickCount % this.interval == 0) {
            return false;
        }

        float scale = entity.thermoo$getTemperatureScale();

        if (!this.selector.temperatureScaleRange().matches(scale)) {
            return false;
        }

        boolean anyApplied = false;

        for (TemperatureEffectV2 effect : this.effects) {
            anyApplied |= effect.apply(entity, level);
        }

        return anyApplied;
    }

    @Override
    public void remove(LivingEntity entity, Level level) {
        for (TemperatureEffectV2 effect : this.effects) {
            effect.remove(entity, level);
        }
    }
}
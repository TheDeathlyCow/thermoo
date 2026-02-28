package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusSelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record TemperatureStatusImpl(
        @NotNull TemperatureStatusSelector selector,
        int interval,
        @NotNull List<TemperatureEffect> effects
) implements TemperatureStatus {
    public boolean apply(LivingEntity entity, Level level) {
        if (entity.tickCount % this.interval == 0) {
            return false;
        }

        float scale = entity.thermoo$getTemperatureScale();

        if (!this.selector.temperatureScaleRange().matches(scale)) {
            return false;
        }

        boolean anyApplied = false;

        for (TemperatureEffect effect : this.effects) {
            anyApplied |= effect.apply(entity, level);
        }

        return anyApplied;
    }

    public void remove(LivingEntity entity, Level level) {
        for (TemperatureEffect effect : this.effects) {
            effect.remove(entity, level);
        }
    }
}
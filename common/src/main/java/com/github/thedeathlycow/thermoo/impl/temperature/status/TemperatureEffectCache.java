package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import net.minecraft.core.Holder;

import java.util.List;

public interface TemperatureEffectCache {
    void thermoo$setStatuses(List<Holder.Reference<TemperatureStatus>> effects);

    List<Holder.Reference<TemperatureStatus>> thermoo$getStatuses();
}
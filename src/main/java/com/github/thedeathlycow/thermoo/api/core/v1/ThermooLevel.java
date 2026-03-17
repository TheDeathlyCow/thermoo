package com.github.thedeathlycow.thermoo.api.core.v1;

import com.github.thedeathlycow.thermoo.api.core.v1.source.BuiltinTemperatureSources;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

/**
 * Thermoo extensions of {@link net.minecraft.world.level.Level}.
 * <p>
 * Note: This interface is automatically implemented on all levels via Mixin and interface injection.
 */
@ApiStatus.NonExtendable
public interface ThermooLevel {
    /**
     * A manager of shared instances of {@link TemperatureChange}. This is analogous to {@link Level#damageSources()}
     */
    default BuiltinTemperatureSources thermoo$temperatureSources() {
        throw new NotImplementedException();
    }
}
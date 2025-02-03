package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface EnvironmentLookup {
    static EnvironmentLookup getInstance() {
        return EnvironmentLookupImpl.INSTANCE;
    }

    double findTemperature(World world, BlockPos pos, TemperatureUnit unit);

    double findRelativeHumidity(World world, BlockPos pos);

    static double fallbackTemperature(TemperatureUnit unit) {
        return unit.fromCelsius(20.0);
    }

    static double fallbackRelativeHumidity() {
        return 0.5;
    }
}
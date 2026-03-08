package com.github.thedeathlycow.thermoo.api.core.v1;

import com.github.thedeathlycow.thermoo.api.core.v1.source.BuiltinTemperatureSources;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ThermooLevel {
    default BuiltinTemperatureSources thermoo$temperatureSources() {
        throw new NotImplementedException();
    }
}
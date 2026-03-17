package com.github.thedeathlycow.thermoo.api.kt

import com.github.thedeathlycow.thermoo.api.core.v1.ThermooLevel
import com.github.thedeathlycow.thermoo.api.core.v1.source.BuiltinTemperatureSources

val ThermooLevel.temperatureSources: BuiltinTemperatureSources
    get() = this.`thermoo$temperatureSources`()
package com.github.thedeathlycow.thermoo.api.kt

import com.github.thedeathlycow.thermoo.api.core.v2.ThermooLevel
import com.github.thedeathlycow.thermoo.api.core.v2.source.BuiltinTemperatureSources

val ThermooLevel.temperatureSources: BuiltinTemperatureSources
    get() = this.`thermoo$temperatureSources`()
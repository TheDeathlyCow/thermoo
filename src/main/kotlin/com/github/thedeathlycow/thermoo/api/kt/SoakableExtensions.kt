package com.github.thedeathlycow.thermoo.api.kt

import com.github.thedeathlycow.thermoo.api.temperature.Soakable

var Soakable.wetTicks: Int
    get() = this.`thermoo$getWetTicks`()
    set(value) = this.`thermoo$setWetTicks`(value)

val Soakable.maxWetTicks: Int
    get() = this.`thermoo$getMaxWetTicks`()

fun Soakable.ignoresFrigidWater(): Boolean = this.`thermoo$ignoresFrigidWater`()

val Soakable.isWet: Boolean
    get() = this.`thermoo$isWet`()

fun Soakable.addWetTicks(delta: Int) {
    this.`thermoo$addWetTicks`(delta)
}

val Soakable.isSoaked: Boolean
    get() = this.`thermoo$isSoaked`()

val Soakable.soakedScale: Float
    get() = this.`thermoo$getSoakedScale`()

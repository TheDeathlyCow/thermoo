package com.github.thedeathlycow.thermoo.api.kt

import com.github.thedeathlycow.thermoo.api.core.v1.HeatingMode
import com.github.thedeathlycow.thermoo.api.core.v1.HeatingModes
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

/**
 * The current temperature of this temperature aware object
 */
var TemperatureAware.temperature: Int
    get() = this.`thermoo$getTemperature`()
    set(value) = this.`thermoo$setTemperature`(value)

/**
 * The minimum allowed temperature of the temperature aware object
 */
val TemperatureAware.minTemperature: Int
    get() = this.`thermoo$getMinTemperature`()

/**
 * The maximum allowed temperature of the temperature aware object
 */
val TemperatureAware.maxTemperature: Int
    get() = this.`thermoo$getMaxTemperature`()


val TemperatureAware.coldResistance: Double
    get() = this.`thermoo$getColdResistance`()

val TemperatureAware.heatResistance: Double
    get() = this.`thermoo$getHeatResistance`()

val TemperatureAware.environmentColdResistance: Double
    get() = this.`thermoo$getEnvironmentColdResistance`()

val TemperatureAware.environmentHeatResistance: Double
    get() = this.`thermoo$getEnvironmentHeatResistance`()

/**
 * Checks if an entity can overheat
 */
fun TemperatureAware.canBeWarm(): Boolean = this.`thermoo$canOverheat`()

/**
 * Checks if an entity can freeze
 */
fun TemperatureAware.canBeCold(): Boolean = this.`thermoo$canFreeze`()

val TemperatureAware.isCold: Boolean
    get() = this.`thermoo$isCold`()

val TemperatureAware.isWarm: Boolean
    get() = this.`thermoo$isWarm`()

fun TemperatureAware.addTemperature(temperatureChange: Int, context: TemperatureChange) {
    this.`thermoo$addTemperature`(temperatureChange, context)
}

fun TemperatureAware.addTemperature(temperatureChange: Int) {
    this.`thermoo$addTemperature`(temperatureChange)
}

val TemperatureAware.temperatureScale: Float
    get() = this.`thermoo$getTemperatureScale`()

val TemperatureAware.random: RandomSource
    get() = this.`thermoo$getRandom`()

val LivingEntity.temperatureAware: TemperatureAware
    get() = TemperatureAware.get(this)

val Entity.temperatureAware: TemperatureAware?
    get() = TemperatureAware.getNullable(this)
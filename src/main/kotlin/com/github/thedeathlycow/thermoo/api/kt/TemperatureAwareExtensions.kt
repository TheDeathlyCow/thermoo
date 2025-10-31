package com.github.thedeathlycow.thermoo.api.kt

import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware
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

@Deprecated(message = "Clashes with entity method of same name", replaceWith = ReplaceWith("this.canBeCold()"))
fun TemperatureAware.canFreeze(): Boolean = this.canBeCold()

@Deprecated(message = "Does not follow name convention of canBeCold", replaceWith = ReplaceWith("this.canBeWarm()"))
fun TemperatureAware.canOverheat(): Boolean = this.canBeWarm()

val TemperatureAware.isCold: Boolean
    get() = this.`thermoo$isCold`()

val TemperatureAware.isWarm: Boolean
    get() = this.`thermoo$isWarm`()

fun TemperatureAware.addTemperature(temperatureChange: Int, mode: HeatingMode = HeatingModes.ABSOLUTE) {
    this.`thermoo$addTemperature`(temperatureChange, mode)
}

val TemperatureAware.temperatureScale: Float
    get() = this.`thermoo$getTemperatureScale`()

val TemperatureAware.random: RandomSource
    get() = this.`thermoo$getRandom`()

val LivingEntity.temperatureAware: TemperatureAware
    get() = TemperatureAware.get(this)

val Entity.temperatureAware: TemperatureAware?
    get() = TemperatureAware.getNullable(this)
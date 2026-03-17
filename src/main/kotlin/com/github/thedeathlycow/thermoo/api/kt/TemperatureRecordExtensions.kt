package com.github.thedeathlycow.thermoo.api.kt

import com.github.thedeathlycow.thermoo.api.util.v1.TemperatureRecord
import com.github.thedeathlycow.thermoo.api.util.v1.TemperatureUnit

/**
 * Gets this number's double value as a Celsius temperature record
 */
val Number.C: TemperatureRecord
    get() = TemperatureRecord(this.toDouble(), TemperatureUnit.CELSIUS)

/**
 * Gets this number's double value as a Kelvin temperature record
 */
val Number.K: TemperatureRecord
    get() = TemperatureRecord(this.toDouble(), TemperatureUnit.KELVIN)

/**
 * Gets this number's double value as a Fahrenheit temperature record
 */
val Number.F: TemperatureRecord
    get() = TemperatureRecord(this.toDouble(), TemperatureUnit.FAHRENHEIT)

/**
 * Gets this number's double value as a Rankine temperature record
 */
val Number.R: TemperatureRecord
    get() = TemperatureRecord(this.toDouble(), TemperatureUnit.RANKINE)

val TemperatureRecord.value: Double
    get() = this.value()

val TemperatureRecord.unit: TemperatureUnit
    get() = this.unit()

/**
 * Adds a temperature difference to this record
 *
 * @see TemperatureRecord.add
 */
operator fun TemperatureRecord.plus(other: TemperatureRecord): TemperatureRecord = this.add(other)

/**
 * Subtracts a temperature difference from this record
 *
 * @see TemperatureRecord.add
 */
operator fun TemperatureRecord.minus(other: TemperatureRecord): TemperatureRecord =
    this.add(TemperatureRecord(-other.value, other.unit))
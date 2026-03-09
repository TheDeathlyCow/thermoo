package com.github.thedeathlycow.thermoo.api.core.v1;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;


/**
 * TemperatureAware entities are things that are sensitive to temperature. In Thermoo, the Temperature of a Thermally-aware
 * object is an arbitrary unit that represents the total heat (or cold) they have collected. It is not based on standard
 * units of temperature like Celsius, Kelvin, or Fahrenheit. Positive values of temperature are treated as 'warm' and
 * negative values are treated as 'cold'.
 * <p>
 * This class is interface injected into {@link net.minecraft.world.entity.LivingEntity}. Therefore, ALL methods must have a
 * default implementation. Therefore, all methods that would normally be declared abstract are instead made to throw a
 * {@link NotImplementedException}.
 */
@ApiStatus.NonExtendable
public interface TemperatureAware {


    /**
     * @return Returns the current temperature of the temperature aware object
     */
    default int thermoo$getTemperature() {
        throw new NotImplementedException();
    }

    /**
     * @param temperature Sets the current temperature of the temperature aware object to an exact value
     */
    default void thermoo$setTemperature(int temperature) {
        throw new NotImplementedException();
    }

    /**
     * @return Returns the minimum allowed temperature of the temperature aware object.
     */
    default int thermoo$getMinTemperature() {
        throw new NotImplementedException();
    }

    /**
     * @return Returns the maximum allowed temperature of the temperature aware object
     */
    default int thermoo$getMaxTemperature() {
        throw new NotImplementedException();
    }

    /**
     * Supplies the cold resistance of the temperature aware object. Cold Resistance is a percentage on a scale from
     * 0 to 10, where 0 cold resistance corresponds to 0%, and 10 cold resistance corresponds to 100%.
     *
     * @return Returns the cold resistance of the temperature aware object
     */
    default double thermoo$getColdResistance() {
        throw new NotImplementedException();
    }

    /**
     * Supplies the heat resistance of the temperature aware object. Heat Resistance is a percentage on a scale from
     * 0 to 10, where 0 heat resistance corresponds to 0%, and 10 heat resistance corresponds to 100%.
     *
     * @return Returns the heat resistance of the temperature aware object
     */
    default double thermoo$getHeatResistance() {
        throw new NotImplementedException();
    }

    /**
     * Supplies the environmental cold resistance of a temperature aware object. Environmental cold resistance is a chance
     * to dodge a strictly negative {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents environment temperature change}.
     *
     * @return Returns a double in the range [0, 1] that is the chance that a negative environment temperature change
     * will be dodged
     */
    default double thermoo$getEnvironmentColdResistance() {
        return 0.0;
    }

    /**
     * Supplies the environmental heat resistance of a temperature aware object. Environmental heat resistance is a chance
     * to dodge a strictly positive {@link com.github.thedeathlycow.thermoo.api.environment.event.ServerPlayerEnvironmentTickEvents environment temperature change}.
     *
     * @return Returns a double in the range [0, 1] that is the chance that a positive environment temperature change
     * will be dodged
     */
    default double thermoo$getEnvironmentHeatResistance() {
        return 0.0;
    }

    /**
     * @return Returns if the thermally aware object can be affected by cold
     */
    default boolean thermoo$canFreeze() {
        throw new NotImplementedException();
    }

    /**
     * @return Returns if the thermally aware object can be affected by heat
     */
    default boolean thermoo$canOverheat() {
        throw new NotImplementedException();
    }

    default boolean thermoo$isCold() {
        return this.thermoo$getTemperature() <= 0;
    }

    default boolean thermoo$isWarm() {
        return this.thermoo$getTemperature() >= 0;
    }

    /**
     * Adds or removes some amount of temperature to the thermally aware object.
     * <p>
     * Additional context can be supplied by the {@link TemperatureChange context}. Some builtin context instances can
     * be obtained from {@link ThermooLevel#thermoo$temperatureSources()}.
     *
     * @param temperatureChange The amount of temperature to add/remove. Positive change adds, negative change removes.
     * @param context           The context of the change.
     */
    default void thermoo$addTemperature(int temperatureChange, TemperatureChange context) {
        throw new NotImplementedException();
    }

    /**
     * Adds or removes some amount of temperature to the thermally aware object. Uses the
     * {@linkplain com.github.thedeathlycow.thermoo.api.core.v1.source.TemperatureSources#ABSOLUTE absolute temperature source}.
     *
     * @param temperatureChange The amount of temperature to add/remove. Positive change adds, negative change removes.
     */
    default void thermoo$addTemperature(int temperatureChange) {
        throw new NotImplementedException();
    }

    /**
     * @return Returns the current temperature as a -1 to +1 percentage scale of the minimum/maximum temperature.
     */
    default float thermoo$getTemperatureScale() {
        int temperature = this.thermoo$getTemperature();

        if (temperature == 0) {
            return 0.0f;
        }

        int bound = 0;
        if (temperature < 0) {
            bound = -this.thermoo$getMinTemperature();
        } else {
            bound = this.thermoo$getMaxTemperature();
        }

        if (bound == 0) {
            return 0.0f;
        }

        return ((float) temperature) / bound;
    }

    /**
     * @return Returns a random number generator object associated with this temperature aware
     */
    default RandomSource thermoo$getRandom() {
        return RandomSource.create();
    }

    /**
     * Returns the temperature aware component of an {@link Entity}, or {@code null} if the entity does not have such
     * a component.
     * <p>
     * Primarily intended for platforms where interface injection does not work as cleanly.
     *
     * @param entity The entity
     * @return Returns an upcast reference to the same entity given, or {@code null} if the entity has no such component.
     */
    @Nullable
    static TemperatureAware getNullable(Entity entity) {
        if (entity instanceof TemperatureAware temperatureAware) {
            return temperatureAware;
        }
        return null;
    }

    /**
     * Returns the temperature aware component of a {@link LivingEntity}.
     * <p>
     * Primarily intended for platforms where interface injection does not work as cleanly.
     *
     * @param livingEntity The temperature-aware living entity
     * @return Returns an upcast reference to the same entity given
     */
    static TemperatureAware get(LivingEntity livingEntity) {
        return livingEntity;
    }
}

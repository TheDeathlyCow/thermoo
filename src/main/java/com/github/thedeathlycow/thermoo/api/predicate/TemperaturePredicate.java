package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Predicate for testing the temperature of a {@link TemperatureAware}
 * <p>
 * This is injected into {@link net.minecraft.predicate.entity.EntityPredicate} via mixin.
 * This mean that this is NOT a new loot condition, it is actually an extension of the vanilla predicate
 * {@link net.minecraft.predicate.entity.EntityPredicate} and can therefore be used anywhere that it is used. For exameple,
 * as part of the vanilla loot condition {@code minecraft:entity_properties}
 * <p>
 * Example usage:
 * <pre>
 * {
 * 	    "condition": "minecraft:entity_properties",
 * 	    "entity": "this",
 * 	    "predicate": {
 * 		    "thermoo.temperature": {
 * 			    "scale": {@link NumberRange.DoubleRange},
 * 			    "temperature": {@link NumberRange.IntRange}
 *          }
 *      }
 * }
 * </pre>
 * <p>
 * Note that both {@code scale} and {@code temperature} are optional, and if not supplied will default to
 * {@link NumberRange.DoubleRange#ANY} and {@link NumberRange.IntRange#ANY} respectively.
 *
 * @see net.minecraft.predicate.entity.EntityPredicate
 */
public class TemperaturePredicate {

    /**
     * "Any" predicate that will always return as true when tested
     */
    public static final TemperaturePredicate ANY = new TemperaturePredicate(
            NumberRange.DoubleRange.ANY,
            NumberRange.IntRange.ANY
    );

    /**
     * The temperature scale range accepted by this predicate
     */
    private final NumberRange.DoubleRange scaleRange;
    /**
     * The absolute temperature value range accepted by this predicate
     */
    private final NumberRange.IntRange temperatureRange;

    /**
     * Constructs a new temperature predicate with a scale and temperature range
     *
     * @param scaleRange       The temperature scale range accepted by this predicate
     * @param temperatureRange The absolute temperature value range accepted by this predicate
     */
    public TemperaturePredicate(
            NumberRange.DoubleRange scaleRange,
            NumberRange.IntRange temperatureRange
    ) {
        this.scaleRange = scaleRange;
        this.temperatureRange = temperatureRange;
    }

    /**
     * Tests the temperature of a {@link TemperatureAware} against this predicate.
     * <p>
     * If this predicate is {@link #ANY} then this test will always be {@code true}.
     * <p>
     * Otherwise, BOTH the temperature scale and temperature value of the {@code tempAware} must match
     * the ranges of this predicate
     *
     * @param tempAware The {@link TemperatureAware} to test
     * @return Returns true if the predicate accepts {@code tempAware}
     */
    public boolean test(TemperatureAware tempAware) {
        if (this == ANY) {
            return true;
        }

        float scale = tempAware.thermoo$getTemperatureScale();
        int temp = tempAware.thermoo$getTemperature();

        return this.scaleRange.test(scale) && this.temperatureRange.test(temp);
    }

    /**
     * Serializes this predicate to JSON
     * <p>
     * If this instance is {@link #ANY}, then returns {@link JsonNull}
     *
     * @return Returns a {@link JsonElement} that represents this instance
     */
    public JsonElement toJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("scale", this.scaleRange.toJson());
        jsonObject.add("temperature", this.temperatureRange.toJson());
        return jsonObject;
    }

    /**
     * Returns the {@link TemperaturePredicate} that is represented by the given {@code json}.
     * <p>
     * If the given {@code json} is null or {@link JsonNull}, then returns {@link #ANY}
     *
     * @param json The json object to deserialize
     * @return Returns the temperature predicate represented by the given {@code json}.
     */
    @Contract("!null->new")
    public static TemperaturePredicate fromJson(@Nullable JsonElement json) {
        if (json == null || json.isJsonNull()) {
            return ANY;
        }

        JsonObject jsonObject = JsonHelper.asObject(json, "thermoo.temperature");

        var scaleRange = NumberRange.DoubleRange.ANY;
        var tempRange = NumberRange.IntRange.ANY;

        if (jsonObject.has("scale")) {
            scaleRange = NumberRange.DoubleRange.fromJson(jsonObject.get("scale"));
        }

        if (jsonObject.has("temperature")) {
            tempRange = NumberRange.IntRange.fromJson(jsonObject.get("temperature"));
        }

        return new TemperaturePredicate(scaleRange, tempRange);
    }

    /**
     * Builder class for {@link TemperaturePredicate}
     *
     * @see TemperaturePredicate
     */
    public static class Builder {
        private NumberRange.DoubleRange scale = NumberRange.DoubleRange.ANY;
        private NumberRange.IntRange temperature = NumberRange.IntRange.ANY;

        public static TemperaturePredicate.Builder create() {
            return new TemperaturePredicate.Builder();
        }

        public TemperaturePredicate.Builder scale(NumberRange.DoubleRange scale) {
            this.scale = scale;
            return this;
        }

        public TemperaturePredicate.Builder temperature(NumberRange.IntRange temperature) {
            this.temperature = temperature;
            return this;
        }

        public TemperaturePredicate build() {
            return new TemperaturePredicate(this.scale, this.temperature);
        }
    }
}

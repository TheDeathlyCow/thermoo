package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class TemperaturePredicate {

    public static final TemperaturePredicate ANY = new TemperaturePredicate(
            NumberRange.FloatRange.ANY,
            NumberRange.IntRange.ANY
    );

    private final NumberRange.FloatRange scaleRange;
    private final NumberRange.IntRange temperatureRange;

    public TemperaturePredicate(
            NumberRange.FloatRange scaleRange,
            NumberRange.IntRange temperatureRange
    ) {
        this.scaleRange = scaleRange;
        this.temperatureRange = temperatureRange;
    }

    public boolean test(TemperatureAware tempAware) {
        if (this == ANY) {
            return true;
        }

        float scale = tempAware.thermoo$getTemperatureScale();
        int temp = tempAware.thermoo$getTemperature();

        return this.scaleRange.test(scale) && this.temperatureRange.test(temp);
    }

    public JsonElement toJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("scale", this.scaleRange.toJson());
        jsonObject.add("temperature", this.temperatureRange.toJson());
        return jsonObject;
    }

    @Contract("!null->new")
    public static TemperaturePredicate fromJson(@Nullable JsonElement json) {
        if (json == null || json.isJsonNull()) {
            return ANY;
        }

        JsonObject jsonObject = JsonHelper.asObject(json, "thermoo.temperature");

        var scaleRange = NumberRange.FloatRange.ANY;
        var tempRange = NumberRange.IntRange.ANY;

        if (jsonObject.has("scale")) {
            scaleRange = NumberRange.FloatRange.fromJson(jsonObject.get("scale"));
        }

        if (jsonObject.has("temperature")) {
            tempRange = NumberRange.IntRange.fromJson(jsonObject.get("temperature"));
        }

        return new TemperaturePredicate(scaleRange, tempRange);
    }

    public static class Builder {
        private NumberRange.FloatRange scale = NumberRange.FloatRange.ANY;
        private NumberRange.IntRange temperature = NumberRange.IntRange.ANY;

        public static TemperaturePredicate.Builder create() {
            return new TemperaturePredicate.Builder();
        }

        public TemperaturePredicate.Builder scale(NumberRange.FloatRange scale) {
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

package com.github.thedeathlycow.thermoo.api.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TemperatureRecordTest {
    @Test
    void decodeNumberIsCelsius() {
        JsonElement json = new JsonPrimitive(20.0);
        DataResult<Pair<TemperatureRecord, JsonElement>> result = TemperatureRecord.CODEC.decode(JsonOps.INSTANCE, json);
        Assertions.assertDoesNotThrow(() -> result.getOrThrow());

        TemperatureRecord decoded = result.getOrThrow().getFirst();
        Assertions.assertEquals(20.0, decoded.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, decoded.unit());
    }

    @Test
    void decodeExplicitCelsiusIsCelsius() {
        JsonElement json = GsonHelper.parse("""
                {
                    "value": 20.0,
                    "unit": "celsius"
                }
                """);
        DataResult<Pair<TemperatureRecord, JsonElement>> result = TemperatureRecord.CODEC.decode(JsonOps.INSTANCE, json);
        Assertions.assertDoesNotThrow(() -> result.getOrThrow());

        TemperatureRecord decoded = result.getOrThrow().getFirst();
        Assertions.assertEquals(20.0, decoded.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, decoded.unit());
    }

    @Test
    void decodeExplicitFahrenheitIsFahrenheit() {
        JsonElement json = GsonHelper.parse("""
                {
                    "value": 60.0,
                    "unit": "fahrenheit"
                }
                """);
        DataResult<Pair<TemperatureRecord, JsonElement>> result = TemperatureRecord.CODEC.decode(JsonOps.INSTANCE, json);
        Assertions.assertDoesNotThrow(() -> result.getOrThrow());

        TemperatureRecord decoded = result.getOrThrow().getFirst();
        Assertions.assertEquals(60.0, decoded.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.FAHRENHEIT, decoded.unit());
    }

    @Test
    void encodeCelsiusIsObject() {
        var temperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        TemperatureRecord.CODEC.encodeStart(JsonOps.INSTANCE, temperature);

        DataResult<JsonElement> result = TemperatureRecord.CODEC.encodeStart(JsonOps.INSTANCE, temperature);
        Assertions.assertDoesNotThrow(() -> result.getOrThrow());

        JsonElement encoded = result.getOrThrow();
        var expected = new JsonObject();
        expected.addProperty("value", 20.0);
        expected.addProperty("unit", TemperatureUnit.CELSIUS.getSerializedName());
        Assertions.assertEquals(expected, encoded);
    }

    @Test
    void encodeFahrenheitIsObject() {
        var temperature = new TemperatureRecord(60, TemperatureUnit.FAHRENHEIT);
        TemperatureRecord.CODEC.encodeStart(JsonOps.INSTANCE, temperature);

        DataResult<JsonElement> result = TemperatureRecord.CODEC.encodeStart(JsonOps.INSTANCE, temperature);
        Assertions.assertDoesNotThrow(() -> result.getOrThrow());

        JsonElement encoded = result.getOrThrow();
        var expected = new JsonObject();
        expected.addProperty("value", 60.0);
        expected.addProperty("unit", TemperatureUnit.FAHRENHEIT.getSerializedName());
        Assertions.assertEquals(expected, encoded);
    }

    @Test
    void sumTwoCelsius() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.CELSIUS);

        var sum = roomTemperature.sum(add);
        Assertions.assertEquals(30.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }

    @Test
    void sumCelsiusAndKelvin() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var sum = roomTemperature.sum(add);
        Assertions.assertEquals(-243.15, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }

    @Test
    void sumCelsiusAndFahrenheit() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(32, TemperatureUnit.FAHRENHEIT);

        var sum = roomTemperature.sum(add);
        Assertions.assertEquals(20, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }

    @Test
    void addTwoCelsius() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var sum = roomTemperature.add(add);
        Assertions.assertEquals(30.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }

    @Test
    void addKelvinToCelsius() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.KELVIN);

        var sum = roomTemperature.add(add);
        Assertions.assertEquals(30.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }

    @Test
    void addTwoFahrenheit() {
        var roomTemperature = new TemperatureRecord(70, TemperatureUnit.FAHRENHEIT);
        var add = new TemperatureRecord(30, TemperatureUnit.FAHRENHEIT);

        var sum = roomTemperature.add(add);
        Assertions.assertEquals(100.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.FAHRENHEIT, sum.unit());
    }

    @Test
    void addRankineToFahrenheit() {
        var roomTemperature = new TemperatureRecord(70, TemperatureUnit.FAHRENHEIT);
        var add = new TemperatureRecord(30, TemperatureUnit.RANKINE);

        var sum = roomTemperature.add(add);
        Assertions.assertEquals(100.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.FAHRENHEIT, sum.unit());
    }

    @Test
    void addCelsiusToFahrenheit() {
        var roomTemperature = new TemperatureRecord(70, TemperatureUnit.FAHRENHEIT);
        var add = new TemperatureRecord(10, TemperatureUnit.CELSIUS);

        var sum = roomTemperature.add(add);
        Assertions.assertEquals(88.0, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.FAHRENHEIT, sum.unit());
    }

    @Test
    void addFahrenheitToCelsius() {
        var roomTemperature = new TemperatureRecord(20, TemperatureUnit.CELSIUS);
        var add = new TemperatureRecord(10, TemperatureUnit.FAHRENHEIT);

        var sum = roomTemperature.add(add);
        Assertions.assertEquals(25.555, sum.value(), 1e-2);
        Assertions.assertEquals(TemperatureUnit.CELSIUS, sum.unit());
    }
}
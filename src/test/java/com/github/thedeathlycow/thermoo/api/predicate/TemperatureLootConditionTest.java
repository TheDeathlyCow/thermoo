package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.ThermooTest;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.NumberRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class TemperatureLootConditionTest {
    LootContext mockContext;
    VillagerEntity mockVillager;

    static final int MIN_BOUNDARY_VALUE = 50;
    static final int MAX_BOUNDARY_VALUE = 100;

    static final float MIN_BOUNDARY_SCALE = 0.25f;
    static final float MAX_BOUNDARY_SCALE = 0.75f;

    static final NumberRange.IntRange VALUE_RANGE = NumberRange.IntRange.between(MIN_BOUNDARY_VALUE, MAX_BOUNDARY_VALUE);
    static final NumberRange.DoubleRange SCALE_RANGE = NumberRange.DoubleRange.between(MIN_BOUNDARY_SCALE, MAX_BOUNDARY_SCALE);

    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();
    }

    @BeforeEach
    void mockLootContext() {
        mockContext = Mockito.mock(LootContext.class);
        mockVillager = Mockito.mock(VillagerEntity.class);

        Mockito.when(mockContext.get(LootContextParameters.THIS_ENTITY))
                .thenReturn(mockVillager);
    }

    @ParameterizedTest
    @ValueSource(
            ints = {
                    MIN_BOUNDARY_VALUE,
                    (MAX_BOUNDARY_VALUE + MIN_BOUNDARY_VALUE) / 2, // midpoint
                    MAX_BOUNDARY_VALUE
            }
    )
    void temperatureValue_InBoundary_true(int temperature) {
        Mockito.when(mockVillager.thermoo$getTemperature())
                .thenReturn(temperature);

        var condition = new TemperatureLootCondition(VALUE_RANGE, NumberRange.DoubleRange.ANY);

        Assertions.assertTrue(condition.test(mockContext));
    }

    @ParameterizedTest
    @ValueSource(
            ints = {
                    Integer.MIN_VALUE,
                    MIN_BOUNDARY_VALUE - 1,
                    MAX_BOUNDARY_VALUE + 1,
                    Integer.MAX_VALUE
            }
    )
    void temperatureValue_OutsideBoundary_false(int temperature) {
        Mockito.when(mockVillager.thermoo$getTemperature())
                .thenReturn(temperature);

        var condition = new TemperatureLootCondition(VALUE_RANGE, NumberRange.DoubleRange.ANY);

        Assertions.assertFalse(condition.test(mockContext));
    }

    @ParameterizedTest
    @ValueSource(
            floats = {
                    MIN_BOUNDARY_SCALE,
                    (MAX_BOUNDARY_SCALE + MIN_BOUNDARY_SCALE) / 2.0f, // midpoint
                    MAX_BOUNDARY_SCALE
            }
    )
    void temperatureScale_InBoundary_true(float temperatureScale) {
        Mockito.when(mockVillager.thermoo$getTemperatureScale())
                .thenReturn(temperatureScale);

        var condition = new TemperatureLootCondition(NumberRange.IntRange.ANY, SCALE_RANGE);

        Assertions.assertTrue(condition.test(mockContext));
    }

    @ParameterizedTest
    @ValueSource(
            floats = {
                    Float.NEGATIVE_INFINITY,
                    0f,
                    MIN_BOUNDARY_SCALE * 0.99f,
                    MAX_BOUNDARY_SCALE * 1.01f,
                    1f,
                    Float.POSITIVE_INFINITY
            }
    )
    void temperatureScale_OutsideBoundary_false(float temperatureScale) {
        Mockito.when(mockVillager.thermoo$getTemperatureScale())
                .thenReturn(temperatureScale);

        var condition = new TemperatureLootCondition(NumberRange.IntRange.ANY, SCALE_RANGE);

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void temperatureValueInBoundary_temperatureScaleOutsideBoundary_false() {
        final int temperature = MIN_BOUNDARY_VALUE + 1;
        Mockito.when(mockVillager.thermoo$getTemperature())
                .thenReturn(temperature);

        var condition = new TemperatureLootCondition(VALUE_RANGE, SCALE_RANGE);

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void temperatureValueOutsideBoundary_temperatureScaleInBoundary_false() {
        final int temperature = MAX_BOUNDARY_VALUE + 1;
        Mockito.when(mockVillager.thermoo$getTemperature())
                .thenReturn(temperature);

        var condition = new TemperatureLootCondition(VALUE_RANGE, SCALE_RANGE);

        Assertions.assertFalse(condition.test(mockContext));
    }
}

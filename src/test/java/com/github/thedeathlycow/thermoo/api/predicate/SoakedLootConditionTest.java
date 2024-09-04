package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.ThermooTest;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
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

class SoakedLootConditionTest {

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
    void soakedValue_InBoundary_true(int soaked) {
        Mockito.when(mockVillager.thermoo$getWetTicks())
                .thenReturn(soaked);

        var condition = new SoakedLootCondition(VALUE_RANGE, NumberRange.DoubleRange.ANY);

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
    void soakedValue_OutsideBoundary_false(int soaked) {
        Mockito.when(mockVillager.thermoo$getWetTicks())
                .thenReturn(soaked);

        var condition = new SoakedLootCondition(VALUE_RANGE, NumberRange.DoubleRange.ANY);

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
    void soakedScale_InBoundary_true(float soaked) {
        Mockito.when(mockVillager.thermoo$getSoakedScale())
                .thenReturn(soaked);

        var condition = new SoakedLootCondition(NumberRange.IntRange.ANY, SCALE_RANGE);

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
    void soakedScale_OutsideBoundary_false(float soaked) {
        Mockito.when(mockVillager.thermoo$getSoakedScale())
                .thenReturn(soaked);

        var condition = new SoakedLootCondition(NumberRange.IntRange.ANY, SCALE_RANGE);

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void soakedValueInBoundary_soakedScaleOutsideBoundary_false() {
        final int soaked = MIN_BOUNDARY_VALUE + 1;
        Mockito.when(mockVillager.thermoo$getWetTicks())
                .thenReturn(soaked);

        var condition = new SoakedLootCondition(VALUE_RANGE, SCALE_RANGE);

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void soakedValueOutsideBoundary_soakedScaleInBoundary_false() {
        final int soaked = MAX_BOUNDARY_VALUE + 1;
        Mockito.when(mockVillager.thermoo$getWetTicks())
                .thenReturn(soaked);

        var condition = new SoakedLootCondition(VALUE_RANGE, SCALE_RANGE);

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void entityNotSoakable_anyValueOrScale_false() {
        BoatEntity mockBoat = Mockito.mock(BoatEntity.class);
        Mockito.when(mockContext.get(LootContextParameters.THIS_ENTITY))
                .thenReturn(mockBoat);

        var condition = new SoakedLootCondition(NumberRange.IntRange.ANY, NumberRange.DoubleRange.ANY);

        Assertions.assertFalse(condition.test(mockContext));
    }
}

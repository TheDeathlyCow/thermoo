/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.ThermooTest;
import com.github.thedeathlycow.thermoo.api.entity.v1.predicate.SoakedLootCondition;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class SoakedLootConditionTest {

    LootContext mockContext;
    Villager mockVillager;

    static final int MIN_BOUNDARY_VALUE = 50;
    static final int MAX_BOUNDARY_VALUE = 100;

    static final float MIN_BOUNDARY_SCALE = 0.25f;
    static final float MAX_BOUNDARY_SCALE = 0.75f;

    static final MinMaxBounds.Ints VALUE_RANGE = MinMaxBounds.Ints.between(MIN_BOUNDARY_VALUE, MAX_BOUNDARY_VALUE);
    static final MinMaxBounds.Doubles SCALE_RANGE = MinMaxBounds.Doubles.between(MIN_BOUNDARY_SCALE, MAX_BOUNDARY_SCALE);

    @BeforeAll
    static void setup() {
        ThermooTest.bootstrapRegistries();
    }

    @BeforeEach
    void mockLootContext() {
        mockContext = Mockito.mock(LootContext.class);
        mockVillager = Mockito.mock(Villager.class);

        Mockito.when(mockContext.getParameter(LootContextParams.THIS_ENTITY))
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

        var condition = SoakedLootCondition.builder(VALUE_RANGE).build();

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

        var condition = SoakedLootCondition.builder(VALUE_RANGE).build();

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

        var condition = SoakedLootCondition.builder(SCALE_RANGE).build();

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

        var condition = SoakedLootCondition.builder(SCALE_RANGE).build();

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void entityNotSoakable_anyValue_false() {
        Boat mockBoat = Mockito.mock(Boat.class);
        Mockito.when(mockContext.getParameter(LootContextParams.THIS_ENTITY))
                .thenReturn(mockBoat);

        var condition = SoakedLootCondition.builder(MinMaxBounds.Ints.ANY).build();

        Assertions.assertFalse(condition.test(mockContext));
    }

    @Test
    void entityNotSoakable_anyScale_false() {
        Boat mockBoat = Mockito.mock(Boat.class);
        Mockito.when(mockContext.getParameter(LootContextParams.THIS_ENTITY))
                .thenReturn(mockBoat);

        var condition = SoakedLootCondition.builder(MinMaxBounds.Doubles.ANY).build();

        Assertions.assertFalse(condition.test(mockContext));
    }
}

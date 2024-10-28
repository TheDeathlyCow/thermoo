package com.github.thedeathlycow.thermoo.impl.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HeartOverlayTrackerTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
    void firstRowOfHeartsHasSizeTen(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(10, value);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19})
    void secondRowOfHeartsHasSizeTwenty(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(20, value);
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 21, 22, 23, 24, 25, 26, 27, 28, 29})
    void thirdRowOfHeartsHasSizeThirty(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(30, value);
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 31, 32, 33, 34, 35, 36, 37, 38, 39})
    void fourthRowOfHeartsHasSizeForty(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(40, value);
    }
}

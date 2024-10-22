package com.github.thedeathlycow.thermoo.impl.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HeartOverlayTrackerTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void nextSizeIsTen(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(10, value);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    void nextSizeIsTwenty(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(20, value);
    }

    @ParameterizedTest
    @ValueSource(ints = {21, 22, 23, 24, 25, 26, 27, 28, 29, 30})
    void nextSizeIsThirty(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(30, value);
    }

    @ParameterizedTest
    @ValueSource(ints = {31, 32, 33, 34, 35, 36, 37, 38, 39, 40})
    void nextSizeIsForty(int index) {
        int value = HeartOverlayTracker.getNextSize(index);
        Assertions.assertEquals(40, value);
    }
}

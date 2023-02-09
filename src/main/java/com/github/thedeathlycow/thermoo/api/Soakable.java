package com.github.thedeathlycow.thermoo.api;

import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.NotImplementedException;

public interface Soakable {

    default void setWetTicks(int amount) {
        throw new NotImplementedException();
    }

    default int getWetTicks() {
        throw new NotImplementedException();
    }

    default int getMaxWetTicks() {
        return 0;
    }

    default boolean isSoaked() {
        return this.getWetTicks() >= this.getMaxWetTicks();
    }

    default float getWetnessScale() {
        int maxWetness = this.getMaxWetTicks();
        if (maxWetness <= 0) {
            return 0.0f;
        }

        return MathHelper.clamp(
                ((float)this.getWetTicks()) / maxWetness,
                0.0f, 1.0f
        );
    }

    default boolean ignoresFrigidWater() {
        return false;
    }




}

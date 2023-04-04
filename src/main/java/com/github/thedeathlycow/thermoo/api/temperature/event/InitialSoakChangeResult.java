package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;

/**
 * Initial Environment Change result for {@link Soakable}s
 */
public class InitialSoakChangeResult extends InitialEnvironmentChangeResult<Soakable> {

    public InitialSoakChangeResult(int initialChange) {
        super(initialChange);
    }

    /**
     * Increases the wet ticks of the affectee by the specified amount
     *
     * @param affectee The affectee of the change
     * @param amount   The amount of change to apply
     */
    @Override
    protected void applyChange(Soakable affectee, int amount) {
        int current = affectee.thermoo$getWetTicks();
        affectee.thermoo$setWetTicks(current + amount);
    }
}

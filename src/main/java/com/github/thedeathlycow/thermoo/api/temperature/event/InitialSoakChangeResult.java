package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;

public class InitialSoakChangeResult extends InitialEnvironmentChangeResult<Soakable> {

    public InitialSoakChangeResult(int initialChange) {
        super(initialChange);
    }

    @Override
    public void applyChange(Soakable affectee, int amount) {
        int current = affectee.thermoo$getWetTicks();
        affectee.thermoo$setWetTicks(current + amount);
    }
}

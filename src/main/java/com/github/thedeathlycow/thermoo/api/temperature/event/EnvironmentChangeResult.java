package com.github.thedeathlycow.thermoo.api.temperature.event;

public class EnvironmentChangeResult {

    private boolean appliedChange = false;

    public boolean isAppliedChange() {
        return appliedChange;
    }

    public void setAppliedChange() {
        this.appliedChange = true;
    }
}

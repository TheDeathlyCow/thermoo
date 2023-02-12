package com.github.thedeathlycow.thermoo.api.temperature.event;

/**
 * Stores information relating to changes in Environment effects, such as temperature from
 * {@link com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware} and {@link com.github.thedeathlycow.thermoo.api.temperature.Soakable}
 */
public class EnvironmentChangeResult {

    /**
     * Whether the change has been applied
     */
    private boolean appliedChange = false;

    /**
     * @return Returns if the change has been applied
     */
    public boolean isAppliedChange() {
        return appliedChange;
    }

    /**
     * Invoked when the change is applied
     */
    public void setAppliedChange() {
        this.appliedChange = true;
    }
}

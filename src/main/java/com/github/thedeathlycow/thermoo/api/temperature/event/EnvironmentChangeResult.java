package com.github.thedeathlycow.thermoo.api.temperature.event;

/**
 * Stores information relating to changes in Environment effects, such as temperature from
 * {@link com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware} and {@link com.github.thedeathlycow.thermoo.api.temperature.Soakable}
 * <p>
 * Listeners should declare when they apply changes to the temperature of the affectee, so that other listeners know
 * not to apply the change twice.
 */
public class EnvironmentChangeResult {

    /**
     * Whether the initial change has been applied by a listener
     */
    private boolean appliedInitialChange = false;

    /**
     * Whether any extra changes have been applied by a listener
     */
    private boolean appliedExtraChange = false;

    /**
     * The initial temperature change that triggered the event
     */
    private final int initialTemperatureChange;

    /**
     * The sum total of extra temperature changes applied by listeners in addition to the initial change
     */
    private int extraTemperatureChange;

    public EnvironmentChangeResult(int initialTemperatureChange) {
        this.initialTemperatureChange = initialTemperatureChange;
        this.extraTemperatureChange = 0;
    }


    /**
     * @return Returns if the initial change has been applied
     */
    public boolean isInitialChangeApplied() {
        return appliedInitialChange;
    }

    /**
     * Declare that a listener has applied the initial change
     */
    public void setAppliedInitialChange() {
        this.appliedInitialChange = true;
    }

    /**
     * @return Returns the initial temperature change
     */
    public int getInitialTemperatureChange() {
        return initialTemperatureChange;
    }

    /**
     * Declare that a listener has applied an extra temperature change, beyond the initial change
     *
     * @param temperatureChange The temperature change added
     */
    public void addAdditionalChange(int temperatureChange) {
        this.extraTemperatureChange += temperatureChange;
        this.appliedExtraChange = true;
    }

    /**
     * @return Returns the sum total of the extra temperature changes
     */
    public int getExtraTemperatureChange() {
        return this.extraTemperatureChange;
    }

    /**
     * @return Returns if a listener has applied any extra temperature changes
     */
    public boolean isExtraChangeApplied() {
        return this.appliedExtraChange;
    }

    /**
     * @return Returns the sum total of the initial temperature change and all extra temperature changes
     */
    public int getTotalTemperatureChange() {
        return this.initialTemperatureChange + this.extraTemperatureChange;
    }
}

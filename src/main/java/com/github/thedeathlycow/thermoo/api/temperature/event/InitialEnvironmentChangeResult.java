package com.github.thedeathlycow.thermoo.api.temperature.event;

/**
 * This class is to ensure that changes in environmental effects, such as temperature from
 * {@link com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware} or wetness from a {@link com.github.thedeathlycow.thermoo.api.temperature.Soakable}
 * that come from common sources dictated by the {@link com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController}
 * are not applied more than once to an affectee.
 * <p>
 * Changes are not actually applied until after the event invocation is complete, so that it may be modified by listeners.
 * By default, the change will NOT be applied by the event - a listener must explicitly tell Thermoo to do so. This is to
 * allow for other mods or datapacks to define their own implementations for passive temperature changes, using the Thermoo
 * temperature attribute. Or to allow for them to disable passive temperature changes in the case they do not want them.
 */
public abstract class InitialEnvironmentChangeResult<T> {

    /**
     * Whether the initial change has been applied by a listener
     */
    private boolean appliedInitialChange = false;

    /**
     * The initial temperature change that triggered the event
     */
    private int initialChange;

    private final T affectee;

    /**
     * Creates an initial environment change result with an affectee and an amount
     *
     * @param affectee      The entity affected by the change
     * @param initialChange The change's initial value
     */
    public InitialEnvironmentChangeResult(T affectee, int initialChange) {
        this.affectee = affectee;
        this.initialChange = initialChange;
    }

    /**
     * Applies the initial change to an affectee.
     *
     * @param affectee The affectee of the change
     * @param amount   The amount of change to apply
     */
    protected abstract void applyChange(T affectee, int amount);

    /**
     * @return Returns the initial change value of the result
     */
    public int getInitialChange() {
        return initialChange;
    }

    /**
     * Set the initial change to a new value
     *
     * @param amount The new value for the initial change
     */
    public void setInitialChange(int amount) {
        this.initialChange = amount;
    }

    /**
     * @return Returns true if the initial change was applied
     */
    public boolean isInitialChangeApplied() {
        return appliedInitialChange;
    }

    /**
     * Marks the affectee as
     * Applies the initial change to the environment aware entity, if it has not already been applied
     * <p>
     * Changes are not actually applied until after the event invocation is complete, so that it may be modified by listeners.
     */
    public final void applyInitialChange() {
        if (!this.appliedInitialChange) {
            this.appliedInitialChange = true;
        }
    }

    /**
     * Called AFTER the event this was created for has completed its invocation. Will actually apply the changes to
     * from event so that listeners may modify the change if they so wish.
     */
    public void onEventComplete() {
        if (this.isInitialChangeApplied()) {
            this.applyChange(this.affectee, initialChange);
        }
    }
}

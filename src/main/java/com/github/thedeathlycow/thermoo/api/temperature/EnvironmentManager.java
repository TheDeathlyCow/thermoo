package com.github.thedeathlycow.thermoo.api.temperature;

import com.github.thedeathlycow.thermoo.impl.EnvironmentControllerImpl;
import com.github.thedeathlycow.thermoo.impl.Thermoo;

/**
 * Controls what instance of the {@link EnvironmentManager} is to be used by Thermoo events
 */
public final class EnvironmentManager {

    /**
     * Singleton instance of this class
     */
    public static final EnvironmentManager INSTANCE = new EnvironmentManager();

    private EnvironmentController controller;

    /**
     * @return Returns the current default {@link EnvironmentController} used by Thermoo events
     */
    public EnvironmentController getController() {
        return controller;
    }

    /**
     * Sets the default {@link EnvironmentController} to be used by Thermoo events.
     * <p>
     * If this is NOT set, then it will be set to a default instance provided by Thermoo.
     * <p>
     * NOTE: This will override the existing event, so be careful that no other mods are using this!
     *
     * @param controller The controller to set
     */
    public void setController(EnvironmentController controller) {
        this.controller = controller;

        var sb = new StringBuilder();
        sb.append("The Thermoo Environment Controller has been updated");
        var trace = Thread.currentThread().getStackTrace();
        if (trace.length >= 3) {
            sb.append(" by ");
            sb.append(trace[2].getClassName());
            sb.append("#");
            sb.append(trace[2].getMethodName());
        }
        Thermoo.LOGGER.info(sb.toString());
    }

    private EnvironmentManager() {
        this.controller = new EnvironmentControllerImpl();
    }

}

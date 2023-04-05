package com.github.thedeathlycow.thermoo.api.temperature;

import com.github.thedeathlycow.thermoo.impl.EnvironmentControllerImpl;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controls what instance of the {@link EnvironmentManager} is to be used by Thermoo events
 */
public final class EnvironmentManager {

    /**
     * Singleton instance of this class
     */
    public static final EnvironmentManager INSTANCE = new EnvironmentManager();

    private final Map<String, EnvironmentController> controllers = new HashMap<>();
    private String controller;

    /**
     * @return Returns the current default {@link EnvironmentController} used by Thermoo events
     */
    public EnvironmentController getController() {
        return this.controllers.get(controller);
    }

    /**
     * Gets an {@link EnvironmentController} by name
     *
     * @param name The name of the controller
     * @return The controller
     */
    public Optional<EnvironmentController> getControllerByName(String name) {
        return Optional.ofNullable(this.controllers.get(name));
    }

    /**
     * Adds a new {@link EnvironmentController} to be used by Thermoo events with a specified name.
     * <p>
     * Which controller ends up being used is determined by the Thermoo config.
     *
     * @param name       The name of the controller. It is recommended to use your modid for this.
     * @param controller The controller to set
     */
    public void putController(String name, EnvironmentController controller) {
        this.controllers.put(name, controller);
    }

    /**
     * Sets the current controller to be used by Thermoo.
     * <p>
     * If the controller has not been added to the manager by {@link EnvironmentManager#putController(String, EnvironmentController)},
     * then an {@link IllegalArgumentException} will be thrown.
     *
     * @param name The name of the controller to be used by Thermoo
     */
    public void setController(String name) {
        if (this.controllers.containsKey(name)) {
            this.controller = name;
        } else {
            throw new IllegalArgumentException(
                    String.format("Cannot set controller '%s' as it does not exist in the environment manager", name)
            );
        }
    }

    private EnvironmentManager() {
        this.putController(Thermoo.MODID, new EnvironmentControllerImpl());
        this.setController(Thermoo.getConfig().environmentConfig.getController());
    }

}

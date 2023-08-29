package com.github.thedeathlycow.thermoo.api.temperature;

import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

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
    @NotNull
    public EnvironmentController getController() {
        return controller;
    }

    /**
     * Appends an additional environment controller decorator to the existing environment controller
     *
     * @param decorator The controller decorator constructor
     */
    public void addController(Function<EnvironmentController, EnvironmentController> decorator) {
        this.controller = decorator.apply(this.controller);
    }

    /**
     * Peels off the top decorator of the controller. If the controller has no child, then this will do nothing.
     *
     * @return Returns the previous decorator
     */
    @NotNull
    public EnvironmentController peelController() {
        EnvironmentController old = this.controller;

        EnvironmentController decorated = this.controller.getDecorated();
        if (decorated != null) {
            this.controller = decorated;
        }

        return old;
    }

    private void resetController() {
        this.controller = new EmptyEnvironmentController();
    }

    private EnvironmentManager() {
        this.resetController();

        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> {
                    this.resetController();
                    this.addController(EnvironmentControllerInitializeEvent.EVENT.invoker()::decorateController);
                }
        );
    }

}

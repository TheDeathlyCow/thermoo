package com.github.thedeathlycow.thermoo.api.temperature;

import com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.jetbrains.annotations.Contract;
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

    @NotNull
    private EnvironmentController controller;

    /**
     * Gets the currently configured environment controller. Note that the controller is reset on server stop/start.
     * By default, the controller is an instance of {@link EmptyEnvironmentController}.
     *
     * @return Returns the current environment controller used by Thermoo.
     */
    @NotNull
    public EnvironmentController getController() {
        return controller;
    }

    /**
     * Appends an additional environment controller decorator to the existing environment controller.
     * <p>
     * Note that this controller will be removed on server stop, and then reinitialized with {@link EnvironmentControllerInitializeEvent}.
     * It is therefore preferred for mods to add their decorators via that event rather than through direct access to this method.
     * <p>
     * This method is left public primarily for testing/mocking purposes, and should not be used normally.
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

    /**
     * Factory method for creating a default controller instance
     *
     * @return Returns a new instance of the default controller
     */
    @Contract("->new")
    private EnvironmentController createDefaultController() {
        return new EmptyEnvironmentController();
    }

    private EnvironmentManager() {
        this.controller = this.createDefaultController();

        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> {
                    this.addController(EnvironmentControllerInitializeEvent.EVENT.invoker()::decorateController);
                }
        );
        ServerLifecycleEvents.SERVER_STOPPING.register(
                server -> {
                    this.controller = this.createDefaultController();
                }
        );
    }

}

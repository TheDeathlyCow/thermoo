package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;

/**
 * Phased event for applying environment controllers in an ordered manner.
 */
public class EnvironmentControllerInitializeEvent {

    /**
     * First phase after setting values in default. Use this to apply changes to base values.
     */
    public static final Identifier MODIFY_PHASE = Thermoo.id("modify");

    /**
     * Overriding phase of environment controller initialization. Use this to force override the base controllers and
     * change values.
     */
    public static final Identifier OVERRIDE_PHASE = Thermoo.id("override");

    /**
     * Final phase of environment controller initialization. Controllers added in this phase should not change base values,
     * only read them.
     */
    public static final Identifier LISTENER_PHASE = Thermoo.id("listener");


    /**
     * Called when the {@link EnvironmentManager} initializes its controller. This occurs on every server start,
     * before worlds are created.
     * <p>
     * The event runs in phases which are ordered as follows:
     * <ol>
     *     <li>{@link Event#DEFAULT_PHASE}: Earliest phase, sets the initial values for the controller.</li>
     *     <li>{@link #MODIFY_PHASE}: Modifies controller initial values (such as percentage increases).</li>
     *     <li>{@link #OVERRIDE_PHASE}: Force override values. Used if you really want to specifically disable something or set a new value. Should never rely on base values.</li>
     *     <li>{@link #LISTENER_PHASE}: Listener phase. Occurs last, but should not write any new values, just read and return base values.</li>
     * </ol>
     * <p>
     * Follow links for further details about each phase
     */

    public static final Event<Callback> EVENT = EventFactory.createWithPhases(
            Callback.class,
            callbacks -> controller -> {
                EnvironmentController result = controller;
                for (Callback callback : callbacks) {
                    result = callback.decorateController(result);
                }
                return result;
            },
            Event.DEFAULT_PHASE, MODIFY_PHASE, OVERRIDE_PHASE, LISTENER_PHASE
    );

    @FunctionalInterface
    public interface Callback {

        /**
         * Create a new controller decorator with the base controller as its param
         *
         * @param controller The controller to decorate
         * @return Returns a newly decorated controller
         */
        EnvironmentController decorateController(EnvironmentController controller);

    }

}

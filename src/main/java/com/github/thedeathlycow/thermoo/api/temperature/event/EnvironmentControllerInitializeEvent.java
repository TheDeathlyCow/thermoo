package com.github.thedeathlycow.thermoo.api.temperature.event;

import com.github.thedeathlycow.thermoo.api.temperature.EmptyEnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;

public class EnvironmentControllerInitializeEvent {

    public static final Identifier OVERRIDE_PHASE = Thermoo.id("override");

    public static final Identifier LISTENER_PHASE = Thermoo.id("listener");


    public static final Event<Callback> EVENT = EventFactory.createWithPhases(
            Callback.class,
            callbacks -> controller -> {
                EnvironmentController result = controller;
                for (Callback callback : callbacks) {
                    result = callback.decorateController(result);
                }
                return result;
            },
            Event.DEFAULT_PHASE, OVERRIDE_PHASE, LISTENER_PHASE
    );

    @FunctionalInterface
    public interface Callback {

        EnvironmentController decorateController(EnvironmentController controller);

    }

}

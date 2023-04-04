package com.github.thedeathlycow.thermoo.api.temperature;

import com.github.thedeathlycow.thermoo.impl.EnvironmentControllerImpl;

public final class EnvironmentManager {

    public static final EnvironmentManager INSTANCE = new EnvironmentManager();

    private EnvironmentController controller;

    public EnvironmentController getController() {
        return controller;
    }

    public void setController(EnvironmentController controller) {
        this.controller = controller;
    }

    private EnvironmentManager() {
        this.controller = new EnvironmentControllerImpl();
    }

}

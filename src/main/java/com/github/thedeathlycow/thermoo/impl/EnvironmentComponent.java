package com.github.thedeathlycow.thermoo.impl;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface EnvironmentComponent extends Component {

    double SYNC_DISTANCE = 32;

    int getValue();

    void setValue(int value);

}

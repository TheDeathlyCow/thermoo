package com.github.thedeathlycow.thermoo.impl;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface EnvironmentComponent extends Component {

    int getValue();

    void setValue(int value);

}

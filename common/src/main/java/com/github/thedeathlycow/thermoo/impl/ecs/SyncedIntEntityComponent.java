package com.github.thedeathlycow.thermoo.impl.ecs;

/// A component that stores an `int` and can be synchronized.
public interface SyncedIntEntityComponent {
    int getValue();

    void setValue(int value);

    boolean isDirty();
}

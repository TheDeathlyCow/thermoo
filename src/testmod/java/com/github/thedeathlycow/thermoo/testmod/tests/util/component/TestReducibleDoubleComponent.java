package com.github.thedeathlycow.thermoo.testmod.tests.util.component;

import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponent;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public record TestReducibleDoubleComponent(double value) implements ReducibleComponent<TestReducibleDoubleComponent> {
    public static final Codec<TestReducibleDoubleComponent> CODEC = Codec.DOUBLE
            .xmap(TestReducibleDoubleComponent::new, TestReducibleDoubleComponent::value);

    public static final ComponentType<TestReducibleDoubleComponent> KEY = ComponentType.<TestReducibleDoubleComponent>builder()
            .codec(CODEC)
            .build();

    @Override
    public TestReducibleDoubleComponent reduceWith(TestReducibleDoubleComponent other) {
        return new TestReducibleDoubleComponent(this.value + other.value());
    }
}

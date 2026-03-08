package com.github.thedeathlycow.thermoo.api.core.v1.source;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface TemperatureSource {
    Component description();

    Holder<TemperatureReduction> reduction();
}

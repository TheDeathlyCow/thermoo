package com.github.thedeathlycow.thermoo.api.temperature.effect.v2;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public non-sealed interface ClientTemperatureEffect extends TemperatureEffect<ClientLevel>{
}
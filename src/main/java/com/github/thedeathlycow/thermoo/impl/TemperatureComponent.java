package com.github.thedeathlycow.thermoo.impl;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class TemperatureComponent implements EnvironmentComponent, AutoSyncedComponent {

    private static final String TEMPERATURE_KEY = "temperature";

    private int temperature = 0;

    private final LivingEntity provider;

    public TemperatureComponent(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public int getValue() {
        return this.temperature;
    }

    @Override
    public void setValue(int value) {
        this.temperature = value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.temperature = tag.getInt(TEMPERATURE_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt(TEMPERATURE_KEY, this.temperature);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.temperature);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.temperature = buf.readVarInt();
    }
}

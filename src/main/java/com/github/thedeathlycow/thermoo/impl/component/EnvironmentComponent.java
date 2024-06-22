package com.github.thedeathlycow.thermoo.impl.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class EnvironmentComponent implements Component, AutoSyncedComponent {

    private static final String NBT_KEY = "value";

    private static final int SYNC_DISTANCE = 32;

    private int value = 0;

    private final LivingEntity provider;

    private boolean dirty = false;

    public EnvironmentComponent(LivingEntity provider) {
        this.provider = provider;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        if (this.value != value) {
            this.value = value;
            this.markDirty();
        }
    }

    public void markDirty() {
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.value = tag.getInt(NBT_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt(NBT_KEY, this.value);
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.value);
        this.dirty = false;
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        this.value = buf.readVarInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        final BlockPos providerPos = this.provider.getBlockPos();
        return player == this.provider
                || providerPos.isWithinDistance(player.getSyncedPos(), EnvironmentComponent.SYNC_DISTANCE);
    }
}

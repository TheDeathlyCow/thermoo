package com.github.thedeathlycow.thermoo.impl.component;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    public void readData(ValueInput readView) {
        this.value = readView.getIntOr(NBT_KEY, 0);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putInt(NBT_KEY, this.value);
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeVarInt(this.value);
        this.dirty = false;
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        this.value = buf.readVarInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        final BlockPos providerPos = this.provider.blockPosition();
        return player == this.provider
                || providerPos.closerToCenterThan(player.trackingPosition(), EnvironmentComponent.SYNC_DISTANCE);
    }

    @Override
    public boolean isRequiredOnClient() {
        return false;
    }
}

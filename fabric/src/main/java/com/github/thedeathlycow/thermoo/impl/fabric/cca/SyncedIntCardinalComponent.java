package com.github.thedeathlycow.thermoo.impl.fabric.cca;

import com.github.thedeathlycow.thermoo.impl.ecs.SyncedIntEntityComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

public final class SyncedIntCardinalComponent implements SyncedIntEntityComponent, CardinalComponent, AutoSyncedComponent {
    private static final String NBT_KEY = "value";
    private static final int SYNC_DISTANCE = 32;

    private final LivingEntity provider;
    private int value = 0;
    private boolean dirty = false;

    public SyncedIntCardinalComponent(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void setValue(int value) {
        if (this.value != value) {
            this.value = value;
            this.dirty = true;
        }
    }

    @Override
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
                || providerPos.closerToCenterThan(player.trackingPosition(), SYNC_DISTANCE);
    }

    @Override
    public boolean isRequiredOnClient() {
        return false;
    }
}
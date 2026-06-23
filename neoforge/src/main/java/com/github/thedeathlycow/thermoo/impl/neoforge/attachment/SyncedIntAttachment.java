/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.impl.neoforge.attachment;

import com.github.thedeathlycow.thermoo.impl.ecs.SyncedIntEntityComponent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class SyncedIntAttachment implements SyncedIntEntityComponent {
    public static final MapCodec<SyncedIntAttachment> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT
                            .fieldOf("value")
                            .forGetter(SyncedIntAttachment::getValue)
            ).apply(instance, SyncedIntAttachment::new)
    );

    public static final StreamCodec<ByteBuf, SyncedIntAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncedIntAttachment::getValue,
            SyncedIntAttachment::new
    );

    private int value;
    private boolean dirty;

    public SyncedIntAttachment() {
        this(0);
    }

    public SyncedIntAttachment(int value) {
        this.value = value;
        this.dirty = false;
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
}
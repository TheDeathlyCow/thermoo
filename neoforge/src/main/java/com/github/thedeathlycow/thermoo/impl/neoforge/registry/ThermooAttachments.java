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

package com.github.thedeathlycow.thermoo.impl.neoforge.registry;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.neoforge.attachment.SyncedIntAttachment;
import com.github.thedeathlycow.thermoo.impl.neoforge.attachment.TemperatureStatusSettingsAttachment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ThermooAttachments {
    private static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Thermoo.MODID);

    public static final Supplier<AttachmentType<SyncedIntAttachment>> TEMPERATURE = register(
            "temperature",
            AttachmentType.builder(() -> new SyncedIntAttachment())
                    .serialize(SyncedIntAttachment.CODEC)
                    .sync(SyncedIntAttachment.STREAM_CODEC)
    );

    public static final Supplier<AttachmentType<SyncedIntAttachment>> WETNESS = register(
            "wetness",
            AttachmentType.builder(() -> new SyncedIntAttachment())
                    .serialize(SyncedIntAttachment.CODEC)
                    .sync(SyncedIntAttachment.STREAM_CODEC)
    );

    public static final Supplier<AttachmentType<TemperatureStatusSettingsAttachment>> TEMPERATURE_STATUS_SETTINGS = register(
            "temperature_status_settings",
            AttachmentType.serializable(TemperatureStatusSettingsAttachment::new)
    );

    public static void initialize(IEventBus modBus) {
        Thermoo.LOGGER.debug("Initialized Thermoo Neoforge attachments");
        REGISTRY.register(modBus);
    }

    private static <T> Supplier<AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
        return REGISTRY.register(name, builder::build);
    }

    private ThermooAttachments() {

    }
}
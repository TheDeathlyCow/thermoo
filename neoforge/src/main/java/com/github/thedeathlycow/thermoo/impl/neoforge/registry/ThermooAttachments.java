package com.github.thedeathlycow.thermoo.impl.neoforge.registry;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.neoforge.attachment.SyncedIntAttachment;
import com.github.thedeathlycow.thermoo.impl.neoforge.attachment.TemperatureStatusSettingsAttachment;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ThermooAttachments {
    public static final AttachmentType<SyncedIntAttachment> TEMPERATURE = register(
            "temperature",
            AttachmentType.builder(() -> new SyncedIntAttachment())
                    .serialize(SyncedIntAttachment.CODEC)
                    .sync(SyncedIntAttachment.STREAM_CODEC)
    );

    public static final AttachmentType<SyncedIntAttachment> WETNESS = register(
            "wetness",
            AttachmentType.builder(() -> new SyncedIntAttachment())
                    .serialize(SyncedIntAttachment.CODEC)
                    .sync(SyncedIntAttachment.STREAM_CODEC)
    );

    public static final AttachmentType<TemperatureStatusSettingsAttachment> TEMPERATURE_STATUS_SETTINGS = register(
            "temperature_status_settings",
            AttachmentType.serializable(TemperatureStatusSettingsAttachment::new)
    );

    public static void initialize() {
        Thermoo.LOGGER.debug("Initialized Thermoo Neoforge attachments");
    }

    private static <T> AttachmentType<T> register(String name, AttachmentType.Builder<T> builder) {
        return Registry.register(NeoForgeRegistries.ATTACHMENT_TYPES, Thermoo.id(name), builder.build());
    }

    private ThermooAttachments() {

    }
}
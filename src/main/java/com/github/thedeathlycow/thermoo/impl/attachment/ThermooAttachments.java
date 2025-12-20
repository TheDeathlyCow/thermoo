package com.github.thedeathlycow.thermoo.impl.attachment;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ThermooAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES,
            Thermoo.MODID
    );

    public static final Supplier<AttachmentType<Integer>> TEMPERATURE = ATTACHMENT_TYPES.register(
            "temperature",
            () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .sync(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static final Supplier<AttachmentType<Integer>> WETNESS = ATTACHMENT_TYPES.register(
            "wetness",
            () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .sync(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static final Supplier<AttachmentType<TemperatureEffectAttachment>> TEMPERATURE_EFFECTS = ATTACHMENT_TYPES.register(
            "temperature_effects",
            () -> AttachmentType.builder(TemperatureEffectAttachment::new)
                    .build()
    );

    private ThermooAttachments() {

    }
}
package com.github.thedeathlycow.thermoo.impl.compat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record PatchedVersion(
        String minecraftVersion,
        List<String> mods
) {
    public static final Codec<PatchedVersion> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING
                            .fieldOf("minecraft_version")
                            .forGetter(PatchedVersion::minecraftVersion),
                    Codec.STRING.listOf()
                            .fieldOf("mods")
                            .forGetter(PatchedVersion::mods)
            ).apply(instance, PatchedVersion::new)
    );
}
package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.platform.Loader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record PatchedVersion(
        Loader loader,
        List<String> minecraftVersions,
        List<String> mods
) {
    public static final Codec<PatchedVersion> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Loader.CODEC
                            .fieldOf("loader")
                            .forGetter(PatchedVersion::loader),
                    Codec.STRING.listOf()
                            .fieldOf("minecraft_versions")
                            .forGetter(PatchedVersion::minecraftVersions),
                    Codec.STRING.listOf()
                            .fieldOf("mods")
                            .forGetter(PatchedVersion::mods)
            ).apply(instance, PatchedVersion::new)
    );
}
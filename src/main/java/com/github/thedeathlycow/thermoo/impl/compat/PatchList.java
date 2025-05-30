package com.github.thedeathlycow.thermoo.impl.compat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record PatchList(
        List<PatchedVersion> patches
) {
    public static final Codec<PatchList> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    PatchedVersion.CODEC.listOf()
                            .fieldOf("patches")
                            .forGetter(PatchList::patches)
            ).apply(instance, PatchList::new)
    );
}

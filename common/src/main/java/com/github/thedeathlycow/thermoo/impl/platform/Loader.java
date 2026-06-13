package com.github.thedeathlycow.thermoo.impl.platform;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Loader implements StringRepresentable {
    FABRIC("fabric"),
    NEOFORGE("neoforge");

    public static final Codec<Loader> CODEC = StringRepresentable.fromValues(Loader::values);

    private final String name;

    Loader(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
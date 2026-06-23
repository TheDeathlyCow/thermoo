package com.github.thedeathlycow.thermoo.impl.test.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooGameRules;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

public class ThermooGameRulesImpl implements ThermooGameRules {
    @Override
    public GameRule<Boolean> forBoolean(Identifier id, boolean defaultValue) {
        return null;
    }

    @Override
    public <E extends Enum<E>> GameRule<E> forEnum(Identifier id, E defaultValue, Codec<E> codec) {
        return null;
    }
}
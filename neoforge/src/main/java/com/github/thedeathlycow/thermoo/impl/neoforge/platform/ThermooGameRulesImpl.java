package com.github.thedeathlycow.thermoo.impl.neoforge.platform;

import com.github.thedeathlycow.thermoo.impl.neoforge.ThermooNeoforge;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooGameRules;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

public class ThermooGameRulesImpl implements ThermooGameRules {
    @Override
    public GameRule<Boolean> forBoolean(Identifier id, boolean defaultValue) {
        return ThermooNeoforge.booleanGameRule(id, defaultValue);
    }

    @Override
    public <E extends Enum<E>> GameRule<E> forEnum(Identifier id, E defaultValue, Codec<E> codec) {
        return ThermooNeoforge.enumGameRule(id, defaultValue, codec);
    }
}
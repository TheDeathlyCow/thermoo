package com.github.thedeathlycow.thermoo.impl.fabric.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooGameRules;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

public class ThermooGameRulesImpl implements ThermooGameRules {
    @Override
    public GameRule<Boolean> forBoolean(Identifier id, boolean defaultValue) {
        return GameRuleBuilder.forBoolean(defaultValue).buildAndRegister(id);
    }

    @Override
    public <E extends Enum<E>> GameRule<E> forEnum(Identifier id, E defaultValue, Codec<E> codec) {
        return GameRuleBuilder.forEnum(defaultValue).codec(codec).buildAndRegister(id);
    }
}
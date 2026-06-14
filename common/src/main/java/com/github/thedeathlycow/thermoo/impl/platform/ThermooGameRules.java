package com.github.thedeathlycow.thermoo.impl.platform;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

/// Platform independent API for creating unit tests. Based on the Fabric API GameRuleBuilder. Mostly meant for testing.
///
/// This is not a stable API!
public interface ThermooGameRules {
    GameRule<Boolean> forBoolean(Identifier id, boolean defaultValue);

    <E extends Enum<E>> GameRule<E> forEnum(Identifier id, E defaultValue, Codec<E> codec);
}

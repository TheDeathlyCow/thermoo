package com.github.thedeathlycow.thermoo.impl.neoforge.platform;

import com.github.thedeathlycow.thermoo.impl.platform.ThermooGameRules;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public class ThermooGameRulesImpl implements ThermooGameRules {
    @Override
    public GameRule<Boolean> forBoolean(Identifier id, boolean defaultValue) {
        GameRule<Boolean> rule = new GameRule<>(
                GameRuleCategory.MISC,
                GameRuleType.BOOL,
                BoolArgumentType.bool(),
                GameRuleTypeVisitor::visitBoolean,
                Codec.BOOL,
                b -> b ? 1 : 0,
                defaultValue,
                FeatureFlagSet.of()
        );

        return Registry.register(BuiltInRegistries.GAME_RULE, id, rule);
    }

    @Override
    public <E extends Enum<E>> GameRule<E> forEnum(Identifier id, E defaultValue, Codec<E> codec) {
        GameRule<E> rule = new GameRule<>(
                GameRuleCategory.MISC,
                GameRuleType.INT,
                null,
                GameRuleTypeVisitor::visitInteger,
                codec,
                Enum::ordinal,
                defaultValue,
                FeatureFlagSet.of()
        );

        return Registry.register(BuiltInRegistries.GAME_RULE, id, rule);
    }
}
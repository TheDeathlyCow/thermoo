package com.github.thedeathlycow.thermoo.impl.neoforge;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import com.nerjal.unruled_api.UnruledApi;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class ThermooNeoforge implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        Thermoo.onInitialize(mod);
    }

    public static GameRule<Boolean> booleanGameRule(Identifier id, boolean defaultValue) {
        return UnruledApi.registerBoolean(id, GameRuleCategory.MISC, defaultValue);
    }

    public static <E extends Enum<E>> GameRule<E> enumGameRule(Identifier id, E defaultValue, Codec<E> codec) {
        return UnruledApi.registerEnum(id, GameRuleCategory.MISC, defaultValue, defaultValue.getDeclaringClass());
    }
}
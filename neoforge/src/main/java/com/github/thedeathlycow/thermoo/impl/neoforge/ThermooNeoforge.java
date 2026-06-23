/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lessner General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.impl.neoforge;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import com.nerjal.unruled_api.UnruledApi;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.YumiMods;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class ThermooNeoforge implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        Thermoo.onInitialize(mod);

        if (YumiMods.get().isDevelopmentEnvironment()) {
            Thermoo.LOGGER.info("Thermoo Neoforge initialized");
        }
    }

    public static GameRule<Boolean> booleanGameRule(Identifier id, boolean defaultValue) {
        return UnruledApi.registerBoolean(id, GameRuleCategory.MISC, defaultValue);
    }

    public static <E extends Enum<E>> GameRule<E> enumGameRule(Identifier id, E defaultValue, Codec<E> codec) {
        return UnruledApi.registerEnum(id, GameRuleCategory.MISC, defaultValue, defaultValue.getDeclaringClass());
    }
}
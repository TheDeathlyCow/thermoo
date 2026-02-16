package com.github.thedeathlycow.thermoo.impl.client.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public interface ThermooDebugScreenEntry extends DebugScreenEntry {
    DebugEntryCategory CATEGORY = new DebugEntryCategory(Component.translatable("debug.options.category.thermoo"), 3.0f);

    default DebugEntryCategory category() {
        return CATEGORY;
    }
}
package com.github.thedeathlycow.thermoo.impl.client.debug;

import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.network.chat.Component;

public interface ThermooDebugScreenEntry extends DebugScreenEntry {
    DebugEntryCategory CATEGORY = new DebugEntryCategory(Component.translatable("debug.options.category.thermoo"), 3.0f);

    @Override
    default DebugEntryCategory category() {
        return CATEGORY;
    }
}
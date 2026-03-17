package com.github.thedeathlycow.thermoo.impl.client.debug;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.environment.v2.EnvironmentLookup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class DebugEnvironmentComponents implements ThermooDebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        @Nullable Entity cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity != null) {
            DataComponentMap components = EnvironmentLookup.getInstance()
                    .findEnvironmentComponents(level, cameraEntity.blockPosition());

            List<String> displayed = components.stream()
                    .map(component -> {
                        String name = Util.getRegisteredName(ThermooRegistries.ENVIRONMENT_COMPONENT_TYPE, component.type());
                        return "%s: %s".formatted(name, component.value().toString());
                    }).toList();

            displayer.addToGroup(DebugEnvironments.GROUP, "Environment components:");
            displayer.addToGroup(DebugEnvironments.GROUP, displayed);
        }
    }
}
package com.github.thedeathlycow.thermoo.impl.client.debug;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugSelfStatuses implements ThermooDebugScreenEntry {
    public static final Identifier GROUP = Thermoo.id("entity");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        @Nullable Entity cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity instanceof LivingEntity livingEntity && level instanceof ServerLevel serverLevel) {
            displayer.addToGroup(
                    GROUP,
                    "Temperature: %d (%.2f%%)".formatted(
                            livingEntity.thermoo$getTemperature(),
                            livingEntity.thermoo$getTemperatureScale() * 100
                    )
            );

            displayer.addToGroup(
                    GROUP,
                    "Soaking: %d (%.2f%%)".formatted(
                            livingEntity.thermoo$getWetTicks(),
                            livingEntity.thermoo$getSoakedScale() * 100
                    )
            );
        }
    }
}
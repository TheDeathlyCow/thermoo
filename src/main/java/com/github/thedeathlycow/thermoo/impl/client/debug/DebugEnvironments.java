package com.github.thedeathlycow.thermoo.impl.client.debug;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class DebugEnvironments implements ThermooDebugScreenEntry {
    public static final Identifier GROUP = Thermoo.id("thermoo");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        @Nullable Entity cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity != null && level instanceof ServerLevel serverLevel) {
            Registry<EnvironmentDefinition> registry = serverLevel
                    .registryAccess()
                    .lookupOrThrow(ThermooRegistryKeys.ENVIRONMENT);

            Stream<EnvironmentDefinition> environments = EnvironmentLookupImpl.getAllMatchingEnvironments(
                    level.getBiome(cameraEntity.blockPosition()),
                    registry
            );


            displayer.addToGroup(GROUP, "Environments available:");

            List<String> displayed = environments.map(environment -> {
                return "%s".formatted(Util.getRegisteredName(registry, environment));
            }).toList();
            displayer.addToGroup(GROUP, displayed);
        }
    }
}
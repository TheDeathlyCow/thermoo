package com.github.thedeathlycow.thermoo.impl.client.debug;

import com.github.thedeathlycow.thermoo.api.season.TemperateSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeason;
import com.github.thedeathlycow.thermoo.api.season.ThermooSeasonState;
import com.github.thedeathlycow.thermoo.api.season.TropicalSeason;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class DebugSeasons implements ThermooDebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        @Nullable Entity cameraEntity = minecraft.getCameraEntity();

        if (level != null && cameraEntity != null) {
            Optional<ThermooSeasonState<TemperateSeason>> temperateSeason = TemperateSeason.getCurrentState(level, cameraEntity.blockPosition());
            Optional<ThermooSeasonState<TropicalSeason>> tropicalSeason = TropicalSeason.getCurrentState(level, cameraEntity.blockPosition());

            this.addLine(displayer, "Temperate Season", temperateSeason);
            this.addLine(displayer, "Tropical Season", tropicalSeason);
        }
    }

    private <S extends ThermooSeason> void addLine(DebugScreenDisplayer displayer, String name, Optional<ThermooSeasonState<S>> result) {
        if (result.isPresent()) {
            ThermooSeasonState<?> state = result.get();
            displayer.addToGroup(DebugEnvironments.GROUP, "%s: %s (%s%%)".formatted(name, state.season().getSerializedName(), state.progress() * 100));
        } else {
            displayer.addToGroup(DebugEnvironments.GROUP, "%s: [None]".formatted(name));
        }
    }
}
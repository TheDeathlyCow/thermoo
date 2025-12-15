package com.github.thedeathlycow.thermoo.api.season;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Mod-agnostic Seasons enum. Thermoo does not any provide seasons-like functionality itself, but this can be used to
 * better integrate with any mods that do provide season functionality.
 */
public enum TemperateSeason implements StringRepresentable {
    SPRING("spring"),
    SUMMER("summer"),
    AUTUMN("autumn"),
    WINTER("winter");

    public static final Codec<TemperateSeason> CODEC = StringRepresentable.fromEnum(TemperateSeason::values);

    private final String name;

    TemperateSeason(String name) {
        this.name = name;
    }

    /**
     * Shorthand for invoking {@link ThermooSeasonEvents#GET_CURRENT_SEASON}.
     * <p>
     * Retrieves the current season, if a season mod is loaded. Thermoo does not add seasons by itself, seasons must be
     * implemented by another mod like Fabric Seasons or Serene Seasons. This event just places season integration into
     * a common source.
     * <p>
     * This event should only ever return the temperate seasons, that is {@link #SPRING}, {@link #SUMMER},
     * {@link #AUTUMN}, or {@link #WINTER}, and never the tropical seasons. For tropical seasons, use
     * {@link TropicalSeason}
     *
     * @param level The current world / level to get the season from.
     * @return Returns the current season if a Seasons mod is installed, or empty if no seasons mod is installed.
     * @see TropicalSeason to get the current tropical season
     */
    public static Optional<TemperateSeason> getCurrentSeason(Level level) {
        return ThermooSeasonEvents.GET_CURRENT_SEASON.invoker().getCurrentSeason(level);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

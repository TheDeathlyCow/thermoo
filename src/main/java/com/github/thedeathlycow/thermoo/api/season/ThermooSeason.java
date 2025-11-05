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
public enum ThermooSeason implements StringRepresentable {
    SPRING(false, "spring"),
    SUMMER(false, "summer"),
    AUTUMN(false, "autumn"),
    WINTER(false, "winter"),
    TROPICAL_DRY(true, "dry"),
    TROPICAL_WET(true, "wet");

    public static final Codec<ThermooSeason> CODEC = StringRepresentable.fromEnum(ThermooSeason::values);

    private final boolean isTropical;
    private final String name;

    ThermooSeason(boolean isTropical, String name) {
        this.isTropical = isTropical;
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
     * {@link #getCurrentTropicalSeason(Level, BlockPos)}
     *
     * @param world The current world / level to get the season from.
     * @return Returns the current season if a Seasons mod is installed, or empty if no seasons mod is installed.
     * @see #getCurrentTropicalSeason(Level, BlockPos) to get the current tropical season
     */
    public static Optional<ThermooSeason> getCurrentSeason(Level world) {
        return ThermooSeasonEvents.GET_CURRENT_SEASON.invoker().getCurrentSeason(world);
    }

    /**
     * Shorthand for invoking {@link ThermooSeasonEvents#GET_CURRENT_TROPICAL_SEASON}.
     * <p>
     * Retrieves the current tropical season at a position in the world. If the position queried is not in a tropical
     * biome, or a seasons mod is not loaded, then empty should be returned.
     * <p>
     * If any listener returns a non-empty season, then all further processing is cancelled and that season is returned.
     * <p>
     * Returns empty by default.
     *
     * @param world The world to query
     * @param pos   The position in the world to query
     * @return If the queried pos is a tropical area and a seasons mod is loaded, returns one of {@link #TROPICAL_DRY} or
     * {@link #TROPICAL_WET}
     * @see #getCurrentSeason(Level) for the standard 'temperate' seasons
     */
    public static Optional<ThermooSeason> getCurrentTropicalSeason(Level world, BlockPos pos) {
        return ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.invoker().getCurrentTropicalSeason(world, pos);
    }

    /**
     * @return Returns true if this season is {@link #TROPICAL_DRY} or {@link #TROPICAL_WET}
     */
    public boolean isTropical() {
        return isTropical;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

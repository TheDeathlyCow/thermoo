package com.github.thedeathlycow.thermoo.api.season;

import net.minecraft.world.World;

import java.util.Optional;

/**
 * Mod-agnostic Seasons enum for any Thermoo dependencies.
 */
public enum ThermooSeasons {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER;

    /**
     * Shorthand for invoking {@link ThermooSeasonEvents#GET_CURRENT_SEASON}.
     * <p>
     * Retrieves the current season, if a season mod is loaded. Thermoo does not add seasons by itself, seasons must be
     * implemented by another mod like Fabric Seasons or Serene Seasons. This event just places season integration into
     * a common source.
     *
     * @param world The current world / level to get the season from.
     * @return Returns the current season if a Seasons mod is installed, or empty if no seasons mod is installed.
     */
    public static Optional<ThermooSeasons> getCurrentSeason(World world) {
        return ThermooSeasonEvents.GET_CURRENT_SEASON.invoker().getCurrentSeason(world);
    }

}

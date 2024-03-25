package com.github.thedeathlycow.thermoo.api.season;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Events related to Seasons mod integration in Thermoo. Thermoo does not add seasonal functionality by itself, seasons
 * must be implemented by another mod like Fabric Seasons or Serene Seasons. This only provides the ability to query
 * seasons if you want to use them.
 */
public class ThermooSeasonEvents {

    /**
     * Retrieves the current season. This event just places season integration into
     * a common source.
     * <p>
     * If any listener returns a non-empty season, then all further processing is cancelled and that season is returned.
     * <p>
     * Returns empty by default.
     */
    public static final Event<CurrentSeasonCallback> GET_CURRENT_SEASON = EventFactory.createArrayBacked(
            CurrentSeasonCallback.class,
            callbacks -> world -> {
                for (CurrentSeasonCallback callback : callbacks) {
                    Optional<ThermooSeasons> season = callback.getCurrentSeason(world);
                    if (season.isPresent()) {
                        return season;
                    }
                }

                return Optional.empty();
            }
    );

    @FunctionalInterface
    public interface CurrentSeasonCallback {

        Optional<ThermooSeasons> getCurrentSeason(World world);

    }

}

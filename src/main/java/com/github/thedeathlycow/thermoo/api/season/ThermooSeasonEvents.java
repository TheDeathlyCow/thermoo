package com.github.thedeathlycow.thermoo.api.season;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Events related to Seasons mod integration in Thermoo
 */
public class ThermooSeasonEvents {

    /**
     * Retrieves the current season, if a season mod is loaded. Thermoo does not add seasons by itself, seasons must be
     * implemented by another mod like Fabric Seasons or Serene Seasons. This event just places season integration into
     * a common source.
     * <p>
     * Returns empty if no season mod is installed.
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

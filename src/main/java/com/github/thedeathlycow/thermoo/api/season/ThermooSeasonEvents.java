package com.github.thedeathlycow.thermoo.api.season;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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
     * 
     * @see ThermooSeason#getCurrentSeason(Level) 
     */
    public static final Event<CurrentSeasonCallback> GET_CURRENT_SEASON = EventFactory.createArrayBacked(
            CurrentSeasonCallback.class,
            callbacks -> world -> {
                for (CurrentSeasonCallback callback : callbacks) {
                    Optional<ThermooSeason> season = callback.getCurrentSeason(world);
                    if (season.isPresent() && !season.get().isTropical()) {
                        return season;
                    }
                }

                return Optional.empty();
            }
    );

    /**
     * Retrieves the current tropical season at a positive in the world. If the position queried is not in a tropical
     * biome, or a seasons mod is not loaded, then empty should be returned.
     * <p>
     * If any listener returns a non-empty season, then all further processing is cancelled and that season is returned.
     * <p>
     * Returns empty by default.
     * 
     * @see ThermooSeason#getCurrentTropicalSeason(Level, BlockPos)
     */
    public static final Event<CurrentTropicalSeasonCallback> GET_CURRENT_TROPICAL_SEASON = EventFactory.createArrayBacked(
            CurrentTropicalSeasonCallback.class,
            callbacks -> (world, pos) -> {
                for (CurrentTropicalSeasonCallback callback : callbacks) {
                    Optional<ThermooSeason> season = callback.getCurrentTropicalSeason(world, pos);
                    if (season.isPresent() && season.get().isTropical()) {
                        return season;
                    }
                }

                return Optional.empty();
            }
    );

    @FunctionalInterface
    public interface CurrentSeasonCallback {
        Optional<ThermooSeason> getCurrentSeason(Level world);
    }

    @FunctionalInterface
    public interface CurrentTropicalSeasonCallback {
        Optional<ThermooSeason> getCurrentTropicalSeason(Level world, BlockPos pos);
    }
}

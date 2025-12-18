package com.github.thedeathlycow.thermoo.api.season;

import com.github.thedeathlycow.thermoo.api.environment.attribute.ThermooEnvironmentAttributes;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Events related to Seasons in Thermoo. Note that Thermoo will not provide any seasons mod functionality by itself,
 * that must be provided by an external seasons mod. This is primarily intended to be used for mod-agnostic seasons mod
 * integration.
 */
public final class ThermooSeasonEvents {
    private ThermooSeasonEvents() {

    }

    /**
     * Retrieves the current temperate season at a position in a level, if a season mod is loaded. Thermoo does not add
     * seasons by itself, seasons must be implemented by another mod like Fabric Seasons or Serene Seasons. This event
     * just places season integration into a common source.
     * <p>
     * If any listener returns a non-empty season, then all further processing is cancelled and that season is returned.
     * <p>
     * If the queried position does not have seasons, or a seasons mod is not installed, then returns the environment
     * attribute value of {@link ThermooEnvironmentAttributes#TEMPERATE_SEASON}.
     * 
     * @see TemperateSeason#getCurrentSeason(Level, BlockPos)
     * @see #GET_CURRENT_TROPICAL_SEASON
     */
    public static final Event<CurrentSeasonCallback<TemperateSeason>> GET_CURRENT_SEASON = EventFactory.createArrayBacked(
            CurrentSeasonCallback.class,
            callbacks -> (level, pos) -> {
                for (CurrentSeasonCallback<TemperateSeason> callback : callbacks) {
                    Optional<ThermooSeasonState<TemperateSeason>> season = callback.getCurrentSeason(level, pos);
                    if (season.isPresent()) {
                        return season;
                    }
                }

                return level.environmentAttributes()
                        .getValue(ThermooEnvironmentAttributes.TEMPERATE_SEASON, pos);
            }
    );

    /**
     * Retrieves the current tropical season at a position in a level, if a season mod is loaded. Thermoo does not add
     * seasons by itself, seasons must be implemented by another mod like Fabric Seasons or Serene Seasons. This event
     * just places season integration into a common source.
     * <p>
     * If any listener returns a non-empty season, then all further processing is cancelled and that season is returned.
     * <p>
     * If the queried position is not tropical, or a seasons mod is not installed, then returns the environment
     * attribute value of {@link ThermooEnvironmentAttributes#TROPICAL_SEASON}.
     * 
     * @see TropicalSeason#getCurrentSeason(Level, BlockPos)
     * @see #GET_CURRENT_SEASON
     */
    public static final Event<CurrentSeasonCallback<TropicalSeason>> GET_CURRENT_TROPICAL_SEASON = EventFactory.createArrayBacked(
            CurrentSeasonCallback.class,
            callbacks -> (level, pos) -> {
                for (CurrentSeasonCallback<TropicalSeason> callback : callbacks) {
                    Optional<ThermooSeasonState<TropicalSeason>> season = callback.getCurrentSeason(level, pos);
                    if (season.isPresent()) {
                        return season;
                    }
                }

                return level.environmentAttributes()
                        .getValue(ThermooEnvironmentAttributes.TROPICAL_SEASON, pos);
            }
    );

    @FunctionalInterface
    public interface CurrentSeasonCallback<S extends ThermooSeason> {
        Optional<ThermooSeasonState<S>> getCurrentSeason(Level level, BlockPos pos);
    }
}

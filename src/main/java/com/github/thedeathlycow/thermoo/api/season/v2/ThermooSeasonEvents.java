package com.github.thedeathlycow.thermoo.api.season.v2;

import com.github.thedeathlycow.thermoo.api.environment.v2.attribute.ThermooEnvironmentAttributes;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
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
     * Retrieves the current temperate season state at a position in a level, if a season mod is loaded. Thermoo does
     * not add seasons by itself, seasons must be implemented by another mod like Fabric Seasons or Serene Seasons. This
     * event just places season integration into a common source.
     * <p>
     * If any listener returns a non-empty season state, then all further processing is cancelled and that state is
     * returned.
     * <p>
     * If the queried position does not have seasons, or a seasons mod is not installed, then returns a state based
     * on the current value of the {@linkplain ThermooEnvironmentAttributes environment attributes}.
     *
     * @see TemperateSeason#getCurrentState(Level, BlockPos)
     * @see #GET_CURRENT_TROPICAL_SEASON
     */
    public static final Event<CurrentSeasonCallback<TemperateSeason>> GET_CURRENT_SEASON = EventFactory.createArrayBacked(
            CurrentSeasonCallback.class,
            callbacks -> (level, pos) -> {
                for (CurrentSeasonCallback<TemperateSeason> callback : callbacks) {
                    Optional<ThermooSeasonState<TemperateSeason>> season = callback.getCurrentSeasonState(level, pos);
                    if (season.isPresent()) {
                        return season;
                    }
                }

                EnvironmentAttributeSystem attributes = level.environmentAttributes();

                return attributes.getValue(ThermooEnvironmentAttributes.TEMPERATE_SEASON, pos)
                        .map(season -> {
                            float progress = attributes.getDimensionValue(ThermooEnvironmentAttributes.TEMPERATE_SEASON_PROGRESS);
                            return ThermooSeasonState.of(season, progress);
                        });
            }
    );

    /**
     * Retrieves the current tropical season state at a position in a level, if a season mod is loaded. Thermoo does not
     * add seasons by itself, seasons must be implemented by another mod like Fabric Seasons or Serene Seasons. This
     * event just places season integration into a common source.
     * <p>
     * If any listener returns a non-empty season state, then all further processing is cancelled and that state is
     * returned.
     * <p>
     * If the queried position does not have seasons, or a seasons mod is not installed, then returns a state based
     * on the current value of the {@linkplain ThermooEnvironmentAttributes environment attributes}.
     *
     * @see TropicalSeason#getCurrentState(Level, BlockPos)
     * @see #GET_CURRENT_SEASON
     */
    public static final Event<CurrentSeasonCallback<TropicalSeason>> GET_CURRENT_TROPICAL_SEASON = EventFactory.createArrayBacked(
            CurrentSeasonCallback.class,
            callbacks -> (level, pos) -> {
                for (CurrentSeasonCallback<TropicalSeason> callback : callbacks) {
                    Optional<ThermooSeasonState<TropicalSeason>> season = callback.getCurrentSeasonState(level, pos);
                    if (season.isPresent()) {
                        return season;
                    }
                }

                EnvironmentAttributeSystem attributes = level.environmentAttributes();

                return attributes.getValue(ThermooEnvironmentAttributes.TROPICAL_SEASON, pos)
                        .map(season -> {
                            float progress = attributes.getDimensionValue(ThermooEnvironmentAttributes.TROPICAL_SEASON_PROGRESS);
                            return ThermooSeasonState.of(season, progress);
                        });
            }
    );

    @FunctionalInterface
    public interface CurrentSeasonCallback<S extends ThermooSeason> {
        Optional<ThermooSeasonState<S>> getCurrentSeasonState(Level level, BlockPos pos);
    }
}

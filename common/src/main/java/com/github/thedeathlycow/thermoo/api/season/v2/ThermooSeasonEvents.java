package com.github.thedeathlycow.thermoo.api.season.v2;

import com.github.thedeathlycow.thermoo.api.environment.v2.attribute.ThermooEnvironmentAttributes;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.commons.TriState;
import dev.yumi.commons.event.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

/// Events related to Seasons in Thermoo. Note that Thermoo will not provide any seasons mod functionality by itself,
/// that must be provided by an external seasons mod. This is primarily intended to be used for mod-agnostic seasons mod
/// integration.
public final class ThermooSeasonEvents {
    private ThermooSeasonEvents() {

    }

    /// Retrieves the current temperate season state at a position in a level, if a season mod is loaded. Thermoo does
    /// not add seasons by itself, seasons must be implemented by another mod like Fabric Seasons or Serene Seasons. This
    /// event just places season integration into a common source.
    ///
    /// If any listener returns a non-empty season state, then all further processing is cancelled and that state is
    /// returned.
    ///
    /// If the queried position does not have seasons, or a seasons mod is not installed, then returns a state based
    /// on the current value of the {@linkplain ThermooEnvironmentAttributes environment attributes}.
    ///
    /// @see TemperateSeason#getCurrentState(Level, BlockPos)
    /// @see #GET_CURRENT_TROPICAL_SEASON
    public static final Event<Identifier, CurrentSeasonCallback<TemperateSeason>> GET_CURRENT_SEASON = Thermoo.EVENT_MANAGER.create(
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

    /// Retrieves the current tropical season state at a position in a level, if a season mod is loaded. Thermoo does not
    /// add seasons by itself, seasons must be implemented by another mod like Fabric Seasons or Serene Seasons. This
    /// event just places season integration into a common source.
    ///
    /// If any listener returns a non-empty season state, then all further processing is cancelled and that state is
    /// returned.
    ///
    /// If the queried position does not have seasons, or a seasons mod is not installed, then returns a state based
    /// on the current value of the {@linkplain ThermooEnvironmentAttributes environment attributes}.
    ///
    /// @see TropicalSeason#getCurrentState(Level, BlockPos)
    /// @see #GET_CURRENT_SEASON
    public static final Event<Identifier, CurrentSeasonCallback<TropicalSeason>> GET_CURRENT_TROPICAL_SEASON = Thermoo.EVENT_MANAGER.create(
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

    /// A season-aware hook for checking if an area can be snowy.
    ///
    /// Snowy checks can be fickle and inconsistent between seasons mods, this provides a single method that will
    /// determine if a seasons mod thinks it is cold enough to snow.
    ///
    /// This is not a substitute for vanilla checks! A default return indicates that callers should rely upon vanilla
    /// checks instead. It is not defined exactly when this event will return default
    ///
    /// - Returns [true][TriState#TRUE] if any listener thinks the area is definitely snowy.
    /// - Returns [false][TriState#FALSE] if any listener think the area definitely is not snowy.
    /// - Returns [default][TriState#DEFAULT] if no listener believes the area must be snowy or not snowy, and callers
    /// should fall back to vanilla processing.
    public static final Event<Identifier, ColdEnoughToSnow> IS_COLD_ENOUGH_TO_SNOW = Thermoo.EVENT_MANAGER.create(
            ColdEnoughToSnow.class,
            listeners -> (level, pos, biome) -> {
                for (ColdEnoughToSnow listener : listeners) {
                    TriState isSnowy = listener.isColdEnoughToSnow(level, pos, biome);
                    if (isSnowy != TriState.DEFAULT) {
                        return isSnowy;
                    }
                }

                return TriState.DEFAULT;
            }
    );

    @FunctionalInterface
    public interface CurrentSeasonCallback<S extends ThermooSeason> {
        Optional<ThermooSeasonState<S>> getCurrentSeasonState(Level level, BlockPos pos);
    }

    @FunctionalInterface
    public interface ColdEnoughToSnow {
        TriState isColdEnoughToSnow(Level level, BlockPos pos, Holder<Biome> biome);
    }
}

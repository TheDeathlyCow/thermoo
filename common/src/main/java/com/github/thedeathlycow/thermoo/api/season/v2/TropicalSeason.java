package com.github.thedeathlycow.thermoo.api.season.v2;

import com.github.thedeathlycow.thermoo.api.environment.v2.attribute.ThermooEnvironmentAttributes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Enumeration for the three tropical seasons. Note that Thermoo will not provide any seasons mod
 * functionality by itself, that must be provided by an external seasons mod. This is primarily intended to be used for
 * mod-agnostic seasons mod integration.
 * @see TemperateSeason
 */
public enum TropicalSeason implements ThermooSeason {
    DRY("dry"),
    WET("wet"),
    MILD("mild");

    public static final Codec<TropicalSeason> CODEC = StringRepresentable.fromEnum(TropicalSeason::values);

    private final String name;

    TropicalSeason(String name) {
        this.name = name;
    }


    @Override
    public String getSerializedName() {
        return this.name;
    }

    /**
     * Shorthand for invoking {@link ThermooSeasonEvents#GET_CURRENT_TROPICAL_SEASON}.
     * <p>
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
     * @see ThermooSeasonEvents#GET_CURRENT_TROPICAL_SEASON
     * @see TemperateSeason#getCurrentState(Level, BlockPos)
     */
    public static Optional<ThermooSeasonState<TropicalSeason>> getCurrentState(Level level, BlockPos pos) {
        return ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.invoker().getCurrentSeasonState(level, pos);
    }

    @Override
    public ThermooSeasonState<TropicalSeason> createState() {
        return ThermooSeasonState.of(this);
    }
}
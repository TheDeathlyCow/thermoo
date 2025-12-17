package com.github.thedeathlycow.thermoo.api.season;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Enumeration for the two tropical seasons. Note that Thermoo will not provide any seasons mod
 * functionality by itself, that must be provided by an external seasons mod. This is primarily intended to be used for
 * mod-agnostic seasons mod integration.
 * @see TemperateSeason
 */
public enum TropicalSeason implements ThermooSeason<TropicalSeason> {
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
     * Retrieves the current tropical season at a position in a level, if a season mod is loaded. Thermoo does not add
     * seasons by itself, seasons must be implemented by another mod like Fabric Seasons or Serene Seasons. This event
     * just places season integration into a common source.
     * <p>
     * If any listener returns a non-empty season, then all further processing is cancelled and that season is returned.
     * <p>
     * If the queried position is not tropical, or a seasons mod is not installed, then returns empty.
     *
     * @param level The current world / level to get the season from.
     * @return Returns the current season if a Seasons mod is installed, or empty if no seasons mod is installed.
     * @see TemperateSeason to get the standard 'temperate' season
     */
    public static Optional<ThermooSeasonState<TropicalSeason>> getCurrentSeason(Level level, BlockPos pos) {
        return ThermooSeasonEvents.GET_CURRENT_TROPICAL_SEASON.invoker().getCurrentSeason(level, pos);
    }

    @Override
    public ThermooSeasonState<TropicalSeason> createState() {
        return ThermooSeasonState.of(this);
    }
}
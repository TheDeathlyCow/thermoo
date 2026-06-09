package com.github.thedeathlycow.thermoo.api.season.v2;

import dev.yumi.commons.TriState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

/// Contains common functionality for all thermoo season types.
///
/// @see TemperateSeason
/// @see TropicalSeason
@ApiStatus.NonExtendable
public sealed interface ThermooSeason extends StringRepresentable permits TemperateSeason, TropicalSeason {
    /// Creates a season state that represents the start of this season.
    ThermooSeasonState<? extends ThermooSeason> createState();

    /// Convenience method for invoking [ThermooSeasonEvents#IS_COLD_ENOUGH_TO_SNOW]
    static TriState isColdEnoughToSnow(Level level, BlockPos pos) {
        Holder<Biome> biome = level.getBiomeManager().getNoiseBiomeAtPosition(pos);
        return ThermooSeasonEvents.IS_COLD_ENOUGH_TO_SNOW.invoker().isColdEnoughToSnow(level, pos, biome);
    }
}
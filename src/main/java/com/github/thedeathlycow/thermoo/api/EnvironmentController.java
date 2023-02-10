package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.EnvironmentControllerImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implements computations for various passive environmental effects, such as passive freezing/warming from biomes,
 * and passive warming from heat sources (torches, campfires, etc).
 *
 * The default implementation is provided by {@link EnvironmentController#INSTANCE}, and what is used by Frostiful,
 * however if you wish you may re-implement this class for your own mod.
 */
public interface EnvironmentController {

    /**
     * The default controller used by the Frostiful family of mods.
     */
    EnvironmentController INSTANCE = new EnvironmentControllerImpl();

    /**
     * Computes the temperature delta of a {@link TemperatureAware} player for this tick.
     * This includes the {@link EnvironmentController#getLocalTemperatureDelta} and {@link EnvironmentController#getSoakedFreezingMultiplier(Soakable)},
     * but not {@link EnvironmentController#getWarmthFromHeatSources(LivingEntity)}.
     *
     * @param player The player
     * @return Returns the change in temperature of the player for the tick
     */
    int getPassiveTemperatureDelta(PlayerEntity player);

    /**
     * Computes the local temperature delta at a given position in a world.
     *
     * @param world The world
     * @param pos   The position in that world
     * @return The passive temperature delta at {@code pos} in {@code world}.
     */
    int getLocalTemperatureDelta(World world, BlockPos pos);

    /**
     * Gets the passive warmth that should be applied to a {@link TemperatureAware} entity from nearby heat sources
     *
     * @param entity The entity to compute the warmth of
     * @return Returns the temperature delta that should be applied from nearby temperature sources.
     */
    int getWarmthFromHeatSources(LivingEntity entity);

    /**
     * Computes passive warmth for {@link TemperatureAware} entities on fire, and ticks effects.
     *
     * @param entity The entity that may be on fire
     * @return Returns the temperature delta that should be applied from being on fire.
     */
    int tickWarmthOnFire(LivingEntity entity);

    /**
     * Gets the wetness increase for a {@link Soakable} player this tick
     *
     * @param player The player to compute increase for
     * @return Returns the soaking delta for the player.
     */
    int getSoakChangeForPlayer(PlayerEntity player);

    /**
     * Computes how much a {@link Soakable}'s passive <b>freezing</b> should be increased by
     * with respect to their wetness. Only relevant if the Soakable is also a {@link TemperatureAware}.
     *
     * @param soakable The soakable
     * @return Returns the multiplier for passive freezing based on the wetness.
     */
    float getSoakedFreezingMultiplier(Soakable soakable);
}

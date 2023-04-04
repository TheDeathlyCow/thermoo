package com.github.thedeathlycow.thermoo.api.temperature;

import com.github.thedeathlycow.thermoo.impl.EnvironmentControllerImpl;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implements computations for various passive environmental effects, such as passive freezing/warming from biomes,
 * and passive warming from heat sources (torches, campfires, etc).
 * <p>
 * The default implementation is provided by {@link EnvironmentManager#getController()}, and what is used by Frostiful,
 * however if you wish you may re-implement this class for your own mod.
 */
public interface EnvironmentController {

    /**
     * Computes the local temperature change at a given position in a world.
     *
     * @param world The world
     * @param pos   The position in that world
     * @return The passive temperature change at {@code pos} in {@code world}.
     */
    int getLocalTemperatureChange(World world, BlockPos pos);

    /**
     * Gets the passive warmth that should be applied to a {@link TemperatureAware} from nearby heat sources. Temperature
     * awares are only given warmth from these sources if they are already cold.
     *
     *
     * @param temperatureAware The temperature aware to compute the warmth of
     * @param world            The world the temperature aware is in
     * @param pos              The position to check
     * @return Returns the temperature change that should be applied from nearby temperature sources.
     * @see EnvironmentController#getHeatAtLocation(World, BlockPos)
     */
    int getWarmthFromHeatSources(TemperatureAware temperatureAware, World world, BlockPos pos);

    /**
     * Computes passive warmth for {@link TemperatureAware} entities on fire. If the entity is not on fire,
     * this will return 0.
     *
     * @param entity The entity to tick warm from other sources
     * @return Returns the temperature change that should be applied
     */
    int getOnFireWarmthRate(LivingEntity entity);

    /**
     * Gets the wetness increase for a {@link Soakable} player this tick
     *
     * @param entity The player to compute increase for
     * @return Returns the soaking change for the player.
     */
    int getSoakChange(LivingEntity entity);

    /**
     * Calculates the passive warmth nearby heat sources at a location in a world.
     * 'Heat sources' being things that exist in the world that produce heat around them. By default, this includes
     * most artificial light producing sources, such as torches, campfires, lit furnaces, glowstone, and more.
     *
     * @param world            The world the temperature aware is in
     * @param pos              The position to check
     * @return Returns the temperature change that should be applied from nearby temperature sources.
     */
    int getHeatAtLocation(World world, BlockPos pos);

    /**
     * Calculates the heat produced by a block state.
     *
     * @param state The block state heat source
     * @return The warmth that the state produces around it
     */
    int getHeatFromBlockState(BlockState state);

    /**
     * Checks if a block state is a heat source, as defined by this controller
     *
     * @param state The block state to check
     * @return Returns if a block state is a heat source
     * @see EnvironmentController#getHeatFromBlockState(BlockState)
     */
    boolean isHeatSource(BlockState state);

    /**
     * Check if a position in a world is heated
     *
     * @param world The world of the position
     * @param pos   The position to check
     * @return Returns if the location in the world is heated
     * @see EnvironmentController#getWarmthFromHeatSources(TemperatureAware, World, BlockPos)
     * @see EnvironmentController#getHeatAtLocation(World, BlockPos)
     */
    boolean isAreaHeated(World world, BlockPos pos);


}

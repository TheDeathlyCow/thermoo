package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import oshi.annotation.concurrent.Immutable;

/**
 * Implements computations for various passive environmental effects, such as passive freezing/warming from biomes,
 * and passive warming from heat sources (torches, campfires, etc).
 * <p>
 * The default implementation is provided by {@link EmptyEnvironmentController} which sets all values to either 0, false,
 * or null by default. However, if you wish you may extend (or even replace!) the functionality of the default controller
 * by use of the {@link EnvironmentControllerDecorator}. It is best to do this through the initialize event in
 * {@link com.github.thedeathlycow.thermoo.api.temperature.event.EnvironmentControllerInitializeEvent}
 *
 * @see EnvironmentControllerDecorator
 * @see EmptyEnvironmentController
 */
@Immutable
public sealed interface EnvironmentController permits EnvironmentControllerDecorator, EmptyEnvironmentController {

    /**
     * Gets the controller that this controller decorates. If this controller is a leaf (not a decorator), then returns
     * null.
     *
     * @return Returns the environment controller that this controller decorates. Returns null if this decorates no
     * controller (i.e., is a leaf)
     */
    @Nullable
    default EnvironmentController getDecorated() {
        return null;
    }

    /**
     * Computes the local temperature change from the environment at a given position in a world.
     *
     * @param world The world
     * @param pos   The position in that world
     * @return The passive temperature change at {@code pos} in {@code world}.
     */
    int getLocalTemperatureChange(World world, BlockPos pos);

    /**
     * Computes the environmental temperature change for a player, based on a local temperature computed from
     * {@link #getLocalTemperatureChange(World, BlockPos)}
     *
     * @param player           The player to compute the temperature change for
     * @param localTemperature The base local temperature
     * @return Returns the passive environmental temperature change for the player this tick
     */
    int getEnvironmentTemperatureForPlayer(PlayerEntity player, int localTemperature);

    /**
     * Computes temperature changes for {@link LivingEntity}s from heat effects. For example, being on fire or freezing
     * in powder snow
     *
     * @param entity The entity to tick warmth effects for
     * @return Returns the temperature change that should be applied
     */
    int getTemperatureEffectsChange(LivingEntity entity);

    /**
     * Gets the amount of warmth generated by a floor block state.
     * <p>
     * Hot floor is different from {@link #getHeatFromBlockState(BlockState)}, as it ONLY applies to entities stepping on
     * the block - it does not affect the area around the block. An example implementation would be to provide warmth from
     * {@link net.minecraft.block.Blocks#MAGMA_BLOCK}, but not provide area heat.
     * <p>
     * You can also use this for blocks that are cold to step on.
     *
     * @param state The state of the floor.
     * @return Returns the heat to apply each tick to entities standing on the block state
     */
    int getFloorTemperature(BlockState state);

    /**
     * Gets the wetness increase for a {@link Soakable} this tick
     *
     * @param soakable The soakable to compute increase for
     * @return Returns the soaking change for the player.
     */
    int getSoakChange(Soakable soakable);

    /**
     * Calculates the passive warmth nearby heat sources at a location in a world.
     * 'Heat sources' being things that exist in the world that produce heat around them. By default, this includes
     * most artificial light producing sources, such as torches, campfires, lit furnaces, glowstone, and more.
     *
     * @param world The world the temperature aware is in
     * @param pos   The position to check
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
     * @see EnvironmentController#getHeatAtLocation(World, BlockPos)
     */
    boolean isAreaHeated(World world, BlockPos pos);


}

package com.github.thedeathlycow.thermoo.api;

import com.github.thedeathlycow.thermoo.impl.EnvironmentControllerImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implements computations for various passive environmental effects, such as passive freezing/warming from biomes,
 * and passive warming from heat sources (torches, campfires, etc).
 * <p>
 * The default implementation is provided by {@link EnvironmentController#INSTANCE}, and what is used by Frostiful,
 * however if you wish you may re-implement this class for your own mod.
 */
public interface EnvironmentController {

    /**
     * The default controller used by the Frostiful family of mods.
     */
    EnvironmentController INSTANCE = new EnvironmentControllerImpl();


    /**
     * Ticks the entity once. Ensures that entities are not ticked twice in the same server tick.
     * It is recommended that this method from a mixin to {@link LivingEntity#tick}
     *
     * @param entity The entity to tick
     */
    void tickEntity(LivingEntity entity);

    /**
     * Ticks the player once. Ensures that player soaking is not ticked twice; however their temperature may be ticked
     * twice depending on the behaviour of {@code doPassiveChange}.
     * It is recommended that this method from a mixin to {@link PlayerEntity#tick}
     * <p>
     * Note that {@link EnvironmentController#tickEntity(LivingEntity)} should also be called on the player.
     *
     * @param player          The player to tick
     * @param doPassiveChange Predicate that determines if the passive change should be applied.
     */
    void tickPlayer(PlayerEntity player, ChangeTemperaturePredicate doPassiveChange);

    /**
     * Computes the temperature change of a {@link TemperatureAware} player for this tick.
     * The returned value includes local biome temperature change with the soaked increase, but does not include the change
     * from heat sources.
     *
     * @param player The player
     * @return Returns the change in temperature of the player for the tick
     */
    int getPassiveTemperatureChange(PlayerEntity player);

    /**
     * Computes the local temperature change at a given position in a world.
     *
     * @param world The world
     * @param pos   The position in that world
     * @return The passive temperature change at {@code pos} in {@code world}.
     */
    int getLocalTemperatureChange(World world, BlockPos pos);

    /**
     * Gets the passive warmth that should be applied to a {@link TemperatureAware} entity from nearby heat sources
     *
     * @param entity The entity to compute the warmth of
     * @return Returns the temperature change that should be applied from nearby temperature sources.
     */
    int getWarmthFromHeatSources(LivingEntity entity);

    /**
     * Computes passive warmth for {@link TemperatureAware} entities on fire, and ticks effects.
     *
     * @param entity The entity that may be on fire
     * @return Returns the temperature change that should be applied from being on fire.
     */
    int tickWarmthOnFire(LivingEntity entity);

    /**
     * Gets the wetness increase for a {@link Soakable} player this tick
     *
     * @param player The player to compute increase for
     * @return Returns the soaking change for the player.
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

    /**
     * Callback for whether to apply passive temperature change to a player
     */
    @FunctionalInterface
    interface ChangeTemperaturePredicate {

        /**
         * Test if the player should have their temperature changed passive.
         *
         * @param player            The player to test
         * @param world             The world of the player
         * @param temperatureChange The temperature change to be applied
         * @param alreadyTicked     Whether {@link EnvironmentController#tickPlayer(PlayerEntity, ChangeTemperaturePredicate)}
         *                          has already been called this tick
         * @return Returns if the player should have their temperature changed passive
         */
        boolean test(PlayerEntity player, World world, int temperatureChange, boolean alreadyTicked);

    }


}

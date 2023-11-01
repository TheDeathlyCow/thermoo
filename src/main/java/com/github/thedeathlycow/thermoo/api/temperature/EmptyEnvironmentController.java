package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The most basic concrete implementation of the environment controller. All methods return either {@code 0},
 * {@code false}, or {@code null}.
 */
public final class EmptyEnvironmentController implements EnvironmentController {

    /**
     * Package-private constructor for the controller
     */
    EmptyEnvironmentController() {

    }

    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        return 0;
    }

    @Override
    public double getBaseValueForAttribute(EntityAttribute attribute, LivingEntity entity) {
        return 0;
    }

    @Override
    public int getEnvironmentTemperatureForPlayer(PlayerEntity player, int localTemperature) {
        return 0;
    }

    @Override
    public int getTemperatureEffectsChange(LivingEntity entity) {
        return 0;
    }

    @Override
    public int getFloorTemperature(LivingEntity entity, World world, BlockState state, BlockPos pos) {
        return 0;
    }

    @Override
    public int getSoakChange(Soakable soakable) {
        return 0;
    }

    @Override
    public int getHeatAtLocation(World world, BlockPos pos) {
        return 0;
    }

    @Override
    public int getHeatFromBlockState(BlockState state) {
        return 0;
    }

    @Override
    public boolean isHeatSource(BlockState state) {
        return false;
    }

    @Override
    public boolean isColdSource(BlockState state) {
        return false;
    }

    @Override
    public boolean isAreaHeated(World world, BlockPos pos) {
        return false;
    }

    /**
     * @return Returns the name of the class as the string representation
     */
    @Override
    public String toString() {
        return this.getClass().getName();
    }
}

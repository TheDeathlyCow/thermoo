package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Decorator for the {@link EnvironmentController}, to be extended by clients for the purpose of adding or replacing
 * functionality from the default environment controller.
 */
public abstract non-sealed class EnvironmentControllerDecorator implements EnvironmentController {

    protected final EnvironmentController controller;

    protected EnvironmentControllerDecorator(EnvironmentController controller) {

        if (controller == null) {
            throw new IllegalArgumentException("The base controller for the decorator may not be null!");
        }

        this.controller = controller;
    }

    @Override
    @NotNull
    public final EnvironmentController getDecorated() {
        return this.controller;
    }

    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        return controller.getLocalTemperatureChange(world, pos);
    }

    @Override
    public int getTemperatureEffectsChange(LivingEntity entity) {
        return controller.getTemperatureEffectsChange(entity);
    }

    @Override
    public int getFloorTemperature(BlockState state) {
        return controller.getFloorTemperature(state);
    }

    @Override
    public int getSoakChange(Soakable soakable) {
        return controller.getSoakChange(soakable);
    }

    @Override
    public int getHeatAtLocation(World world, BlockPos pos) {
        return controller.getHeatAtLocation(world, pos);
    }

    @Override
    public int getHeatFromBlockState(BlockState state) {
        return controller.getHeatFromBlockState(state);
    }

    @Override
    public boolean isHeatSource(BlockState state) {
        return controller.isHeatSource(state);
    }

    @Override
    public boolean isAreaHeated(World world, BlockPos pos) {
        return controller.isAreaHeated(world, pos);
    }

    /**
     * Wraps the class name around the string representation of the {@link #controller} so that it is easy to see which mods
     * are decorating the Thermoo environment controller
     * <p>
     * Subclasses may not override this method any further.
     *
     * @return Returns the class name of this class, and the string representation of the {@link #controller}
     */
    @Override
    public final String toString() {
        return String.format("%s{%s}", this.getClass().getName(), this.controller.toString());
    }
}

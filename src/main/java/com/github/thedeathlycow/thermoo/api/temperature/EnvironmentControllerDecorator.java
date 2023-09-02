package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Decorator for the {@link EnvironmentController}, to be extended by other mods for the purpose of adding or replacing
 * functionality from the default environment controller.
 * <p>
 * Every method by default delegates to the base {@link #controller}
 */
public abstract non-sealed class EnvironmentControllerDecorator implements EnvironmentController {

    /**
     * The base controller to decorate with new functionality. This field may never be {@code null}
     */
    @NotNull
    protected final EnvironmentController controller;

    /**
     * Constructs a decorator out of a base controller
     *
     * @param controller The base {@link #controller}
     */
    protected EnvironmentControllerDecorator(EnvironmentController controller) {

        if (controller == null) {
            throw new IllegalArgumentException("The base controller for the decorator may not be null!");
        }

        this.controller = controller;
    }

    /**
     * Getter for the base controller
     *
     * @return Returns the decorated base controller
     */
    @Override
    @NotNull
    public final EnvironmentController getDecorated() {
        return this.controller;
    }

    @Override
    public double getBaseValueForAttribute(EntityAttribute attribute, LivingEntity entity) {
        return controller.getBaseValueForAttribute(attribute, entity);
    }

    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        return controller.getLocalTemperatureChange(world, pos);
    }

    @Override
    public int getEnvironmentTemperatureForPlayer(PlayerEntity player, int localTemperature) {
        return controller.getEnvironmentTemperatureForPlayer(player, localTemperature);
    }

    @Override
    public int getTemperatureEffectsChange(LivingEntity entity) {
        return controller.getTemperatureEffectsChange(entity);
    }

    @Override
    public int getFloorTemperature(LivingEntity entity, World world, BlockState state, BlockPos pos) {
        return controller.getFloorTemperature(entity, world, state, pos);
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

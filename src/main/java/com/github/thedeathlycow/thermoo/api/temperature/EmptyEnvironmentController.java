package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The most basic concrete implementation of the environment controller. All methods return either {@code 0},
 * {@code false}, or {@code null}.
 *
 * @deprecated Replaced with {@linkplain com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition the environment datapack registry}
 */
@Deprecated
public final class EmptyEnvironmentController implements EnvironmentController {

    /**
     * Package-private constructor for the controller
     */
    EmptyEnvironmentController() {

    }

    @Override
    public int getLocalTemperatureChange(Level world, BlockPos pos) {
        return 0;
    }

    @Override
    @Deprecated
    public double getBaseValueForAttribute(Holder<Attribute> attribute, LivingEntity entity) {
        return 0;
    }

    @Override
    public int getTemperatureEffectsChange(LivingEntity entity) {
        return 0;
    }

    @Override
    public int getFloorTemperature(LivingEntity entity, Level world, BlockState state, BlockPos pos) {
        return 0;
    }

    @Override
    public int getSoakChange(Soakable soakable) {
        return 0;
    }

    @Override
    public int getHeatAtLocation(Level world, BlockPos pos) {
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
    public boolean isAreaHeated(Level world, BlockPos pos) {
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

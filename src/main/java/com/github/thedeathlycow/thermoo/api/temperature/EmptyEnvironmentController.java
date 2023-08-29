package com.github.thedeathlycow.thermoo.api.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class EmptyEnvironmentController implements EnvironmentController {
    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        return 0;
    }

    @Override
    public int getTemperatureEffectsChange(LivingEntity entity) {
        return 0;
    }

    @Override
    public int getFloorTemperature(BlockState state) {
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
    public boolean isAreaHeated(World world, BlockPos pos) {
        return false;
    }
}

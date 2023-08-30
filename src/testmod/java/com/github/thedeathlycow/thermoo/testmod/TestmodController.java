package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class TestmodController extends EnvironmentControllerDecorator {
    /**
     * Constructs a decorator out of a base controller
     *
     * @param controller The base {@link #controller}
     */
    public TestmodController(EnvironmentController controller) {
        super(controller);
    }

    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        if (world.getDimension().natural()) {
            Biome biome = world.getBiome(pos).value();
            float temperature = biome.getTemperature();
            return this.getTempChangeFromBiomeTemperature(
                    world,
                    temperature,
                    !biome.hasPrecipitation()
            );
        } else if (world.getDimension().ultrawarm()) {
            return Thermoo.getConfig().environmentConfig.getUltrawarmWarmRate();
        }
        return 0;
    }

    @Override
    public int getEnvironmentTemperatureForPlayer(PlayerEntity player, int localTemperature) {
        return localTemperature;
    }

    @Override
    public int getFloorTemperature(BlockState state) {
        if (state.isOf(Blocks.MAGMA_BLOCK)) {
            ThermooConfig config = Thermoo.getConfig();
            return config.environmentConfig.getHotFloorWarmth();
        } else {
            return controller.getFloorTemperature(state);
        }
    }

    @Override
    public int getTemperatureEffectsChange(LivingEntity entity) {

        int warmth = 0;
        ThermooConfig config = Thermoo.getConfig();

        if (entity.isOnFire()) {
            warmth += config.environmentConfig.getOnFireWarmRate();
        }

        if (entity.wasInPowderSnow) {
            warmth -= config.environmentConfig.getPowderSnowFreezeRate();
        }

        return warmth;
    }


    @Override
    public int getSoakChange(Soakable soakable) {

        if (!(soakable instanceof LivingEntity entity)) {
            return controller.getSoakChange(soakable);
        }


        boolean isAreaDry = true;
        int soakChange = 0;
        ThermooConfig config = Thermoo.getConfig();


        // add wetness from rain
//        if (entity.thermoo$invokeIsBeingRainedOn()) {
//            soakChange += config.environmentConfig.getRainWetnessIncrease();
//            isAreaDry = false;
//        }

        // add wetness when touching, but not submerged in, water
        if (entity.isTouchingWater() || entity.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
            soakChange += config.environmentConfig.getTouchingWaterWetnessIncrease();
            isAreaDry = false;
        }

        // immediately soak players in water
        if (entity.isSubmergedInWater() /*|| invoker.thermoo$invokeIsInsideBubbleColumn()*/) {
            soakChange = entity.thermoo$getMaxWetTicks();
            isAreaDry = false;
        }

        // dry off slowly when not being wetted
        if (isAreaDry && entity.thermoo$isWet()) {
            soakChange = -config.environmentConfig.getDryRate();
        }

        // increase drying from block light
        int blockLightLevel = entity.getWorld().getLightLevel(LightType.BLOCK, entity.getBlockPos());
        if (blockLightLevel > 0) {
            soakChange -= blockLightLevel / 4;
        }

        if (entity.isOnFire()) {
            soakChange -= config.environmentConfig.getOnFireDryDate();
        }

        return soakChange;
    }

    @Override
    public int getHeatAtLocation(World world, BlockPos pos) {
        ThermooConfig config = Thermoo.getConfig();

        int lightLevel = world.getLightLevel(LightType.BLOCK, pos);
        int minLightLevel = config.environmentConfig.getMinLightForWarmth();

        int warmth = 0;
        if (lightLevel >= minLightLevel) {
            warmth += config.environmentConfig.getWarmthPerLightLevel() * (lightLevel - minLightLevel);
        }

        return warmth;
    }

    @Override
    public int getHeatFromBlockState(BlockState state) {
        return state.getLuminance();
    }

    @Override
    public boolean isHeatSource(BlockState state) {
        int minLightForWarmth = Thermoo.getConfig().environmentConfig.getMinLightForWarmth();
        return state.getLuminance() >= minLightForWarmth;
    }

    @Override
    public boolean isAreaHeated(World world, BlockPos pos) {
        int minLightForWarmth = Thermoo.getConfig().environmentConfig.getMinLightForWarmth();
        return world.getLightLevel(LightType.BLOCK, pos) > minLightForWarmth;
    }

    private int getTempChangeFromBiomeTemperature(World world, float temperature, boolean isDryBiome) {
        ThermooConfig config = Thermoo.getConfig();
        double mul = config.environmentConfig.getBiomeTemperatureMultiplier();
        double cutoff = config.environmentConfig.getPassiveFreezingCutoffTemp();

        double tempShift = 0.0;
        if (world.isNight() && config.environmentConfig.doDryBiomeNightFreezing()) {
            if (isDryBiome) {
                temperature = Math.min(temperature, config.environmentConfig.getDryBiomeNightTemperature());
            } else {
                tempShift = config.environmentConfig.getNightTimeTemperatureDecrease();
            }
        }

        return MathHelper.floor(mul * (temperature - cutoff - tempShift) - 1);
    }

}

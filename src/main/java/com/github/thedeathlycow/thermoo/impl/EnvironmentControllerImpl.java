package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.mixin.EntityInvoker;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class EnvironmentControllerImpl implements EnvironmentController {

    @Override
    public int getLocalTemperatureChange(World world, BlockPos pos) {
        if (world.getDimension().natural()) {
            Biome biome = world.getBiome(pos).value();
            float temperature = biome.getTemperature();
            return this.getTempChangeFromBiomeTemperature(
                    world,
                    temperature,
                    biome.getPrecipitation() == Biome.Precipitation.NONE
            );
        } else if (world.getDimension().ultrawarm()) {
            return Thermoo.getConfig().environmentConfig.getUltrawarmWarmRate();
        }
        return 0;
    }

    @Override
    public int getWarmthFromHeatSources(TemperatureAware temperatureAware, World world, BlockPos pos) {
        int warmth = 0;
        ThermooConfig config = Thermoo.getConfig();

        int lightLevel = world.getLightLevel(LightType.BLOCK, pos);
        int minLightLevel = config.environmentConfig.getMinLightForWarmth();

        if (temperatureAware.thermoo$isCold() && lightLevel >= minLightLevel) {
            warmth += config.environmentConfig.getWarmthPerLightLevel() * (lightLevel - minLightLevel);
        }

        return warmth;
    }

    @Override
    public int getOnFireWarmthRate(LivingEntity entity) {

        int warmth = 0;
        ThermooConfig config = Thermoo.getConfig();

        if (entity.isOnFire()) {
            warmth += config.environmentConfig.getOnFireWarmRate();
        }

        return warmth;
    }


    @Override
    public int getSoakChange(LivingEntity entity) {

        EntityInvoker invoker = (EntityInvoker) entity;

        boolean isAreaDry = true;
        int soakChange = 0;
        ThermooConfig config = Thermoo.getConfig();


        // add wetness from rain
        if (invoker.thermoo$invokeIsBeingRainedOn()) {
            soakChange += config.environmentConfig.getRainWetnessIncrease();
            isAreaDry = false;
        }

        // add wetness when touching, but not submerged in, water
        if (entity.isTouchingWater() || entity.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
            soakChange += config.environmentConfig.getTouchingWaterWetnessIncrease();
            isAreaDry = false;
        }

        // immediately soak players in water
        if (entity.isSubmergedInWater() || invoker.thermoo$invokeIsInsideBubbleColumn()) {
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

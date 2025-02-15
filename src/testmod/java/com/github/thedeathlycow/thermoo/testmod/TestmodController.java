package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentControllerDecorator;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.testmod.config.ThermooConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
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
    public int getSoakChange(Soakable soakable) {

        if (!(soakable instanceof LivingEntity entity)) {
            return controller.getSoakChange(soakable);
        }


        boolean isAreaDry = true;
        int soakChange = 0;
        ThermooConfig config = ThermooTestMod.getConfig();


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

    private int getTempChangeFromBiomeTemperature(World world, float temperature, boolean isDryBiome) {
        ThermooConfig config = ThermooTestMod.getConfig();
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

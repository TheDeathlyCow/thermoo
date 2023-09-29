package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class LivingEntityEnvironmentTickImpl {


    public static void tick(LivingEntity entity, int lastTickTemperature) {
        World world = entity.getWorld();

        if (world.isClient && entity.isPlayer() && entity.age % 20 == 0) {
            Thermoo.LOGGER.info("Temperature: {}, Wetness: {}", entity.thermoo$getTemperature(), entity.thermoo$getWetTicks());
        }

        if (world.isClient() || entity.isSpectator() || entity.isDead() || entity.isRemoved()) {
            return;
        }

        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();

        int tempChange;

        // tick area heat sources
        tempChange = controller.getHeatAtLocation(world, entity.getRootVehicle().getBlockPos());
        if (tempChange != 0) {
            entity.thermoo$addTemperature(tempChange, HeatingModes.PASSIVE);
        }

        tempChange = controller.getTemperatureEffectsChange(entity);
        if (tempChange != 0) {
            entity.thermoo$addTemperature(tempChange, HeatingModes.ACTIVE);
        }

        int soakChange = controller.getSoakChange(entity);
        if (soakChange != 0) {
            entity.thermoo$addWetTicks(soakChange);
            ThermooComponents.WETNESS.sync(entity);
        }

        if (lastTickTemperature != entity.thermoo$getTemperature()) {
            ThermooComponents.TEMPERATURE.sync(entity);
        }
    }

    private LivingEntityEnvironmentTickImpl() {

    }

}

package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.event.PlayerEnvironmentEvents;
import com.github.thedeathlycow.thermoo.impl.component.ThermooComponents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LivingEntityEnvironmentTickImpl {


    public static void tick(LivingEntity entity) {
        Level world = entity.level();

        if (world.isClientSide() || entity.isSpectator() || entity.isDeadOrDying() || entity.isRemoved()) {
            return;
        }

        EnvironmentController controller = EnvironmentManager.INSTANCE.getController();

        int tempChange;

        // tick area heat sources
        tempChange = controller.getHeatAtLocation(world, entity.getRootVehicle().blockPosition());
        tempChange = controller.applyAwareHeat(entity, tempChange);
        if (tempChange != 0) {
            entity.thermoo$addTemperature(tempChange, HeatingModes.PASSIVE);
        }

        tempChange = controller.getTemperatureEffectsChange(entity);
        if (tempChange != 0) {
            entity.thermoo$addTemperature(tempChange, HeatingModes.ACTIVE);
        }

        int soakChange = controller.getSoakChange(entity);
        boolean isSyncTick = entity.tickCount % 20 == 0;

        if (soakChange != 0) {
            entity.thermoo$addWetTicks(soakChange);
        }

        if (isSyncTick || ThermooComponents.TEMPERATURE.get(entity).isDirty()) {
            ThermooComponents.TEMPERATURE.sync(entity);
        }

        if (isSyncTick || ThermooComponents.WETNESS.get(entity).isDirty()) {
            ThermooComponents.WETNESS.sync(entity);
        }
    }

    public static void tickPlayer(Player player) {
        Level world = player.level();

        if (world.isClientSide || player.isSpectator()) {
            return;
        }

        BlockPos pos = player.blockPosition();
        var controller = EnvironmentManager.INSTANCE.getController();
        int temperatureChange = controller.getLocalTemperatureChange(world, pos);

        if (temperatureChange != 0) {

            TriState canApplyChange = PlayerEnvironmentEvents.CAN_APPLY_PASSIVE_TEMPERATURE_CHANGE
                    .invoker()
                    .canApplyChange(temperatureChange, player);

            if (canApplyChange != TriState.FALSE) {
                temperatureChange = controller.getEnvironmentTemperatureForPlayer(player, temperatureChange);
                player.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
            }
        }
    }

    private LivingEntityEnvironmentTickImpl() {

    }

}

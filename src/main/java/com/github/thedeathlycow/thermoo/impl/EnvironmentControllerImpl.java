package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.HeatingModes;
import com.github.thedeathlycow.thermoo.api.Soakable;
import com.github.thedeathlycow.thermoo.mixin.EntityInvoker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EnvironmentControllerImpl implements EnvironmentController {

    private final Set<UUID> heatTickedEntities = new HashSet<>();
    private final Set<UUID> tickedPlayers = new HashSet<>();

    public EnvironmentControllerImpl() {
        ServerTickEvents.END_SERVER_TICK.register(world -> {
            heatTickedEntities.clear();
            tickedPlayers.clear();
        });
    }

    @Override
    public void tickEntity(LivingEntity entity) {

        // ensure that entities arent ticked twice in the same tick
        UUID entityID = entity.getUuid();
        if (heatTickedEntities.contains(entityID)) {
            return;
        }
        heatTickedEntities.add(entityID);


        int temperatureChange = 0;

        // tick heat sources
        temperatureChange += this.getWarmthFromHeatSources(entity);

        // tick on fire
        temperatureChange += this.tickWarmthOnFire(entity);

        // add passive temp change
        entity.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
    }

    @Override
    public void tickPlayer(PlayerEntity player, ChangeTemperaturePredicate doPassiveChange) {

        UUID playerID = player.getUuid();
        boolean alreadyTicked = tickedPlayers.contains(playerID);
        tickedPlayers.add(playerID);

        // tick passive temp delta - may be ticked twice if doPassiveChange allows
        int temperatureChange = this.getPassiveTemperatureChange(player);
        if (doPassiveChange.test(player, player.world, temperatureChange, alreadyTicked)) {
            player.thermoo$addTemperature(temperatureChange, HeatingModes.PASSIVE);
        }

        // ensure that player soaking isnt ticked twice in the same tick
        if (!alreadyTicked) {
            // tick soaking
            int soakChange = this.getSoakChangeForPlayer(player);
            int wetTicks = player.thermoo$getWetTicks();
            player.thermoo$setWetTicks(wetTicks + soakChange);
        }
    }

    @Override
    public int getPassiveTemperatureChange(PlayerEntity player) {

        int localChange = this.getLocalTemperatureChange(player.world, player.getBlockPos());
        boolean isFreezing = localChange < 0;

        boolean isImmune = localChange == 0
                || (isFreezing && !player.thermoo$canFreeze())
                || (!isFreezing && !player.thermoo$canOverheat());
        if (isImmune) {
            return 0;
        }

        if (isFreezing) {
            float modifier = this.getSoakedFreezingMultiplier(player);
            localChange = MathHelper.ceil(localChange * (1 + modifier));
        }

        return localChange;
    }

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
            return -100; // TODO: config
        }
        return 0;
    }

    @Override
    public int getWarmthFromHeatSources(LivingEntity entity) {
        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();
        int warmth = 0;

        int lightLevel = world.getLightLevel(LightType.BLOCK, pos);

        int minLightLevel = 5;

        if (entity.thermoo$isCold() && lightLevel >= minLightLevel) {
            warmth += 10 * (lightLevel - minLightLevel); // TODO: config
        }

        return warmth;
    }

    @Override
    public int tickWarmthOnFire(LivingEntity entity) {
        int warmth = 0;
        if (entity.isOnFire()) {
            warmth += 100; // TODO: config

            boolean isImmuneToFire = entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)
                    || entity.isFireImmune();

            // entities that are on fire but resistant to it should are extinguished
            if (isImmuneToFire && entity.thermoo$isCold()) {
                entity.extinguish();
            }
        }

        return warmth;
    }

    @Override
    public int getSoakChangeForPlayer(PlayerEntity player) {

        EntityInvoker invoker = (EntityInvoker) player;

        boolean isDry = true;
        int soakChange = 0;

        // add wetness from rain
        if (invoker.thermoo$invokeIsBeingRainedOn()) {
            soakChange += 1;
            isDry = false;
        }

        // add wetness when touching, but not submerged in, water
        if (player.isTouchingWater() || player.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
            soakChange += 5;
            isDry = false;
        }

        // immediately soak players in water
        if (player.isSubmergedInWater() || invoker.thermoo$invokeIsInsideBubbleColumn()) {
            soakChange = player.thermoo$getMaxWetTicks();
            isDry = false;
        }

        // dry off slowly when not being wetted
        if (isDry) {
            soakChange = -1;
        }

        // increase drying from block light
        int blockLightLevel = player.getWorld().getLightLevel(LightType.BLOCK, player.getBlockPos());
        if (blockLightLevel > 0) {
            soakChange -= blockLightLevel / 4;
        }

        if (player.isOnFire()) {
            soakChange -= 100;
        }

        return soakChange;
    }

    @Override
    public float getSoakedFreezingMultiplier(Soakable soakable) {

        if (soakable.thermoo$ignoresFrigidWater()) {
            return 0.0f;
        }

        return 2.1f * soakable.thermoo$getSoakedScale();
    }

    private int getTempChangeFromBiomeTemperature(World world, float temperature, boolean isDryBiome) {
        // TODO: config
        double mul = 4.0;
        double cutoff = 0.25;

        double tempShift = 0.0;
        if (world.isNight()) {
            if (isDryBiome) {
                temperature = Math.min(temperature, 0.0f);
            } else {
                tempShift = 0.25;
            }
        }

        return MathHelper.floor(-mul * (temperature - cutoff - tempShift) + 1);
    }
}

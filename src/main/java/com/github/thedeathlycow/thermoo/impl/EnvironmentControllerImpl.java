package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentController;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.impl.config.ThermooConfig;
import com.github.thedeathlycow.thermoo.impl.config.ThermooEnvironmentConfig;
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
            return Thermoo.getConfig().environmentConfig.getUltrawarmWarmRate();
        }
        return 0;
    }

    @Override
    public int getWarmthFromHeatSources(LivingEntity entity) {
        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();
        int warmth = 0;
        ThermooConfig config = Thermoo.getConfig();

        int lightLevel = world.getLightLevel(LightType.BLOCK, pos);

        int minLightLevel = config.environmentConfig.getMinLightForWarmth();

        if (entity.thermoo$isCold() && lightLevel >= minLightLevel) {
            warmth += config.environmentConfig.getWarmthPerLightLevel() * (lightLevel - minLightLevel);
        }

        boolean isSubmerged = entity.isSubmergedInWater()
                || ((EntityInvoker) entity).thermoo$invokeIsInsideBubbleColumn();

        if (isSubmerged && entity.hasStatusEffect(StatusEffects.CONDUIT_POWER)) {
            warmth += config.environmentConfig.getConduitPowerWarmthPerTick();
        }

        return warmth;
    }

    @Override
    public int tickWarmthOnFire(LivingEntity entity) {
        int warmth = 0;
        ThermooConfig config = Thermoo.getConfig();
        if (entity.isOnFire()) {
            warmth += config.environmentConfig.getOnFireWarmRate();

            boolean isImmuneToFire = entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)
                    || entity.isFireImmune();

            // entities that are on fire but resistant to it are extinguished
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
        ThermooConfig config = Thermoo.getConfig();


        // add wetness from rain
        if (invoker.thermoo$invokeIsBeingRainedOn()) {
            soakChange += config.environmentConfig.getRainWetnessIncrease();
            isDry = false;
        }

        // add wetness when touching, but not submerged in, water
        if (player.isTouchingWater() || player.getBlockStateAtPos().isOf(Blocks.WATER_CAULDRON)) {
            soakChange += config.environmentConfig.getTouchingWaterWetnessIncrease();
            isDry = false;
        }

        // immediately soak players in water
        if (player.isSubmergedInWater() || invoker.thermoo$invokeIsInsideBubbleColumn()) {
            soakChange = player.thermoo$getMaxWetTicks();
            isDry = false;
        }

        // dry off slowly when not being wetted
        if (isDry) {
            soakChange = -config.environmentConfig.getDryRate();
        }

        // increase drying from block light
        int blockLightLevel = player.getWorld().getLightLevel(LightType.BLOCK, player.getBlockPos());
        if (blockLightLevel > 0) {
            soakChange -= blockLightLevel / 4;
        }

        if (player.isOnFire()) {
            soakChange -= config.environmentConfig.getOnFireDryDate();
        }

        return soakChange;
    }

    @Override
    public float getSoakedFreezingMultiplier(Soakable soakable) {

        if (soakable.thermoo$ignoresFrigidWater()) {
            return 0.0f;
        }
        ThermooEnvironmentConfig config = Thermoo.getConfig().environmentConfig;

        return config.getPassiveFreezingWetnessScaleMultiplier() * soakable.thermoo$getSoakedScale();
    }

    private int getTempChangeFromBiomeTemperature(World world, float temperature, boolean isDryBiome) {
        ThermooConfig config = Thermoo.getConfig();
        double mul = config.environmentConfig.getBiomeTemperatureMultiplier();
        double cutoff = config.environmentConfig.getPassiveFreezingMaxTemp();

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

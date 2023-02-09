package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class EnvironmentAwareEntityMixin extends Entity implements TemperatureAware, Soakable {

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow
    public abstract boolean canBreatheInWater();

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    protected abstract boolean tryUseTotem(DamageSource source);

    @Unique
    private static final TrackedData<Integer> THERMOO_TEMPERATURE = DataTracker.registerData(
            EnvironmentAwareEntityMixin.class,
            TrackedDataHandlerRegistry.INTEGER
    );

    @Unique
    private static final TrackedData<Integer> THERMOO_WETNESS = DataTracker.registerData(
            EnvironmentAwareEntityMixin.class,
            TrackedDataHandlerRegistry.INTEGER
    );

    public EnvironmentAwareEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void setWetTicks(int amount) {
        this.dataTracker.set(THERMOO_WETNESS, amount);
    }

    @Override
    public int getWetTicks() {
        return this.dataTracker.get(THERMOO_WETNESS);
    }

    @Override
    public int getMaxWetTicks() {
        return 20 * 30;
    }


    @Override
    public boolean ignoresFrigidWater() {
        boolean canBreatheInWater = this.canBreatheInWater()
                || this.hasStatusEffect(StatusEffects.WATER_BREATHING)
                || this.hasStatusEffect(StatusEffects.CONDUIT_POWER);

        return canBreatheInWater && this.isSubmergedInWater();
    }

    @Override
    public int getTemperature() {
        return this.dataTracker.get(THERMOO_TEMPERATURE);
    }

    @Override
    public void setTemperature(int temperature) {
        this.dataTracker.set(THERMOO_TEMPERATURE, temperature);
    }

    @Override
    public int getMinTemperature() {
        double minTemp = this.getAttributeValue(ThermooAttributes.MIN_TEMPERATURE);

        return (int) minTemp * 140;
    }

    @Override
    public int getMaxTemperature() {
        double minTemp = this.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE);

        return (int) minTemp * 140;
    }

    @Override
    public double getColdResistance() {
        return this.getAttributeValue(ThermooAttributes.FROST_RESISTANCE);
    }

    @Override
    public double getHeatResistance() {
        return this.getAttributeValue(ThermooAttributes.HEAT_RESISTANCE);
    }

    @Override
    public void addTemperature(int temperatureDelta, HeatingMode mode) {
        if (temperatureDelta == 0) {
            // adding 0 will always do nothing
            return;
        }

        int currentTemperature = this.getTemperature();
        int modifiedDelta = mode.applyResistance(this, temperatureDelta);
        this.setTemperature(currentTemperature + modifiedDelta);
    }

    @Inject(
            method = "initDataTracker",
            at = @At("TAIL")
    )
    private void syncData(CallbackInfo ci) {
        this.dataTracker.startTracking(THERMOO_TEMPERATURE, 0);
        this.dataTracker.startTracking(THERMOO_WETNESS, 0);
    }

    @Inject(
            method = "createLivingAttributes",
            at = @At("TAIL")
    )
    private static void addThermooAttributesToLivingEntities(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();

        builder.add(ThermooAttributes.MAX_TEMPERATURE);
        builder.add(ThermooAttributes.MIN_TEMPERATURE);
        builder.add(ThermooAttributes.FROST_RESISTANCE);
        builder.add(ThermooAttributes.HEAT_RESISTANCE);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    private void addDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound thermoo = new NbtCompound();

        int wetTicks = this.getWetTicks();
        if (wetTicks > 0) {
            thermoo.putInt("WetTicks", wetTicks);
        }

        int temperature = this.getTemperature();
        if (temperature > 0) {
            thermoo.putInt("Temperature", temperature);
        }

        nbt.put("Thermoo", thermoo);
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    private void readDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        if (!nbt.contains("Thermoo", NbtElement.COMPOUND_TYPE)) {
            this.setTemperature(0);
            this.setWetTicks(0);
            return;
        }

        NbtCompound thermoo = nbt.getCompound("Thermoo");

        int temperature = 0;
        int wetTicks = 0;

        if (thermoo.contains("Temperature", NbtElement.INT_TYPE)) {
            temperature = thermoo.getInt("Temperature");
        }

        if (thermoo.contains("WetTicks", NbtElement.INT_TYPE)) {
            wetTicks = thermoo.getInt("WetTicks");
        }

        this.setTemperature(temperature);
        this.setWetTicks(wetTicks);
    }
}

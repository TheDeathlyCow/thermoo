package com.github.thedeathlycow.thermoo.mixin;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.ThermooTags;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureBoundModifiers;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class EnvironmentAwareEntityMixin extends Entity implements TemperatureAware, Soakable {

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow
    public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Shadow
    public abstract boolean canBreatheInWater();

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    private static final UUID THERMOO_MIN_TEMPERATURE_OVERRIDE_ID = UUID.fromString("f68aeafc-2d30-446a-8930-57404eb308a2");
    private static final UUID THERMOO_MAX_TEMPERATURE_OVERRIDE_ID = UUID.fromString("45a74c0c-1696-4cd9-b849-2b82e174f82d");

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
    public void thermoo$setWetTicks(int amount) {
        int value = MathHelper.clamp(amount, 0, this.thermoo$getMaxWetTicks());
        this.dataTracker.set(THERMOO_WETNESS, value);
    }

    @Override
    public int thermoo$getWetTicks() {
        return this.dataTracker.get(THERMOO_WETNESS);
    }

    @Override
    public int thermoo$getMaxWetTicks() {
        return 20 * 30;
    }


    @Override
    public boolean thermoo$ignoresFrigidWater() {
        boolean canBreatheInWater = this.canBreatheInWater()
                || this.hasStatusEffect(StatusEffects.WATER_BREATHING)
                || this.hasStatusEffect(StatusEffects.CONDUIT_POWER);

        return canBreatheInWater && this.isSubmergedInWater();
    }

    @Override
    public int thermoo$getTemperature() {
        return this.dataTracker.get(THERMOO_TEMPERATURE);
    }

    @Override
    public void thermoo$setTemperature(int temperature) {
        int value = MathHelper.clamp(temperature, this.thermoo$getMinTemperature(), this.thermoo$getMaxTemperature());
        this.dataTracker.set(THERMOO_TEMPERATURE, value);
    }

    @Override
    public int thermoo$getMinTemperature() {
        double minTemp = this.getAttributeValue(ThermooAttributes.MIN_TEMPERATURE);

        return -(int) (minTemp * 140);
    }

    @Override
    public int thermoo$getMaxTemperature() {
        double maxTemp = this.getAttributeValue(ThermooAttributes.MAX_TEMPERATURE);

        return (int) (maxTemp * 140);
    }

    @Override
    public double thermoo$getColdResistance() {
        return this.getAttributeValue(ThermooAttributes.FROST_RESISTANCE);
    }

    @Override
    public double thermoo$getHeatResistance() {
        return this.getAttributeValue(ThermooAttributes.HEAT_RESISTANCE);
    }

    @Override
    public boolean thermoo$canFreeze() {

        EntityType<?> type = this.getType();

        if (this.isSpectator()) {
            return false;
        } else if (type.isIn(ThermooTags.BENEFITS_FROM_COLD_ENTITY_TYPE)) {
            // entities that benefit from heat override entities that are immune to it
            return true;
        } else if (type.isIn(ThermooTags.COLD_IMMUNE_ENTITY_TYPE)) {
            return false;
        } else if (this.isPlayer()) {
            final LivingEntity instance = (LivingEntity) (Object) this;
            return !((PlayerEntity) instance).isCreative();
        } else {
            return true;
        }
    }

    @Override
    public boolean thermoo$canOverheat() {
        EntityType<?> type = this.getType();

        if (this.isSpectator()) {
            return false;
        } else if (type.isIn(ThermooTags.BENEFITS_FROM_HEAT_ENTITY_TYPE)) {
            // entities that benefit from heat override entities that are immune to it
            return true;
        } else if (type.isIn(ThermooTags.HEAT_IMMUNE_ENTITY_TYPE)) {
            return false;
        } else if (this.isPlayer()) {
            final LivingEntity instance = (LivingEntity) (Object) this;
            return !((PlayerEntity) instance).isCreative();
        } else {
            return true;
        }
    }

    @Override
    public void thermoo$addTemperature(int temperatureChange, HeatingMode mode) {
        if (temperatureChange == 0) {
            // adding 0 will always do nothing
            return;
        }

        // do not add temperature if immune
        boolean isFreezing = temperatureChange < 0;

        if ((isFreezing && !this.thermoo$canFreeze()) || (!isFreezing && !this.thermoo$canOverheat())) {
            return;
        }

        int currentTemperature = this.thermoo$getTemperature();
        int modifiedChange = mode.applyResistance(this, temperatureChange);
        this.thermoo$setTemperature(currentTemperature + modifiedChange);
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
            method = "<init>",
            at = @At("TAIL")
    )
    private void addOverrideBoundsModifiers(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo ci) {

        var boundRegistry = TemperatureBoundModifiers.getInstance();
        boundRegistry.getLowerBoundIncrease(entityType).ifPresent(value -> {
            addTemperatureOverrideModifier(
                    ThermooAttributes.MIN_TEMPERATURE,
                    THERMOO_MIN_TEMPERATURE_OVERRIDE_ID,
                    "Base minimum temperature modifier",
                    value
            );
        });
        boundRegistry.getLowerBoundIncrease(entityType).ifPresent(value -> {
            addTemperatureOverrideModifier(
                    ThermooAttributes.MAX_TEMPERATURE,
                    THERMOO_MAX_TEMPERATURE_OVERRIDE_ID,
                    "Base maximum temperature modifier",
                    value
            );
        });
    }

    private void addTemperatureOverrideModifier(EntityAttribute attribute, UUID id, String name, double value) {
        EntityAttributeInstance temperature = this.getAttributeInstance(attribute);
        var modifier = new EntityAttributeModifier(
                id,
                name,
                value,
                EntityAttributeModifier.Operation.ADDITION
        );
        if (!temperature.hasModifier(modifier)) {
            temperature.addPersistentModifier(modifier);
        }
    }

    @Inject(
            method = "createLivingAttributes",
            at = @At("TAIL")
    )
    private static void addThermooAttributesToLivingEntities(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();

        // register attributes to living entities
        Thermoo.LOGGER.info("Current class lookup: {}", MethodHandles.lookup().lookupClass().getSimpleName());

        builder.add(ThermooAttributes.MIN_TEMPERATURE);
        builder.add(ThermooAttributes.MAX_TEMPERATURE);
        builder.add(ThermooAttributes.FROST_RESISTANCE);
        builder.add(ThermooAttributes.HEAT_RESISTANCE);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    private void addDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound thermoo = new NbtCompound();

        int wetTicks = this.thermoo$getWetTicks();
        if (wetTicks > 0) {
            thermoo.putInt("WetTicks", wetTicks);
        }

        int temperature = this.thermoo$getTemperature();
        thermoo.putInt("Temperature", temperature);

        nbt.put("Thermoo", thermoo);
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    private void readDataFromNbt(NbtCompound nbt, CallbackInfo ci) {

        if (!nbt.contains("Thermoo", NbtElement.COMPOUND_TYPE)) {
            this.thermoo$setTemperature(0);
            this.thermoo$setWetTicks(0);
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

        this.thermoo$setTemperature(temperature);
        this.thermoo$setWetTicks(wetTicks);
    }
}

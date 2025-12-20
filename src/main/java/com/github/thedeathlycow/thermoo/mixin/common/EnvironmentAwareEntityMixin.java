package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.ThermooTags;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.impl.attachment.ThermooAttachments;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class EnvironmentAwareEntityMixin extends Entity implements TemperatureAware, Soakable {

    @Shadow
    public abstract boolean canBreatheUnderwater();

    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    public EnvironmentAwareEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void thermoo$setWetTicks(int amount) {
        int value = Mth.clamp(amount, 0, this.thermoo$getMaxWetTicks());
        this.setData(ThermooAttachments.WETNESS, value);
    }

    @Override
    public int thermoo$getWetTicks() {
        return this.getData(ThermooAttachments.WETNESS);
    }

    @Override
    public int thermoo$getMaxWetTicks() {
        // base of 600
        int base = EnvironmentManager.INSTANCE.getController().getMaxWetTicks(this);
        double multiplier = this.getAttributeValue(ThermooAttributes.MAX_SOAKING_TICK_MULTIPLIER);
        return Mth.floor(base * multiplier);
    }


    @Override
    public boolean thermoo$ignoresFrigidWater() {
        boolean canBreatheInWater = this.canBreatheUnderwater()
                || this.hasEffect(MobEffects.WATER_BREATHING)
                || this.hasEffect(MobEffects.CONDUIT_POWER);

        return canBreatheInWater && this.isUnderWater();
    }

    @Override
    public int thermoo$getTemperature() {
        return this.getData(ThermooAttachments.TEMPERATURE);
    }

    @Override
    public void thermoo$setTemperature(int temperature) {
        int value = Mth.clamp(temperature, this.thermoo$getMinTemperature(), this.thermoo$getMaxTemperature());
        this.setData(ThermooAttachments.TEMPERATURE, value);
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
    public double thermoo$getEnvironmentColdResistance() {
        return this.getAttributeValue(ThermooAttributes.ENVIRONMENT_FROST_RESISTANCE);
    }

    @Override
    public double thermoo$getEnvironmentHeatResistance() {
        return this.getAttributeValue(ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE);
    }

    @Override
    public boolean thermoo$canFreeze() {

        EntityType<?> type = this.getType();

        if (this.isSpectator()) {
            return false;
        } else if (type.is(ThermooTags.BENEFITS_FROM_COLD_ENTITY_TYPE)) {
            // entities that benefit from heat override entities that are immune to it
            return true;
        } else if (type.is(ThermooTags.COLD_IMMUNE_ENTITY_TYPE)) {
            return false;
        } else if ((Entity) this instanceof Player player) {
            return !player.isCreative();
        } else {
            return true;
        }
    }

    @Override
    public boolean thermoo$canOverheat() {
        EntityType<?> type = this.getType();

        if (this.isSpectator()) {
            return false;
        } else if (type.is(ThermooTags.BENEFITS_FROM_HEAT_ENTITY_TYPE)) {
            // entities that benefit from heat override entities that are immune to it
            return true;
        } else if (type.is(ThermooTags.HEAT_IMMUNE_ENTITY_TYPE)) {
            return false;
        } else if ((Entity) this instanceof Player player) {
            return !player.isCreative();
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

    @Override
    public RandomSource thermoo$getRandom() {
        return this.random;
    }

    @Inject(
            method = "createLivingAttributes",
            at = @At("TAIL")
    )
    private static void addThermooAttributesToLivingEntities(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        AttributeSupplier.Builder builder = cir.getReturnValue();

        // register attributes to living entities
        builder.add(ThermooAttributes.MIN_TEMPERATURE);
        builder.add(ThermooAttributes.MAX_TEMPERATURE);
        builder.add(ThermooAttributes.MAX_SOAKING_TICK_MULTIPLIER);
        builder.add(ThermooAttributes.FROST_RESISTANCE);
        builder.add(ThermooAttributes.HEAT_RESISTANCE);
        builder.add(ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE);
        builder.add(ThermooAttributes.ENVIRONMENT_FROST_RESISTANCE);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void afterTick(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            this.getData(ThermooAttachments.TEMPERATURE_EFFECTS).serverTick((LivingEntity) (Object) this);
        }
    }
}

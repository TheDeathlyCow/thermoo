package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.ThermooTags;
import com.github.thedeathlycow.thermoo.api.temperature.EnvironmentManager;
import com.github.thedeathlycow.thermoo.api.temperature.HeatingMode;
import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.github.thedeathlycow.thermoo.impl.component.ThermooComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class EnvironmentAwareEntityMixin extends Entity implements TemperatureAware, Soakable {

    @Shadow
    public abstract boolean canBreatheInWater();


    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    public EnvironmentAwareEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void thermoo$setWetTicks(int amount) {
        int value = MathHelper.clamp(amount, 0, this.thermoo$getMaxWetTicks());
        ThermooComponents.WETNESS.get(this).setValue(value);
    }

    @Override
    public int thermoo$getWetTicks() {
        return ThermooComponents.WETNESS.get(this).getValue();
    }

    @Override
    public int thermoo$getMaxWetTicks() {
        return EnvironmentManager.INSTANCE.getController().getMaxWetTicks(this);
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
        return ThermooComponents.TEMPERATURE.get(this).getValue();
    }

    @Override
    public void thermoo$setTemperature(int temperature) {
        int value = MathHelper.clamp(temperature, this.thermoo$getMinTemperature(), this.thermoo$getMaxTemperature());
        ThermooComponents.TEMPERATURE.get(this).setValue(value);
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
            method = "createLivingAttributes",
            at = @At("TAIL")
    )
    private static void addThermooAttributesToLivingEntities(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();

        // register attributes to living entities
        builder.add(ThermooAttributes.MIN_TEMPERATURE);
        builder.add(ThermooAttributes.MAX_TEMPERATURE);
        builder.add(ThermooAttributes.FROST_RESISTANCE);
        builder.add(ThermooAttributes.HEAT_RESISTANCE);
    }
}

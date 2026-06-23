/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.core.v2.Soakable;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureAware;
import com.github.thedeathlycow.thermoo.api.core.v2.TemperatureChange;
import com.github.thedeathlycow.thermoo.api.core.v2.event.TemperatureChangeEvents;
import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.entity.v1.ThermooEntityTypeTags;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.yumi.commons.TriState;
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

@Mixin(LivingEntity.class)
public abstract class EnvironmentAwareEntityMixin extends Entity implements TemperatureAware, Soakable {

    private static final int BASE_MAX_SOAK_TICKS = 600;

    @Shadow
    public abstract boolean canBreatheUnderwater();

    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    public EnvironmentAwareEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void thermoo$setWetTicks(int amount) {
        int value = Mth.clamp(amount, 0, this.thermoo$getMaxWetTicks());
        ThermooServices.COMPONENTS.getWetnessComponent((LivingEntity) (Object) this).setValue(value);
    }

    @Override
    public int thermoo$getWetTicks() {
        return ThermooServices.COMPONENTS.getWetnessComponent((LivingEntity) (Object) this).getValue();
    }

    @Override
    public int thermoo$getMaxWetTicks() {
        double multiplier = this.getAttributeValue(ThermooAttributes.MAX_SOAKING_TICK_MULTIPLIER);
        return Mth.floor(BASE_MAX_SOAK_TICKS * multiplier);
    }


    @Override
    public boolean thermoo$ignoresFrigidWater() {
        boolean canBreatheInWater = this.canBreatheUnderwater()
                || this.hasEffect(MobEffects.WATER_BREATHING)
                || this.hasEffect(MobEffects.CONDUIT_POWER);

        return canBreatheInWater && this.isUnderWater();
    }

    @Override
    public void thermoo$setTemperature(int temperature) {
        int value = Mth.clamp(temperature, this.thermoo$getMinTemperature(), this.thermoo$getMaxTemperature());
        ThermooServices.COMPONENTS.getTemperatureComponent((LivingEntity) (Object) this).setValue(value);
    }

    @Override
    public int thermoo$getTemperature() {
        return ThermooServices.COMPONENTS.getTemperatureComponent((LivingEntity) (Object) this).getValue();
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
        if (this.isSpectator()) {
            return false;
        } else if (this.is(ThermooEntityTypeTags.BENEFITS_FROM_COLD_ENTITY_TYPE)) {
            // entities that benefit from heat override entities that are immune to it
            return true;
        } else if (this.is(ThermooEntityTypeTags.COLD_IMMUNE_ENTITY_TYPE)) {
            return false;
        } else if ((Entity) this instanceof Player player) {
            return !player.isCreative();
        } else {
            return true;
        }
    }

    @Override
    public boolean thermoo$canOverheat() {
        if (this.isSpectator()) {
            return false;
        } else if (this.is(ThermooEntityTypeTags.BENEFITS_FROM_HEAT_ENTITY_TYPE)) {
            // entities that benefit from heat override entities that are immune to it
            return true;
        } else if (this.is(ThermooEntityTypeTags.HEAT_IMMUNE_ENTITY_TYPE)) {
            return false;
        } else if ((Entity) this instanceof Player player) {
            return !player.isCreative();
        } else {
            return true;
        }
    }

    @Override
    public void thermoo$addTemperature(int temperatureChange, TemperatureChange context) {
        if (temperatureChange == 0) {
            // adding 0 will always do nothing
            return;
        }

        // do not add temperature if immune
        boolean isFreezing = temperatureChange < 0;

        if ((isFreezing && !this.thermoo$canFreeze()) || (!isFreezing && !this.thermoo$canOverheat())) {
            return;
        }

        LivingEntity self = (LivingEntity) (Object) this;

        int oldTemperature = this.thermoo$getTemperature();
        int modifiedChange = context.applyReduction(self, temperatureChange);

        TemperatureChangeEvents.AllowChange invoker = TemperatureChangeEvents.ALLOW_TEMPERATURE_CHANGE.invoker();

        if (modifiedChange != 0 && invoker.allowChange(self, temperatureChange, modifiedChange, context) != TriState.FALSE) {
            int newTemperature = oldTemperature + modifiedChange;
            this.thermoo$setTemperature(newTemperature);

            TemperatureChangeEvents.AFTER_TEMPERATURE_CHANGE.invoker()
                    .afterChange(self, oldTemperature, newTemperature, context);
        }
    }

    @Override
    public void thermoo$addTemperature(int temperatureChange) {
        this.thermoo$addTemperature(temperatureChange, this.level().thermoo$temperatureSources().absolute());
    }

    @Override
    public RandomSource thermoo$getRandom() {
        return this.random;
    }


    @WrapMethod(
            method = "createLivingAttributes"
    )
    private static AttributeSupplier.Builder addThermooAttributesToLivingEntities(Operation<AttributeSupplier.Builder> original) {
        AttributeSupplier.Builder builder = original.call();

        // register attributes to living entities
        builder.add(ThermooAttributes.MIN_TEMPERATURE);
        builder.add(ThermooAttributes.MAX_TEMPERATURE);
        builder.add(ThermooAttributes.MAX_SOAKING_TICK_MULTIPLIER);
        builder.add(ThermooAttributes.FROST_RESISTANCE);
        builder.add(ThermooAttributes.HEAT_RESISTANCE);
        builder.add(ThermooAttributes.ENVIRONMENT_HEAT_RESISTANCE);
        builder.add(ThermooAttributes.ENVIRONMENT_FROST_RESISTANCE);
        return builder;
    }
}

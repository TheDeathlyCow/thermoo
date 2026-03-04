package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureEffect;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusSelector;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Optional;

public record TemperatureStatusImpl(
        @NotNull TemperatureStatusSelector selector,
        @Range(from = 1, to = Integer.MAX_VALUE) int interval,
        @NotNull List<TemperatureEffect> effects
) implements TemperatureStatus {
    public static final int DEFAULT_INTERVAL = 20;

    public boolean apply(LivingEntity entity, Level level) {
        float scale = entity.thermoo$getTemperatureScale();

        if (!this.selector.temperatureScaleRange().matches(scale)) {
            return false;
        }

        LootItemCondition predicate = this.selector.predicate().orElse(null);

        if (predicate != null && level instanceof ServerLevel serverLevel) {
            boolean result = predicate.test(
                    new LootContext.Builder(
                            new LootParams.Builder(serverLevel)
                                    .withParameter(LootContextParams.THIS_ENTITY, entity)
                                    .withParameter(LootContextParams.ORIGIN, entity.position())
                                    .create(LootContextParamSets.COMMAND)
                    ).create(Optional.empty())
            );

            if (!result) {
                return false;
            }
        }

        boolean anyApplied = false;

        for (TemperatureEffect effect : this.effects) {
            anyApplied |= effect.apply(entity, level);
        }

        return anyApplied;
    }

    public void remove(LivingEntity entity, Level level) {
        for (TemperatureEffect effect : this.effects) {
            effect.remove(entity, level);
        }
    }
}
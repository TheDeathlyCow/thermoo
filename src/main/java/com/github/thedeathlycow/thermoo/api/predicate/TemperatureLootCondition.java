package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.NumberRange;

public record TemperatureLootCondition(
        NumberRange.IntRange value,
        NumberRange.DoubleRange scale
) implements LootCondition {

    public static final Codec<TemperatureLootCondition> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    NumberRange.IntRange.CODEC
                            .fieldOf("value")
                            .orElse(NumberRange.IntRange.ANY)
                            .forGetter(TemperatureLootCondition::value),
                    NumberRange.DoubleRange.CODEC
                            .fieldOf("scale")
                            .orElse(NumberRange.DoubleRange.ANY)
                            .forGetter(TemperatureLootCondition::scale)
            ).apply(instance, TemperatureLootCondition::new)
    );

    @Override
    public LootConditionType getType() {
        return ThermooLootConditionTypes.TEMPERATURE;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof TemperatureAware temperatureAware) {
            return this.value.test(temperatureAware.thermoo$getTemperature())
                    && this.scale.test(temperatureAware.thermoo$getTemperatureScale());
        }

        return false;
    }
}

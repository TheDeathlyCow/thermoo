package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.NumberRange;

public record SoakedLootCondition(
        NumberRange.IntRange value,
        NumberRange.DoubleRange scale
) implements LootCondition {

    public static final Codec<SoakedLootCondition> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    NumberRange.IntRange.CODEC
                            .fieldOf("value")
                            .orElse(NumberRange.IntRange.ANY)
                            .forGetter(SoakedLootCondition::value),
                    NumberRange.DoubleRange.CODEC
                            .fieldOf("scale")
                            .orElse(NumberRange.DoubleRange.ANY)
                            .forGetter(SoakedLootCondition::scale)
            ).apply(instance, SoakedLootCondition::new)
    );

    @Override
    public LootConditionType getType() {
        return ThermooLootConditionTypes.SOAKED;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof Soakable soakable) {
            return this.value.test(soakable.thermoo$getWetTicks())
                    && this.scale.test(soakable.thermoo$getSoakedScale());
        }

        return false;
    }

}

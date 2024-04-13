package com.github.thedeathlycow.thermoo.api.predicate;

import net.minecraft.loot.condition.LootConditionType;

public class ThermooLootConditionTypes {


    public static final LootConditionType TEMPERATURE = new LootConditionType(TemperatureLootCondition.CODEC);
    public static final LootConditionType SOAKED = new LootConditionType(SoakedLootCondition.CODEC);

    private ThermooLootConditionTypes() {

    }
}

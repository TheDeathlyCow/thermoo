package com.github.thedeathlycow.thermoo.api.predicate;

import net.minecraft.loot.condition.LootConditionType;

/**
 * Custom {@linkplain LootConditionType loot condition types} provided by Thermoo
 */
public class ThermooLootConditionTypes {


    /**
     * Tests the temperature of an entity
     */
    public static final LootConditionType TEMPERATURE = new LootConditionType(TemperatureLootCondition.CODEC);

    /**
     * Tests the soaking value of an entity
     */
    public static final LootConditionType SOAKED = new LootConditionType(SoakedLootCondition.CODEC);

    private ThermooLootConditionTypes() {

    }
}

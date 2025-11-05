package com.github.thedeathlycow.thermoo.api.predicate;

import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Custom {@linkplain LootItemConditionType loot condition types} provided by Thermoo
 */
public class ThermooLootConditionTypes {


    /**
     * Tests the temperature of an entity
     */
    public static final LootItemConditionType TEMPERATURE = new LootItemConditionType(TemperatureLootCondition.CODEC);

    /**
     * Tests the soaking value of an entity
     */
    public static final LootItemConditionType SOAKED = new LootItemConditionType(SoakedLootCondition.CODEC);


    private ThermooLootConditionTypes() {

    }
}

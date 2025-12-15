package com.github.thedeathlycow.thermoo.api.predicate;

import com.github.thedeathlycow.thermoo.api.temperature.Soakable;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Loot condition used to test the soaking values of an entity in a predicate. Only works for entities that implement
 * {@link Soakable}, which by default is only {@link net.minecraft.world.entity.LivingEntity}. All other entities will return
 * always false.
 *
 * @param value The {@linkplain Soakable#thermoo$getWetTicks() soaking value}
 * @param scale The {@linkplain Soakable#thermoo$getSoakedScale() soaking scale}
 */
public record SoakedLootCondition(
        MinMaxBounds.Ints value,
        MinMaxBounds.Doubles scale
) implements LootItemCondition {

    public static final MapCodec<SoakedLootCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MinMaxBounds.Ints.CODEC
                            .fieldOf("value")
                            .orElse(MinMaxBounds.Ints.ANY)
                            .forGetter(SoakedLootCondition::value),
                    MinMaxBounds.Doubles.CODEC
                            .fieldOf("scale")
                            .orElse(MinMaxBounds.Doubles.ANY)
                            .forGetter(SoakedLootCondition::scale)
            ).apply(instance, SoakedLootCondition::new)
    );

    @Override
    public LootItemConditionType getType() {
        return ThermooLootConditionTypes.SOAKED;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Soakable soakable) {
            return this.value.matches(soakable.thermoo$getWetTicks())
                    && this.scale.matches(soakable.thermoo$getSoakedScale());
        }

        return false;
    }

    public static LootItemCondition.Builder builder(MinMaxBounds.Ints value) {
        return () -> new SoakedLootCondition(value, MinMaxBounds.Doubles.ANY);
    }

    public static LootItemCondition.Builder builder(MinMaxBounds.Doubles scale) {
        return () -> new SoakedLootCondition(MinMaxBounds.Ints.ANY, scale);
    }
}

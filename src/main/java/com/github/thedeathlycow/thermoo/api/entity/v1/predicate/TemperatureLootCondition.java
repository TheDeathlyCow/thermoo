package com.github.thedeathlycow.thermoo.api.entity.v1.predicate;

import com.github.thedeathlycow.thermoo.api.core.v1.Soakable;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureAware;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/// Loot condition used to test the temperature values of an entity in a predicate. Only works for entities that implement
/// [TemperatureAware], which is all instances [net.minecraft.world.entity.LivingEntity]. All other entities will cause this
/// condition to be `false`.
public final class TemperatureLootCondition implements LootItemCondition {

    public static final MapCodec<TemperatureLootCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MinMaxBounds.Ints.CODEC
                            .fieldOf("value")
                            .orElse(MinMaxBounds.Ints.ANY)
                            .forGetter(TemperatureLootCondition::value),
                    MinMaxBounds.Doubles.CODEC
                            .fieldOf("scale")
                            .orElse(MinMaxBounds.Doubles.ANY)
                            .forGetter(TemperatureLootCondition::scale)
            ).apply(instance, TemperatureLootCondition::new)
    );

    private final MinMaxBounds.Ints value;
    private final MinMaxBounds.Doubles scale;

    private TemperatureLootCondition(MinMaxBounds.Ints value, MinMaxBounds.Doubles scale) {
        this.value = value;
        this.scale = scale;
    }

    @Override
    public MapCodec<TemperatureLootCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof TemperatureAware temperatureAware) {
            return this.value.matches(temperatureAware.thermoo$getTemperature())
                    && this.scale.matches(temperatureAware.thermoo$getTemperatureScale());
        }

        return false;
    }

    public static LootItemCondition.Builder builder(MinMaxBounds.Ints value) {
        return () -> new TemperatureLootCondition(value, MinMaxBounds.Doubles.ANY);
    }

    public static LootItemCondition.Builder builder(MinMaxBounds.Doubles scale) {
        return () -> new TemperatureLootCondition(MinMaxBounds.Ints.ANY, scale);
    }

    /// Range of [soaking ticks][Soakable#thermoo$getWetTicks()] that are required for this condition to pass.
    public MinMaxBounds.Ints value() {
        return value;
    }

    /// Range of [soaking scale][Soakable#thermoo$getSoakedScale()] that are required for this condition to pass.
    public MinMaxBounds.Doubles scale() {
        return scale;
    }
}

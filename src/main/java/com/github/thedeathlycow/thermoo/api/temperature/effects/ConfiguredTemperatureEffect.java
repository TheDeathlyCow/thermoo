package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * Represents a configured instance of a {@link TemperatureEffect} type.
 * See the <a href="https://github.com/TheDeathlyCow/frostiful/wiki/Temperature-Effects">wiki page</a> for details on
 * how to implement this in a datapack.
 * <p>
 * A configured temperature effect is more like an instance of a temperature effect, and this is the class that is
 * directly instantiated from a temperature effect JSON file in a datapack.
 *
 * @param <C> The config type
 * @see TemperatureEffect
 */
public final class ConfiguredTemperatureEffect<C> {

    /**
     * The temperature effect type
     */
    private final TemperatureEffect<C> type;

    /**
     * The config of the effect
     */
    private final C config;

    /**
     * If not null, then only applies the effect to entities for which this predicate is TRUE.
     */
    private final Optional<LootItemCondition> predicate;

    /**
     * If not null, then only applies this effect to entities of the specific type. This is more
     * performant than using predicates if you want to apply an effect only to one specific type.
     */
    private final HolderSet<EntityType<?>> entityTypes;

    /**
     * The temperature scale at which this should be applied to an entity. This is more
     * performant than using predicates if you want to apply an effect only within a particular
     * temperature range
     */
    private final MinMaxBounds.Doubles temperatureScaleRange;

    /**
     * Priority for loading. Effects with a higher priority at the same resource location will
     * not be overridden by effects with lower priority from other mods/datapacks at the same
     * resource location. Allows mods to reliably override the temperature effects of other
     * mods, regardless of mod load order (which is arbitrary in Fabric). Defaults to 0 if not
     * specified.
     */
    private final int loadingPriority;

    @ApiStatus.Internal
    public ConfiguredTemperatureEffect(
            TemperatureEffect<C> type,
            C config,
            Optional<LootItemCondition> predicate,
            HolderSet<EntityType<?>> entityTypes,
            MinMaxBounds.Doubles temperatureScaleRange,
            int loadingPriority
    ) {
        this.type = type;
        this.config = config;
        this.predicate = predicate;
        this.entityTypes = entityTypes;
        this.temperatureScaleRange = temperatureScaleRange;
        this.loadingPriority = loadingPriority;
    }

    /**
     * Codec for all configured temperature effects. Dispatches config codec based on
     * {@linkplain TemperatureEffect type}.
     */
    public static final Codec<ConfiguredTemperatureEffect<?>> CODEC = ThermooRegistries.TEMPERATURE_EFFECTS
            .byNameCodec()
            .dispatch(
                    "type",
                    ConfiguredTemperatureEffect::type,
                    TemperatureEffect::getCodec
            );

    /**
     * Tests and applies this effect to a living entity if possible
     *
     * @param victim The living entity to possibly apply the effect to
     * @deprecated Use {@link #apply(LivingEntity)}
     */
    @Deprecated
    public void applyIfPossible(LivingEntity victim) {
        this.apply(victim);
    }

    /**
     * Tests and applies this effect to a living entity, and returns success if it was applied.
     * <p>
     * Returns false on client.
     *
     * @param victim The living entity to possibly apply the effect to
     * @return Returns {@code true} if the effect was applied.
     */
    public boolean apply(LivingEntity victim) {
        Level world = victim.level();

        if (world.isClientSide()) {
            return false;
        }

        ServerLevel serverLevel = (ServerLevel) world;
        boolean shouldApply = this.type.shouldApply(victim, this.config)
                && this.temperatureScaleRange.matches(victim.thermoo$getTemperatureScale())
                && this.testPredicate(victim, serverLevel);

        if (shouldApply) {
            this.type.apply(victim, serverLevel, this.config);
            return true;
        }

        return false;
    }

    /**
     * Called the first tick that a configured temperature effect could not be applied
     *
     * @param victim The entity the effect was applied to
     */
    public void remove(LivingEntity victim) {
        Level level = victim.level();

        if (level.isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        this.type.remove(victim, serverLevel, this.config);
    }

    private boolean testPredicate(LivingEntity victim, ServerLevel world) {
        return this.predicate.isEmpty()
                || this.predicate.get().test(
                new LootContext.Builder(
                        new LootParams.Builder(world)
                                .withParameter(LootContextParams.THIS_ENTITY, victim)
                                .withParameter(LootContextParams.ORIGIN, victim.position())
                                .create(LootContextParamSets.COMMAND)
                ).create(Optional.empty())
        );
    }

    public TemperatureEffect<C> type() {
        return type;
    }

    public C config() {
        return config;
    }

    public Optional<LootItemCondition> predicate() {
        return predicate;
    }

    public HolderSet<EntityType<?>> entityTypes() {
        return entityTypes;
    }

    public MinMaxBounds.Doubles temperatureScaleRange() {
        return temperatureScaleRange;
    }

    public int loadingPriority() {
        return loadingPriority;
    }
}

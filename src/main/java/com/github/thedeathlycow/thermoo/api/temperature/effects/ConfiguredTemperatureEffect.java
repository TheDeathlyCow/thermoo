package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.predicate.NumberRange;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
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
    private final Optional<LootCondition> predicate;

    /**
     * If not null, then only applies this effect to entities of the specific type. This is more
     * performant than using predicates if you want to apply an effect only to one specific type.
     */
    private final RegistryEntryList<EntityType<?>> entityTypes;

    /**
     * The temperature scale at which this should be applied to an entity. This is more
     * performant than using predicates if you want to apply an effect only within a particular
     * temperature range
     */
    private final NumberRange.DoubleRange temperatureScaleRange;

    /**
     * Priority for loading. Effects with a higher priority at the same resource location will
     * not be overridden by effects with lower priority from other mods/datapacks at the same
     * resource location. Allows mods to reliably override the temperature effects of other
     * mods, regardless of mod load order (which is arbitrary in Fabric). Defaults to 0 if not
     * specified.
     */
    private final int loadingPriority;

    /**
     * Was the effect applied in the last tick?
     * <p>
     * Transient value, used to determine when to call {@link TemperatureEffect#remove(LivingEntity, ServerWorld, Object)}
     */
    private boolean wasApplied = false;

    ConfiguredTemperatureEffect(
            TemperatureEffect<C> type,
            C config,
            Optional<LootCondition> predicate,
            RegistryEntryList<EntityType<?>> entityTypes,
            NumberRange.DoubleRange temperatureScaleRange,
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
            .getCodec()
            .dispatch(
                    "type",
                    ConfiguredTemperatureEffect::type,
                    TemperatureEffect::getCodec
            );

    /**
     * Tests and applies this effect to a living entity if possible
     *
     * @param victim The living entity to possibly apply the effect to
     */
    public void applyIfPossible(LivingEntity victim) {

        World world = victim.getWorld();

        if (world.isClient) {
            return;
        }

        ServerWorld serverWorld = (ServerWorld) world;
        boolean shouldApply = this.type.shouldApply(victim, this.config)
                && this.temperatureScaleRange.test(victim.thermoo$getTemperatureScale())
                && this.testPredicate(victim, serverWorld);

        if (shouldApply) {
            this.type.apply(victim, serverWorld, this.config);
            wasApplied = true;
        } else if (wasApplied) {
            this.type.remove(victim, serverWorld, this.config);
            wasApplied = false;
        }
    }

    private boolean testPredicate(LivingEntity victim, ServerWorld world) {
        return this.predicate.isEmpty()
                || this.predicate.get().test(
                new LootContext.Builder(
                        new LootContextParameterSet.Builder(world)
                                .add(LootContextParameters.THIS_ENTITY, victim)
                                .add(LootContextParameters.ORIGIN, victim.getPos())
                                .build(LootContextTypes.COMMAND)
                ).build(Optional.empty())
        );
    }

    public TemperatureEffect<C> type() {
        return type;
    }

    public C config() {
        return config;
    }

    public Optional<LootCondition> predicate() {
        return predicate;
    }

    public RegistryEntryList<EntityType<?>> entityTypes() {
        return entityTypes;
    }

    /**
     *
     * @return Returns the first entity type in {@link #entityTypes}, if present
     * @deprecated Use {@link #entityTypes()}
     */
    @Deprecated(since = "4.2")
    public Optional<EntityType<?>> entityType() {
        if (this.entityTypes.size() > 0) {
            return Optional.of(this.entityTypes.get(0).value());
        }
        return Optional.empty();
    }

    public NumberRange.DoubleRange temperatureScaleRange() {
        return temperatureScaleRange;
    }

    public int loadingPriority() {
        return loadingPriority;
    }
}

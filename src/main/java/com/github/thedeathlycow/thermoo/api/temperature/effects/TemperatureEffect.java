package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.NumberRange;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;

/**
 * A temperature effect is some effect that is applied to a {@link LivingEntity} based on their current temperature,
 * as determined by {@link com.github.thedeathlycow.thermoo.api.temperature.TemperatureAware}. This class represents the
 * parent class for all temperature effect 'types', which implement the specific behaviour of an effect. Effects may apply
 * potion effects, modify attributes, apply damage, and more.
 * <p>
 * Effect types can be configured to only apply effects of different strengths or only under certain conditions. The config
 * is provided by the C generic type.
 * <p>
 * The config is specified via a datapack in the folder {@code data/{namespace}/thermoo/temperature_effects/}.
 *
 * @param <C> The config type of the effect
 * @see ConfiguredTemperatureEffect
 */
public abstract class TemperatureEffect<C> {

    /**
     * Codec for configured temperature effects with this effect type's config
     */
    private final MapCodec<ConfiguredTemperatureEffect<C>> codec;

    /**
     * @param configCodec Codec for the config type
     */
    protected TemperatureEffect(Codec<C> configCodec) {
        this.codec = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        configCodec
                                .fieldOf("config")
                                .forGetter(ConfiguredTemperatureEffect::config),
                        LootCondition.CODEC
                                .optionalFieldOf("entity")
                                .forGetter(ConfiguredTemperatureEffect::predicate),
                        RegistryCodecs.entryList(RegistryKeys.ENTITY_TYPE)
                                .optionalFieldOf("entity_type", RegistryEntryList.of())
                                .forGetter(ConfiguredTemperatureEffect::entityTypes),
                        NumberRange.DoubleRange.CODEC
                                .fieldOf("temperature_scale_range")
                                .orElse(NumberRange.DoubleRange.ANY)
                                .forGetter(ConfiguredTemperatureEffect::temperatureScaleRange),
                        Codec.INT
                                .fieldOf("loading_priority")
                                .orElse(0)
                                .forGetter(ConfiguredTemperatureEffect::loadingPriority)
                ).apply(
                        instance,
                        (config, lootCondition, entityTypes, doubleRange, loadingPriority) -> {
                            return new ConfiguredTemperatureEffect<>(
                                    this,
                                    config,
                                    lootCondition,
                                    entityTypes,
                                    doubleRange,
                                    loadingPriority
                            );
                        }
                )
        );
    }

    /**
     * Applies the effect to a living entity
     *
     * @param victim      The living entity to apply the effect to
     * @param serverWorld The server world of the victim
     * @param config      The effect config
     */
    public abstract void apply(LivingEntity victim, ServerWorld serverWorld, C config);

    /**
     * Tests if the effect should be applied to a living entity.
     * Note that even if this returns {@code true}, the effect is not guaranteed to be applied. This is because all
     * entity must pass the predicate specified by {@link ConfiguredTemperatureEffect#predicate()}.
     *
     * @param victim The victim to test if the effect should be applied to
     * @param config The effect config
     * @return Returns if the effect should be applied to the victim
     */
    public abstract boolean shouldApply(LivingEntity victim, C config);

    /**
     * Called the first tick that a temperature effect could not be applied
     *
     * @param victim      The entity the effect was applied to
     * @param serverWorld The server world of the entity
     * @param config      The effect config
     */
    public void remove(LivingEntity victim, ServerWorld serverWorld, C config) {
        // Empty by default
    }

    /**
     * @return Returns the {@linkplain #codec}
     */
    public final MapCodec<ConfiguredTemperatureEffect<C>> getCodec() {
        return this.codec;
    }
}

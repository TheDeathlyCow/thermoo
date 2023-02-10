package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.entity.LivingEntity;
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
     * entity must pass the predicate specified by {@link ConfiguredTemperatureEffect#predicate}.
     *
     * @param victim The victim to test if the effect should be applied to
     * @param config The effect config
     * @return Returns if the effect should be applied to the victim
     */
    public abstract boolean shouldApply(LivingEntity victim, C config);

    /**
     * Deserializes a JSON element into a new config instance that is valid for this effect type
     *
     * @param json    The JSON element that represents the config of this effect type
     * @param context The JSON deserialization context
     * @return Returns a new config instance specified by the JSON element given
     * @throws JsonParseException Thrown if the given JSON element is not a legal representation of the config for this
     *                            effect type
     */
    public abstract C configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonParseException;

}

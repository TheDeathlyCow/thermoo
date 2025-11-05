package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.temperature.effect.TemperatureEffectManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A container class that contains all the various temperature effect types provided by Thermoo. You can, of course, create
 * your own effects for your mod. As an example, see melt damage in Frostiful!
 * <p>
 * Note that they are actually registered outside the API in {@link com.github.thedeathlycow.thermoo.impl.ThermooCommonRegisters}.
 */
public final class TemperatureEffects {

    /**
     * An empty temperature effect; does nothing. Useful for overriding effects from mods or other datapacks
     */
    public static final TemperatureEffect<EmptyTemperatureEffect.Config> EMPTY = new EmptyTemperatureEffect(
            EmptyTemperatureEffect.CODEC
    );

    /**
     * A meta temperature effect that allows multiple child effects to be applied under the same conditions.
     */
    public static final TemperatureEffect<SequenceTemperatureEffect.Config> SEQUENCE = new SequenceTemperatureEffect(
            SequenceTemperatureEffect.CODEC
    );

    /**
     * A temperature effect that executes a datapack function on an interval
     */
    public static final TemperatureEffect<FunctionTemperatureEffect.Config> FUNCTION = new FunctionTemperatureEffect(
            FunctionTemperatureEffect.CODEC
    );

    /**
     * Applies {@linkplain  net.minecraft.world.effect.MobEffect status effects} to entities based on their
     * temperature
     */
    public static final TemperatureEffect<StatusEffectTemperatureEffect.Config> MOB_EFFECT = new StatusEffectTemperatureEffect(
            StatusEffectTemperatureEffect.CODEC
    );

    /**
     * @deprecated This field was named based on Yarn Mappings, use {@link #MOB_EFFECT} to better conform to Official
     * Mappings. Also note that when this field is removed, the underlying class will also be renamed to
     * MobEffectTemperatureEffect.
     */
    @Deprecated(since = "8.1.0", forRemoval = true)
    public static final TemperatureEffect<StatusEffectTemperatureEffect.Config> STATUS_EFFECT = new StatusEffectTemperatureEffect(
            StatusEffectTemperatureEffect.CODEC
    );

    /**
     * Applies scaled {@linkplain net.minecraft.world.entity.ai.attributes.AttributeModifier attribute modifiers} to
     * entities based on their temperature
     */
    public static final TemperatureEffect<ScalingAttributeModifierTemperatureEffect.Config> SCALING_ATTRIBUTE_MODIFIER = new ScalingAttributeModifierTemperatureEffect(
            ScalingAttributeModifierTemperatureEffect.CODEC
    );

    /**
     * Applies {@linkplain net.minecraft.world.entity.ai.attributes.AttributeModifier attribute modifiers} to entities
     */
    public static final TemperatureEffect<AttributeModifierTemperatureEffect.Config> ATTRIBUTE_MODIFIER = new AttributeModifierTemperatureEffect(
            AttributeModifierTemperatureEffect.CODEC
    );

    /**
     * Applies damage to entities on an interval
     *
     * @since 1.5
     */
    public static final TemperatureEffect<DamageTemperatureEffect.Config> DAMAGE = new DamageTemperatureEffect(DamageTemperatureEffect.CODEC);

    /**
     * Returns all currently loaded {@link ConfiguredTemperatureEffect}s that are available to be applied to the
     * {@code entity}'s type.
     *
     * @param entity The entity to fetch the effects for
     * @return Returns the effects loaded for the entity type
     * @deprecated This method is primarily an implementation detail and should not have been exposed in the API
     */
    @Deprecated(since = "4.3", forRemoval = true)
    public static Collection<ConfiguredTemperatureEffect<?>> getEffectsForEntity(LivingEntity entity) {
        return TemperatureEffectManager.INSTANCE.getEffectsForEntity(entity);
    }

    /**
     * Gets the effect from an ID.
     *
     * @return Returns the effect with the given id, or null if not present.
     */
    @Nullable
    public static ConfiguredTemperatureEffect<?> getEffect(ResourceLocation id) {
        return TemperatureEffectManager.INSTANCE.getEffect(id);
    }


    /**
     * @return Returns all currently loaded {@link ConfiguredTemperatureEffect}s
     */
    public static Collection<ConfiguredTemperatureEffect<?>> getLoadedConfiguredEffects() {
        return TemperatureEffectManager.INSTANCE.getAllEffects();
    }

    private TemperatureEffects() {
    }
}

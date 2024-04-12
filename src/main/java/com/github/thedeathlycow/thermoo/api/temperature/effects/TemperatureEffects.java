package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.TemperatureEffectLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;

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
    public static final TemperatureEffect<?> EMPTY = new EmptyTemperatureEffect(EmptyTemperatureEffect.CODEC);

    /**
     * A meta temperature effect that allows multiple child effects to be applied under the same conditions.
     */
    public static final TemperatureEffect<?> SEQUENCE = new SequenceTemperatureEffect();

    /**
     * Applies {@linkplain  net.minecraft.entity.effect.StatusEffect status effects} to entities based on their
     * temperature
     */
    public static final TemperatureEffect<?> STATUS_EFFECT = new StatusEffectTemperatureEffect();

    /**
     * Applies scaled {@linkplain net.minecraft.entity.attribute.EntityAttributeModifier attribute modifiers} to
     * entities based on their temperature
     */
    public static final TemperatureEffect<?> SCALING_ATTRIBUTE_MODIFIER = new ScalingAttributeModifierTemperatureEffect();

    /**
     * Applies damage to entities on an interval
     *
     * @since 1.5
     */
    public static final TemperatureEffect<?> DAMAGE = new DamageTemperatureEffect(DamageTemperatureEffect.CODEC);

    /**
     * Specifically applies {@link DamageSources#freeze()} to entities based on their temperature
     *
     * @deprecated As of 1.5, you should use {@link #DAMAGE} instead, as it does not hardcode a damage source
     */
    @Deprecated(since = "1.5")
    public static final TemperatureEffect<?> FREEZE_DAMAGE_LEGACY = new LegacyDamageTemperatureEffect(
            (serverWorld) -> serverWorld.getDamageSources().freeze()
    );

    /**
     * Returns all currently loaded {@link ConfiguredTemperatureEffect}s that are mapped to the {@code entity}'s type.
     *
     * @param entity The entity to fetch the effects for
     * @return Returns the effects loaded for the entity type
     */
    public static Collection<ConfiguredTemperatureEffect<?>> getEffectsForEntity(LivingEntity entity) {
        return TemperatureEffectLoader.INSTANCE.getEffectsForEntity(entity);
    }


    /**
     * @return Returns all currently loaded {@link ConfiguredTemperatureEffect}s
     */
    public static Collection<ConfiguredTemperatureEffect<?>> getLoadedConfiguredEffects() {
        return TemperatureEffectLoader.INSTANCE.getGlobalEffects();
    }

    private TemperatureEffects() {
    }
}

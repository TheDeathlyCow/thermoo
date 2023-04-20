package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.TemperatureEffectLoader;
import net.minecraft.entity.damage.DamageSource;

import java.util.Collection;

/**
 * A container class that contains all the various temperature effect types provided by Thermoo. You can, of course, create
 * your own effects for your mod. As an example, see melt damage in Frostiful!
 * <p>
 * Note that they are actually registered outside the API in {@link com.github.thedeathlycow.thermoo.impl.ThermooCommonRegisters}.
 */
public final class TemperatureEffects {

    public static final TemperatureEffect<?> EMPTY = new EmptyTemperatureEffect();

    public static final TemperatureEffect<?> STATUS_EFFECT = new StatusEffectTemperatureEffect();

    public static final TemperatureEffect<?> SCALING_ATTRIBUTE_MODIFIER = new ScalingAttributeModifierTemperatureEffect();

    public static final TemperatureEffect<?> FREEZE_DAMAGE_LEGACY = new LegacyDamageTemperatureEffect(
            (serverWorld) -> serverWorld.getDamageSources().freeze()
    );


    /**
     * @return Returns all currently loaded {@link ConfiguredTemperatureEffect}s
     */
    public static Collection<ConfiguredTemperatureEffect<?>> getLoadedConfiguredEffects() {
        return TemperatureEffectLoader.INSTANCE.getEffects();
    }

    private TemperatureEffects() {
    }
}

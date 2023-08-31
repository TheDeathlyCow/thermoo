package com.github.thedeathlycow.thermoo.api;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom {@link EntityAttribute}s provided by Thermoo
 */
public final class ThermooAttributes {


    private static final Map<EntityType<?>, Object> TEMPERATURE_BOUNDS = new HashMap<>();


    /**
     * The minimum temperature of an entity. By default, this is 0.
     * <p>
     * Note that this is separate from {@link #MAX_TEMPERATURE}. Each point of this attribute is decreases the minimum
     * temperature of an entity by 140 points (140 points is the maximum number of freezing ticks that entities may have
     * for powder snow freezing in vanilla).
     * <p>
     * The default value for all entities is set to 0. To override this default for specific <b>VANILLA</b> entities, you have two
     * choices:
     * <ol>
     * <li>
     * First, you can apply an attribute modifier with {@link com.github.thedeathlycow.thermoo.api.temperature.TemperatureBoundModifiers}.
     * This modifier is applied additively to the base attribute value of this attribute everytime an instance of a given
     * entity type is constructed as an attribute modifier.
     * </li>
     * <li>
     * Alternatively, you can mixin-inject into the {@code createXAttributes()} method. For example, for all living
     * entities you could inject into {@link LivingEntity#createLivingAttributes()}, and for players inject into
     * {@link PlayerEntity#createPlayerAttributes()}. This has the benefit of applying to the base value and does not
     * create extra data for the game to track.
     * </li>
     * </ol>
     * If you are creating your own <b>CUSTOM</b> entity, you should make a {@code createXAttributes()} method that contains
     * the temperature attribute values and register it with {@link net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry}
     *
     * @see #MAX_TEMPERATURE
     */
    public static final EntityAttribute MIN_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.min_temperature", 0.0, 0.0, 8192
    ).setTracked(false);

    /**
     * The maximum temperature of an entity. By default, this is 0.
     * <p>
     * Note that this is separate from {@link #MIN_TEMPERATURE}. Each point of this attribute is increases the maximum
     * temperature of an entity by 140 points (140 points is the maximum number of freezing ticks that entities may have
     * for powder snow freezing in vanilla).
     * <p>
     * The default value for all entities is set to 0. To override this default for specific <b>VANILLA</b> entities, you have two
     * choices:
     * <ol>
     * <li>
     * First, you can apply an attribute modifier with {@link com.github.thedeathlycow.thermoo.api.temperature.TemperatureBoundModifiers}.
     * This modifier is applied additively to the base attribute value of this attribute everytime an instance of a given
     * entity type is constructed as an attribute modifier.
     * </li>
     * <li>
     * Alternatively, you can mixin-inject into the {@code createXAttributes()} method. For example, for all living
     * entities you could inject into {@link LivingEntity#createLivingAttributes()}, and for players inject into
     * {@link PlayerEntity#createPlayerAttributes()}. This has the benefit of applying to the base value and does not
     * create extra data for the game to track.
     * </li>
     * </ol>
     * If you are creating your own <b>CUSTOM</b> entity, you should make a {@code createXAttributes()} method that contains
     * the temperature attribute values and register it with {@link net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry}
     *
     * @see #MIN_TEMPERATURE
     */
    public static final EntityAttribute MAX_TEMPERATURE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.max_temperature", 0.0, 0.0, 8192
    ).setTracked(false);

    /**
     * The cold resistance of an entity. 1 point of frost resistance corresponds to a 10% cold reduction
     *
     * @see #HEAT_RESISTANCE
     */
    public static final EntityAttribute FROST_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.frost_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);

    /**
     * The heat resistance of an entity. 1 point of heat resistance corresponds to a 10% heat reduction
     *
     * @see #FROST_RESISTANCE
     */
    public static final EntityAttribute HEAT_RESISTANCE = new ClampedEntityAttribute(
            "attribute.thermoo.generic.heat_resistance", 0.0, -10.0, 10.0
    ).setTracked(false);
}

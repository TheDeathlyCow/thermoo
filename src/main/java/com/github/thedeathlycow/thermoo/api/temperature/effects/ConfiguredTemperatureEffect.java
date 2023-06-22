package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.api.ThermooRegistries;
import com.google.gson.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Represents a configured instance of a {@link TemperatureEffect} type.
 * See the <a href="https://github.com/TheDeathlyCow/frostiful/wiki/Temperature-Effects">wiki page</a> for details on
 * how to implement this in a datapack.
 *
 * @param <C> The config type
 * @see TemperatureEffect
 */
public class ConfiguredTemperatureEffect<C> {


    /**
     * The temperature effect type
     */
    private final TemperatureEffect<C> type;

    /**
     * The config instance for the type
     */
    private final C config;

    /**
     * A datapack predicate that potential victims will be tested against. The predicate must pass in order for the
     * effect to be applied - regardless of the output of {@link TemperatureEffect#shouldApply(LivingEntity, Object)}
     */
    @Nullable
    private final LootCondition predicate;

    private final NumberRange.FloatRange temperatureScaleRange;

    public ConfiguredTemperatureEffect(
            TemperatureEffect<C> type,
            C config,
            @Nullable LootCondition predicate,
            NumberRange.FloatRange temperatureScaleRange
    ) {
        this.type = type;
        this.config = config;
        this.predicate = predicate;
        this.temperatureScaleRange = temperatureScaleRange;
    }

    /**
     * Constructs a new {@link ConfiguredTemperatureEffect} of a specified type and config JSON object
     *
     * @param type                  The type of the effect to create
     * @param configJson            The config of the type
     * @param context               The JSON deserialization context
     * @param predicate             The entity predicate of the effect
     * @param temperatureScaleRange Temperature scale range predicate
     * @param <C>                   The type of the effect type config
     * @return Returns a new {@link ConfiguredTemperatureEffect} based on the JSON representation given by {@code configJson}
     * @throws JsonParseException Thrown if {@code configJson} is not a valid representation of the config type {@code C}
     */
    public static <C> ConfiguredTemperatureEffect<C> fromJson(
            TemperatureEffect<C> type,
            JsonElement configJson,
            JsonDeserializationContext context,
            @Nullable LootCondition predicate,
            NumberRange.FloatRange temperatureScaleRange
    ) throws JsonSyntaxException {
        return new ConfiguredTemperatureEffect<>(
                type,
                type.configFromJson(configJson, context),
                predicate,
                temperatureScaleRange
        );
    }

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
        }
    }

    private boolean testPredicate(LivingEntity victim, ServerWorld world) {
        return this.predicate == null
                || this.predicate.test(
                new LootContext.Builder(
                        new LootContextParameterSet.Builder(world)
                                .add(LootContextParameters.THIS_ENTITY, victim)
                                .add(LootContextParameters.ORIGIN, victim.getPos())
                                .build(LootContextTypes.COMMAND)
                ).build(null)
        );
    }

    public static class Serializer implements JsonDeserializer<ConfiguredTemperatureEffect<?>> {

        public static final Gson GSON = LootGsons.getConditionGsonBuilder()
                .registerTypeAdapter(ConfiguredTemperatureEffect.class, new Serializer())
                .create();

        @Override
        public ConfiguredTemperatureEffect<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject json = jsonElement.getAsJsonObject();

            // set required values
            Identifier typeID = new Identifier(json.get("type").getAsString());

            if (!ThermooRegistries.TEMPERATURE_EFFECTS.containsId(typeID)) {
                throw new JsonParseException("Unknown temperature effect type: " + typeID);
            }

            TemperatureEffect<?> effectType = ThermooRegistries.TEMPERATURE_EFFECTS.get(typeID);

            // set optional values
            LootCondition predicate = JsonHelper.deserialize(json, "entity", null, jsonDeserializationContext, LootCondition.class);

            NumberRange.FloatRange temperatureScaleRange = NumberRange.FloatRange.ANY;
            if (json.has("temperature_scale_range")) {
                temperatureScaleRange = NumberRange.FloatRange.fromJson(json.get("temperature_scale_range"));
            }

            return ConfiguredTemperatureEffect.fromJson(
                    effectType,
                    json.get("config"),
                    jsonDeserializationContext,
                    predicate,
                    temperatureScaleRange
            );
        }
    }


}

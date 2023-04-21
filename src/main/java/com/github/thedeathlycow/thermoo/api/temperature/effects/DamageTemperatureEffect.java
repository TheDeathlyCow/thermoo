package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Applies damage to {@link net.minecraft.entity.LivingEntity}s when their temperature scale is within a given range.
 * The amount and interval of the damage pulses can be configured, as well as the damage type. However, the {@link DamageSource}
 * applied only stores the type - the direct source entity, attacker, and position are all {@code null}.
 */
public class DamageTemperatureEffect extends TemperatureEffect<DamageTemperatureEffect.Config> {

    @Nullable
    Registry<DamageType> registry;

    private final Map<RegistryKey<DamageType>, DamageSource> damageSourcePool = new HashMap<>();

    public DamageTemperatureEffect() {
        ServerLifecycleEvents.SERVER_STOPPING.register(
                (server) -> this.invalidateRegistryCache()
        );
    }

    @Override
    public void apply(LivingEntity victim, ServerWorld serverWorld, Config config) {

        if (registry == null) {
            DynamicRegistryManager registryManager = serverWorld.getServer().getRegistryManager();

            this.registry = registryManager.get(RegistryKeys.DAMAGE_TYPE);
        }

        DamageSource source = this.getDamageSourceFromType(config.damageType, this.registry);
        if (source != null) {
            victim.damage(source, config.amount);
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.age % config.damageInterval == 0 && config.amount != 0.0f;
    }

    @Override
    public Config configFromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
        return Config.fromJson(json, context);
    }

    @Nullable
    private DamageSource getDamageSourceFromType(RegistryKey<DamageType> damageType, Registry<DamageType> registry) {
        if (this.damageSourcePool.containsKey(damageType)) {
            return this.damageSourcePool.get(damageType);
        } else if (registry.contains(damageType)) {
            DamageSource source = new DamageSource(registry.entryOf(damageType));
            this.damageSourcePool.put(damageType, source);
            return source;
        } else {
            Thermoo.LOGGER.warn("Trying to use unknown damage type {}", damageType);
            return null;
        }
    }

    private void invalidateRegistryCache() {
        this.registry = null;
        this.damageSourcePool.clear();
        Thermoo.LOGGER.info("Invalidated damage temperature effect registry cache");
    }

    public record Config(
            float amount,
            int damageInterval,
            RegistryKey<DamageType> damageType
    ) {

        public static Config fromJson(JsonElement json, JsonDeserializationContext context) throws JsonSyntaxException {
            JsonObject object = json.getAsJsonObject();

            float amount = object.get("amount").getAsFloat();

            int damageInterval = object.get("damage_interval").getAsInt();

            Identifier damageTypeID = new Identifier(object.get("damage_type").getAsString());

            RegistryKey<DamageType> damageType = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeID);

            return new Config(amount, damageInterval, damageType);
        }

    }

}

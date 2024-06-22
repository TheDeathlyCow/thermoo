package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Applies damage to {@link net.minecraft.entity.LivingEntity}s when their temperature scale is within a given range.
 * The amount and interval of the damage pulses can be configured, as well as the damage type. However, the {@link DamageSource}
 * applied only stores the type - the direct source entity, attacker, and position are all {@code null}.
 */
public class DamageTemperatureEffect extends TemperatureEffect<DamageTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codecs.POSITIVE_FLOAT
                            .fieldOf("amount")
                            .forGetter(Config::amount),
                    Codecs.POSITIVE_INT
                            .fieldOf("damage_interval")
                            .forGetter(Config::damageInterval),
                    RegistryKey.createCodec(RegistryKeys.DAMAGE_TYPE)
                            .fieldOf("damage_type")
                            .forGetter(config -> config.damageType)
            ).apply(instance, Config::new)
    );

    @Nullable
    private Registry<DamageType> registry;

    private final Map<RegistryKey<DamageType>, DamageSource> damageSourcePool = new HashMap<>();

    public DamageTemperatureEffect(Codec<Config> codec) {
        super(codec);
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> this.invalidateRegistryCache()
        );
        ServerLifecycleEvents.SERVER_STOPPING.register(
                server -> this.invalidateRegistryCache()
        );
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(
                (server, resourceManager) -> this.invalidateRegistryCache()
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

    @Nullable
    private DamageSource getDamageSourceFromType(RegistryKey<DamageType> damageType, Registry<DamageType> registry) {
        return this.damageSourcePool.computeIfAbsent(
                damageType,
                key -> {
                    if (!registry.contains(key)) {
                        Thermoo.LOGGER.error("Temperature effect trying to use unknown damage type {}", key);
                        return null;
                    }
                    return new DamageSource(registry.entryOf(key));
                }
        );
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

    }

}

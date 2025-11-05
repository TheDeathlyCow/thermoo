package com.github.thedeathlycow.thermoo.api.temperature.effects;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Applies damage to {@link net.minecraft.world.entity.LivingEntity}s when their temperature scale is within a given range.
 * The amount and interval of the damage pulses can be configured, as well as the damage type. However, the {@link DamageSource}
 * applied only stores the type - the direct source entity, attacker, and position are all {@code null}.
 */
public class DamageTemperatureEffect extends TemperatureEffect<DamageTemperatureEffect.Config> {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExtraCodecs.POSITIVE_FLOAT
                            .fieldOf("amount")
                            .forGetter(Config::amount),
                    ExtraCodecs.POSITIVE_INT
                            .fieldOf("damage_interval")
                            .forGetter(Config::damageInterval),
                    ResourceKey.codec(Registries.DAMAGE_TYPE)
                            .fieldOf("damage_type")
                            .forGetter(config -> config.damageType)
            ).apply(instance, Config::new)
    );

    @Nullable
    private Registry<DamageType> registry;

    private final Map<ResourceKey<DamageType>, DamageSource> damageSourcePool = new HashMap<>();

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
    public void apply(LivingEntity victim, ServerLevel serverWorld, Config config) {

        if (registry == null) {
            RegistryAccess registryManager = serverWorld.getServer().registryAccess();
            this.registry = registryManager.registryOrThrow(Registries.DAMAGE_TYPE);
        }

        DamageSource source = this.getDamageSourceFromType(config.damageType, this.registry);
        if (source != null) {
            victim.hurt(source, config.amount);
        }
    }

    @Override
    public boolean shouldApply(LivingEntity victim, Config config) {
        return victim.tickCount % config.damageInterval == 0 && config.amount != 0.0f;
    }

    @Nullable
    private DamageSource getDamageSourceFromType(ResourceKey<DamageType> damageType, Registry<DamageType> registry) {
        return this.damageSourcePool.computeIfAbsent(
                damageType,
                key -> {
                    if (!registry.containsKey(key)) {
                        Thermoo.LOGGER.error("Temperature effect trying to use unknown damage type {}", key);
                        return null;
                    }
                    return new DamageSource(registry.getHolderOrThrow(key));
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
            ResourceKey<DamageType> damageType
    ) {

    }

}

package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag.TemperatureStatusTags;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.mc.core.api.YumiMods;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.List;

public final class TemperatureStatusManager {
    public static List<Holder.Reference<TemperatureStatus>> getEffects(LivingEntity entity, HolderLookup<TemperatureStatus> lookup) {
        Holder<EntityType<?>> typeHolder = entity.typeHolder();
        TemperatureEffectCache cache = (TemperatureEffectCache) typeHolder.value();
        var statuses = cache.thermoo$getStatuses();

        if (statuses == null) {
            statuses = lookup(typeHolder, lookup);
            cache.thermoo$setStatuses(statuses);

            if (YumiMods.get().isDevelopmentEnvironment() && entity instanceof Player && Thermoo.LOGGER.isInfoEnabled()) {
                Thermoo.LOGGER.info("Player temperature statuses: {}", statuses.stream().map(ref -> ref.key().identifier()).toList());
            }
        }

        return statuses;
    }

    public static void clearCaches(MinecraftServer server) {
        if (YumiMods.get().isDevelopmentEnvironment()) {
            Thermoo.LOGGER.info("Clearing temperature effect cache");
        }

        BuiltInRegistries.ENTITY_TYPE.forEach(entityType -> {
            TemperatureEffectCache cache = (TemperatureEffectCache) entityType;
            cache.thermoo$setStatuses(null);
        });
    }

    public static List<Holder.Reference<TemperatureStatus>> lookup(Holder<EntityType<?>> type, HolderLookup<TemperatureStatus> lookup) {
        return lookup.listElements()
                .filter(statusRef -> statusRef.value().selector().appliesToEntityType(type))
                .sorted(orderByTag(lookup))
                .toList();
    }

    private static Comparator<Holder<TemperatureStatus>> orderByTag(HolderLookup<TemperatureStatus> lookup) {
        Object2IntMap<Holder<TemperatureStatus>> ordering = collectIndices(getOrderOrEmpty(lookup, TemperatureStatusTags.APPLICATION_ORDER));

        return (a, b) -> {
            int ai = ordering.getOrDefault(a, Integer.MAX_VALUE);
            int bi = ordering.getOrDefault(b, Integer.MAX_VALUE);
            return Integer.compare(ai, bi);
        };
    }

    private static Object2IntMap<Holder<TemperatureStatus>> collectIndices(HolderSet<TemperatureStatus> order) {
        int size = order.size();
        Object2IntMap<Holder<TemperatureStatus>> indices = size < 10
                ? new Object2IntArrayMap<>(size)
                : new Object2IntOpenHashMap<>(size);

        for (int i = 0; i < size; i++) {
            var ref = order.get(i);
            indices.put(ref, i);
        }

        return indices;
    }

    private static HolderSet<TemperatureStatus> getOrderOrEmpty(HolderLookup<TemperatureStatus> lookup, TagKey<TemperatureStatus> tag) {
        HolderSet<TemperatureStatus> order = lookup.get(tag).orElse(null);

        if (order == null) {
            order = HolderSet.empty();
        }

        return order;
    }

    private TemperatureStatusManager() {

    }
}
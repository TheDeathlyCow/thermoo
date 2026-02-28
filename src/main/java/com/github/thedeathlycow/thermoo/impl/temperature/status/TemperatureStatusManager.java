package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusTags;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Comparator;
import java.util.List;

public class TemperatureStatusManager {
    public static List<Holder.Reference<TemperatureStatus>> getEffects(LivingEntity entity, HolderLookup<TemperatureStatus> lookup) {
        Holder<EntityType<?>> typeHolder = entity.typeHolder();
        TemperatureEffectCache cache = (TemperatureEffectCache) typeHolder.value();
        var effects = cache.thermoo$getEffects();

        if (effects == null) {
            effects = lookup(typeHolder, lookup);
            cache.thermoo$setEffects(effects);
        }

        return effects;
    }

    @VisibleForTesting
    public static List<Holder.Reference<TemperatureStatus>> lookup(Holder<EntityType<?>> type, HolderLookup<TemperatureStatus> lookup) {
        return lookup.listElements()
                .filter(statusRef -> {
                    HolderSet<EntityType<?>> set = statusRef.value().selector().entityTypes();
                    return set.size() == 0 || set.contains(type);
                })
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
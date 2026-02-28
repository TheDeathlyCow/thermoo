package com.github.thedeathlycow.thermoo.impl.temperature.status;

import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

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

    private static List<Holder.Reference<TemperatureStatus>> lookup(Holder<EntityType<?>> type, HolderLookup<TemperatureStatus> lookup) {
        return lookup.listElements()
                .filter(statusRef -> {
                    HolderSet<EntityType<?>> set = statusRef.value().selector().entityTypes();
                    return set.size() == 0 || set.contains(type);
                })
                .toList();
    }

    private TemperatureStatusManager() {

    }
}
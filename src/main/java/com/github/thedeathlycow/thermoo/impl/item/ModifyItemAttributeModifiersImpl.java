package com.github.thedeathlycow.thermoo.impl.item;

import com.github.thedeathlycow.thermoo.api.item.ModifyItemAttributeModifiersCallback;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.mixin.common.accessor.AttributeModifiersComponentBuilderAccessor;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.exception.UncheckedException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public final class ModifyItemAttributeModifiersImpl {
    private static final Cache<ItemStack, AttributeModifiersComponent> CACHE = CacheBuilder.newBuilder()
            .weakKeys()
            .maximumSize(25L)
            .build();

    public static void initialize() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> CACHE.invalidateAll());
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 20 == 0) {
                Thermoo.LOGGER.info("Modifier cache size: {}", CACHE.size());
            }
        });
    }

    public static AttributeModifiersComponent invoke(ItemStack stack, AttributeModifiersComponent base) {
        try {
            return CACHE.get(
                    stack,
                    () -> {
                        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
                        AttributeModifiersComponentBuilderAccessor accessor = (AttributeModifiersComponentBuilderAccessor) builder;
                        accessor.scorchful$getEntries().addAll(base.modifiers());

                        ModifyItemAttributeModifiersCallback.EVENT.invoker().modifyAttributeModifiers(stack, builder);

                        return new AttributeModifiersComponent(removeDuplicates(builder.build().modifiers()), base.showInTooltip());
                    }
            );
        } catch (ExecutionException e) {
            throw new UncheckedException(e);
        }
    }

    private static List<AttributeModifiersComponent.Entry> removeDuplicates(Collection<AttributeModifiersComponent.Entry> modifiers) {
        Map<Pair<RegistryKey<EntityAttribute>, Identifier>, AttributeModifiersComponent.Entry> map = new LinkedHashMap<>();

        // de-duplicates the modifiers to remove any entries that modify the same attribute and have the same ID
        for (var modifier : modifiers) {
            Pair<RegistryKey<EntityAttribute>, Identifier> key = Pair.of(
                    modifier.attribute().getKey().orElseThrow(),
                    modifier.modifier().id()
            );
            map.put(key, modifier);
        }

        return map.values().stream().toList();
    }

    private ModifyItemAttributeModifiersImpl() {

    }
}
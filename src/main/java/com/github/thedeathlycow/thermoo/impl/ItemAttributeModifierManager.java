package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeModifierManager implements ModifyItemAttributeModifiersCallback {

    @Nullable
    private DynamicRegistryManager manager;

    public static void registerToEventsCommon(ItemAttributeModifierManager instance) {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            instance.manager = server.getRegistryManager();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            instance.manager = null;
        });
        ModifyItemAttributeModifiersCallback.EVENT.register(instance);
    }

    public static void registerToEventsClient(ItemAttributeModifierManager instance) {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (instance.manager == null) {
                instance.manager = handler.getRegistryManager();
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            instance.manager = null;
        });
    }

    @Override
    public void modifyAttributeModifiers(
            ItemStack stack,
            EquipmentSlot slot,
            Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers
    ) {
        if (manager != null) {
            manager.getOptional(ThermooRegistryKeys.ITEM_ATTRIBUTE_MODIFIER)
                    .ifPresent(
                            registry -> {
                                registry.streamEntries().forEach(
                                        entry -> {
                                            entry.value().apply(stack, slot, attributeModifiers);
                                        }
                                );
                            }
                    );
        } else {
            Thermoo.LOGGER.info("Manager is null");
        }
    }
}

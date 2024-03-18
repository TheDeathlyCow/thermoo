package com.github.thedeathlycow.thermoo.impl;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.attribute.ItemAttributeModifier;
import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistryView;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.Nullable;

/**
 * Applies {@link com.github.thedeathlycow.thermoo.api.attribute.ItemAttributeModifier}s to the default attributes of
 * item stacks
 */
public class ItemAttributeModifierManager implements ModifyItemAttributeModifiersCallback {

    public static final ItemAttributeModifierManager INSTANCE = new ItemAttributeModifierManager();

    @Nullable
    private DynamicRegistryManager manager;

    public void registerToEventsCommon() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            this.manager = server.getRegistryManager();
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (this.manager == null) {
                return;
            }

            Registry<ItemAttributeModifier> registry = this.manager.get(ThermooRegistryKeys.ITEM_ATTRIBUTE_MODIFIER);
            Thermoo.LOGGER.info("Loaded {} items attribute modifier(s)", registry != null ? registry.size() : 0);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            this.manager = null;
        });
        ModifyItemAttributeModifiersCallback.EVENT.register(this);
    }

    public void registerToEventsClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (this.manager == null) {
                this.manager = handler.getRegistryManager();
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            this.manager = null;
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

    private ItemAttributeModifierManager() {

    }
}

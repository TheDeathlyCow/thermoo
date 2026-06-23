package com.github.thedeathlycow.thermoo.impl.command;

import com.github.thedeathlycow.thermoo.mixin.common.accessor.ArgumentTypeInfosAccessor;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

/// A platform independent abstraction for Argument Type registration, based on the equivalent class provided by Fabric
///
/// This is not a stable API!
public class ThermooArgumentTypeRegistry {
    public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void registerArgumentType(
            Identifier id, Class<? extends A> clazz, ArgumentTypeInfo<A, T> serializer) {
        ArgumentTypeInfosAccessor.thermoo_getClassMap().put(clazz, serializer);
        Registry.register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, id, serializer);
    }
}
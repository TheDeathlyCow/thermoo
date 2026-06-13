package com.github.thedeathlycow.thermoo.impl.platform.event;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.mojang.brigadier.CommandDispatcher;
import dev.yumi.commons.event.Event;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.Identifier;

/// A platform independent abstraction for a Command Registration API, based on the equivalent class provided by Fabric
///
/// This is not a stable API!
public interface ThermooCommandRegistrationCallback {
    Event<Identifier, ThermooCommandRegistrationCallback> EVENT = Thermoo.IMPL_EVENT_MANAGER.create(
            ThermooCommandRegistrationCallback.class,
            listeners -> (dispatcher, buildContext, selection) -> {
                for (ThermooCommandRegistrationCallback listener : listeners) {
                    listener.register(dispatcher, buildContext, selection);
                }
            });

    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection);
}
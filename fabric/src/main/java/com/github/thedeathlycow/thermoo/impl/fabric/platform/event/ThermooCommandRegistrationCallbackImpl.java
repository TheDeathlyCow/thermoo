package com.github.thedeathlycow.thermoo.impl.fabric.platform.event;

import com.github.thedeathlycow.thermoo.impl.platform.event.ThermooCommandRegistrationCallback;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ThermooCommandRegistrationCallbackImpl implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
            ThermooCommandRegistrationCallback.EVENT.invoker().register(dispatcher, buildContext, selection);
        });
    }
}
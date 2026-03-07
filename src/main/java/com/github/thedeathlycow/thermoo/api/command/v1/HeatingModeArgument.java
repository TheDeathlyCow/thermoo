package com.github.thedeathlycow.thermoo.api.command.v1;

import com.github.thedeathlycow.thermoo.api.core.v1.HeatingModes;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.util.StringRepresentable;

/**
 * An argument type to specify a value of the {@link HeatingModes} enum.
 */
public final class HeatingModeArgument extends StringRepresentableArgument<HeatingModes> {

    public static final Codec<HeatingModes> CODEC = StringRepresentable.fromEnum(HeatingModes::values);

    private HeatingModeArgument() {
        super(CODEC, HeatingModes::values);
    }

    public static HeatingModeArgument heatingMode() {
        return new HeatingModeArgument();
    }

    public static HeatingModes getHeatingMode(CommandContext<CommandSourceStack> context, String id) {
        return context.getArgument(id, HeatingModes.class);
    }
}

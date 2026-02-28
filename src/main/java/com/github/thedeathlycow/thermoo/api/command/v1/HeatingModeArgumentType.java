package com.github.thedeathlycow.thermoo.api.command.v1;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.github.thedeathlycow.thermoo.impl.command.TemperatureCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.util.StringRepresentable;

/**
 * An argument type to specify a value of the {@link HeatingModes} enum.
 */
public final class HeatingModeArgumentType extends StringRepresentableArgument<HeatingModes> {

    public static final Codec<HeatingModes> CODEC = StringRepresentable.fromEnum(HeatingModes::values);

    private HeatingModeArgumentType() {
        super(CODEC, HeatingModes::values);
    }

    public static HeatingModeArgumentType heatingMode() {
        return new HeatingModeArgumentType();
    }

    public static HeatingModes getHeatingMode(CommandContext<CommandSourceStack> context, String id) {
        return context.getArgument(id, HeatingModes.class);
    }
}

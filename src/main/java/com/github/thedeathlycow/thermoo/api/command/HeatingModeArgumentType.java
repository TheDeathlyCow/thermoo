package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.util.StringRepresentable;

/**
 * Enum argument type for {@link TemperatureCommand}
 */
public class HeatingModeArgumentType extends StringRepresentableArgument<HeatingModes> {

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

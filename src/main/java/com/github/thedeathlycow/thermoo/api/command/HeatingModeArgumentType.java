package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.temperature.HeatingModes;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;

public class HeatingModeArgumentType extends EnumArgumentType<HeatingModes> {

    public static final Codec<HeatingModes> CODEC = StringIdentifiable.createCodec(HeatingModes::values);

    private HeatingModeArgumentType() {
        super(CODEC, HeatingModes::values);
    }

    public static HeatingModeArgumentType heatingMode() {
        return new HeatingModeArgumentType();
    }

    public static HeatingModes getHeatingMode(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, HeatingModes.class);
    }
}

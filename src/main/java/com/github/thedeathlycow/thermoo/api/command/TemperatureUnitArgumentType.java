package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;

public class TemperatureUnitArgumentType extends EnumArgumentType<TemperatureUnit> {
    public static final Codec<TemperatureUnit> CODEC = StringIdentifiable.createCodec(TemperatureUnit::values);

    private TemperatureUnitArgumentType() {
        super(CODEC, TemperatureUnit::values);
    }

    public static TemperatureUnitArgumentType temperatureUnit() {
        return new TemperatureUnitArgumentType();
    }

    public static TemperatureUnit getTemperatureUnit(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, TemperatureUnit.class);
    }
}

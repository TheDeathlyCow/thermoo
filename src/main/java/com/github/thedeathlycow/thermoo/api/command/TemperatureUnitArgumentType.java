package com.github.thedeathlycow.thermoo.api.command;

import com.github.thedeathlycow.thermoo.api.util.TemperatureUnit;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.util.StringRepresentable;

public final class TemperatureUnitArgumentType extends StringRepresentableArgument<TemperatureUnit> {
    public static final Codec<TemperatureUnit> CODEC = StringRepresentable.fromEnum(TemperatureUnit::values);

    private TemperatureUnitArgumentType() {
        super(CODEC, TemperatureUnit::values);
    }

    public static TemperatureUnitArgumentType temperatureUnit() {
        return new TemperatureUnitArgumentType();
    }

    public static TemperatureUnit getTemperatureUnit(CommandContext<CommandSourceStack> context, String id) {
        return context.getArgument(id, TemperatureUnit.class);
    }
}

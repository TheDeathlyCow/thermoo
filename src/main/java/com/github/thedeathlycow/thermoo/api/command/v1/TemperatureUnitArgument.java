package com.github.thedeathlycow.thermoo.api.command.v1;

import com.github.thedeathlycow.thermoo.api.util.v1.TemperatureUnit;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.util.StringRepresentable;

/**
 * An argument type to specify {@link TemperatureUnit}s.
 */
public final class TemperatureUnitArgument extends StringRepresentableArgument<TemperatureUnit> {
    public static final Codec<TemperatureUnit> CODEC = StringRepresentable.fromEnum(TemperatureUnit::values);

    private TemperatureUnitArgument() {
        super(CODEC, TemperatureUnit::values);
    }

    public static TemperatureUnitArgument temperatureUnit() {
        return new TemperatureUnitArgument();
    }

    public static TemperatureUnit getTemperatureUnit(CommandContext<CommandSourceStack> context, String id) {
        return context.getArgument(id, TemperatureUnit.class);
    }
}

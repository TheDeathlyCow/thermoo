package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.environment.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public final class SetPressureFromAltitude implements EnvironmentProvider {
    // mbar / meter
    private static final double DEFAULT_PRESSURE_CHANGE = -0.12;

    public static final MapCodec<SetPressureFromAltitude> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    AtmosphericPressureComponent.CODEC
                            .optionalFieldOf("pressure_change_mbar_per_meter", DEFAULT_PRESSURE_CHANGE)
                            .forGetter(SetPressureFromAltitude::pressureChangePerMeter)
            ).apply(instance, SetPressureFromAltitude::new)
    );

    private final double pressureChangeMbarPerMeter;

    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        int altitude = pos.getY() - level.getSeaLevel();

        double seaLevelPressure = builder.getOrDefault(EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE, AtmosphericPressureComponent.DEFAULT);

        double deltaP = pressureChangeMbarPerMeter * altitude;

        builder.set(
                EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE,
                seaLevelPressure + deltaP
        );
    }

    @Override
    public EnvironmentProviderType<SetPressureFromAltitude> getType() {
        return EnvironmentProviderTypes.SET_PRESSURE_FROM_ALTITUDE;
    }

    public double pressureChangePerMeter() {
        return this.pressureChangeMbarPerMeter;
    }

    private SetPressureFromAltitude(double pressureChangeMbarPerMeter) {
        this.pressureChangeMbarPerMeter = pressureChangeMbarPerMeter;
    }

    private SetPressureFromAltitude() {
        this(DEFAULT_PRESSURE_CHANGE);
    }
}
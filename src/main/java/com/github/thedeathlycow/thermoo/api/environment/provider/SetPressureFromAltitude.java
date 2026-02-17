package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.environment.component.AtmosphericPressureComponent;
import com.github.thedeathlycow.thermoo.api.environment.component.EnvironmentComponentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

/**
 * An environment provider that sets the pressure component based on altitude above or below
 * {@linkplain Level#getSeaLevel() sea level}.
 */
public final class SetPressureFromAltitude implements EnvironmentProvider {
    private static final double DEFAULT_PRESSURE_CHANGE = -0.12; // in mbar/block

    public static final MapCodec<SetPressureFromAltitude> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.DOUBLE
                            .optionalFieldOf("pressure_change_mbar_per_block", DEFAULT_PRESSURE_CHANGE)
                            .forGetter(SetPressureFromAltitude::pressureChangePerBlock)
            ).apply(instance, SetPressureFromAltitude::new)
    );

    private final double pressureChangeMbarPerBlock;

    private SetPressureFromAltitude(double pressureChangeMbarPerBlock) {
        this.pressureChangeMbarPerBlock = pressureChangeMbarPerBlock;
    }

    public static SetPressureFromAltitude create(double pressureChangeMbarPerMeter) {
        if (!Double.isFinite(pressureChangeMbarPerMeter)) {
            throw new IllegalArgumentException("Pressure change must be finite");
        }

        return new SetPressureFromAltitude(pressureChangeMbarPerMeter);
    }

    public static SetPressureFromAltitude create() {
        return new SetPressureFromAltitude(DEFAULT_PRESSURE_CHANGE);
    }

    /**
     * Sets the atmospheric pressure based on the queried {@code pos} altitude above {@linkplain Level#getSeaLevel() sea level}.
     * <p>
     * The position is clamped to the minimum and maximum bounds of the world, and the final pressure will not be less
     * than 0.
     * <p>
     * Any pressure values currently in the map are treated as the pressure at sea level.
     *
     * @param level   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A component map builder to append to
     */
    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        int altitude = Mth.clamp(pos.getY(), level.getMinY(), level.getMaxY()) - level.getSeaLevel();

        double seaLevelPressure = builder.getOrDefault(EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE, AtmosphericPressureComponent.DEFAULT);

        double deltaP = pressureChangeMbarPerBlock * altitude;

        builder.set(
                EnvironmentComponentTypes.ATMOSPHERIC_PRESSURE,
                Math.max(0, seaLevelPressure + deltaP)
        );
    }

    @Override
    public EnvironmentProviderType<SetPressureFromAltitude> getType() {
        return EnvironmentProviderTypes.SET_PRESSURE_FROM_ALTITUDE;
    }

    /**
     * A finite value that sets how much to adjust pressure by, expressed in millibars per block above sea level.
     */
    public double pressureChangePerBlock() {
        return this.pressureChangeMbarPerBlock;
    }
}
package com.github.thedeathlycow.thermoo.api.environment.v2.provider;

import com.github.thedeathlycow.thermoo.api.environment.v2.component.EnvironmentComponentTypes;
import com.github.thedeathlycow.thermoo.api.environment.v2.component.TemperatureRecordComponent;
import com.github.thedeathlycow.thermoo.api.util.v1.TemperatureRecord;
import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.mixin.common.accessor.ComponentMapBuilderAccessor;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Contract;

/**
 * Intended to be used as a modifier, this provider shifts the existing temperature component in a map by a given value.
 */
public final class TemperatureShiftEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<TemperatureShiftEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    TemperatureRecord.CODEC
                            .fieldOf("shift")
                            .forGetter(TemperatureShiftEnvironmentProvider::shift)
            ).apply(instance, TemperatureShiftEnvironmentProvider::new)
    );

    private final TemperatureRecord shift;

    private TemperatureShiftEnvironmentProvider(TemperatureRecord shift) {
        this.shift = shift;
    }

    /**
     * Creates a new provider
     *
     * @param shift Provider temperature shift value
     * @return Returns a new temperature shift provider instance
     */
    @Contract("_->new")
    public static TemperatureShiftEnvironmentProvider create(TemperatureRecord shift) {
        return new TemperatureShiftEnvironmentProvider(shift);
    }

    /**
     * {@linkplain TemperatureRecord#add(TemperatureRecord) Adds} the shift value of this provider to the existing
     * temperature component in the builder. If no temperature component is in the builder, then this will skip and log
     * a warning.
     *
     * @param level   The world/level being queried
     * @param pos     The position in the world to query
     * @param biome   The biome at the position in the world
     * @param builder A reducible component map builder to append to
     */
    @Override
    public void buildCurrentComponents(Level level, BlockPos pos, Holder<Biome> biome, DataComponentMap.Builder builder) {
        ComponentMapBuilderAccessor accessor = (ComponentMapBuilderAccessor) builder;
        if (builder.contains(EnvironmentComponentTypes.TEMPERATURE)) {
            TemperatureRecord base = builder.getOrDefault(EnvironmentComponentTypes.TEMPERATURE, TemperatureRecordComponent.DEFAULT);
            TemperatureRecord shifted = base.add(this.shift);
            builder.set(EnvironmentComponentTypes.TEMPERATURE, shifted);
        } else {
            Thermoo.LOGGER.warn("Unable to shift a missing temperature component in: {}", accessor.thermoo$getMap());
        }
    }

    @Override
    public EnvironmentProviderType<TemperatureShiftEnvironmentProvider> getType() {
        return EnvironmentProviderTypes.TEMPERATURE_SHIFT;
    }

    public TemperatureRecord shift() {
        return shift;
    }
}
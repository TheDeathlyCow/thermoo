package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.core.v1.ThermooLevel;
import com.github.thedeathlycow.thermoo.api.core.v1.source.BuiltinTemperatureSources;
import com.github.thedeathlycow.thermoo.impl.core.BuiltinTemperatureSourcesImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin implements ThermooLevel {
    @Unique
    private BuiltinTemperatureSources thermoo$temperatureSources;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(
            final WritableLevelData levelData,
            final ResourceKey<Level> dimension,
            final RegistryAccess registryAccess,
            final Holder<DimensionType> dimensionTypeRegistration,
            final boolean isClientSide,
            final boolean isDebug,
            final long biomeZoomSeed,
            final int maxChainedNeighborUpdates,
            CallbackInfo ci
    ) {
        this.thermoo$temperatureSources = new BuiltinTemperatureSourcesImpl(registryAccess);
    }

    @Override
    @Unique
    public BuiltinTemperatureSources thermoo$temperatureSources() {
        return this.thermoo$temperatureSources;
    }
}
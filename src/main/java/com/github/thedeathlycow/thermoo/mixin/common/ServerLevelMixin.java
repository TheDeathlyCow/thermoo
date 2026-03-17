package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.core.v1.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.core.v1.TemperatureChange;
import com.github.thedeathlycow.thermoo.impl.core.ThermooServerLevel;
import com.github.thedeathlycow.thermoo.impl.core.UpdateEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements ThermooServerLevel {
    @Unique
    private List<TemperatureChange> thermoo$tickingTemperatureSources;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void init(
            final MinecraftServer server,
            final Executor executor,
            final LevelStorageSource.LevelStorageAccess levelStorage,
            final ServerLevelData levelData,
            final ResourceKey<Level> dimension,
            final LevelStem levelStem,
            final boolean isDebug,
            final long biomeZoomSeed,
            final List<CustomSpawner> customSpawners,
            final boolean tickTime,
            CallbackInfo ci
    ) {
        this.thermoo$tickingTemperatureSources = server.registryAccess().lookupOrThrow(ThermooRegistryKeys.TEMPERATURE_SOURCE)
                .listElements()
                .filter(ref -> ref.value().tickInterval() > 0 && UpdateEvents.hasRegisteredEvents(ref.key()))
                .map(TemperatureChange::create)
                .toList();
    }
    
    @Override
    public List<TemperatureChange> thermoo$tickingTemperatureSources() {
        return this.thermoo$tickingTemperatureSources;
    }
}
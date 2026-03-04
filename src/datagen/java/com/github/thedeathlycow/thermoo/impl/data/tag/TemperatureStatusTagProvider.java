package com.github.thedeathlycow.thermoo.impl.data.tag;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatusTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class TemperatureStatusTagProvider extends FabricTagsProvider<TemperatureStatus> {
    public TemperatureStatusTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, ThermooRegistryKeys.TEMPERATURE_STATUS, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(TemperatureStatusTags.APPLICATION_ORDER);

        builder(TemperatureStatusTags.HARMFUL);

        builder(TemperatureStatusTags.BENEFICIAL);

        builder(TemperatureStatusTags.NEUTRAL);

        builder(TemperatureStatusTags.COLD);

        builder(TemperatureStatusTags.WARM);
    }
}
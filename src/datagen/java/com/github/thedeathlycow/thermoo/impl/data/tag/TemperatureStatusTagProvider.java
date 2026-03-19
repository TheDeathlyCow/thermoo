package com.github.thedeathlycow.thermoo.impl.data.tag;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag.ConventionalTemperatureStatusTags;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.tag.TemperatureStatusTags;
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
        generateThermooTags();

        generateConventionalTags();

        generateTagAliases();
    }

    private void generateThermooTags() {
        builder(TemperatureStatusTags.APPLICATION_ORDER);

        builder(TemperatureStatusTags.HARMFUL);

        builder(TemperatureStatusTags.BENEFICIAL);

        builder(TemperatureStatusTags.NEUTRAL);

        builder(TemperatureStatusTags.COLD);

        builder(TemperatureStatusTags.WARM);
    }

    private void generateConventionalTags() {
        builder(ConventionalTemperatureStatusTags.HARMFUL);

        builder(ConventionalTemperatureStatusTags.BENEFICIAL);

        builder(ConventionalTemperatureStatusTags.NEUTRAL);

        builder(ConventionalTemperatureStatusTags.COLD);

        builder(ConventionalTemperatureStatusTags.WARM);
    }

    private void generateTagAliases() {
        aliasGroup("harmful").add(TemperatureStatusTags.HARMFUL).add(ConventionalTemperatureStatusTags.HARMFUL);

        aliasGroup("beneficial").add(TemperatureStatusTags.BENEFICIAL).add(ConventionalTemperatureStatusTags.BENEFICIAL);

        aliasGroup("neutral").add(TemperatureStatusTags.NEUTRAL).add(ConventionalTemperatureStatusTags.NEUTRAL);

        aliasGroup("cold").add(TemperatureStatusTags.COLD).add(ConventionalTemperatureStatusTags.COLD);

        aliasGroup("warm").add(TemperatureStatusTags.WARM).add(ConventionalTemperatureStatusTags.WARM);
    }
}
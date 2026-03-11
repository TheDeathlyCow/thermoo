package com.github.thedeathlycow.thermoo.impl.data;

import com.github.thedeathlycow.thermoo.api.ThermooAttributes;
import com.github.thedeathlycow.thermoo.api.core.v1.source.*;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class TemperatureSourceProvider extends FabricDynamicRegistryProvider {
    public TemperatureSourceProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(
                TemperatureSources.ABSOLUTE,
                TemperatureSource.builder(Component.empty())
                        .build()
        );

        entries.add(
                TemperatureSources.ACTIVE,
                TemperatureSource.builder(Component.empty())
                        .withReduction(ScaledAttributeReduction.create(
                                ThermooAttributes.FROST_RESISTANCE,
                                ThermooAttributes.HEAT_RESISTANCE,
                                0.1
                        ))
                        .withTickInterval(1)
                        .build()
        );

        entries.add(
                TemperatureSources.PASSIVE,
                TemperatureSource.builder(Component.empty())
                        .withReduction(ReinforcingAttributeReduction.create(
                                ThermooAttributes.FROST_RESISTANCE,
                                ThermooAttributes.HEAT_RESISTANCE,
                                0.1
                        ))
                        .withTickInterval(1)
                        .build()
        );

        entries.add(
                TemperatureSources.ENVIRONMENT,
                TemperatureSource.builder(Component.empty())
                        .withReduction(new RandomlyDodgeReduction(
                                ThermooAttributes.FROST_RESISTANCE,
                                ThermooAttributes.HEAT_RESISTANCE
                        ))
                        .build()
        );
    }

    @Override
    public String getName() {
        return "ThermooTemperatureSources";
    }
}
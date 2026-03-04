package com.github.thedeathlycow.thermoo.impl.data;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.Nullable;

public class ThermooDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return Thermoo.MODID;
    }
}
package com.github.thedeathlycow.thermoo.impl.data;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import com.github.thedeathlycow.thermoo.impl.data.tag.TemperatureStatusTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThermooDatagen implements DataGeneratorEntrypoint {
    public static final String MODID = Thermoo.MODID + "-datagen";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        LOGGER.info("Running Thermoo datagen");
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(TemperatureStatusTagProvider::new);
        pack.addProvider(TemperatureSourceProvider::new);
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return Thermoo.MODID;
    }
}
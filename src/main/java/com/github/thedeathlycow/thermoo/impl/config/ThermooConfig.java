package com.github.thedeathlycow.thermoo.impl.config;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.util.Properties;

public record ThermooConfig(
        boolean enableThermooPatchesBug,
        URI thermooPatchesPatchListUrl
) {
    private static final String ENABLE_THERMOO_PATCHES_BUG_KEY = "enable_thermoo_patches_bug";
    private static final String THERMOO_PATCHES_PATCH_LIST_URL_KEY = "thermoo_patches_patch_list_url";

    private ThermooConfig(Properties properties) {
        this(
                Boolean.parseBoolean(properties.getProperty(ENABLE_THERMOO_PATCHES_BUG_KEY)),
                URI.create(properties.getProperty(THERMOO_PATCHES_PATCH_LIST_URL_KEY))
        );
    }

    public static ThermooConfig initialize() {
        var properties = new Properties(newDefaultConfig());
        File configFile = getConfigFile();
        readConfig(properties, configFile);
        return new ThermooConfig(properties);
    }

    private static void readConfig(Properties properties, File file) {
        try (FileReader reader = new FileReader(file)){
            properties.load(reader);
        } catch (FileNotFoundException e) {
            writeConfig(properties, file);
        } catch (IOException e) {
            Thermoo.LOGGER.error("Unable to read Thermoo config file, falling back to default config", e);
        }
    }

    private static void writeConfig(Properties properties, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            properties.store(writer, "Thermoo Config file, used for internal configuration only.");
        } catch (IOException e) {
            Thermoo.LOGGER.error("Unable to write Thermoo default config file", e);
        }
    }

    private static Properties newDefaultConfig() {
        var properties = new Properties();

        properties.setProperty(ENABLE_THERMOO_PATCHES_BUG_KEY, "true");
        properties.setProperty(THERMOO_PATCHES_PATCH_LIST_URL_KEY, "https://gist.githubusercontent.com/TheDeathlyCow/1164d3721101f0540d455cc9f63fcac8/raw/74f939ed1a89a0e603891affd3bd9e2d8dc75c8b/thermoo-patches-patch-list.json");

        return properties;
    }

    private static File getConfigFile() {
        Path path = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("thermoo.properties");

        File file = path.toFile();

        if (!file.isFile()) {
            throw new IllegalStateException("Thermoo config is not a file");
        }

        return file;
    }
}
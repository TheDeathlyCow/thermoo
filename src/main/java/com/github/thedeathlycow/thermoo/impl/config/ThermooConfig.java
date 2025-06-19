package com.github.thedeathlycow.thermoo.impl.config;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Properties;

public record ThermooConfig(
        boolean enableThermooPatchesNag,
        URI thermooPatchesPatchListUrl
) {
    private static final String ENABLE_THERMOO_PATCHES_NAG_KEY = "enable_thermoo_patches_nag";
    private static final String THERMOO_PATCHES_PATCH_LIST_URL_KEY = "thermoo_patches_patch_list_url";

    private ThermooConfig(Properties properties) {
        this(
                Boolean.parseBoolean(properties.getProperty(ENABLE_THERMOO_PATCHES_NAG_KEY)),
                URI.create(properties.getProperty(THERMOO_PATCHES_PATCH_LIST_URL_KEY))
        );
    }

    public static ThermooConfig create() {
        var properties = new Properties(newDefaultConfig());
        Path configFile = getConfigPath();
        readConfig(properties, configFile);
        return new ThermooConfig(properties);
    }

    private static void readConfig(Properties properties, Path path) {
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (NoSuchFileException e) {
            writeConfig(newDefaultConfig(), path);
        } catch (IOException e) {
            Thermoo.LOGGER.error("Unable to read Thermoo config path, falling back to default config", e);
        }
    }

    private static void writeConfig(Properties properties, Path path) {
        try (OutputStream output = Files.newOutputStream(path)) {
            properties.store(output, "Thermoo Config path, used for internal configuration only.");
        } catch (IOException e) {
            Thermoo.LOGGER.error("Unable to write Thermoo default config path", e);
        }
    }

    private static Properties newDefaultConfig() {
        var properties = new Properties();

        properties.setProperty(ENABLE_THERMOO_PATCHES_NAG_KEY, "true");
        properties.setProperty(THERMOO_PATCHES_PATCH_LIST_URL_KEY, "https://thermoo.thedeathlycow.com/assets/thermoo-patches-patch-list.json");

        return properties;
    }

    private static Path getConfigPath() {
        return FabricLoader.getInstance()
                .getConfigDir()
                .resolve("thermoo.properties");
    }
}
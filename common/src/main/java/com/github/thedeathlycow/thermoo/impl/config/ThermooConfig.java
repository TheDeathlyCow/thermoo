/*
 * Thermoo: A temperature and environment library for Minecraft mods.
 * Copyright (C) 2026	TheDeathlyCow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.github.thedeathlycow.thermoo.impl.config;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import dev.yumi.mc.core.api.YumiMods;

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
        boolean enablePolymerPatch,
        URI thermooPatchesPatchListUrl
) {
    private static final String ENABLE_THERMOO_PATCHES_NAG_KEY = "enable_thermoo_patches_nag";
    private static final String ENABLE_POLYMER_PATCH_KEY = "enable_polymer_patch";
    private static final String THERMOO_PATCHES_PATCH_LIST_URL_KEY = "thermoo_patches_patch_list_url";

    private ThermooConfig(Properties properties) {
        this(
                Boolean.parseBoolean(properties.getProperty(ENABLE_THERMOO_PATCHES_NAG_KEY)),
                Boolean.parseBoolean(properties.getProperty(ENABLE_POLYMER_PATCH_KEY)),
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
            properties.store(output, "Thermoo Config file, used for internal configuration only.");
        } catch (IOException e) {
            Thermoo.LOGGER.error("Unable to write Thermoo default config path", e);
        }
    }

    private static Properties newDefaultConfig() {
        var properties = new Properties();

        properties.setProperty(ENABLE_THERMOO_PATCHES_NAG_KEY, "true");
        properties.setProperty(ENABLE_POLYMER_PATCH_KEY, "true");
        properties.setProperty(THERMOO_PATCHES_PATCH_LIST_URL_KEY, "https://thermoo.thedeathlycow.com/assets/thermoo-patches-patch-list-v2.json");

        return properties;
    }

    private static Path getConfigPath() {
        return YumiMods.get()
                .getConfigDirectory()
                .resolve("thermoo.properties");
    }
}
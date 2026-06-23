package com.github.thedeathlycow.thermoo.api.core.v2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageManager;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class TemperatureUnitLookup {
    /// ISO 3166 alpha-2 region codes.
    ///
    /// Sets the following locales to use the Fahrenheit system (shown in order they are assigned):
    /// United States, US Minor Outlying Islands, US Virgin Islands, American Samoa, Puerto Rico, Guam, Northern Mariana
    /// Islands, Liberia, The Bahamas, The Federated States of Micronesia, Marshall Islands, The Cayman Islands, Belize,
    /// Antigua and Barbuda, Saint Kitts and Nevis.
    ///
    /// This set could change in the future if these adopt the Celsius scale as their primarily unit of temperature.
    ///
    /// See [IANA language subtags](https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry).
    private static final Set<String> ISO_REGIONS_THAT_USE_FAHRENHEIT = new HashSet<>(
            Set.of("US", "UM", "VI", "AS", "PR", "GU", "MP", "LR", "BS", "FM", "MH", "KY", "BZ", "AG", "KN")
    );

    /// Gets the currently selected temperature unit from the player's currently selected language. For languages based
    /// on a language from the United States locale, this will be Fahrenheit. For all other locales, this is Celsius.
    ///
    /// Currently, the languages that will return Fahrenheit are English (U.S.), Hawaiian, and LOLCAT.
    ///
    /// This method is only available on the client.
    ///
    /// @return Returns Fahrenheit if the currently selected language is from a US-based locale.
    public static TemperatureUnit fromCurrentLanguage() {
        LanguageManager manager = Minecraft.getInstance().getLanguageManager();
        return manager.getSelected().endsWith("_us")
                ? TemperatureUnit.FAHRENHEIT
                : TemperatureUnit.CELSIUS;
    }

    /// Gets the temperature unit from the given language Minecraft language code. For languages based on a language from
    /// the United States locale, this will be Fahrenheit. For all other locales, this is Celsius.
    ///
    /// Currently, the languages that will return Fahrenheit are English (U.S.), Hawaiian, and LOLCAT.
    ///
    /// @return Returns Fahrenheit if the language code is from a US-based locale.
    public static TemperatureUnit fromLanguageCode(String mcLanguageCode) {
        return mcLanguageCode.endsWith("_us")
                ? TemperatureUnit.FAHRENHEIT
                : TemperatureUnit.CELSIUS;
    }

    /// Gets the temperature unit from the default locale. For the United States, Liberia, and a few Caribbean countries,
    /// this is Fahrenheit. For all others it is Celsius.
    ///
    /// @return Returns the temperature unit associated with the default locale
    public static TemperatureUnit fromLocale() {
        return fromLocale(Locale.getDefault());
    }

    /// Gets the temperature unit from the given locale. For the United States, Liberia, and a few Caribbean countries,
    /// this is Fahrenheit. For all others it is Celsius.
    ///
    /// @return Returns the temperature unit associated with the given locale
    public static TemperatureUnit fromLocale(Locale locale) {
        return ISO_REGIONS_THAT_USE_FAHRENHEIT.contains(locale.getCountry().toUpperCase())
                ? TemperatureUnit.FAHRENHEIT
                : TemperatureUnit.CELSIUS;
    }

    private TemperatureUnitLookup() {

    }
}
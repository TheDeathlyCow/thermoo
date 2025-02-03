package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;

public final class EnvironmentDefinition {
    public static final Codec<EnvironmentDefinition> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("biomes")
                            .forGetter(EnvironmentDefinition::biomes),
                    EnvironmentProvider.PROVIDER_CODEC
                            .fieldOf("provider")
                            .forGetter(EnvironmentDefinition::provider)
            ).apply(instance, EnvironmentDefinition::new)
    );

    private final RegistryEntryList<Biome> biomes;

    private final EnvironmentProvider provider;

    public EnvironmentDefinition(RegistryEntryList<Biome> biomes, EnvironmentProvider provider) {
        this.biomes = biomes;
        this.provider = provider;
    }

    public RegistryEntryList<Biome> biomes() {
        return this.biomes;
    }

    public EnvironmentProvider provider() {
        return this.provider;
    }
}
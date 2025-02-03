package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;

public final class Environment {
    public static final Codec<Environment> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .fieldOf("biomes")
                            .forGetter(Environment::biomes),
                    EnvironmentProvider.PROVIDER_CODEC
                            .fieldOf("provider")
                            .forGetter(Environment::provider)
            ).apply(instance, Environment::new)
    );

    private final RegistryEntryList<Biome> biomes;

    private final EnvironmentProvider provider;

    public Environment(RegistryEntryList<Biome> biomes, EnvironmentProvider provider) {
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
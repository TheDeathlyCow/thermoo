package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;

/**
 * Defines a biome's environmental temperature and relative humidity values. Must be defined in a datapack registry
 * in order to work.
 */
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

    private EnvironmentDefinition(RegistryEntryList<Biome> biomes, EnvironmentProvider provider) {
        this.biomes = biomes;
        this.provider = provider;
    }

    /**
     * Creates an environment definition
     * @param biomes The biomes this definition provides for
     * @param provider The provider of this definition
     * @return Returns a new definition
     */
    @Contract("_,_->new")
    public static EnvironmentDefinition create(RegistryEntryList<Biome> biomes, EnvironmentProvider provider) {
        return new EnvironmentDefinition(biomes, provider);
    }

    /**
     * @return The biomes that this definition provides an environment for
     */
    public RegistryEntryList<Biome> biomes() {
        return this.biomes;
    }

    /**
     * @return The environment provider for this definition
     */
    public EnvironmentProvider provider() {
        return this.provider;
    }
}
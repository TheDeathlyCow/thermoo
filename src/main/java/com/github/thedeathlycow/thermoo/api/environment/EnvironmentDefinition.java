package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
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
                    RegistryCodecs.entryList(RegistryKeys.BIOME)
                            .optionalFieldOf("exclude_biomes", RegistryEntryList.empty())
                            .forGetter(EnvironmentDefinition::excludeBiomes),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("provider")
                            .forGetter(EnvironmentDefinition::provider)
            ).apply(instance, EnvironmentDefinition::new)
    );

    private final RegistryEntryList<Biome> biomes;

    private final RegistryEntryList<Biome> excludeBiomes;

    private final RegistryEntry<EnvironmentProvider> provider;

    private EnvironmentDefinition(
            RegistryEntryList<Biome> biomes,
            RegistryEntryList<Biome> excludeBiomes,
            RegistryEntry<EnvironmentProvider> provider
    ) {
        this.biomes = biomes;
        this.excludeBiomes = excludeBiomes;
        this.provider = provider;
    }

    // TODO: replace create methods with a builder (especially when modifiers are added!)

    /**
     * Creates an environment definition
     *
     * @param biomes   The biomes this definition provides for
     * @param provider The base value provider of this definition
     * @return Returns a new definition
     */
    @Contract("_,_->new")
    public static EnvironmentDefinition create(RegistryEntryList<Biome> biomes, RegistryEntry<EnvironmentProvider> provider) {
        return new EnvironmentDefinition(biomes, RegistryEntryList.empty(), provider);
    }

    /**
     * Creates an environment definition
     *
     * @param biomes        The biomes this definition provides for
     * @param excludeBiomes The biomes this definition has been blocked from providing for
     * @param provider      The base value provider of this definition
     * @return Returns a new definition
     */
    @Contract("_,_,_->new")
    public static EnvironmentDefinition create(
            RegistryEntryList<Biome> biomes,
            RegistryEntryList<Biome> excludeBiomes,
            RegistryEntry<EnvironmentProvider> provider
    ) {
        return new EnvironmentDefinition(biomes, excludeBiomes, provider);
    }

    /**
     * Checks that this definition can provide an environment for the given biome
     *
     * @param biome The biome to check
     * @return Returns {@code true} if the biome is in this definition's {@linkplain #biomes() biome list}, and NOT in
     * this definition's {@linkplain #excludeBiomes() excluded biome list}.
     */
    public boolean providesFor(RegistryEntry<Biome> biome) {
        return this.biomes().contains(biome) && !this.excludeBiomes().contains(biome);
    }

    /**
     * The biomes that this environment provides for
     *
     * @return The biomes that this definition provides an environment for
     */
    public RegistryEntryList<Biome> biomes() {
        return this.biomes;
    }

    /**
     * The biomes that the environment has been blocked from providing for
     *
     * @return The biomes that this definition provides excludes
     */
    public RegistryEntryList<Biome> excludeBiomes() {
        return this.excludeBiomes;
    }

    /**
     * @return The environment provider for this definition
     */
    public RegistryEntry<EnvironmentProvider> provider() {
        return this.provider;
    }
}
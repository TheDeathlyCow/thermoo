package com.github.thedeathlycow.thermoo.api.environment;

import com.github.thedeathlycow.thermoo.api.environment.provider.EnvironmentProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Contract;

/**
 * Defines a biome's environmental temperature and relative humidity values. Must be defined in a datapack registry
 * in order to work.
 */
public final class EnvironmentDefinition {
    private static final int DEFAULT_PRIORITY = 1000;

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
                            .forGetter(EnvironmentDefinition::provider),
                    Codec.INT
                            .optionalFieldOf("priority", DEFAULT_PRIORITY)
                            .forGetter(EnvironmentDefinition::priority)
            ).apply(instance, EnvironmentDefinition::new)
    );

    private final RegistryEntryList<Biome> biomes;

    private final RegistryEntryList<Biome> excludeBiomes;

    private final RegistryEntry<EnvironmentProvider> provider;

    private final int priority;

    private EnvironmentDefinition(
            RegistryEntryList<Biome> biomes,
            RegistryEntryList<Biome> excludeBiomes,
            RegistryEntry<EnvironmentProvider> provider,
            int priority
    ) {
        this.biomes = biomes;
        this.excludeBiomes = excludeBiomes;
        this.provider = provider;
        this.priority = priority;
    }

    public static EnvironmentDefinition.Builder builder(
            RegistryEntryList<Biome> biomes,
            RegistryEntry<EnvironmentProvider> provider
    ) {
        return new Builder(biomes, provider);
    }

    /**
     * Creates an environment definition
     *
     * @param biomes   The biomes this definition provides for
     * @param provider The base value provider of this definition
     * @return Returns a new definition
     * @deprecated Use {@link #builder(RegistryEntryList, RegistryEntry)}
     */
    @Contract("_,_->new")
    @Deprecated(since = "4.5")
    public static EnvironmentDefinition create(RegistryEntryList<Biome> biomes, RegistryEntry<EnvironmentProvider> provider) {
        return builder(biomes, provider).build();
    }

    /**
     * Creates an environment definition
     *
     * @param biomes        The biomes this definition provides for
     * @param excludeBiomes The biomes this definition has been blocked from providing for
     * @param provider      The base value provider of this definition
     * @return Returns a new definition
     * @deprecated Use {@link #builder(RegistryEntryList, RegistryEntry)}
     */
    @Contract("_,_,_->new")
    @Deprecated(since = "4.5")
    public static EnvironmentDefinition create(
            RegistryEntryList<Biome> biomes,
            RegistryEntryList<Biome> excludeBiomes,
            RegistryEntry<EnvironmentProvider> provider
    ) {
        return builder(biomes, provider).excludeBiomes(excludeBiomes).build();
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

    /**
     * Determines the priority for which this environment should be applied to a biome. Environments with a HIGHER
     * priority will be applied FIRST, and environments with a LOWER priority will be applied LAST. Environments with the
     * same priority may be applied in any order.
     * <p>
     * The default priority is {@value DEFAULT_PRIORITY}.
     *
     * @return Returns this environment's priority.
     */
    public int priority() {
        return this.priority;
    }

    public static class Builder {
        private final RegistryEntryList<Biome> biomes;
        private final RegistryEntry<EnvironmentProvider> provider;
        private RegistryEntryList<Biome> excludeBiomes = RegistryEntryList.empty();
        private int priority = DEFAULT_PRIORITY;

        private Builder(RegistryEntryList<Biome> biomes, RegistryEntry<EnvironmentProvider> provider) {
            this.biomes = biomes;
            this.provider = provider;
        }

        public Builder excludeBiomes(RegistryEntryList<Biome> biomes) {
            this.excludeBiomes = biomes;
            return this;
        }

        public Builder withPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public EnvironmentDefinition build() {
            return new EnvironmentDefinition(
                    this.biomes,
                    this.excludeBiomes,
                    this.provider,
                    this.priority
            );
        }
    }
}
package com.github.thedeathlycow.thermoo.api.environment.provider;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.util.component.ReducibleComponentMapBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public final class ModifyEnvironmentProvider implements EnvironmentProvider {
    public static final MapCodec<ModifyEnvironmentProvider> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryCodecs.entryList(ThermooRegistryKeys.ENVIRONMENT_PROVIDER)
                            .fieldOf("modifiers")
                            .forGetter(ModifyEnvironmentProvider::modifiers),
                    EnvironmentProvider.ENTRY_CODEC
                            .fieldOf("base")
                            .forGetter(ModifyEnvironmentProvider::base)
            ).apply(instance, ModifyEnvironmentProvider::new)
    );

    private final RegistryEntryList<EnvironmentProvider> modifiers;
    private final RegistryEntry<EnvironmentProvider> base;

    private ModifyEnvironmentProvider(
            RegistryEntryList<EnvironmentProvider> modifiers,
            RegistryEntry<EnvironmentProvider> base
    ) {
        this.modifiers = modifiers;
        this.base = base;
    }

    public ModifyEnvironmentProvider create(
            RegistryEntryList<EnvironmentProvider> modifiers,
            RegistryEntry<EnvironmentProvider> base
    ) {
        return new ModifyEnvironmentProvider(modifiers, base);
    }

    @Override
    public ComponentMap findCurrentComponents(World world, BlockPos pos, RegistryEntry<Biome> biome) {
        ComponentMap.Builder baseBuilder = ComponentMap.builder()
                .addAll(base.value().findCurrentComponents(world, pos, biome));

        ReducibleComponentMapBuilder modifiedBuilder = ReducibleComponentMapBuilder.create(baseBuilder);
        for (RegistryEntry<EnvironmentProvider> modifer : this.modifiers) {
            modifiedBuilder.addAll(modifer.value().findCurrentComponents(world, pos, biome));
        }

        return modifiedBuilder.build();
    }

    @Override
    public EnvironmentProviderType<?> getType() {
        return EnvironmentProviderTypes.MODIFY;
    }

    public RegistryEntryList<EnvironmentProvider> modifiers() {
        return modifiers;
    }

    public RegistryEntry<EnvironmentProvider> base() {
        return base;
    }
}
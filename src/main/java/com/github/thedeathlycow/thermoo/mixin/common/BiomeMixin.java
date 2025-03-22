package com.github.thedeathlycow.thermoo.mixin.common;

import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.impl.environment.ThermooBiome;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(Biome.class)
public class BiomeMixin implements ThermooBiome {
    @Unique
    private final List<EnvironmentDefinition> thermoo$environments = new ArrayList<>();

    @Override
    @Unique
    public List<EnvironmentDefinition> thermoo$getEnvironments() {
        return this.thermoo$environments;
    }

    @Override
    @Unique
    public void thermoo$addEnvironment(EnvironmentDefinition environment) {
        this.thermoo$environments.add(environment);
    }

    @Override
    @Unique
    public void thermoo$clearEnvironments() {
        this.thermoo$environments.clear();
    }
}
package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;

@SuppressWarnings("unused")
public class EnvironmentPriorityTests {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void environments_loaded_in_priority_order(TestContext context) {
        ServerWorld world = context.getWorld();
        Registry<EnvironmentDefinition> registry = world.getRegistryManager().get(ThermooRegistryKeys.ENVIRONMENT);
        RegistryEntry<Biome> netherWastes = world.getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getEntry(BiomeKeys.NETHER_WASTES)
                .orElseThrow(() -> new GameTestException("Missing nether wastes biome!"));

        List<Identifier> loadedEnvironments = EnvironmentLookupImpl.getAllMatchingEnvironments(netherWastes, registry)
                .map(registry::getId)
                .toList();

        List<Identifier> expectedEnvironments = List.of(
                ThermooTestMod.id("priority/high_priority"),
                ThermooTestMod.id("priority/default_priority"),
                ThermooTestMod.id("priority/low_priority")
        );

        context.assertEquals(loadedEnvironments, expectedEnvironments, "Nether Wastes Environment Providers");
        context.complete();
    }
}
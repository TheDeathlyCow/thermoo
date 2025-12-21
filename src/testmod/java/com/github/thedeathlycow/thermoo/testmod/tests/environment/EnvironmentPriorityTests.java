package com.github.thedeathlycow.thermoo.testmod.tests.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import com.github.thedeathlycow.thermoo.testmod.ThermooTestMod;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.List;

@SuppressWarnings("unused")
@GameTestHolder(ThermooTestMod.MODID)
public class EnvironmentPriorityTests {
    @GameTest(template = FabricGameTest.EMPTY_STRUCTURE)
    public void environments_loaded_in_priority_order(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Registry<EnvironmentDefinition> registry = world.registryAccess().registryOrThrow(ThermooRegistryKeys.ENVIRONMENT);
        Holder<Biome> netherWastes = world.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getHolder(Biomes.NETHER_WASTES)
                .orElseThrow(() -> new GameTestAssertException("Missing nether wastes biome!"));

        List<ResourceLocation> loadedEnvironments = EnvironmentLookupImpl.getAllMatchingEnvironments(netherWastes, registry)
                .map(registry::getKey)
                .toList();

        List<ResourceLocation> expectedEnvironments = List.of(
                ThermooTestMod.id("priority/high_priority"),
                ThermooTestMod.id("priority/default_priority"),
                ThermooTestMod.id("priority/low_priority")
        );

        context.assertValueEqual(loadedEnvironments, expectedEnvironments, "Nether Wastes Environment Providers");
        context.succeed();
    }
}
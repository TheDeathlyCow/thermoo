package com.github.thedeathlycow.thermoo.gametest.tests.environment;

import com.github.thedeathlycow.thermoo.api.ThermooRegistryKeys;
import com.github.thedeathlycow.thermoo.api.environment.EnvironmentDefinition;
import com.github.thedeathlycow.thermoo.gametest.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.environment.EnvironmentLookupImpl;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

@SuppressWarnings("unused")
public class EnvironmentPriorityTests {
    @GameTest
    public void environments_loaded_in_priority_order(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Registry<EnvironmentDefinition> registry = level.registryAccess().lookupOrThrow(ThermooRegistryKeys.ENVIRONMENT);
        Holder<Biome> netherWastes = level.registryAccess()
                .lookupOrThrow(Registries.BIOME)
                .getOrThrow(Biomes.NETHER_WASTES);

        List<Identifier> loadedEnvironments = EnvironmentLookupImpl.getAllMatchingEnvironments(netherWastes, registry)
                .map(registry::getKey)
                .toList();

        List<Identifier> expectedEnvironments = List.of(
                ThermooTestMod.location("priority/high_priority"),
                ThermooTestMod.location("priority/default_priority"),
                ThermooTestMod.location("priority/low_priority")
        );

        helper.assertValueEqual(loadedEnvironments, expectedEnvironments, Component.literal("Nether Wastes Environment Providers"));
        helper.succeed();
    }
}
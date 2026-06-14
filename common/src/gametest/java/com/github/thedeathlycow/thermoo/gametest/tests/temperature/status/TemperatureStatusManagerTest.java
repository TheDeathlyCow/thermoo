package com.github.thedeathlycow.thermoo.gametest.tests.temperature.status;

import com.github.thedeathlycow.thermoo.api.core.v2.registry.ThermooRegistries;
import com.github.thedeathlycow.thermoo.api.temperature.status.v2.TemperatureStatus;
import com.github.thedeathlycow.thermoo.gametest.init.ThermooTestMod;
import com.github.thedeathlycow.thermoo.impl.temperature.status.TemperatureStatusManager;
import com.github.thedeathlycow.thermoo.gametest.util.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

import java.util.List;

@SuppressWarnings("unused")
public class TemperatureStatusManagerTest {
    @GameTest
    public void applicationOrderSetsLookupOrder(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        HolderLookup<TemperatureStatus> lookup = level.holderLookup(ThermooRegistries.TEMPERATURE_STATUS);

        List<Holder.Reference<TemperatureStatus>> statuses = TemperatureStatusManager.lookup(EntityType.PLAYER.builtInRegistryHolder(), lookup);

        List<Holder.Reference<TemperatureStatus>> expected = List.of(
                lookup.getOrThrow(key("ordered/first")),
                lookup.getOrThrow(key("ordered/second")),
                lookup.getOrThrow(key("ordered/third")),
                lookup.getOrThrow(key("ordered/fourth")),
                lookup.getOrThrow(key("attribute_test")),
                lookup.getOrThrow(key("function_test")),
                lookup.getOrThrow(key("damage_test")),
                lookup.getOrThrow(key("mod_exists_test")),
                lookup.getOrThrow(key("mob_effect_test")),
                lookup.getOrThrow(key("disabled_by_default")),
                lookup.getOrThrow(key("scaling_attribute_test")) // tests that items not in tag are last
        );

        helper.assertValueEqual(statuses, expected, "Temperature Status Order");

        helper.succeed();
    }

    @GameTest
    public void modDoesNotExistTestIsNotLoaded(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        HolderLookup<TemperatureStatus> lookup = level.holderLookup(ThermooRegistries.TEMPERATURE_STATUS);

        helper.assertTrue(lookup.get(key("mod_does_not_exist_test")).isEmpty(), "Effect did not respect load conditions");

        helper.succeed();
    }

    @GameTest
    public void modExistsTestIsLoaded(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        HolderLookup<TemperatureStatus> lookup = level.holderLookup(ThermooRegistries.TEMPERATURE_STATUS);

        helper.assertTrue(lookup.get(key("mod_exists_test")).isPresent(), "Effect did not respect load conditions");

        helper.succeed();
    }

    private ResourceKey<TemperatureStatus> key(String name) {
        return ResourceKey.create(ThermooRegistries.TEMPERATURE_STATUS, ThermooTestMod.id(name));
    }
}
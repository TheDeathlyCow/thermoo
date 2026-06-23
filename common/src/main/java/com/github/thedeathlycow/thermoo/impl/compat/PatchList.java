package com.github.thedeathlycow.thermoo.impl.compat;

import com.github.thedeathlycow.thermoo.impl.platform.Loader;
import com.github.thedeathlycow.thermoo.impl.platform.ThermooServices;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.yumi.commons.function.YumiPredicates;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.YumiMods;

import java.util.ArrayList;
import java.util.List;

public record PatchList(
        List<PatchedVersion> patches
) {
    public static final Codec<PatchList> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    PatchedVersion.CODEC.listOf()
                            .fieldOf("patches")
                            .forGetter(PatchList::patches)
            ).apply(instance, PatchList::new)
    );

    public List<ModContainer> getPatchAvailableMods(YumiMods loader) {
        String gameVersion = loader.getMod("minecraft")
                .orElseThrow()
                .getVersionString();

        List<ModContainer> patchAvailableMods = new ArrayList<>();
        Loader currentLoader = ThermooServices.PLATFORM.getLoader();

        for (PatchedVersion patch : this.patches) {
            if (patch.loader().matches(currentLoader) && patch.minecraftVersions().contains(gameVersion)) {
                this.extendPatchAvailableMods(loader, patchAvailableMods, patch);
            }
        }

        return patchAvailableMods;
    }

    private void extendPatchAvailableMods(YumiMods loader, List<ModContainer> patchAvailableMods, PatchedVersion patch) {
        for (String modid : patch.mods()) {
            loader.getMod(modid).ifPresent(patchAvailableMods::add);
        }
    }
}

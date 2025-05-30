package com.github.thedeathlycow.thermoo.impl.compat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

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

    public List<ModContainer> getPatchAvailableMods() throws VersionParsingException {
        Version gameVersion = FabricLoader.getInstance().getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion();

        List<ModContainer> patchAvailableMods = new ArrayList<>();

        for (PatchedVersion patch : this.patches) {
            VersionPredicate predicate = VersionPredicate.parse(patch.minecraftVersion());
            if (predicate.test(gameVersion)) {
                this.extendPatchAvailableMods(patchAvailableMods, patch);
            }
        }

        return patchAvailableMods;
    }

    private void extendPatchAvailableMods(List<ModContainer> patchAvailableMods, PatchedVersion patch) {
        for (String modid : patch.mods()) {
            FabricLoader.getInstance().getModContainer(modid).ifPresent(patchAvailableMods::add);
        }
    }
}

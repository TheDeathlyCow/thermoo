package com.github.thedeathlycow.thermoo.api.armor.material;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public enum ThermalResistanceLevel {

    NEUTRAL(null, null),
    VERY_WEAK(ArmorMaterialTags.VERY_WEAK_TO_COLD, ArmorMaterialTags.VERY_WEAK_TO_HEAT),
    WEAK(ArmorMaterialTags.WEAK_TO_COLD, ArmorMaterialTags.WEAK_TO_HEAT),
    RESISTANT(ArmorMaterialTags.RESISTANT_TO_COLD, ArmorMaterialTags.RESISTANT_TO_HEAT),
    VERY_RESISTANT(ArmorMaterialTags.VERY_RESISTANT_TO_COLD, ArmorMaterialTags.VERY_RESISTANT_TO_HEAT);

    @Nullable
    private final TagKey<ArmorMaterial> coldTag;

    @Nullable
    private final TagKey<ArmorMaterial> heatTag;

    ThermalResistanceLevel(TagKey<ArmorMaterial> coldTag, TagKey<ArmorMaterial> heatTag) {
        this.coldTag = coldTag;
        this.heatTag = heatTag;
    }

    @ApiStatus.Internal
    @Nullable
    public TagKey<ArmorMaterial> getColdTag() {
        return this.coldTag;
    }

    @ApiStatus.Internal
    @Nullable
    public TagKey<ArmorMaterial> getHeatTag() {
        return heatTag;
    }
}

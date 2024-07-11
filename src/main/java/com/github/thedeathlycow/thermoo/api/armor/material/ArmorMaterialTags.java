package com.github.thedeathlycow.thermoo.api.armor.material;

import com.github.thedeathlycow.thermoo.impl.Thermoo;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

@SuppressWarnings("unused")
public class ArmorMaterialTags {

    public static final TagKey<ArmorMaterial> VERY_RESISTANT_TO_HEAT = createArmorMaterialTag("very_resistant_to_heat");
    public static final TagKey<ArmorMaterial> RESISTANT_TO_HEAT = createArmorMaterialTag("resistant_to_heat");
    public static final TagKey<ArmorMaterial> WEAK_TO_HEAT = createArmorMaterialTag("weak_to_heat");
    public static final TagKey<ArmorMaterial> VERY_WEAK_TO_HEAT = createArmorMaterialTag("very_weak_to_heat");

    public static final TagKey<ArmorMaterial> VERY_RESISTANT_TO_COLD = createArmorMaterialTag("very_resistant_to_cold");
    public static final TagKey<ArmorMaterial> RESISTANT_TO_COLD = createArmorMaterialTag("resistant_to_cold");
    public static final TagKey<ArmorMaterial> WEAK_TO_COLD = createArmorMaterialTag("weak_to_cold");
    public static final TagKey<ArmorMaterial> VERY_WEAK_TO_COLD = createArmorMaterialTag("very_weak_to_cold");

    private static TagKey<ArmorMaterial> createArmorMaterialTag(String path) {
        return TagKey.of(RegistryKeys.ARMOR_MATERIAL, Thermoo.id(path));
    }

    private ArmorMaterialTags() {}

}

package com.github.thedeathlycow.thermoo.testmod;

import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialEvents;
import com.github.thedeathlycow.thermoo.api.armor.material.ArmorMaterialTags;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

public class ArmorMaterialListener implements ArmorMaterialEvents.GetResistance {

    public static final ArmorMaterialListener COLD = new ArmorMaterialListener(
            new Level(ArmorMaterialTags.VERY_WEAK_TO_COLD, -1),
            new Level(ArmorMaterialTags.WEAK_TO_COLD, -0.5),
            new Level(ArmorMaterialTags.RESISTANT_TO_COLD, 0.5),
            new Level(ArmorMaterialTags.VERY_RESISTANT_TO_COLD, 1)
    );

    public static final ArmorMaterialListener HEAT = new ArmorMaterialListener(
            new Level(ArmorMaterialTags.VERY_WEAK_TO_HEAT, -1),
            new Level(ArmorMaterialTags.WEAK_TO_HEAT, -0.5),
            new Level(ArmorMaterialTags.RESISTANT_TO_HEAT, 0.5),
            new Level(ArmorMaterialTags.VERY_RESISTANT_TO_HEAT, 1)
    );

    private final Level[] levels;

    private ArmorMaterialListener(Level... levels) {
        this.levels = levels;
    }

    @Override
    public double getValue(RegistryEntry<ArmorMaterial> armorMaterial, ArmorItem.Type armorType) {
        for (Level level : this.levels) {
            if (armorMaterial.isIn(level.tag())) {
                return level.value();
            }
        }
        return Double.NaN;
    }

    private record Level(
            TagKey<ArmorMaterial> tag,
            double value
    ) {
    }
}

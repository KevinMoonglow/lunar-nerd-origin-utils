package io.github.KevinMoonglow.lunar_origins.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class AmethystGlassBowl extends GlassBowl {
    public AmethystGlassBowl(ArmorMaterial material, ArmorItem.Type slot, Settings settings, int[] sealantDrainSteps) {
        super(material, slot, settings, sealantDrainSteps);
        FULL_KEY = "item.lunar_origins.amethyst_bowl.full";
    }
}

package io.github.KevinMoonglow.lunar_origins.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class DivingHelmet extends GlassBowl {
    public DivingHelmet(ArmorMaterial material, ArmorItem.Type slot, Settings settings, int[] sealantDrainSteps) {
        super(material, slot, settings, sealantDrainSteps);
        FULL_KEY = "item.lunar_origins.diving_helmet.full";
    }

    @Override
    public boolean isCracked(ItemStack stack) {
        return false;
    }
}

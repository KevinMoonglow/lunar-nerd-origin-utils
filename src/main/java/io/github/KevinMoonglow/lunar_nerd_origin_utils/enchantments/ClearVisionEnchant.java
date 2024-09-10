package io.github.KevinMoonglow.lunar_nerd_origin_utils.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ClearVisionEnchant extends Enchantment {
    public ClearVisionEnchant() {
        super(Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[] {EquipmentSlot.HEAD});
    }
    @Override
    public int getMinPower(int level) {
        return 20;
    }

    @Override
    public int getMaxPower(int level) {
        return 50;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other);
    }
}

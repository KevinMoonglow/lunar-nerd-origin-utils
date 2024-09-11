package io.github.KevinMoonglow.lunar_origins.material;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class AmethystGlassBowlMaterial implements ArmorMaterial {
    private static final int[] BASE_DURABILITY = new int[] {6, 7, 8, 10};
    private static final int[] PROTECTION_VALUES = new int[] {1, 2, 3, 1};


    @Override
    public int getDurability(ArmorItem.Type slot) {
        return BASE_DURABILITY[slot.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public int getProtection(ArmorItem.Type slot) {
        return PROTECTION_VALUES[slot.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 30;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.AMETHYST_SHARD);
    }

    @Override
    public String getName() {
        return "lunar_origins:amethyst_fish_bowl";
    }

    @Override
    public float getToughness() {
        return 0.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}

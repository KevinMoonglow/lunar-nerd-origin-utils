package io.github.KevinMoonglow.lunar_origins.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FishBowl extends ArmorItem {
    public FishBowl(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity != null && entity.isPlayer() && slot == EquipmentSlot.HEAD.getEntitySlotId()) {
            PlayerEntity p = (PlayerEntity) entity;

            ItemStack headStack = p.getEquippedStack(EquipmentSlot.HEAD);

            if(headStack.getItem() == LunarOriginsItems.FISH_BOWL && !p.isSubmergedInWater()) {
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 60, 0, false, false));
            }
        }
    }
}

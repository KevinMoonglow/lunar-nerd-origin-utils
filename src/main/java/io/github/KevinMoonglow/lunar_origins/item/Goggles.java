package io.github.KevinMoonglow.lunar_origins.item;

import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Goggles extends ArmorItem implements PowerGrantingItem {

    private final List<StackPowerUtil.StackPower> POWERS;

    public Goggles(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
        POWERS = new LinkedList<>();

        /* Power that grants night vision in water. The power itself has conditions that will check for being underwater
         and the water level in the bowl. */
        StackPowerUtil.StackPower nightVisionGogglesPower = new StackPowerUtil.StackPower();
        nightVisionGogglesPower.powerId = new Identifier(Lunar_origins.MOD_ID, "night_vision_for_goggles");
        nightVisionGogglesPower.slot = EquipmentSlot.HEAD;
        nightVisionGogglesPower.isHidden = true;
        nightVisionGogglesPower.isNegative = false;
        POWERS.add(nightVisionGogglesPower);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity.isLiving()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (slot != EquipmentSlot.HEAD.getEntitySlotId()) {
                NbtCompound nbt = stack.getOrCreateNbt();
                nbt.putBoolean("waterEyeLevel", livingEntity.isSubmergedInWater());
            }
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putBoolean("waterEyeLevel", player.isSubmergedInWater());


        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public Collection<StackPowerUtil.StackPower> getPowers(ItemStack stack, EquipmentSlot slot) {
        return POWERS.stream().filter(p -> p.slot == slot).toList();
    }

    @Override
    public Text getName(ItemStack stack) {
        Text s = super.getName(stack);
        NbtCompound nbt = stack.getOrCreateNbt();
        if(nbt.getBoolean("waterEyeLevel")) {
            return Text.translatable("item.lunar_origins.goggles.flooded");
        }
        else return s;
    }
}

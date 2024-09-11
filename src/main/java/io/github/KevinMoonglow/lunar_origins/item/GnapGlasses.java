package io.github.KevinMoonglow.lunar_origins.item;

import dev.emi.trinkets.api.*;
import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GnapGlasses extends TrinketItem {
   public GnapGlasses(Settings settings) {
        super(settings);

    }


    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        PowerHolderComponent powers = PowerHolderComponent.KEY.get(entity);
        powers.addPower(PowerTypeRegistry.get(new Identifier(Lunar_origins.MOD_ID, "dizzied_from_glasses")), new Identifier(Lunar_origins.MOD_ID, "face_equip"));
        powers.addPower(PowerTypeRegistry.get(new Identifier(Lunar_origins.MOD_ID, "gnap_glasses")), new Identifier(Lunar_origins.MOD_ID, "face_equip"));
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        PowerHolderComponent powers = PowerHolderComponent.KEY.get(entity);
        powers.removePower(PowerTypeRegistry.get(new Identifier(Lunar_origins.MOD_ID, "dizzied_from_glasses")), new Identifier(Lunar_origins.MOD_ID, "face_equip"));
        powers.removePower(PowerTypeRegistry.get(new Identifier(Lunar_origins.MOD_ID, "gnap_glasses")), new Identifier(Lunar_origins.MOD_ID, "face_equip"));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.lunar_origins.gnap_glasses.tooltip.1").formatted(Formatting.GRAY));


    }
}

package io.github.KevinMoonglow.lunar_origins.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class BowlSealant extends Item {

    public static final int SEALANT_AMOUNT = 12000;
    public BowlSealant(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if(clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            ItemStack target = slot.getStack();
            Item targetItem = target.getItem();
            if (targetItem instanceof GlassBowl) {
                boolean result = ((GlassBowl) targetItem).applySealant(target, SEALANT_AMOUNT);
                if (result) {
                    stack.decrement(1);
                }
                return true;
            }
        }

        return super.onStackClicked(stack, slot, clickType, player);
    }
}
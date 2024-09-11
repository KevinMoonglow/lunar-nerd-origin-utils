package io.github.KevinMoonglow.lunar_origins.mixin;

import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.PlayerScreenHandler$1")
abstract public class EquipmentControlMixin extends Slot {
    EquipmentControlMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(method = "canInsert", at= @At("HEAD"), cancellable = true)
    public void canInsertControl(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity owner = ((PlayerInventory)inventory).player;

        EquipmentSlot slot = MobEntity.getPreferredEquipmentSlot(stack);
        if(slot == EquipmentSlot.FEET) {
            if (stack.isIn(TagKey.of(RegistryKeys.ITEM, new Identifier(Lunar_origins.MOD_ID, "flipper_shaped")))) {
                PowerHolderComponent powers = PowerHolderComponent.KEY.get(owner);
                if (!powers.hasPower(PowerTypeRegistry.get(new Identifier(Lunar_origins.MOD_ID, "flipper_feet")))) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}

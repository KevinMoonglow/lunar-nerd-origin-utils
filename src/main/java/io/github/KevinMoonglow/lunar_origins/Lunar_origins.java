package io.github.KevinMoonglow.lunar_origins;

import io.github.KevinMoonglow.lunar_origins.effects.LunarOriginsEffects;
import io.github.KevinMoonglow.lunar_origins.enchantments.LunarOriginsEnchants;
import io.github.KevinMoonglow.lunar_origins.item.Goggles;
import io.github.KevinMoonglow.lunar_origins.item.LunarOriginsItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;


public class Lunar_origins implements ModInitializer {
    public static String MOD_ID = "lunar_origins";

    public static final Identifier BURIED_TREASURE_LOOT_ID = new Identifier("chests/buried_treasure");
    public static final Identifier SHIPWRECK_TREASURE_LOOT_ID = new Identifier("chests/shipwreck_treasure");
    public static final Identifier DESERT_TEMPLE_TREASURE_ID = new Identifier("chests/desert_pyramid");
    public static final Identifier BASTION_TREASURE_ID = new Identifier("chests/bastion_treasure");

    public void initLoot() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if(source.isBuiltin() && BURIED_TREASURE_LOOT_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder();
                poolBuilder.with(
                    ItemEntry.builder(Items.POTION)
                        .apply(SetPotionLootFunction.builder(LunarOriginsItems.CLEAR_VISION_POTION))
                ).rolls(UniformLootNumberProvider.create(0, 3));

                tableBuilder.pool(poolBuilder);

                poolBuilder = LootPool.builder();
                poolBuilder.with(
                    ItemEntry.builder(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsLootFunction.Builder()
                            .enchantment(LunarOriginsEnchants.CLEAR_VISION_ENCHANT, ConstantLootNumberProvider.create(1))
                        )
                        .weight(1)
                );
                poolBuilder.with(
                    ItemEntry.builder(Items.ENCHANTED_BOOK)
                        .apply(new SetEnchantmentsLootFunction.Builder()
                            .enchantment(LunarOriginsEnchants.MOLDING_ENCHANT, ConstantLootNumberProvider.create(1))
                        )
                        .weight(2)
                );
                poolBuilder.with(
                    EmptyEntry.builder()
                        .weight(5)
                );
                tableBuilder.pool(poolBuilder);
            } else if (source.isBuiltin() && SHIPWRECK_TREASURE_LOOT_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder();
                poolBuilder.with(
                    ItemEntry.builder(Items.POTION)
                        .apply(SetPotionLootFunction.builder(LunarOriginsItems.CLEAR_VISION_POTION))
                ).rolls(UniformLootNumberProvider.create(0, 2));
                tableBuilder.pool(poolBuilder);

                poolBuilder = LootPool.builder();
                poolBuilder.with(
                    ItemEntry.builder(LunarOriginsItems.BOWL_SEALANT)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0, 4)))
                        .weight(1)
                );
                poolBuilder.with(
                    EmptyEntry.builder()
                        .weight(7)
                );
                tableBuilder.pool(poolBuilder);
            }
            else if(source.isBuiltin() && DESERT_TEMPLE_TREASURE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder();
                poolBuilder.with(
                    ItemEntry.builder(LunarOriginsItems.BOWL_SEALANT)
                ).rolls(UniformLootNumberProvider.create(0, 6));
                tableBuilder.pool(poolBuilder);
            }
            else if(source.isBuiltin() && BASTION_TREASURE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder();
                poolBuilder.with(
                    ItemEntry.builder(LunarOriginsItems.BOWL_SUPER_SEALANT)
                        .weight(1)
                );
                poolBuilder.with(
                    ItemEntry.builder(LunarOriginsItems.BOWL_SEALANT)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(6, 15)))
                        .weight(4)
                ).rolls(UniformLootNumberProvider.create(1, 2));
                tableBuilder.pool(poolBuilder);
            }
        });
    }

    public void initEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((livingEntity, source, amount) -> {
            ItemStack head = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
            if(head != null && head.getItem() instanceof Goggles) {
                NbtCompound nbt = head.getNbt();
                if(nbt != null) {
                    if(Math.random() < 0.1) {
                        nbt.putBoolean("waterEyeLevel", livingEntity.isSubmergedInWater());
                    }
                }
            }
            return true;
        });

    }

    @Override
    public void onInitialize() {
        LunarOriginsEnchants.initEnchants();
        LunarOriginsEffects.initEffects();
        LunarOriginsItems.initItems();
        initLoot();
        initEvents();
    }
}

package io.github.KevinMoonglow.lunar_origins.client;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.KevinMoonglow.lunar_origins.item.AmethystGlassBowl;
import io.github.KevinMoonglow.lunar_origins.item.DivingHelmet;
import io.github.KevinMoonglow.lunar_origins.item.GlassBowl;
import io.github.KevinMoonglow.lunar_origins.item.LunarOriginsItems;
import io.github.KevinMoonglow.lunar_origins.renderer.GnapGlassesRenderer;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class Lunar_originsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(LunarOriginsItems.GLASS_BOWL, new Identifier("water_level"),
            (itemStack, clientWorld, livingEntity, i) -> {
                if (!itemStack.hasNbt()) {
                    return 0.0f;
                }

                assert itemStack.getNbt() != null;
                int waterLevel = itemStack.getNbt().getInt("waterLevel");

                return waterLevel / (float) GlassBowl.MAX_WATER;
            });
        ModelPredicateProviderRegistry.register(LunarOriginsItems.AMETHYST_BOWL, new Identifier("water_level"),
            (itemStack, clientWorld, livingEntity, i) -> {
                if (!itemStack.hasNbt()) {
                    return 0.0f;
                }

                assert itemStack.getNbt() != null;
                int waterLevel = itemStack.getNbt().getInt("waterLevel");

                return waterLevel / (float) AmethystGlassBowl.MAX_WATER;
            });
        ModelPredicateProviderRegistry.register(LunarOriginsItems.DIVING_HELMET, new Identifier("water_level"),
            (itemStack, clientWorld, livingEntity, i) -> {
                NbtCompound nbt = itemStack.getNbt();
                if (nbt == null) return 0.0f;
                int waterLevel = nbt.getInt("waterLevel");
                return waterLevel / (float) DivingHelmet.MAX_WATER;
            });
        ModelPredicateProviderRegistry.register(LunarOriginsItems.GOGGLES, new Identifier("water_level"),
            (itemStack, clientWorld, livingEntity, i) -> {
                NbtCompound nbt = itemStack.getNbt();
                if (nbt == null) return 0.0f;
                return nbt.getInt("waterEyeLevel");
            });


        ModelPredicateProviderRegistry.register(LunarOriginsItems.GLASS_BOWL, new Identifier("worn"),
            (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity != null && itemStack == livingEntity.getEquippedStack(EquipmentSlot.HEAD)) {
                    return 1.0f;
                }
                return 0.0f;
            });
        ModelPredicateProviderRegistry.register(LunarOriginsItems.AMETHYST_BOWL, new Identifier("worn"),
            (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity != null && itemStack == livingEntity.getEquippedStack(EquipmentSlot.HEAD)) {
                    return 1.0f;
                }
                return 0.0f;
            });


        ModelPredicateProviderRegistry.register(LunarOriginsItems.FISH_BOWL, new Identifier("worn"),
            (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity != null && itemStack == livingEntity.getEquippedStack(EquipmentSlot.HEAD)) {
                    return 1.0f;
                }
                return 0.0f;
            });
        TrinketRendererRegistry.registerRenderer(LunarOriginsItems.GNAP_GLASSES, new GnapGlassesRenderer());

        ModelPredicateProviderRegistry.register(LunarOriginsItems.GNAP_GLASSES, new Identifier("worn"),
            (itemStack, clientWorld, livingEntity, i) -> {
                Optional<TrinketComponent> option = TrinketsApi.getTrinketComponent(livingEntity);
                if (option.isPresent()) {
                    TrinketComponent trinketComponent = option.get();
                    if (trinketComponent.isEquipped(Predicate.isEqual(itemStack))) {
                        //if(trinketComponent.isEquipped(LunarOriginsItems.GNAP_GLASSES)) {
                        return 1.0f;
                    }
                }
                return 0.0f;
            });
        ModelPredicateProviderRegistry.register(LunarOriginsItems.GNAP_GLASSES, new Identifier("face"),
            (itemStack, clientWorld, livingEntity, i) -> {
                if (!itemStack.hasNbt()) {
                    return 0.0f;
                }

                assert itemStack.getNbt() != null;
                boolean faceModel = itemStack.getNbt().getBoolean("faceModel");

                return faceModel ? 1.0f : 0.0f;
            });


        //TrinketsApi.registerTrinketPredicate();

        @Nullable PowerType waterFocusPower = null;
        try {
            waterFocusPower = PowerTypeRegistry.get(new Identifier(Lunar_origins.MOD_ID, "gnap_water_focus"));
        }
        catch(IllegalArgumentException ignored) {}


        @Nullable PowerType finalWaterFocusPower = waterFocusPower;
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if(stack.getItem() == LunarOriginsItems.GNAP_GLASSES) {
                PlayerEntity player = MinecraftClient.getInstance().player;

                PowerHolderComponent powers = player != null ? PowerHolderComponent.KEY.get(player) : null;
                if(powers != null && finalWaterFocusPower != null && powers.hasPower(finalWaterFocusPower)) {
                    lines.add(1, Text.translatable("item.lunar_origins.gnap_glasses.tooltip.needed").formatted(Formatting.GRAY));
                }
                else {
                    lines.add(1, Text.translatable("item.lunar_origins.gnap_glasses.tooltip.uncomfortable").formatted(Formatting.GRAY));
                }
            }
        });
    }
}

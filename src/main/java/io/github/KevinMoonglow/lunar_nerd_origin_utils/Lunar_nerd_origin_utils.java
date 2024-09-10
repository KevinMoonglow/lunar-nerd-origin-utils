package io.github.KevinMoonglow.lunar_nerd_origin_utils;

import io.github.KevinMoonglow.lunar_nerd_origin_utils.enchantments.ClearVisionEnchant;
import io.github.KevinMoonglow.lunar_nerd_origin_utils.enchantments.MoldingEnchant;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class Lunar_nerd_origin_utils implements ModInitializer {
    public static String MOD_ID = "lunar_nerd_origin_utils";

    public static final Enchantment CLEAR_VISION_ENCHANT = new ClearVisionEnchant();
    public static final Enchantment MOLDING_ENCHANT = new MoldingEnchant();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "clear_vision"), CLEAR_VISION_ENCHANT);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "molding"), MOLDING_ENCHANT);
    }
}

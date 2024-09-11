package io.github.KevinMoonglow.lunar_origins.enchantments;

import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LunarOriginsEnchants {
    public static final Enchantment CLEAR_VISION_ENCHANT = new ClearVisionEnchant();
    public static final Enchantment MOLDING_ENCHANT = new MoldingEnchant();

    public static void initEnchants() {
        Registry.register(Registries.ENCHANTMENT, new Identifier(Lunar_origins.MOD_ID, "clear_vision"), LunarOriginsEnchants.CLEAR_VISION_ENCHANT);
        Registry.register(Registries.ENCHANTMENT, new Identifier(Lunar_origins.MOD_ID, "molding"), LunarOriginsEnchants.MOLDING_ENCHANT);
    }
}

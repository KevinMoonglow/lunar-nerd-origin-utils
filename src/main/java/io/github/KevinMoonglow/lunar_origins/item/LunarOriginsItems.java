package io.github.KevinMoonglow.lunar_origins.item;

import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.KevinMoonglow.lunar_origins.effects.LunarOriginsEffects;
import io.github.KevinMoonglow.lunar_origins.material.*;
import io.github.KevinMoonglow.lunar_origins.mixin.BrewingRecipeRegistryMixin;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LunarOriginsItems {
    public static final Potion CLEAR_VISION_POTION = Registry.register(Registries.POTION, new Identifier(Lunar_origins.MOD_ID, "clear_vision"),
        new Potion(new StatusEffectInstance(LunarOriginsEffects.CLEAR_VISION, 3600)));
    public static final Potion LONG_CLEAR_VISION_POTION = Registry.register(Registries.POTION, new Identifier(Lunar_origins.MOD_ID, "long_clear_vision"),
        new Potion("clear_vision", new StatusEffectInstance(LunarOriginsEffects.CLEAR_VISION, 9600)));
    public static final Potion HYDRATION_POTION = Registry.register(Registries.POTION, new Identifier(Lunar_origins.MOD_ID, "hydration"),
        new Potion(new StatusEffectInstance(LunarOriginsEffects.HYDRATION, 3600)));
    public static final Potion LONG_HYDRATION_POTION = Registry.register(Registries.POTION, new Identifier(Lunar_origins.MOD_ID, "long_hydration"),
        new Potion("hydration", new StatusEffectInstance(LunarOriginsEffects.HYDRATION, 9600)));

    public static final Item KELP_CARROT = new Item(
            new FabricItemSettings().food(FoodComponents.KELP_CARROT));
    public static final Item GNAP_GLASSES = new GnapGlasses(new FabricItemSettings().maxCount(1));
    public static final ItemGroup LUNAR_ORIGINS_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(LunarOriginsItems.LUNA_GLASSES))
        .displayName(Text.translatable("itemGroup.lunar_origins.lunar_origins"))
        .entries((context, entries) -> {
            entries.add(LunarOriginsItems.LUNA_GLASSES);
            entries.add(LunarOriginsItems.GNAP_GLASSES);
            entries.add(LunarOriginsItems.FISH_BOWL);
            entries.add(LunarOriginsItems.GLASS_BOWL);
            entries.add(LunarOriginsItems.AMETHYST_BOWL);
            entries.add(LunarOriginsItems.DIVING_HELMET);
            entries.add(LunarOriginsItems.KELP_CARROT);
        })
        .build();
    public static final ArmorMaterial glassesArmorMaterial = new GlassesArmorMaterial();
    public static final Item LUNA_GLASSES = new LunaGlasses(glassesArmorMaterial, ArmorItem.Type.HELMET,
            new FabricItemSettings().maxCount(1));
    public static final ArmorMaterial glassBowlArmorMaterial = new GlassBowlMaterial();
    public static final Item GLASS_BOWL = new GlassBowl(glassBowlArmorMaterial, ArmorItem.Type.HELMET,
            new FabricItemSettings().maxCount(1), new int[]{15, 6, 4, 2, 1});
    public static final Item FISH_BOWL = new FishBowl(glassBowlArmorMaterial, ArmorItem.Type.HELMET,
            new FabricItemSettings().maxCount(1));
    public static final ArmorMaterial amethystBowlArmorMaterial = new AmethystGlassBowlMaterial();
    public static final Item AMETHYST_BOWL = new AmethystGlassBowl(amethystBowlArmorMaterial, ArmorItem.Type.HELMET,
            new FabricItemSettings().maxCount(1), new int[]{15, 4, 2, 1, 0});
    public static final ArmorMaterial gogglesArmorMaterial = new GogglesArmorMaterial();
    public static final Item GOGGLES = new Goggles(gogglesArmorMaterial, ArmorItem.Type.HELMET,
            new FabricItemSettings().maxCount(1));
    public static final ArmorMaterial copperDivingMaterial = new CopperDivingMaterial();
    public static final Item DIVING_HELMET = new DivingHelmet(copperDivingMaterial, ArmorItem.Type.HELMET,
            new FabricItemSettings().maxCount(1), new int[]{6, 4, 1, 0, 0});
    public static final Item AQUA_GUMMY = new Item(
            new FabricItemSettings()
                    .food(FoodComponents.AQUA_GUMMY)
                    .maxCount(16));
    public static final Item GLIMMERING_AQUA_GUMMY = new Item(
            new FabricItemSettings()
                    .food(FoodComponents.GLIMMERING_AQUA_GUMMY)
                    .maxCount(16));
    public static final Item WOPOL_ICON = new Item(
            new FabricItemSettings()
    );
    public static final Item GNAPOREON_ICON = new Item(
            new FabricItemSettings()
    );
    public static final Item GLASSES_ARM = new Item(new FabricItemSettings());
    public static final Item GLASSES_ARM_RED = new Item(new FabricItemSettings());
    public static final Item GLASSES_FRAME = new Item(new FabricItemSettings());
    public static final Item GLASSES_FRAME_RED = new Item(new FabricItemSettings());
    public static final Item GLASSES_HINGE = new Item(new FabricItemSettings());
    public static final Item HINGE_PIN = new Item(new FabricItemSettings());
    public static final Item HINGE_SPRING = new Item(new FabricItemSettings());
    public static final Item HINGE_PART = new Item(new FabricItemSettings());
    public static final Item GLASSES_LENS = new Item(new FabricItemSettings());
    public static final Item UNFOCUSED_LENS = new Item(new FabricItemSettings());
    public static final Item WIRE_COIL = new Item(new FabricItemSettings());
    public static final Item CRUDE_LENS = new Item(new FabricItemSettings());
    public static final Item LENS_CAST = new Item(new FabricItemSettings());
    public static final Item INCOMPLETE_CAST = new Item(new FabricItemSettings());
    public static final Item INCOMPLETE_HINGE = new Item(new FabricItemSettings());
    public static final Item INCOMPLETE_FRAME = new Item(new FabricItemSettings());
    public static final Item INCOMPLETE_LENS = new Item(new FabricItemSettings());
    public static final Item BOWL_SEALANT = new BowlSealant(new FabricItemSettings());
    public static final Item BOWL_SUPER_SEALANT = new BowlSuperSealant(new FabricItemSettings());

    public enum WaterBowlState {
        NONE,
        AIR,
        WATER,
    }

    public static void initItems() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, KELP_CARROT, LunarOriginsItems.CLEAR_VISION_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(LunarOriginsItems.CLEAR_VISION_POTION, Items.REDSTONE, LunarOriginsItems.LONG_CLEAR_VISION_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, GLIMMERING_AQUA_GUMMY, LunarOriginsItems.HYDRATION_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(LunarOriginsItems.HYDRATION_POTION, Items.REDSTONE, LunarOriginsItems.LONG_HYDRATION_POTION);

        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "luna_glasses"), LunarOriginsItems.LUNA_GLASSES);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "gnap_glasses"), LunarOriginsItems.GNAP_GLASSES);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "fish_bowl"), LunarOriginsItems.FISH_BOWL);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glass_bowl"), LunarOriginsItems.GLASS_BOWL);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "kelp_carrot"), LunarOriginsItems.KELP_CARROT);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "amethyst_bowl"), LunarOriginsItems.AMETHYST_BOWL);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "diving_helmet"), LunarOriginsItems.DIVING_HELMET);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "wopol_icon"), LunarOriginsItems.WOPOL_ICON);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "gnaporeon_icon"), LunarOriginsItems.GNAPOREON_ICON);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "goggles"), LunarOriginsItems.GOGGLES);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "bowl_sealant"), LunarOriginsItems.BOWL_SEALANT);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "bowl_super_sealant"), LunarOriginsItems.BOWL_SUPER_SEALANT);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "aqua_gummy"), LunarOriginsItems.AQUA_GUMMY);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glimmering_aqua_gummy"), LunarOriginsItems.GLIMMERING_AQUA_GUMMY);


        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glasses_arm"), LunarOriginsItems.GLASSES_ARM);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glasses_arm_red"), LunarOriginsItems.GLASSES_ARM_RED);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glasses_frame"), LunarOriginsItems.GLASSES_FRAME);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glasses_frame_red"), LunarOriginsItems.GLASSES_FRAME_RED);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glasses_hinge"), LunarOriginsItems.GLASSES_HINGE);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "hinge_part"), LunarOriginsItems.HINGE_PART);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "hinge_pin"), LunarOriginsItems.HINGE_PIN);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "hinge_spring"), LunarOriginsItems.HINGE_SPRING);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "glasses_lens"), LunarOriginsItems.GLASSES_LENS);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "wire_coil"), LunarOriginsItems.WIRE_COIL);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "crude_lens"), LunarOriginsItems.CRUDE_LENS);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "lens_cast"), LunarOriginsItems.LENS_CAST);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "unfocused_lens"), LunarOriginsItems.UNFOCUSED_LENS);

        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "incomplete_cast"), LunarOriginsItems.INCOMPLETE_CAST);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "incomplete_lens"), LunarOriginsItems.INCOMPLETE_LENS);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "incomplete_frame"), LunarOriginsItems.INCOMPLETE_FRAME);
        Registry.register(Registries.ITEM, new Identifier(Lunar_origins.MOD_ID, "incomplete_hinge"), LunarOriginsItems.INCOMPLETE_HINGE);

        //FluidStorage.ITEM.registerForItems((itemStack, context) ->
        //        GlassBowl.fluidStorage, GLASS_BOWL);


        Registry.register(Registries.ITEM_GROUP, new Identifier("lunar_origins", "lunar_origins"), LunarOriginsItems.LUNAR_ORIGINS_GROUP);
    }
}

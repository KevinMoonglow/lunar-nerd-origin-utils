package io.github.KevinMoonglow.lunar_origins.item;


import io.github.KevinMoonglow.lunar_origins.effects.LunarOriginsEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;



public class FoodComponents {
    public static final FoodComponent KELP_CARROT = new FoodComponent.Builder()
            .hunger(4)
            .saturationModifier(0.9f)
            .statusEffect(new StatusEffectInstance(LunarOriginsEffects.CLEAR_VISION, 1200, 0, false, false, true), 1.0f)
            .build();

    public static final FoodComponent AQUA_GUMMY = new FoodComponent.Builder()
            .alwaysEdible()
            .snack()
            .statusEffect(new StatusEffectInstance(LunarOriginsEffects.HYDRATION, 600), 1.0f)
            .hunger(1)
            .saturationModifier(0)
            .build();
    public static final FoodComponent GLIMMERING_AQUA_GUMMY = new FoodComponent.Builder()
            .alwaysEdible()
            .snack()
            .statusEffect(new StatusEffectInstance(LunarOriginsEffects.HYDRATION, 1200), 1.0f)
            .hunger(1)
            .saturationModifier(0)
            .build();

}

package io.github.KevinMoonglow.lunar_origins.effects;

import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LunarOriginsEffects {
    public static final StatusEffect CLEAR_VISION = new ClearVision();
    public static final StatusEffect HYDRATION = new HydrationEffect();
    public static final StatusEffect WATER_BLEND = new WaterBlending();
    public static final StatusEffect AIR_SWIMMING = new AirSwimming();

    public static void initEffects() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Lunar_origins.MOD_ID, "clear_vision"), CLEAR_VISION);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Lunar_origins.MOD_ID, "hydration"), HYDRATION);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Lunar_origins.MOD_ID, "water_blending"), WATER_BLEND);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Lunar_origins.MOD_ID, "air_swimming"), AIR_SWIMMING);

    }
}

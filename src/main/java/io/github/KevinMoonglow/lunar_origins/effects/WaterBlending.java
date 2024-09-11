package io.github.KevinMoonglow.lunar_origins.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.FluidTags;

public class WaterBlending extends StatusEffect {
    public WaterBlending() {
        super(StatusEffectCategory.BENEFICIAL, 0x62FFE5);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity.getFluidHeight(FluidTags.WATER) > 0) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 40, 0, true, false));
        }
        else {
            entity.removeStatusEffect(LunarOriginsEffects.WATER_BLEND);
        }
    }
}
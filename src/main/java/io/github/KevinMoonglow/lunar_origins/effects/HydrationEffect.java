package io.github.KevinMoonglow.lunar_origins.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class HydrationEffect extends StatusEffect {
    public HydrationEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x62FFE5);
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    }

}
package io.github.KevinMoonglow.lunar_origins.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ClearVision extends StatusEffect {


    public ClearVision() {
        super(StatusEffectCategory.BENEFICIAL, 0x277FD6);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    }
}


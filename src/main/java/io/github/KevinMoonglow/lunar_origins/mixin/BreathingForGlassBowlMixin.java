package io.github.KevinMoonglow.lunar_origins.mixin;

import dev.micalobia.breathinglib.data.BreathingInfo;
import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.KevinMoonglow.lunar_origins.item.GlassBowl;
import io.github.KevinMoonglow.lunar_origins.item.LunarOriginsItems;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.power.ModifyBreathingPower;
import io.github.eggohito.eggolib.power.PrioritizedPower;
import io.github.eggohito.eggolib.util.EntityOffset;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Mixin(ModifyBreathingPower.class)
public class BreathingForGlassBowlMixin extends PrioritizedPower {
    public BreathingForGlassBowlMixin(PowerType<?> powerType, LivingEntity livingEntity, int priority) {
        super(powerType, livingEntity, priority);
    }


    @Inject(method = "integrateCallback", at = @At("HEAD"), cancellable = true)
    private static void modifiedBreathCallback(@NotNull LivingEntity livingEntity, @NotNull CallbackInfoReturnable<TypedActionResult<Optional<BreathingInfo>>> cir) {
        ItemStack stack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
        Item helmItem = stack.getItem();

        boolean hasWaterBreathing = livingEntity.hasStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("minecraft", "water_breathing")));
        boolean hasGills;
        boolean originsWaterBreathing;
        boolean waterBreather;
        try {
            originsWaterBreathing = PowerTypeRegistry.get(new Identifier("origins", "water_breathing")).isActive(livingEntity);
            waterBreather = PowerTypeRegistry.get(new Identifier(Lunar_origins.MOD_ID, "water_breather")).isActive(livingEntity);
        } catch (IllegalArgumentException e) {
            originsWaterBreathing = false;
            waterBreather = false;
        }
        hasGills = originsWaterBreathing || waterBreather;


        LunarOriginsItems.WaterBowlState water = LunarOriginsItems.WaterBowlState.NONE;

        if (helmItem instanceof GlassBowl) {
            NbtCompound nbt = stack.getNbt();

            if (nbt != null) {
                if (hasGills) {
                    /* Determines the effective water bowl state for a water breather. This is forgiving and lets you
                       breathe whenever there's water in the bowl. Even if it's below eye level. */

                    long waterLevel = nbt.getLong("waterLevel");

                    if (waterLevel >= GlassBowl.BREATHE_LEVEL) {
                        water = LunarOriginsItems.WaterBowlState.WATER;
                    } else {
                        water = LunarOriginsItems.WaterBowlState.AIR;
                    }
                } else {
                    // Determines effective water bowl state for an air-breather. This version is based on eye level,
                    // so you can lift your head into the air bubble to take a breath.
                    boolean waterEyeLevel = nbt.getBoolean("waterEyeLevel");
                    if (waterEyeLevel) {
                        water = LunarOriginsItems.WaterBowlState.WATER;
                    } else {
                        water = LunarOriginsItems.WaterBowlState.AIR;
                    }
                }
            }
        }

        List<ModifyBreathingPower> mbps = PowerHolderComponent.getPowers(livingEntity, ModifyBreathingPower.class);
        if (mbps.isEmpty()) {
            switch (water) {
                case NONE -> cir.setReturnValue(TypedActionResult.pass(Optional.empty()));
                case WATER -> {
                    if (hasGills || hasWaterBreathing) {
                        // Origins water breathing has some hardcoded behavior, so we'll have to grant the potion effect
                        // of the Origin in use is relying on that power.
                        if (originsWaterBreathing) {
                            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 100, 0, false, true));
                            cir.setReturnValue(TypedActionResult.pass(Optional.empty()));
                        }
                        else cir.setReturnValue(TypedActionResult.success(Optional.empty()));
                    }
                    else {
                        cir.setReturnValue(TypedActionResult.fail(Optional.empty()));
                    }

                }
                case AIR -> {
                    // Origins water breathing has hardcoded behavior, so we'll just do nothing when that power is
                    // active.
                    if (originsWaterBreathing) cir.setReturnValue(TypedActionResult.pass(Optional.empty()));
                    else if (hasGills && !hasWaterBreathing) cir.setReturnValue(TypedActionResult.fail(Optional.empty()));
                    else cir.setReturnValue(TypedActionResult.success(Optional.empty()));
                }
            }
            return;
        }

        ModifyBreathingPower hpmbp = mbps
                .stream()
                .max(Comparator.comparing(ModifyBreathingPower::getPriority))
                .get();
        BlockPos blockPos = EntityOffset.EYES.getBlockPos(livingEntity);

        if (hpmbp.hasBreathingStatusEffects()) {
            cir.setReturnValue(TypedActionResult.success(Optional.empty()));
            return;
        }
        if (livingEntity instanceof PlayerEntity playerEntity && playerEntity.getAbilities().invulnerable) {
            cir.setReturnValue(TypedActionResult.consume(Optional.empty()));
            return;
        }

        switch (water) {
            case AIR -> {
                if (hasGills) cir.setReturnValue(TypedActionResult.fail(Optional.of(hpmbp.getLoseBreathInfo())));
                else cir.setReturnValue(TypedActionResult.success(Optional.of(hpmbp.getGainBreathInfo())));
                return;
            }
            case WATER -> {
                if (hasGills) cir.setReturnValue(TypedActionResult.success(Optional.of(hpmbp.getGainBreathInfo())));
                else cir.setReturnValue(TypedActionResult.fail(Optional.of(hpmbp.getLoseBreathInfo())));
                return;
            }
        }

        if (hpmbp.canBreatheIn(blockPos)) {
            cir.setReturnValue(TypedActionResult.success(Optional.of(hpmbp.getGainBreathInfo())));
            return;
        }

        cir.setReturnValue(TypedActionResult.fail(Optional.of(hpmbp.getLoseBreathInfo())));
    }
}






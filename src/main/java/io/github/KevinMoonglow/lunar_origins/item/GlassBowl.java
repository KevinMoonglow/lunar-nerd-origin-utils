package io.github.KevinMoonglow.lunar_origins.item;

import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


@SuppressWarnings("UnstableApiUsage")
public class GlassBowl extends ArmorItem implements PowerGrantingItem {

    @SuppressWarnings("UnstableApiUsage")
    public static final long MAX_WATER = FluidConstants.BUCKET;
    public static final int EYE_LEVEL = (int)(MAX_WATER * 0.6);
    public static final int BASE_DRAIN_PER_TICK = 6;
    public static final int CRACK_LOSS_AMOUNT = 2;
    public static final int BREATHE_LEVEL = 20;
    public static final int FISHBOWL_LEVEL = (int)(MAX_WATER * 0.2);
    public static final float VIEW_LOOK_MIN_DELTA = 0.008f;
    public static final int MAX_SEALANT = 72000;
    public final int[] SEALANT_DRAIN_STEPS;

    @SuppressWarnings("UnstableApiUsage")
    public static final SingleFluidStorage fluidStorage = new SingleFluidStorage() {

        @Override
        protected long getCapacity(FluidVariant variant) {
            if (variant.equals(FluidVariant.of(Fluids.WATER)))
                return MAX_WATER;
            return 0;
        }

        @Override
        public void writeNbt(NbtCompound nbt) {
            nbt.putLong("waterLevel", this.amount);
            //Lunar_origins.LOGGER.info("*** writeNbt");
        }

        @Override
        public void readNbt(NbtCompound nbt) {
            amount = nbt.getLong("waterLevel");
            if(amount >= 0)
                variant = FluidVariant.of(Fluids.WATER);
            else {
                variant = FluidVariant.blank();
            }
            //Lunar_origins.LOGGER.info("*** readNbt");
        }

        @Override
        public long getAmount() {
            return super.getAmount();
        }

        @Override
        public FluidVariant getResource() {
            return super.getResource();
        }

        @Override
        protected void onFinalCommit() {
            super.onFinalCommit();
        }
    };

    protected String FULL_KEY = "item.lunar_origins.glass_bowl.full";

    private final List<StackPowerUtil.StackPower> POWERS;
    public GlassBowl(ArmorMaterial material, Type type, Settings settings, int[] sealantDrainSteps) {
        super(material, type, settings);
        POWERS = new LinkedList<>();

        // Power that stops the entity from eating food.
        StackPowerUtil.StackPower blockFoodPower = new StackPowerUtil.StackPower();
        blockFoodPower.powerId = new Identifier(Lunar_origins.MOD_ID, "glass_bowl_blocks_food");
        blockFoodPower.slot = EquipmentSlot.HEAD;
        blockFoodPower.isHidden = true;
        blockFoodPower.isNegative = false;
        POWERS.add(blockFoodPower);

        /* Power that grants night vision in water. The power itself has conditions that will check for being underwater
         and the water level in the bowl. */
        StackPowerUtil.StackPower nightVisionGogglesPower = new StackPowerUtil.StackPower();
        nightVisionGogglesPower.powerId = new Identifier(Lunar_origins.MOD_ID, "night_vision_for_goggles");
        nightVisionGogglesPower.slot = EquipmentSlot.HEAD;
        nightVisionGogglesPower.isHidden = true;
        nightVisionGogglesPower.isNegative = false;
        POWERS.add(nightVisionGogglesPower);

        SEALANT_DRAIN_STEPS = sealantDrainSteps;
    }

    public String getFullKey() {
        return FULL_KEY;
    }

    @Override
    public Text getName(ItemStack stack) {
        if(stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();
            assert nbt != null;
            long waterLevel = nbt.getLong("waterLevel");
            if(waterLevel >= FISHBOWL_LEVEL) {
                return Text.translatable(getFullKey());
            }
        }
        return super.getName(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);

        long waterLevel = 0;
        if(itemStack.hasNbt()) {
            assert itemStack.getNbt() != null;
            waterLevel = itemStack.getNbt().getLong("waterLevel");
        }

        BlockHitResult hitResult = raycast(world, playerEntity,
                waterLevel < MAX_WATER ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);

        if(hitResult.getType() == HitResult.Type.BLOCK && waterLevel < MAX_WATER) {
            BlockPos blockPos = hitResult.getBlockPos();

            if (!world.canPlayerModifyAt(playerEntity, blockPos)) {
                return TypedActionResult.pass(itemStack);
            }
            if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {

                BlockState state = world.getBlockState(blockPos);

                if(state.getBlock() instanceof FluidDrainable fluidBlock) {
                    ItemStack s = fluidBlock.tryDrainFluid(world, blockPos, state);
                    if(!s.isEmpty()) {
                        world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        world.emitGameEvent(playerEntity, GameEvent.FLUID_PICKUP, blockPos);
                    }
                }

                return TypedActionResult.success(this.fill(itemStack, playerEntity), world.isClient());
            }
        }
        //return TypedActionResult.pass(itemStack);
        return super.use(world, playerEntity, hand);
    }

    protected ItemStack fill(@NotNull ItemStack stack, @NotNull PlayerEntity player) {
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putLong("waterLevel", MAX_WATER);
        nbt.putBoolean("waterEyeLevel", true);
        nbt.putBoolean("hasWater", true);

        return stack;
    }

    public boolean isCracked(ItemStack stack) {
        return (float) stack.getDamage() / stack.getMaxDamage() >= 0.5;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity != null && entity.isLiving() && slot == EquipmentSlot.HEAD.getEntitySlotId()) {
            LivingEntity p = (LivingEntity) entity;

            long waterLevel = 0;
            int sealant = 0;
            boolean superSealant = false;


            NbtCompound nbt = null;
            ItemStack headStack = p.getEquippedStack(EquipmentSlot.HEAD);

            if (stack.hasNbt()) {
                nbt = stack.getNbt();
                assert nbt != null;
                waterLevel = nbt.getLong("waterLevel");
                sealant = nbt.getInt("bowlSealant");
                superSealant = nbt.getBoolean("bowlSuperSealant");
            }
            else if(entity.isSubmergedInWater() && headStack == stack) {
                nbt = new NbtCompound();
                stack.setNbt(nbt);
            }

            if (headStack == stack && nbt != null) {
                float sealantPercent = (float)sealant / MAX_SEALANT;

                int sealantLossStep = (int)Math.ceil(sealantPercent * (SEALANT_DRAIN_STEPS.length - 1));
                int sealantLossAmount;

                if(superSealant) sealantLossAmount = 0;
                else sealantLossAmount = SEALANT_DRAIN_STEPS[sealantLossStep];

                boolean cracked = isCracked(stack);

                if (!p.isSubmergedInWater()) {
                    if (waterLevel > 0) {
                        if(sealant > 0 && !superSealant) {
                            sealant -= 1;
                            nbt.putInt("bowlSealant", sealant);
                        }

                        waterLevel -= cracked ? BASE_DRAIN_PER_TICK * CRACK_LOSS_AMOUNT : 0;
                        waterLevel -= (long) BASE_DRAIN_PER_TICK * sealantLossAmount;
                        if(waterLevel < 0) waterLevel = 0;
                        nbt.putLong("waterLevel", waterLevel);
                        nbt.putBoolean("hasWater", waterLevel > 0);
                    }
                }
                else {
                    if (waterLevel < MAX_WATER) {
                        if(sealant > 0 && !superSealant) {
                            sealant -= 1;
                            nbt.putInt("bowlSealant", sealant);
                        }

                        waterLevel += cracked ? BASE_DRAIN_PER_TICK * CRACK_LOSS_AMOUNT : 0;
                        waterLevel += (long) BASE_DRAIN_PER_TICK * sealantLossAmount;
                        if(waterLevel > MAX_WATER) waterLevel = MAX_WATER;
                        nbt.putLong("waterLevel", waterLevel);
                        nbt.putBoolean("hasWater", waterLevel > 0);
                    }
                }

                float viewLevelP = entity.getPitch() / 90f;
                float viewWaterScaling = viewLevelP >= 0f ? viewLevelP * 2.0f + 1.0f : viewLevelP * 2.0f - 1.0f;

                float waterLevelP = (float) waterLevel / MAX_WATER;
                float eyeLevelP = (float) EYE_LEVEL / MAX_WATER;


                float waterLevelAdjusted;
                if(viewWaterScaling > 0)
                    waterLevelAdjusted = waterLevelP * viewWaterScaling;
                else
                    waterLevelAdjusted = 1 - ((1 - waterLevelP) * -viewWaterScaling);


                float waterDiff = waterLevelAdjusted - eyeLevelP;
                float waterDiffScaled = waterDiff > 0f ? waterDiff / (1.0f - eyeLevelP) : waterDiff / eyeLevelP;


                nbt.putFloat("waterViewHeight", waterDiffScaled);

                if (waterDiff > 0 - VIEW_LOOK_MIN_DELTA)
                    nbt.putBoolean("waterEyeLevel", true);
                else if (waterDiff < 0 + VIEW_LOOK_MIN_DELTA) {
                    nbt.putBoolean("waterEyeLevel", false);
                }
            }
        }
    }

    @Override
    public Collection<StackPowerUtil.StackPower> getPowers(ItemStack stack, EquipmentSlot slot) {
        return POWERS.stream().filter(p -> p.slot == slot).toList();
    }

    public boolean applySealant(ItemStack stack, int amount) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int sealant = nbt.getInt("bowlSealant");
        boolean superSealant = nbt.getBoolean("bowlSuperSealant");

        if(sealant >= MAX_SEALANT || superSealant) {
            return false;
        }

        sealant += amount;
        if(sealant > MAX_SEALANT)
            sealant = MAX_SEALANT;

        nbt.putInt("bowlSealant", sealant);
        return true;
    }
    public boolean applySuperSealant(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        boolean superSealant = nbt.getBoolean("bowlSuperSealant");

        if(superSealant) return false;

        nbt.putInt("bowlSealant", MAX_SEALANT);
        nbt.putBoolean("bowlSuperSealant", true);
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        NbtCompound nbt = stack.getNbt();
        if(nbt != null) {
            if(nbt.getBoolean("bowlSuperSealant"))
                tooltip.add(Text.translatable("item.lunar_origins.glass_bowl.tooltip.super_seal").formatted(Formatting.GRAY));
            else if(nbt.getInt("bowlSealant") > 0)
                tooltip.add(Text.translatable("item.lunar_origins.glass_bowl.tooltip.seal").formatted(Formatting.GRAY));
            else
                tooltip.add(Text.translatable("item.lunar_origins.glass_bowl.tooltip.no_seal").formatted(Formatting.GRAY));
        }
        else tooltip.add(Text.translatable("item.lunar_origins.glass_bowl.tooltip.no_seal").formatted(Formatting.GRAY));

        if(nbt != null) {
            boolean superSealant = nbt.getBoolean("bowlSuperSealant");
            int sealant = nbt.getInt("bowlSealant");
            int segments = (int) Math.floor((float)sealant / MAX_SEALANT * 5.0f);
            String s = "█".repeat(segments) + "░".repeat(5 - segments);
            MutableText t = Text.literal(s);

            if(sealant <= 0) t = t.formatted(Formatting.DARK_GRAY);
            else if(superSealant) t = t.formatted(Formatting.YELLOW);
            else t = t.formatted(Formatting.GREEN);
            tooltip.add(t);
        }
    }


    private void putWaterLevel(@Nullable NbtCompound nbt, @NotNull ItemStack stack, long newWater) {
        if(nbt == null) {
            nbt = new NbtCompound();
            stack.setNbt(nbt);
        }
        nbt.putLong("waterLevel", newWater);
        nbt.putBoolean("hasWater", newWater > 0);
    }

    private long pourToBowlStack(
            @NotNull ItemStack source, @Nullable NbtCompound sourceNbt,
            @NotNull ItemStack target, @Nullable NbtCompound targetNbt,
            @Nullable Long maxAmount) {
        long sourceWaterLevel = sourceNbt != null ? sourceNbt.getLong("waterLevel") : 0;
        long targetWaterLevel = targetNbt != null ? targetNbt.getLong("waterLevel") : 0;

        long maxTransfer = Math.max(MAX_WATER - targetWaterLevel, 0);
        long fullTransfer = Math.min(maxTransfer, sourceWaterLevel);
        long actualTransfer = maxAmount != null ? Math.min(maxAmount, fullTransfer) : fullTransfer;

        putWaterLevel(sourceNbt, source, sourceWaterLevel - actualTransfer);
        putWaterLevel(targetNbt, target, targetWaterLevel + actualTransfer);

        return actualTransfer;
    }
    private long pourToBowlStack(
            @NotNull ItemStack source, @Nullable NbtCompound sourceNbt,
            @NotNull ItemStack target, @Nullable NbtCompound targetNbt) {
        return pourToBowlStack(source, sourceNbt, target, targetNbt, null);
    }



    private boolean pourToContainer(ItemStack bowl, ItemStack container, PlayerEntity player, Object targetSlot, Item containerItemType, Item filledItemType, long amount) {
        NbtCompound nbt = bowl.getNbt();
        long waterLevel = nbt != null ? nbt.getLong("waterLevel") : 0;
        Item containerItem = container.getItem();
        if(containerItem == containerItemType) {
            if(waterLevel < amount) return true;

            ItemStack newContainer = new ItemStack(filledItemType);
            if(filledItemType instanceof PotionItem)
                PotionUtil.setPotion(newContainer, Potions.WATER);
            container.decrement(1);

            if(container.getCount() <= 0) {
                if(targetSlot instanceof Slot) ((Slot)targetSlot).setStack(newContainer);
                else if(targetSlot instanceof StackReference) ((StackReference)targetSlot).set(newContainer);
                else assert false;
            }
            else if(!player.getInventory().insertStack(newContainer)) {
                player.dropItem(newContainer, false);
            }

            putWaterLevel(nbt, bowl, waterLevel - amount);
            return true;
        }
        return false;
    }


    private boolean pourFromContainer(ItemStack bowl, ItemStack container, PlayerEntity player, Object sourceSlot, Item containerItemType, Item filledItemType, long amount) {
        NbtCompound nbt = bowl.getNbt();
        long waterLevel = nbt != null ? nbt.getLong("waterLevel") : 0;
        Item containerItem = container.getItem();
        if(containerItem == filledItemType) {
            if(waterLevel + amount > MAX_WATER) return true;
            if (filledItemType instanceof PotionItem && PotionUtil.getPotion(container) != Potions.WATER) {
                return false;
            }
            if(sourceSlot instanceof Slot) ((Slot)sourceSlot).setStack(new ItemStack(containerItemType));
            else if(sourceSlot instanceof StackReference) ((StackReference)sourceSlot).set(new ItemStack(containerItemType));
            else assert false;

            putWaterLevel(nbt, bowl, waterLevel + amount);
            return true;
        }
        return false;
    }


    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if(clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            ItemStack target = slot.getStack();
            Item targetItem = target.getItem();
            NbtCompound nbt = stack.getNbt();
            long waterLevel = nbt != null ? nbt.getLong("waterLevel") : 0;

            if(pourToContainer(stack, target, player, slot, Items.GLASS_BOTTLE, Items.POTION, FluidConstants.BOTTLE))
                return true;
            else if(pourFromContainer(stack, target, player, slot, Items.GLASS_BOTTLE, Items.POTION, FluidConstants.BOTTLE))
                return true;
            else if(pourToContainer(stack, target, player, slot, Items.BUCKET, Items.WATER_BUCKET, FluidConstants.BUCKET))
                return true;
            else if(pourFromContainer(stack, target, player, slot, Items.BUCKET, Items.WATER_BUCKET, FluidConstants.BUCKET))
                return true;
            else if(targetItem instanceof GlassBowl) {
                ItemStack headGear = player.getEquippedStack(EquipmentSlot.HEAD);
                if(headGear == target) return true;

                NbtCompound targetNbt = target.getNbt();
                if(waterLevel > 0)
                    pourToBowlStack(stack, nbt, target, targetNbt);
                else
                    pourToBowlStack(target, targetNbt, stack, nbt);
                return true;
            }

        }
        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if(clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            ItemStack headGear = player.getEquippedStack(EquipmentSlot.HEAD);
            if(headGear == stack) return true;

            if(pourToContainer(stack, otherStack, player, cursorStackReference, Items.GLASS_BOTTLE, Items.POTION, FluidConstants.BOTTLE))
                return true;
            else if(pourFromContainer(stack, otherStack, player, cursorStackReference, Items.GLASS_BOTTLE, Items.POTION, FluidConstants.BOTTLE))
                return true;
            else if(pourToContainer(stack, otherStack, player, cursorStackReference, Items.BUCKET, Items.WATER_BUCKET, FluidConstants.BUCKET))
                return true;
            else if(pourFromContainer(stack, otherStack, player, cursorStackReference, Items.BUCKET, Items.WATER_BUCKET, FluidConstants.BUCKET))
                return true;
        }


        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}

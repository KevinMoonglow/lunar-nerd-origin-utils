package io.github.KevinMoonglow.lunar_origins.renderer;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.RotationAxis;

public class GnapGlassesRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(contextModel instanceof PlayerEntityModel<? extends LivingEntity> playerEntityModel &&
                entity instanceof AbstractClientPlayerEntity player) {
            MinecraftClient mc = MinecraftClient.getInstance();

            if(mc.getCameraEntity() != entity || mc.gameRenderer.getCamera().isThirdPerson() || mc.currentScreen != null) {
                matrices.push();

                playerEntityModel.head.rotate(matrices);

                matrices.translate(0, 0.3, -0.5 );
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f));
                matrices.scale(0.65f, 0.625f, 0.5f);

                ItemStack s = stack.copy();
                NbtCompound nbt = s.getOrCreateNbt();
                nbt.putBoolean("faceModel", true);

                mc.getItemRenderer().renderItem(s, ModelTransformationMode.HEAD, light, 0, matrices, vertexConsumers, mc.world, 0);
                matrices.pop();
            }

        }
    }

}

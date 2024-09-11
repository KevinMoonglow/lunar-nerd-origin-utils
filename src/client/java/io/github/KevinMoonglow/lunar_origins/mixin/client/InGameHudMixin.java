package io.github.KevinMoonglow.lunar_origins.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.KevinMoonglow.lunar_origins.Lunar_origins;
import io.github.KevinMoonglow.lunar_origins.item.AmethystGlassBowl;
import io.github.KevinMoonglow.lunar_origins.item.DivingHelmet;
import io.github.KevinMoonglow.lunar_origins.item.GlassBowl;
import io.github.KevinMoonglow.lunar_origins.item.Goggles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Invoker("renderOverlay")
    abstract void invokeRenderOverlay(DrawContext context, Identifier texture, float opacity);

    @Unique Identifier WATER_TEXTURE = new Identifier("textures/misc/underwater.png");
    @Unique final Identifier AIR_BUBBLE_TEXTURE = new Identifier(Lunar_origins.MOD_ID, "textures/misc/air_bubble_overlay.png");
    @Unique final Identifier GLASS_BOWL_OVERLAY = new Identifier(Lunar_origins.MOD_ID, "textures/misc/glass_bowl_overlay.png");
    @Unique final Identifier GLASS_BOWL_CRACK_OVERLAY = new Identifier(Lunar_origins.MOD_ID, "textures/misc/glass_bowl_crack_overlay.png");
    @Unique final Identifier AMETHYST_BOWL_OVERLAY = new Identifier(Lunar_origins.MOD_ID, "textures/misc/amethyst_bowl_overlay.png");
    @Unique final Identifier DIVING_HELMET_OVERLAY = new Identifier(Lunar_origins.MOD_ID, "textures/misc/diving_helmet_overlay.png");
    @Unique final Identifier AMETHYST_BOWL_CRACK_OVERLAY = new Identifier(Lunar_origins.MOD_ID, "textures/misc/amethyst_bowl_crack_overlay.png");
    @Unique final Identifier GOGGLES_OVERLAY = new Identifier(Lunar_origins.MOD_ID, "textures/misc/goggle_overlay.png");
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I", ordinal = 0))
    private void renderOnHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        Entity viewer = client.getCameraEntity();
        if(viewer != null && viewer.isLiving() && this.client.options.getPerspective().isFirstPerson()) {
            LivingEntity entity = (LivingEntity) viewer;

            ItemStack head = entity.getEquippedStack(EquipmentSlot.HEAD);
            Item item = head.getItem();
            if(item instanceof GlassBowl) {
                NbtCompound nbt = head.getNbt();

                if(nbt != null) {
                    long waterLevel = nbt.getLong("waterLevel");
                    float viewLevelP = entity.getPitch() / 90f;
                    float viewWaterScaling = viewLevelP >= 0f ? viewLevelP * 2.0f + 1.0f : viewLevelP * 2.0f - 1.0f;

                    float waterLevelP = (float) waterLevel / GlassBowl.MAX_WATER;
                    float eyeLevelP = (float) GlassBowl.EYE_LEVEL / GlassBowl.MAX_WATER;


                    float waterLevelAdjusted;
                    if(viewWaterScaling > 0)
                        waterLevelAdjusted = waterLevelP * viewWaterScaling;
                    else
                        waterLevelAdjusted = 1 - ((1 - waterLevelP) * -viewWaterScaling);


                    float waterDiff = waterLevelAdjusted - eyeLevelP;
                    float level = waterDiff > 0f ? waterDiff / (1.0f - eyeLevelP) : waterDiff / eyeLevelP;

                    if(item instanceof DivingHelmet)
                        invokeRenderOverlay(context, DIVING_HELMET_OVERLAY, 1.0f);
                    else if(item instanceof AmethystGlassBowl)
                        invokeRenderOverlay(context, AMETHYST_BOWL_OVERLAY, 0.75f);
                    else
                        invokeRenderOverlay(context, GLASS_BOWL_OVERLAY, 0.75f);

                    if((float)head.getDamage() / head.getMaxDamage() >= 0.5f) {
                        if(!(item instanceof DivingHelmet)) {
                            if (item instanceof AmethystGlassBowl)
                                invokeRenderOverlay(context, AMETHYST_BOWL_CRACK_OVERLAY, 0.75f);
                            else
                                invokeRenderOverlay(context, GLASS_BOWL_CRACK_OVERLAY, 0.75f);
                        }
                    }
                    if(entity.isSubmergedIn(FluidTags.WATER))
                        renderAir(level);
                    else
                        renderWater(level);
                }
            }
            else if(item instanceof Goggles) {
                invokeRenderOverlay(context, GOGGLES_OVERLAY, 1.0f);
            }
        }
    }

    @Unique
    private void renderWater(float level) {
        level *= 0.5f;
        level += 0.5f;
        level = Math.min(level, 1.0f);
        level = Math.max(level, 0.0f);

        double waterHeight = this.scaledHeight * level;
        double waterTop = this.scaledHeight - waterHeight;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.6f);
        RenderSystem.setShaderTexture(0, WATER_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, this.scaledHeight, -91.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -91.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, waterTop, -91.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, waterTop, -91.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    @Unique
    private void renderAir(float level) {
        level *= 0.5f;
        level += 0.5f;
        level = Math.min(level, 1.0f);
        level = Math.max(level, 0.0f);

        double waterHeight = this.scaledHeight * level;
        double waterTop = this.scaledHeight - waterHeight;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.6f);
        RenderSystem.setShaderTexture(0, AIR_BUBBLE_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, waterTop, -91.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, waterTop, -91.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, 0.0f, -91.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, 0.0, -91.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

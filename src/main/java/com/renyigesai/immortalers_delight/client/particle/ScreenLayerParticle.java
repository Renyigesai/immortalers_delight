package com.renyigesai.immortalers_delight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.ScreenLayerParticleModel;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenLayerParticle extends Particle {
    private final ScreenLayerParticleModel<?> model;
    private static final ResourceLocation INFERNAL_FORGING_TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "textures/particle/infernal_forging_screen_layer.png");
    private final int transparency;

    ScreenLayerParticle(ClientLevel pLevel, double pX, double pY, double pZ, int pTransparency) {
        super(pLevel, pX, pY, pZ);
        this.model = new ScreenLayerParticleModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ScreenLayerParticleModel.SCREEN_LAYER_PARTICLE));
        this.gravity = 0.0F;
        this.lifetime = 30;
        this.transparency = pTransparency;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        float f = ((float) this.age + pPartialTicks) / (float) this.lifetime;
        float g = f;
        float f1 = 0.05F + 0.5F * Mth.sin(f * (float) Math.PI);
        if (f < 0.5) f1 = (float) (this.transparency % 100) / 100;
        else f1 *= (float) (this.transparency % 100) / 100;

        PoseStack posestack = new PoseStack();
        posestack.mulPose(pRenderInfo.rotation());
        posestack.scale(-1.0F, -1.0F, 1.0F);
        posestack.translate(0.0F, -1.5F, 3.0F + 0.03 * g);

        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(RenderType.entityTranslucent(getTextureLocation()));
        int color = ((int) (f1 * 255.0F) << 24) | 0xFFFFFF;
        this.model.renderToBuffer(posestack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, color);
        multibuffersource$buffersource.endBatch();
    }

    public ResourceLocation getTextureLocation() {
        return INFERNAL_FORGING_TEXTURE_LOCATION;
    }

    public static ScreenLayerParticle.ScreenLayerParticleProvider screenLayerParticleProvider(SpriteSet sprite) {
        return new ScreenLayerParticle.ScreenLayerParticleProvider(sprite);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ScreenLayerParticleProvider implements ParticleProvider<ScreenLayerParticleOption> {
        private final SpriteSet sprite;

        public ScreenLayerParticleProvider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(ScreenLayerParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ScreenLayerParticle screenLayerparticle = new ScreenLayerParticle(pLevel, pX, pY, pZ, pType.getTransparency());
            screenLayerparticle.setAlpha(1.0F);
            return screenLayerparticle;
        }
    }
}

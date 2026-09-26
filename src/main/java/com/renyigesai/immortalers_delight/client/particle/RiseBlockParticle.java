package com.renyigesai.immortalers_delight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * “被冲击震起的方块”粒子：渲染完整方块模型，具有向上初速度，
 * 受重力影响，落地后快速消失。
 */
@OnlyIn(Dist.CLIENT)
public class RiseBlockParticle extends Particle {

    private final BlockState blockState;
    private final float scale;
//    private final float rotSpeedX;
//    private final float rotSpeedY;
//    private final float rotSpeedZ;
//    private float rotX;
//    private float rotY;
//    private float rotZ;

    protected RiseBlockParticle(ClientLevel level, double x, double y, double z,
                                double xSpeed, double ySpeed, double zSpeed,
                                BlockState blockState) {
        super(level, x, y, z);
        this.blockState = blockState;

        // 基础物理属性
        this.gravity = 1.0F;
        this.hasPhysics = true; // 开启方块碰撞检测，用于落地判断

        // 初速度：保证有一定向上的冲量
//        this.xd = xSpeed + (this.random.nextDouble() * 2.0 - 1.0) * 0.1;
//        this.zd = zSpeed + (this.random.nextDouble() * 2.0 - 1.0) * 0.1;
        this.yd = Math.max(ySpeed, 0.3) + this.random.nextDouble() * 0.2;

        this.friction = 0.98F;
        this.scale = 1;
        this.setSize(0.25F, 0.25F);

        // 随机旋转速度，增加“被震起”的杂乱感
//        this.rotSpeedX = (this.random.nextFloat() - 0.5F) * 40F;
//        this.rotSpeedY = (this.random.nextFloat() - 0.5F) * 40F;
//        this.rotSpeedZ = (this.random.nextFloat() - 0.5F) * 40F;

        // 寿命兜底（若一直不落地，也会在一段时间后消失）
        this.lifetime = 40 + this.random.nextInt(20);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        // 重力
        this.yd -= 0.04 * this.gravity;

        // 移动 + 碰撞检测（hasPhysics=true 时，move 会更新 this.onGround）
        this.move(this.xd, this.yd, this.zd);

        if (this.onGround) {
            // 落地：摩擦力骤增，旋转停止，并让粒子快速消失
            this.xd *= 0.6;
            this.zd *= 0.6;

            // 限制落地后只再存在很短时间
            if (this.lifetime - this.age > 5) {
                this.lifetime = this.age + 5;
            }
        } else {
            this.xd *= this.friction;
            this.yd *= this.friction;
            this.zd *= this.friction;

            // 只在空中旋转
//            this.rotX += this.rotSpeedX;
//            this.rotY += this.rotSpeedY;
//            this.rotZ += this.rotSpeedZ;
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Vec3 cameraPos = pRenderInfo.getPosition();
        double particleX = Mth.lerp(pPartialTicks, this.xo, this.x);
        double particleY = Mth.lerp(pPartialTicks, this.yo, this.y);
        double particleZ = Mth.lerp(pPartialTicks, this.zo, this.z);

        double relX = particleX - cameraPos.x();
        double relY = particleY - cameraPos.y();
        double relZ = particleZ - cameraPos.z();

        // 构建变换矩阵：平移到粒子位置 -> 缩放 -> 旋转
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(relX, relY, relZ);

//        poseStack.translate(0.0, 0.0, 0.0);
//        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(this.rotX + this.rotSpeedX * pPartialTicks));
//        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(this.rotY + this.rotSpeedY * pPartialTicks));
//        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(this.rotZ + this.rotSpeedZ * pPartialTicks));

        poseStack.scale(this.scale, this.scale, this.scale);
        poseStack.translate(-0.5, -0.5, -0.5);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        int light = this.getLightColor(pPartialTicks);
        blockRenderer.renderSingleBlock(
                this.blockState,
                poseStack,
                bufferSource,
                light,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        bufferSource.endBatch();
    }

    @Override
    public int getLightColor(float pPartialTick) {
        BlockPos pos = new BlockPos((int) this.x, (int) this.y, (int) this.z);
        return this.level.hasChunkAt(pos)
                ? LevelRenderer.getLightColor(this.level, pos)
                : 0;
    }

    /* ------------- Provider ------------- */

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<BlockParticleOption> {
        public Provider() {
        }

        @Override
        public Particle createParticle(BlockParticleOption type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            BlockState state = type.getState();
            if (state.isAir()) {
                return null;
            }
            return new RiseBlockParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, state);
        }
    }
}

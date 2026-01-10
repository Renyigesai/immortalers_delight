package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.ModSkullModel;
import com.renyigesai.immortalers_delight.entities.projectile.KiBlastEntity;
import com.renyigesai.immortalers_delight.entities.projectile.ToxicGasGrenadeEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ToxicGasGrenadeRenderer extends EntityRenderer<ToxicGasGrenadeEntity> {
    public static final ModelLayerLocation MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "gas_bomb"), "main");
    private static final ResourceLocation LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/entity/custom/empty.png");
    // 凋灵头颅的模型实例，基于SkullModel（骷髅头模型）实现
    private final ModSkullModel model;
    public ToxicGasGrenadeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ModSkullModel(pContext.bakeLayer(MODEL_LOCATION));
    }
    /**
     * 创建凋灵头颅的模型层定义（用于模型烘焙）
     * 定义模型的网格结构、UV坐标映射、部件位置等，是渲染模型的基础数据
     * @return 凋灵头颅的模型层定义，包含完整的网格信息
     */
    public static LayerDefinition createSkullLayer() {
        // 1. 创建网格定义实例，作为模型的根容器
        MeshDefinition meshdefinition = new MeshDefinition();
        // 2. 获取模型根部件定义，所有模型部件都依附于根部件
        PartDefinition partdefinition = meshdefinition.getRoot();
        // 3. 向根部件添加/替换"head"（头部）部件
        //    - CubeListBuilder.create()：创建立方体列表构建器，定义模型的立方体形状
        //    - texOffs(0, 35)：设置该立方体的UV坐标起始点（对应纹理图的X=0，Y=35）
        //    - addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)：定义立方体的尺寸和位置
        //      前三个参数：立方体的起始坐标（相对部件原点）
        //      后三个参数：立方体的长宽高（X=8F，Y=8F，Z=8F，对应一个正方体头颅）
        //    - PartPose.ZERO：设置部件的姿态（无旋转、无平移、无缩放，默认原点姿态）
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        // 4. 创建模型层定义，指定网格定义、纹理宽度64、纹理高度64（对应原版凋灵纹理的尺寸）
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void render(ToxicGasGrenadeEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        // 1. 保存当前矩阵堆栈状态（入栈），避免影响后续其他实体的渲染
        pPoseStack.pushPose();
        // 2. 对模型进行缩放变换：X轴和Y轴翻转（-1.0F），Z轴保持不变，修正模型的朝向
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        // 3. 计算偏航角（水平旋转）的平滑插值，基于部分游戏刻，实现旋转动作的平滑过渡
        float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        // 4. 计算俯仰角（垂直旋转）的平滑插值，实现上下摆动的平滑过渡
        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.setupAnim(0.0F, f, f1);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        pPoseStack.translate(0.0D, 0.25D, 0.1875D);
        pPoseStack.scale(0.99F, 0.99F, 0.99F);

        this.model.getHead().translateAndRotate(pPoseStack);;
        // 创建一个旋转矩阵，实现投掷物的转动效果
        float f2 = (float)pEntity.tickCount + pPartialTicks;
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        //渲染一个方块模型
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = ImmortalersDelightBlocks.CAUSTIC_ESSENTIAL_OIL.get().defaultBlockState();
        blockRenderDispatcher.renderSingleBlock(state,pPoseStack,pBuffer,pPackedLight,OverlayTexture.NO_OVERLAY);
        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    /**
     * 获取凋灵头颅对应的方块光照等级（用于渲染时的光照计算）
     * 凋灵头颅作为特殊投射物，强制返回最高光照等级15，确保在任何场景下都清晰可见
     * @param pEntity 凋灵头颅实体实例
     * @param pPos 凋灵头颅所在的方块坐标
     * @return 固定返回15（最高光照等级，无明暗衰减）
     */
    protected int getBlockLightLevel(ToxicGasGrenadeEntity pEntity, BlockPos pPos) {
        return 15;
    }
   public ResourceLocation getTextureLocation(ToxicGasGrenadeEntity pEntity) {
        return LOCATION;
    }
}

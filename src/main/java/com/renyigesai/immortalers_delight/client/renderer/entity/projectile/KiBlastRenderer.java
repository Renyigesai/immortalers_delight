package com.renyigesai.immortalers_delight.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.ModSkullModel;
import com.renyigesai.immortalers_delight.entities.projectile.KiBlastEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KiBlastRenderer extends EntityRenderer<KiBlastEntity> {
    public static final ModelLayerLocation MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "ki_blast"), "main");
    private static final ResourceLocation LOCATION = new ResourceLocation(ImmortalersDelightMod.MODID,"textures/item/custom/ki_blast.png");
    // 凋灵头颅的模型实例，基于SkullModel（骷髅头模型）实现
    private final ModSkullModel model;
    public KiBlastRenderer(EntityRendererProvider.Context pContext) {
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

    public void render(KiBlastEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        // 1. 保存当前矩阵堆栈状态（入栈），避免影响后续其他实体的渲染
        pPoseStack.pushPose();

        // 2. 对模型进行缩放变换：X轴和Y轴翻转（-1.0F），Z轴保持不变，修正模型的朝向
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);

        // 3. 计算偏航角（水平旋转）的平滑插值，基于部分游戏刻，实现旋转动作的平滑过渡
        float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        // 4. 计算俯仰角（垂直旋转）的平滑插值，实现上下摆动的平滑过渡
        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());

        // 5. 获取顶点消费者：根据当前实体的纹理，绑定对应的渲染类型，从多缓冲区中获取可用的顶点缓冲区
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));

        // 6. 初始化模型姿态：设置模型的旋转参数（无额外摆动，传入计算后的偏航角和俯仰角）
        this.model.setupAnim(0.0F, f, f1);

        // 7. 将模型渲染到顶点缓冲区：提交模型的顶点数据、颜色、光照等信息
        //    - pPoseStack：当前的矩阵堆栈（包含缩放、旋转变换）
        //    - vertexconsumer：顶点消费者，用于写入渲染数据
        //    - pPackedLight：打包光照值，控制模型的明暗
        //    - OverlayTexture.NO_OVERLAY：无叠加纹理（禁用受伤、发光等叠加效果）
        //    - 后续四个1.0F：RGBA颜色值（纯白，不修改模型纹理颜色）
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // 8. 恢复之前保存的矩阵堆栈状态（出栈），完成当前实体的渲染，不影响后续渲染
        pPoseStack.popPose();

        pPoseStack.pushPose();
        //第一步：先应用与尖牙本体完全相同的基础变换（旋转+缩放+位移）
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        pPoseStack.translate(0.0D, 0.25D, 0.1875D);
        pPoseStack.scale(0.99F, 0.99F, 0.99F);

        // 第二步：将PoseStack移动到base骨骼的位置（核心：复用骨骼动画的变换）
        this.model.getHead().translateAndRotate(pPoseStack);;

        // 第三步：调整铁剑相对于base骨骼的偏移（根据需求微调）
        // 注意：此处的偏移是基于base骨骼的本地坐标系，无需再处理翻转
//        pPoseStack.translate(-1/16F, 0.625F, 0.0f); // 可根据需要调整X/Y/Z偏移
//        // 可选：调整铁剑的旋转角度（比如让剑垂直/水平）
//        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F)); // 示例：绕X轴旋转90度
//        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
        //pPoseStack.scale(1.0F, 1.0F, 1.0F); // 铁剑的缩放（可根据需求调整）
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = //new ItemStack(ImmortalersDelightItems.WARPED_LAUREL.get());
                new ItemStack(ImmortalersDelightItems.DEBUG_ITEM.get());
        stack.setDamageValue(1);
        BakedModel bakedModel = itemRenderer.getModel(stack,pEntity.level(),null,0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight, OverlayTexture.NO_OVERLAY,bakedModel);
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
    protected int getBlockLightLevel(KiBlastEntity pEntity, BlockPos pPos) {
        return 15;
    }
   public ResourceLocation getTextureLocation(KiBlastEntity pEntity) {
        return LOCATION;
    }
}

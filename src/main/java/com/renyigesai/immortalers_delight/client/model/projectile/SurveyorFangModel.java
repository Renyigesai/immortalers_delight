package com.renyigesai.immortalers_delight.client.model.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.entities.projectile.SurveyorFangEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SurveyorFangModel<T extends SurveyorFangEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation SURVEYOR_FANG = new ModelLayerLocation(new ResourceLocation(ImmortalersDelightMod.MODID, "sword_on_a_stick"), "main");
    private final ModelPart bone;

    public SurveyorFangModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -20.0F, -0.5F, 1.0F, 27.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(4, 10).addBox(-1.5F, 3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition tool1_r1 = bone.addOrReplaceChild("tool1_r1", CubeListBuilder.create().texOffs(4, 0).addBox(-0.5F, -4.5F, -0.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, -20.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //this.animate(entity.attackAnimationState, SurveyorFangAnimation.ATTACK,ageInTicks, 1.0f);
        // 步骤1：调整动画进度曲线，让开合动画“先快后慢”
        // pLimbSwing（0~1）→ f（0~1）：通过三次方运算，让动画前期变化快，后期趋于平缓
        float f = limbSwing * 2.4f;
        boolean flag = false;
        if (f > 1.0F) {
            if (f > 1.2f) {
                f -= 1.0F; // 当进度溢出，反向运动
                flag = true;
            }
            else f = 1.0f;
        }
        if (flag) f = 1.0f - f * 5.0f;//下降动画使用快速的线性曲线
        else f = f * f * f; // 上升动画使用三次方曲线，先快后慢
        if (f < -2.4f) f = -2.4f;

        // 步骤2：上下颚旋转（开合动画）
        // Math.PI ≈ 3.14（180度），尖牙初始状态是闭合的（上下颚呈180度）
        // f * 0.35F * PI：开合角度最大约63度（0.35*180）
        //this.upperJaw.zRot = (float)Math.PI - f * 0.35F * (float)Math.PI; // 上颚向上张开
        //this.lowerJaw.zRot = (float)Math.PI + f * 0.35F * (float)Math.PI; // 下颚向下张开

        // 步骤3：尖牙整体上下移动（破土动画）
        // 结合正弦曲线，让尖牙在开合的同时上下摆动，模拟“破土而出”的震动感
        // pLimbSwing*2.7F：调整正弦曲线频率，让摆动更自然；*0.6*12：调整摆动幅度
        float f1 = (limbSwing + Mth.sin(limbSwing * 2.7F)) * 0.6F * 12.0F;
//        if (lastTick != entity.tickCount) {
//            lastTick = entity.tickCount;
//            System.out.println("f1:" + f1);
//            System.out.println("f:" + f);
//            System.out.println("lastTick:" + lastTick);
//        }
        this.bone.y = -28.0F - f * 18.0f; // 上颚Y轴位移
        //this.lowerJaw.y = this.upperJaw.y; // 下颚与上颚同步位移
        //this.base.y = this.upperJaw.y; // 尖牙底座与上颚同步位移
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return bone;
    }

    public void translateToRoot(PoseStack pPoseStack) {
        this.root().translateAndRotate(pPoseStack);
    }
}

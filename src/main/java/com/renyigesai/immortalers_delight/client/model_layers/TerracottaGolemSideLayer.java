package com.renyigesai.immortalers_delight.client.model_layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.TerracottaGolemModel;
import com.renyigesai.immortalers_delight.client.model.TerracottaGolemSideModel;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class TerracottaGolemSideLayer<T extends  TerracottaGolem> extends RenderLayer<T, TerracottaGolemModel<T>> {
    private static final String BRICK_NAME = "decorated_pot_side";
    private static final String ANGLER_NAME = "angler_pottery_pattern";
    private static final String ARCHER_NAME = "archer_pottery_pattern";
    private static final String ARMS_UP_NAME = "arms_up_pottery_pattern";
    private static final String BLADE_NAME = "blade_pottery_pattern";
    private static final String BREWER_NAME = "brewer_pottery_pattern";
    private static final String BURN_NAME = "burn_pottery_pattern";
    private static final String DANGER_NAME = "danger_pottery_pattern";
    private static final String EXPLORER_NAME = "explorer_pottery_pattern";
    private static final String FRIEND_NAME = "friend_pottery_pattern";
    private static final String HEART_NAME = "heart_pottery_pattern";
    private static final String HEARTBREAK_NAME = "heartbreak_pottery_pattern";
    private static final String HOWL_NAME = "howl_pottery_pattern";
    private static final String MINER_NAME = "miner_pottery_pattern";
    private static final String MOURNER_NAME = "mourner_pottery_pattern";
    private static final String PLENTY_NAME = "plenty_pottery_pattern";
    private static final String PRIZE_NAME = "prize_pottery_pattern";
    private static final String SHEAF_NAME = "sheaf_pottery_pattern";
    private static final String SHELTER_NAME = "shelter_pottery_pattern";
    private static final String SKULL_NAME = "skull_pottery_pattern";
    private static final String SNORT_NAME = "snort_pottery_pattern";
    private final List<String> properties;
    private final List<ResourceLocation> textrues;
    private final EntityModel<T> model;

    public TerracottaGolemSideLayer(RenderLayerParent<T, TerracottaGolemModel<T>> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        this.model = new TerracottaGolemSideModel<>(pModelSet.bakeLayer(TerracottaGolemSideModel.TERRACOTTA_GOLEM_SIDE_LAYER));
        this.properties = new ArrayList<String>();
        this.textrues = new ArrayList<ResourceLocation>();
        createTextures();
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
        this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        if (this.model instanceof TerracottaGolemSideModel) {
            for (int j = 0; j < 3; ++j) {
                VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pLivingEntity,j)));
                ((TerracottaGolemSideModel<T>) this.model).renderSide(j, pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        } else {
            VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pLivingEntity)));
            this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
//        Minecraft minecraft = Minecraft.getInstance();
//        boolean flag = minecraft.shouldEntityAppearGlowing(pLivingEntity) && pLivingEntity.isInvisible();
//        if (!pLivingEntity.isInvisible() || flag) {
//            VertexConsumer vertexconsumer;
//            if (flag) {
//                //发光轮廓渲染缓冲区
//                vertexconsumer = pBuffer.getBuffer(RenderType.outline(this.getTextureLocation(pLivingEntity)));
//            } else {
//                //半透明渲染缓冲区
//                vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pLivingEntity)));
//            }
//
//        }
    }


    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(TerracottaGolem pEntity) {
        return this.textrues.get(0);
    }

    public ResourceLocation getTextureLocation(TerracottaGolem pEntity, int pSlotID) {
        if (pSlotID == TerracottaGolem.INV_SLOT_LEFT) {
            return this.textrues.get(pEntity.getLeftDecorateID());
        }
        if (pSlotID == TerracottaGolem.INV_SLOT_BACK) {
            return this.textrues.get(pEntity.getBackDecorateID());
        }
        if (pSlotID == TerracottaGolem.INV_SLOT_RIGHT) {
            return this.textrues.get(pEntity.getRightDecorateID());
        }
        return this.textrues.get(0);
    }


    public void createTextures() {
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + BRICK_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + ANGLER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + ARCHER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + ARMS_UP_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + BLADE_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + BREWER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + BURN_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + DANGER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + EXPLORER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + FRIEND_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + HEART_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + HEARTBREAK_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + HOWL_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + MINER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + MOURNER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + PLENTY_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + PRIZE_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + SHEAF_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + SHELTER_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + SKULL_NAME + ".png"));
        textrues.add(new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/terracotta_golem/" + SNORT_NAME + ".png"));
        properties.add(BRICK_NAME);
        properties.add(ANGLER_NAME);
        properties.add(ARCHER_NAME);
        properties.add(ARMS_UP_NAME);
        properties.add(BLADE_NAME);
        properties.add(BREWER_NAME);
        properties.add(BURN_NAME);
        properties.add(DANGER_NAME);
        properties.add(EXPLORER_NAME);
        properties.add(FRIEND_NAME);
        properties.add(HEART_NAME);
        properties.add(HEARTBREAK_NAME);
        properties.add(HOWL_NAME);
        properties.add(MINER_NAME);
        properties.add(MOURNER_NAME);
        properties.add(PLENTY_NAME);
        properties.add(PRIZE_NAME);
        properties.add(SHEAF_NAME);
        properties.add(SHELTER_NAME);
        properties.add(SKULL_NAME);
        properties.add(SNORT_NAME);
    }
}

package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.SoulInfuserModel;
import com.renyigesai.immortalers_delight.client.model.TerracottaGolemModel;
import com.renyigesai.immortalers_delight.client.model_layers.TerracottaGolemSideLayer;
import com.renyigesai.immortalers_delight.entities.living.SoulInfuserEntity;
import com.renyigesai.immortalers_delight.entities.living.SoulInfuserTargetEntity;
import com.renyigesai.immortalers_delight.entities.living.TerracottaGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HijackedSoulInfuserRenderer extends MobRenderer<SoulInfuserEntity, SoulInfuserModel<SoulInfuserEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/soul_infuser.png");
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/custom/empty.png");
    public HijackedSoulInfuserRenderer(EntityRendererProvider.Context pContext) {
        super(pContext,
                new SoulInfuserModel<>(pContext.bakeLayer(SoulInfuserModel.LAYER_LOCATION)),
                1.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(SoulInfuserEntity pEntity) {
        return TEXTURE2;
    }
}

package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.StrangeArmourStandModel;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import com.renyigesai.immortalers_delight.entities.living.StrangeArmourStand;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StrangeArmourStandRenderer extends MobRenderer<StrangeArmourStand, StrangeArmourStandModel<StrangeArmourStand>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/entity/strange_armour_stand.png");

    public StrangeArmourStandRenderer(EntityRendererProvider.Context pContext) {
        super(pContext,
                new StrangeArmourStandModel<>(pContext.bakeLayer(StrangeArmourStandModel.STRANGE_ARMOUR_STAND)),
                0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(StrangeArmourStand pEntity) {
        return TEXTURE;
    }
}

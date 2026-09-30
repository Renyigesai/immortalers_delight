package com.renyigesai.immortalers_delight.client.renderer.entity;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.AncientMechanismCommanderModel;
import com.renyigesai.immortalers_delight.entities.living.AncientMechanismCommander;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AncientMechanismCommanderRenderer extends MobRenderer<AncientMechanismCommander, AncientMechanismCommanderModel<AncientMechanismCommander>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            ImmortalersDelightMod.MODID, "textures/entity/ancient_mechanism_commander.png");

    public AncientMechanismCommanderRenderer(EntityRendererProvider.Context context) {
        super(context, new AncientMechanismCommanderModel<>(context.bakeLayer(AncientMechanismCommanderModel.ANCIENT_MECHANISM_COMMANDER)), 0.45F);
    }

    @Override
    public ResourceLocation getTextureLocation(AncientMechanismCommander entity) {
        return TEXTURE;
    }
}

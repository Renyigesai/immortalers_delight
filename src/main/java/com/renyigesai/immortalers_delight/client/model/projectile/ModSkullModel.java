package com.renyigesai.immortalers_delight.client.model.projectile;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModSkullModel extends SkullModel {
    public ModSkullModel(ModelPart pRoot) {
        super(pRoot);
    }

    public ModelPart getHead() {
        return head;
    }
}

package com.renyigesai.immortalers_delight.compat.item;

import com.doggystudio.chirencqr.ltc.server.api.ILegendaryFeastLatiao;
import com.doggystudio.chirencqr.ltc.server.item.ItemLatiaoBase;
import com.doggystudio.chirencqr.ltc.server.misc.EnumLatiaoGrade;
import net.minecraft.world.food.FoodProperties;

public class DeveloperLatiaoItem extends ItemLatiaoBase implements ILegendaryFeastLatiao {
    public DeveloperLatiaoItem(int nutrition, float saturation, int effctTime, int effectLevel, EnumLatiaoGrade grade) {
        super(nutrition, saturation, effctTime, effectLevel, grade);
    }

    public DeveloperLatiaoItem(FoodProperties foodProperties, EnumLatiaoGrade grade) {
        super(foodProperties);
        this.grade = grade;
    }
}

package com.renyigesai.immortalers_delight.compat.item;

import com.doggystudio.chirencqr.ltc.server.item.ItemLatiaoBase;
import com.doggystudio.chirencqr.ltc.server.misc.EnumLatiaoGrade;
import net.minecraft.world.food.FoodProperties;

public class LatiaoItem extends ItemLatiaoBase {
    public LatiaoItem(FoodProperties foodProperties, EnumLatiaoGrade grade) {
        super(foodProperties);
        this.grade = grade;
    }
}

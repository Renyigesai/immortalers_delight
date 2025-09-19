package com.renyigesai.immortalers_delight.compat.item;

import com.doggystudio.chirencqr.ltc.server.item.ItemLatiaoBase;
import com.doggystudio.chirencqr.ltc.server.misc.EnumLatiaoGrade;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeveloperLatiaoItem extends ItemLatiaoBase {
    public DeveloperLatiaoItem(int nutrition, float saturation, int effctTime, int effectLevel, EnumLatiaoGrade grade) {
        super(nutrition, saturation, effctTime, effectLevel, grade);
    }

    public DeveloperLatiaoItem(FoodProperties foodProperties, EnumLatiaoGrade grade) {
        super(foodProperties);
        this.grade = grade;
    }
}

package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class GoldenToastItem extends PowerfulAbleFoodItem{
    private int type_id;
    public GoldenToastItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int type) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip);
        this.type_id = type;
    }

    public GoldenToastItem(Properties properties, @Nullable FoodProperties powerFoodProperties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil, int type) {
        super(properties, powerFoodProperties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
        this.type_id = type;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        ItemStack outStack = pStack.copy();
        boolean flag = false;
        if (pLevel instanceof ServerLevel serverLevel && pStack.getItem() instanceof GoldenToastItem thisItem) {

            if (flag) {
                int time = 300;
                if (thisItem.type_id == 1) time = 12000;
                if (thisItem.type_id == 2) time = 3600;
                pLivingEntity.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get(), time));
            }
        }
        return super.finishUsingItem(outStack,pLevel,pLivingEntity);
    }
}

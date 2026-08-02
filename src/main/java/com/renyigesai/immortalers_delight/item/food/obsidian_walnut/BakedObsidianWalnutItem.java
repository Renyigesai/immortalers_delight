package com.renyigesai.immortalers_delight.item.food.obsidian_walnut;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.EnchantAbleFoodItem;
import com.renyigesai.immortalers_delight.potion.CulturalLegacyMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.*;

public class BakedObsidianWalnutItem extends EnchantAbleFoodItem {

    public BakedObsidianWalnutItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
    }

    public BakedObsidianWalnutItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
    }

    public BakedObsidianWalnutItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int tooltipColorId) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, tooltipColorId);
    }

    public BakedObsidianWalnutItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int tooltipColorId, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, tooltipColorId, isFoil);
    }

    /*=============================实现延时转化功能==================================*/
    public static final String TAG_PERSISTENT = "Persistent";
    //两个NBT标签，用于客户端计数（服务端高频修改nbt会导致物品动画异常）
    public static final String TAG_USE_TIME = "UseTime";
    public static final String TAG_PREV_USE_TIME = "PrevUseTime";
    //服务端通过map记录倒计时
    public static final Map<UUID, Integer> itemLifeTick = new HashMap<>();
    public static final String TAG_STACK_ID = "StackID";


    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int i, boolean held) {
        //实现创造限定的稳定性核桃
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(TAG_PERSISTENT)) return;

        if (entity instanceof LivingEntity living) {
            int useTime = getUseTime(stack);
            //客户端记录：每tick增加tick计数，同时保存上一tick的计数
            if (level.isClientSide()) {
                if (tag.getInt(TAG_PREV_USE_TIME) != tag.getInt(TAG_USE_TIME)) {
                    tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
                }

                int maxLoadTime = getMaxLoadTime();
                if (useTime < maxLoadTime) {
                    int set = useTime + 1;
                    setUseTime(stack, set);

                }
            } else {
                //服务端记录：通过map与分配UUID为每个itemStack进行倒计时
                if (tag.hasUUID(TAG_STACK_ID)) {
                    UUID uuid = tag.getUUID(TAG_STACK_ID);
                    int time = itemLifeTick.getOrDefault(uuid,0);
                    itemLifeTick.put(uuid,time + 1);

                    //时间到，转化为燃
                    if (time >= getMaxLoadTime()) {
                        int count = stack.getCount();
                        stack.shrink(count);
                        //生成新物品
                        ItemEntity output = new ItemEntity(
                                entity.level(),
                                entity.getX(),
                                entity.getY() + 0.5,
                                entity.getZ(),
                                new ItemStack(ImmortalersDelightItems.BLAZING_OBSIDIAN_WALNUT.get(),count));
                        output.setDeltaMovement(0,0,0);
                        entity.level().addFreshEntity(output);
                    }
                } else {
                    tag.putUUID(TAG_STACK_ID,UUID.randomUUID());
                }

            }

        }

    }

    private static int getMaxLoadTime() {
        return 600;
    }

    public static int getUseTime(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt(TAG_USE_TIME) : 0;
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
        tag.putInt(TAG_USE_TIME, useTime);
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(TAG_PERSISTENT)) {
            MutableComponent textEmpty = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ "." + this + ".persistent");
            ChatFormatting color = ChatFormatting.GREEN;
            tooltip.add(textEmpty.withStyle(color));
        }
        super.appendHoverText(stack, level, tooltip, isAdvanced);
        if (tag.contains(TAG_USE_TIME,Tag.TAG_INT)) {
            int progress = getMaxLoadTime() - tag.getInt(TAG_USE_TIME);

            if (progress > 0) {
                MutableComponent textValue = Component.translatable(
                        "tooltip." +ImmortalersDelightMod.MODID+ "." + this + ".progress", // 翻译键
                        (progress / 20) // 替换%d占位符
                );
                ChatFormatting color = progress > tag.getInt(TAG_USE_TIME) ? ChatFormatting.GRAY : ChatFormatting.DARK_RED;
                tooltip.add(textValue.withStyle(color));
            }
        }
    }
}

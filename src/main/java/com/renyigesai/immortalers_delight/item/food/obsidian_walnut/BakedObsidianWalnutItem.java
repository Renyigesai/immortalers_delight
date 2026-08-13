package com.renyigesai.immortalers_delight.item.food.obsidian_walnut;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.EnchantAbleFoodItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public static final String TAG_PERSISTENT = "Persistent";
    public static final String TAG_USE_TIME = "UseTime";
    public static final String TAG_PREV_USE_TIME = "PrevUseTime";
    public static final Map<UUID, Integer> itemLifeTick = new HashMap<>();
    public static final String TAG_STACK_ID = "StackID";

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int i, boolean held) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(TAG_PERSISTENT)) return;

        if (entity instanceof LivingEntity) {
            int useTime = getUseTime(stack);
            if (level.isClientSide()) {
                if (tag.getInt(TAG_PREV_USE_TIME) != tag.getInt(TAG_USE_TIME)) {
                    tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }

                int maxLoadTime = getMaxLoadTime();
                if (useTime < maxLoadTime) {
                    setUseTime(stack, useTime + 1);
                }
            } else {
                if (tag.hasUUID(TAG_STACK_ID)) {
                    UUID uuid = tag.getUUID(TAG_STACK_ID);
                    int time = itemLifeTick.getOrDefault(uuid, 0);
                    itemLifeTick.put(uuid, time + 1);

                    if (time >= getMaxLoadTime()) {
                        int count = stack.getCount();
                        stack.shrink(count);
                        ItemEntity output = new ItemEntity(
                                entity.level(),
                                entity.getX(),
                                entity.getY() + 0.5,
                                entity.getZ(),
                                new ItemStack(ImmortalersDelightItems.BLAZING_OBSIDIAN_WALNUT.get(), count));
                        output.setDeltaMovement(0, 0, 0);
                        entity.level().addFreshEntity(output);
                    }
                } else {
                    tag.putUUID(TAG_STACK_ID, UUID.randomUUID());
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
        }
    }

    private static int getMaxLoadTime() {
        return 600;
    }

    public static int getUseTime(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(TAG_USE_TIME);
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
        tag.putInt(TAG_USE_TIME, useTime);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private String tooltipPath() {
        return BuiltInRegistries.ITEM.getKey(this).getPath();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(TAG_PERSISTENT)) {
            MutableComponent textEmpty = Component.translatable("tooltip." + ImmortalersDelightMod.MODID + "." + tooltipPath() + ".persistent");
            tooltip.add(textEmpty.withStyle(ChatFormatting.GREEN));
        }
        super.appendHoverText(stack, context, tooltip, isAdvanced);
        if (tag.contains(TAG_USE_TIME, Tag.TAG_INT)) {
            int progress = getMaxLoadTime() - tag.getInt(TAG_USE_TIME);
            if (progress > 0) {
                MutableComponent textValue = Component.translatable(
                        "tooltip." + ImmortalersDelightMod.MODID + "." + tooltipPath() + ".progress",
                        (progress / 20)
                );
                ChatFormatting color = progress > tag.getInt(TAG_USE_TIME) ? ChatFormatting.GRAY : ChatFormatting.DARK_RED;
                tooltip.add(textValue.withStyle(color));
            }
        }
    }
}

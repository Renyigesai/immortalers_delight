package com.renyigesai.immortalers_delight.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**目前用来填充文字描述Tooltip*/
public class TooltipItemNameBlockItem extends ItemNameBlockItem {
    public final Component tooltip;
    public List<Component> tooltips = new ArrayList<>();

    /**这个构造器传入一个Component，大部分情况可以用这个*/
    public TooltipItemNameBlockItem(Block pBlock, Properties pProperties, Component tooltip) {
        super(pBlock, pProperties);
        this.tooltip = tooltip;
    }

    /**这个构造器传入多个Component，用于显示多条Tooltip*/
    public TooltipItemNameBlockItem(Block pBlock, Properties pProperties,Component... tooltips) {
        super(pBlock, pProperties);
        this.tooltip = Component.empty();
        this.tooltips.addAll(List.of(tooltips));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if (!tooltips.isEmpty()){
            /*多条时需按住Shift查看*/
            if (Screen.hasShiftDown()){
                pTooltip.addAll(tooltips);
            }else {
                pTooltip.add(Component.translatable("tooltip.immortalers_delight.tooltip_item_name_block_item").withStyle(ChatFormatting.GRAY));
            }
        }else {
            pTooltip.add(tooltip);
        }
    }
}

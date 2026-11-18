package com.renyigesai.immortalers_delight.item.antique;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BagItemHandler extends ItemStackHandler {

    private final ItemStack bag;
    private final String key;

    public BagItemHandler(ItemStack bag, String key, int size) {
        super(size);
        this.bag = bag;
        this.key = key;

        // 从物品 NBT 加载初始数据
        CompoundTag tag = bag.getOrCreateTag();
        if (tag.contains(key, Tag.TAG_COMPOUND)) {
            deserializeNBT(tag.getCompound(key));
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        CompoundTag tag = bag.getOrCreateTag();
        tag.put(key, serializeNBT());
    }
}

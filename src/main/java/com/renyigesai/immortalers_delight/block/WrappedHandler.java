package com.renyigesai.immortalers_delight.block;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WrappedHandler implements IItemHandlerModifiable {
    private final IItemHandlerModifiable handler;
    private final Predicate<Integer> extract;
    private final BiPredicate<Integer, ItemStack> insert;
    /**
     * 物品处理器包装类，通过条件断言实现插入/提取操作的过滤控制
     *
     * @param handler 被包装的底层物品处理器
     * @param extract 提取条件断言，参数为槽位索引，返回是否允许提取
     * @param insert  插入条件断言，参数为槽位索引和物品堆叠，返回是否允许插入
     */
    public WrappedHandler(IItemHandlerModifiable handler, Predicate<Integer> extract,
                          BiPredicate<Integer, ItemStack> insert) {
        this.handler = handler;
        this.extract = extract;
        this.insert = insert;
    }
    /**
     * 直接设置指定槽位的物品堆叠（绕过过滤条件）
     *
     * @param slot  要设置的槽位索引
     * @param stack 要设置的物品堆叠
     */
    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.handler.setStackInSlot(slot, stack);
    }
    /**
     * 获取总槽位数
     *
     * @return 底层处理器的槽位总数
     */
    @Override
    public int getSlots() {
        return this.handler.getSlots();
    }
    /**
     * 获取指定槽位的物品堆叠
     *
     * @param slot 要查询的槽位索引
     * @return 槽位中的物品堆叠副本
     */
    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.handler.getStackInSlot(slot);
    }
    /**
     * 尝试插入物品到指定槽位
     *
     * @param slot     目标槽位索引
     * @param stack    要插入的物品堆叠
     * @param simulate 是否模拟操作（true时不实际修改物品）
     * @return 未能插入的剩余物品堆叠（原样返回或调用底层处理器处理）
     */
    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return this.insert.test(slot, stack) ? this.handler.insertItem(slot, stack, simulate) : stack;
    }
    /**
     * 尝试从指定槽位提取物品
     *
     * @param slot     目标槽位索引
     * @param amount   请求提取的数量
     * @param simulate 是否模拟操作（true时不实际修改物品）
     * @return 实际提取的物品堆叠（空堆叠或调用底层处理器处理）
     */
    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.extract.test(slot) ? this.handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }
    /**
     * 获取指定槽位的最大容量
     *
     * @param slot 要查询的槽位索引
     * @return 槽位的最大物品容量
     */
    @Override
    public int getSlotLimit(int slot) {
        return this.handler.getSlotLimit(slot);
    }

    /**
     * 检查物品是否可放入指定槽位
     *
     * @param slot  目标槽位索引
     * @param stack 要检查的物品堆叠
     * @return 当同时满足插入条件和底层验证时返回true
     */
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.insert.test(slot, stack) && this.handler.isItemValid(slot, stack);
    }
}
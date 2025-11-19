package com.renyigesai.immortalers_delight.block.support;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public class SupportBlockEntity extends BlockEntity {
    protected int delay = 0;
    private static final int MAX_TIME = 5 * 20;
    private int timer = 0;

    public SupportBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ImmortalersDelightBlocks.SUPPORT_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    //增加计数，这个方法是给方块调用的
    public int increase(){
        delay ++;
        setChanged();
        return delay;
    }

    //读取NBT，用于加载
    @Override
    public void load(CompoundTag pTag) {
        delay = pTag.getInt("delay");
        super.load(pTag);
    }

    //写入NBT，用于存档
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("delay",delay);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, SupportBlockEntity pBlockEntity) {
        if(pLevel!=null && !pLevel.isClientSide){
            if(pBlockEntity.timer == SupportBlockEntity.MAX_TIME){
                Player nearestPlayer = pLevel.getNearestPlayer(pPos.getX(), pPos.getY(), pPos.getZ(), 10, false);
                Component component = Component.literal("hello");
                if(nearestPlayer!=null){
                    nearestPlayer.sendSystemMessage(component);
                }
                pBlockEntity.timer = 0;
            }
            pBlockEntity.timer++;
        }
    }
}

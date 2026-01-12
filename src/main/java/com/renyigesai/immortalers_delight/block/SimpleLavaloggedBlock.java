package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

public interface SimpleLavaloggedBlock extends BucketPickup, LiquidBlockContainer {
    public static final BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");
    default boolean canPlaceLiquid(BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
        return pFluid == Fluids.LAVA;
    }

    default boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!(Boolean)pState.getValue(LAVALOGGED) && pFluidState.getType() == Fluids.LAVA) {
            if (!pLevel.isClientSide()) {
                pLevel.setBlock(pPos, (BlockState)pState.setValue(SimpleLavaloggedBlock.LAVALOGGED, true), 3);
                pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
            }

            return true;
        } else {
            return false;
        }
    }

    default ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if ((Boolean)pState.getValue(LAVALOGGED)) {
            pLevel.setBlock(pPos, (BlockState)pState.setValue(LAVALOGGED, false), 3);
            if (!pState.canSurvive(pLevel, pPos)) {
                pLevel.destroyBlock(pPos, true);
            }

            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    default Optional<SoundEvent> getPickupSound() {
        return Fluids.LAVA.getPickupSound();
    }
}

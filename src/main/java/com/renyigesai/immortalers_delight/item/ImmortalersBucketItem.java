package com.renyigesai.immortalers_delight.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class ImmortalersBucketItem extends BucketItem {
    public ImmortalersBucketItem(Fluid pContent, Properties pProperties) {
        super(pContent, pProperties);
    }

    public ImmortalersBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        /**
         * Dispense the specified stack, play the dispense sound, and spawn particles.
         */
        public ItemStack execute(BlockSource p_123561_, ItemStack p_123562_) {
            DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem)p_123562_.getItem();
            BlockPos blockpos = p_123561_.getPos().relative(p_123561_.getBlockState().getValue(DispenserBlock.FACING));
            Level level = p_123561_.getLevel();
            if (dispensiblecontaineritem.emptyContents((Player)null, level, blockpos, (BlockHitResult)null, p_123562_)) {
                dispensiblecontaineritem.checkExtraContent((Player)null, level, p_123562_, blockpos);
                return new ItemStack(Items.BUCKET);
            } else {
                return this.defaultDispenseItemBehavior.dispense(p_123561_, p_123562_);
            }
        }
    };
}

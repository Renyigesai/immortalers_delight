package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ClimbingRopeItem extends Item {

    public ClimbingRopeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        if (!(entity instanceof Player player)) {
            return;
        }

        if (player.getMainHandItem() == stack || (player.getItemInHand(InteractionHand.OFF_HAND) == stack && player.getMainHandItem().isEmpty())) {
            if (player.horizontalCollision) {
                BlockPos pos = player.blockPosition().relative(player.getDirection(), 1);
                if (level.getBlockState(pos).is(BlockTags.LOGS)
                        || level.getBlockState(pos).is(BlockTags.BAMBOO_BLOCKS)
                        || level.getBlockState(pos).is(Blocks.BAMBOO)
                        || level.getBlockState(pos).is(ImmortalersDelightBlocks.TRAVASTRUGGLER_LOG.get())) {
                    Vec3 initialVec = player.getDeltaMovement();
                    Vec3 climbVec = new Vec3(initialVec.x, 0.2D, initialVec.z);
                    player.setDeltaMovement(climbVec.scale(0.96D));
                }
            }
        }
    }
}

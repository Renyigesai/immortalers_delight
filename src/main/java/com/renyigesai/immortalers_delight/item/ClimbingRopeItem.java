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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClimbingRopeItem extends Item {

    private static final Map<UUID,Integer> playerCanClimb = new ConcurrentHashMap<>();
    public ClimbingRopeItem(Properties pProperties) {
        super(pProperties);
    }

    /**
     * 物品栏每刻回调（核心重写方法）
     * @param stack 物品堆叠
     * @param level 世界
     * @param entity 持有实体
     * @param slot 槽位ID
     * @param isSelected 是否为手持（快捷栏选中）
     */
    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        if (!(entity instanceof Player player)) {
            return;
        }

        if (player.getMainHandItem() == stack || (player.getItemInHand(InteractionHand.OFF_HAND) == stack && player.getMainHandItem().isEmpty())) {

            if (player.horizontalCollision) {
                BlockPos pos = player.blockPosition().relative(player.getDirection(),1);
//            if (player.tickCount % 5 == 0) {
                if (level.getBlockState(pos).is(BlockTags.LOGS)
                        || level.getBlockState(pos).is(BlockTags.BAMBOO_BLOCKS)
                        || level.getBlockState(pos).is(Blocks.BAMBOO)
                        ||level.getBlockState(pos).is(ImmortalersDelightBlocks.TRAVASTRUGGLER_LOG.get())) {
//                    playerCanClimb.put(player.getUUID())
                    Vec3 initialVec = player.getDeltaMovement();
                    Vec3 climbVec = new Vec3(initialVec.x, 0.2D, initialVec.z);
                    player.setDeltaMovement(climbVec.scale(0.96D));
                }
//                level.getEntity()
//            }
                return;
            }
        }

    }
}
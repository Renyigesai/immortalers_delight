package com.renyigesai.immortalers_delight.potion;
//
//import com.renyigesai.immortalers_delight.block.CulturalLegacyEffectToolBlock;
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber
//public class CulturalLegacyPotionEffect {
//    @SubscribeEvent
//    public static void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
//        if (event == null || event.isCanceled()) {
//            return;
//        }
//
//        // 获取世界和附魔台的位置
//        Level level = event.getLevel();
//        BlockPos pos = event.getPos();
//
//        // 以附魔台为中心，检查 5*5 范围内的方块
//        int goldBlockCount = maxAgeToolBlocksInRange(level, pos, 2); // 2 表示从中心到边缘的距离
//
//        // 根据金块数量计算新的附魔等级
//        int newLevel = calculateNewLevel(event.getEnchantLevel(), goldBlockCount);
//
//        // 设置新的附魔等级
//        event.setEnchantLevel(newLevel);
//    }
//
//    /**
//     * 计算指定范围内的指定方块数量
//     * @param level 世界
//     * @param centerPos 中心位置
//     * @param range 范围（从中心到边缘的距离）
//     * @return 金块数量
//     */
//    private static int maxAgeToolBlocksInRange(Level level, BlockPos centerPos, int range) {
//        int maxAge = 0;
//        for (int x = -range; x <= range; x++) {
//            for (int y = -range; y <= range; y++) {
//                for (int z = -range; z <= range; z++) {
//                    BlockPos checkPos = centerPos.offset(x, y, z);
//                    if (level.getBlockState(checkPos).getBlock() instanceof CulturalLegacyEffectToolBlock block) {
//
//                        maxAge++;
//                    }
//                }
//            }
//        }
//        return maxAge;
//    }
//
//    /**
//     * 根据金块数量计算新的附魔等级
//     * @param originalLevel 原始附魔等级
//     * @param goldBlockCount 金块数量
//     * @return 新的附魔等级
//     */
//    private static int calculateNewLevel(int originalLevel, int goldBlockCount) {
//        // 简单示例：每有一个金块，附魔等级降低 1 级，但不低于 0
//        int newLevel = originalLevel - goldBlockCount;
//        return Math.max(0, newLevel);
//    }
//}

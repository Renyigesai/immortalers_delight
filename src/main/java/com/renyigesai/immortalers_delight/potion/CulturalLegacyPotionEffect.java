package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.block.CulturalLegacyEffectToolBlock;
import com.renyigesai.immortalers_delight.block.SpikeTrapBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightFoodProperties;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CulturalLegacyPotionEffect {

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            ItemStack stack = event.getItem();
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide()) {
                if (stack.getItem().isEdible()) {
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.PUFFERFISH_ROLL) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1));
                    }
                    if (
                            stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.BOWL_OF_STEWED_ROTTEN_MEAT_IN_CLAY_POT) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1));
                    }
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.SCARLET_DEVILS_CAKE_SLICE) {
                        livingEntity.heal(2);
                    }
                    if (stack.getFoodProperties(livingEntity) == ImmortalersDelightFoodProperties.RED_STUFFED_BUN) {
                        if (livingEntity.getRandom().nextInt(3) == 0) {
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3000));
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.LUCK, 600));
                        }
                    }
                }
            }
        }
    }
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

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        if (event == null || event.getEntity() == null || event.getEntity().level().isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        BlockState blockState = level.getBlockState(entity.getOnPos());
        BlockState blockState1 = level.getBlockState(entity.blockPosition());

        if (blockState.getBlock() == ImmortalersDelightBlocks.SPIKE_BAR.get()) {
            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
                entity.hurt(entity.damageSources().cactus(), 2.0f);
            }
        }
        if (blockState.getBlock() == ImmortalersDelightBlocks.LONG_SPIKE_TRAP.get() && blockState.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 4.0f) {
                entity.hurt(entity.damageSources().cactus(), 4.0f);
            }
        }
        if (blockState.getBlock() == ImmortalersDelightBlocks.POISONOUS_LONG_SPIKE_TRAP.get() && blockState.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
                entity.hurt(entity.damageSources().cactus(), 2.0f);
                if(!(entity.hasEffect(ImmortalersDelightMobEffect.WEAK_POISON.get()) || entity.hasEffect(MobEffects.POISON))) entity.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.WEAK_POISON.get(), 100, 4));
            }
        }
        if (blockState1.getBlock() == ImmortalersDelightBlocks.POISONOUS_LONG_SPIKE_TRAP.get() && blockState1.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
                if(!(entity.hasEffect(ImmortalersDelightMobEffect.WEAK_POISON.get()) || entity.hasEffect(MobEffects.POISON))) entity.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.WEAK_POISON.get(), 100, 4));
            }
        }
        if (blockState1.getBlock() == ImmortalersDelightBlocks.POISONOUS_METAL_CALTROP.get()) {
            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,30, 3));
            }
        }
        if (blockState1.getBlock() == ImmortalersDelightBlocks.POISONOUS_SPIKE_TRAP.get() && blockState1.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
                if(!(entity.hasEffect(ImmortalersDelightMobEffect.WEAK_POISON.get()) || entity.hasEffect(MobEffects.POISON))) entity.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1));
            }
        }
    }
}

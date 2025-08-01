package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//@Mod.EventBusSubscriber
public class TrapBlocksEventHelper {
//    @SubscribeEvent
//    public static void onTick(LivingEvent.LivingTickEvent event) {
//        if (event == null || event.getEntity() == null || event.getEntity().level().isClientSide()) {
//            return;
//        }
//        LivingEntity entity = event.getEntity();
//        Level level = entity.level();
//        BlockState blockState = level.getBlockState(entity.getOnPos());
//        BlockState blockState1 = level.getBlockState(entity.blockPosition());

//        if (blockState.getBlock() == ImmortalersDelightBlocks.SPIKE_BAR.get()) {
//            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 3.0f) {
//                entity.hurt(entity.damageSources().cactus(), 3.0f);
//            }
//        }
//        if (blockState.getBlock() == ImmortalersDelightBlocks.LONG_SPIKE_TRAP.get() && blockState.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
//            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 4.0f) {
//                entity.hurt(entity.damageSources().cactus(), 4.0f);
//            }
//        }
//        if (blockState.getBlock() == ImmortalersDelightBlocks.POISONOUS_LONG_SPIKE_TRAP.get() && blockState.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
//            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
//                entity.hurt(entity.damageSources().cactus(), 2.0f);
//                if(!(entity.hasEffect(ImmortalersDelightMobEffect.WEAK_POISON.get()) || entity.hasEffect(MobEffects.POISON))) entity.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.WEAK_POISON.get(), 100, 4));
//            }
//        }
//        if (blockState1.getBlock() == ImmortalersDelightBlocks.POISONOUS_LONG_SPIKE_TRAP.get() && blockState1.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
//            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
//                if(!(entity.hasEffect(ImmortalersDelightMobEffect.WEAK_POISON.get()) || entity.hasEffect(MobEffects.POISON))) entity.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.WEAK_POISON.get(), 100, 4));
//            }
//        }
//        if (blockState1.getBlock() == ImmortalersDelightBlocks.POISONOUS_METAL_CALTROP.get()) {
//            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
//                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,30, 3));
//            }
//        }
//        if (blockState1.getBlock() == ImmortalersDelightBlocks.POISONOUS_SPIKE_TRAP.get() && blockState1.getValue(BlockStateProperties.TILT) == Tilt.FULL) {
//            if (entity instanceof Player ? !((Player) entity).isCreative() : entity.getHealth() > 2.0f) {
//                if(!(entity.hasEffect(ImmortalersDelightMobEffect.WEAK_POISON.get()) || entity.hasEffect(MobEffects.POISON))) entity.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1));
//            }
//        }
//    }
}

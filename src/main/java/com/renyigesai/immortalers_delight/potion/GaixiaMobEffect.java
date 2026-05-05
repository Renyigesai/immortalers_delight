package com.renyigesai.immortalers_delight.potion;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

public class GaixiaMobEffect extends BaseMobEffect {
    public GaixiaMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @EventBusSubscriber
    public static class GaixiaPotion{
        /*生成黏液方块*/
        @SubscribeEvent
        public static void onAttackEntity(LivingDamageEvent.Pre event){
            LivingEntity target = event.getEntity();
            Entity entity = event.getSource().getEntity();
            if (entity == null){
                return;
            }

            Level level = entity.level();
            if (entity instanceof LivingEntity living && living.hasEffect(ImmortalersDelightMobEffect.GAIXIA)){
                target.setDeltaMovement(new Vec3(0.0D, 0.05D, 0.0D));
                if (level.getBlockState(BlockPos.containing(target.getX(),target.getY(),target.getZ())).isAir()) {
                    level.setBlock(BlockPos.containing(target.getX(), target.getY(), target.getZ()), ImmortalersDelightBlocks.GAIXIA_SILME.get().defaultBlockState(), 3);
                }
            }
        }
        /*取消攻击击退*/
        @SubscribeEvent
        public static void onKnockBack(LivingKnockBackEvent event){
            LivingEntity lastAttacker = event.getEntity().getLastAttacker();
            if (lastAttacker != null && lastAttacker.hasEffect(ImmortalersDelightMobEffect.GAIXIA)){
                event.setCanceled(true);
            }
        }
    }
}

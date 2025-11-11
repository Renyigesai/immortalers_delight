package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class GaixiaMobEffect extends MobEffect {
    public GaixiaMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }

    @Mod.EventBusSubscriber
    public static class GaixiaPotion{
        @SubscribeEvent
        public static void onAttackEntity(AttackEntityEvent event){
            Entity target = event.getTarget();
            Player entity = event.getEntity();
            Level level = entity.level();
            if (entity.hasEffect(ImmortalersDelightMobEffect.GAIXIA.get())){
                target.setDeltaMovement(0,0,0);
                /*‘› ±œ»”√÷©÷ÎÕ¯*/
                level.setBlock(BlockPos.containing(target.getX(),target.getY(),target.getZ()), Blocks.COBWEB.defaultBlockState(),3);
            }
        }
    }
}

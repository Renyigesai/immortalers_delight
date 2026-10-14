package com.renyigesai.immortalers_delight.item;

import com.mojang.logging.LogUtils;
import com.renyigesai.immortalers_delight.api.ISnifferSaddleData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class SnifferSaddleItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public SnifferSaddleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, net.minecraft.world.entity.LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Sniffer sniffer) {
            Level level = entity.level();
            
            // 检查嗅探兽是否已经装备自定义鞍座
            if (!hasSaddle(sniffer)) {
                if (!level.isClientSide) {
                    // 装备自定义鞍座 - 使用 EntityData 同步
                    sniffer.getEntityData().set(((ISnifferSaddleData)sniffer).immortalersDelight$getHasSaddleAccessor(), true);
                    
                    LOGGER.info("Sniffer saddle equipped! Entity ID: {}", sniffer.getId());
                    
                    // 播放音效
                    level.playSound(null, sniffer.getX(), sniffer.getY(), sniffer.getZ(), 
                        SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 0.5F, 1.0F);
                    
                    // 消耗物品（创造模式除外）
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return super.interactLivingEntity(stack, player, entity, hand);
    }
    
    public static boolean hasSaddle(Sniffer sniffer) {
        return sniffer.getEntityData().get(((ISnifferSaddleData)sniffer).immortalersDelight$getHasSaddleAccessor());
    }
}

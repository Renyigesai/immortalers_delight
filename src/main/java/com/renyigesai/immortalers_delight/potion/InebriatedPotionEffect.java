package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class InebriatedPotionEffect {

//    @SubscribeEvent
//    public static void onAddToEntity(MobEffectEvent.Added event) {
//        if (event != null && event.getEntity() != null) {
//            LivingEntity entity = event.getEntity();
//            if (!entity.getCommandSenderWorld().isClientSide
//                    && event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get()
//                    && !entity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
//                if (entity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get())) {
//
//                }
//            }
//        }
//    }
    @SubscribeEvent
    public static void onRemoveFromEntity(MobEffectEvent.Remove event) {
        if (event != null && event.getEntity() != null) {
            LivingEntity entity = event.getEntity();

            if (entity instanceof Player player && player.isCreative()) return;
            if (!entity.getCommandSenderWorld().isClientSide
                    && entity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get())
                    && !entity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
                if (event.getEffectInstance() != null
                        && (event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get()
                        || event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.WEAK_POISON.get()
                        || event.getEffectInstance().getEffect() == MobEffects.POISON
                        || event.getEffectInstance().getEffect() == MobEffects.BLINDNESS
                        || event.getEffectInstance().getEffect() == MobEffects.CONFUSION
                        || event.getEffectInstance().getEffect() == MobEffects.MOVEMENT_SLOWDOWN
                        || event.getEffectInstance().getEffect() == MobEffects.WEAKNESS)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}

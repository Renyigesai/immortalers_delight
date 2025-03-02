package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class WarmCurrentSurgesPotionEffect {
    @SubscribeEvent
    public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
        if (event != null && event.getEntity() != null) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity
                    && livingEntity.hasEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get())
                    && event.getEffectInstance().getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
                //int time = Objects.requireNonNull(livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get())).getDuration();
                int lv = Objects.requireNonNull(livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get())).getAmplifier();
                //int timeEvt = event.getEffectInstance().getDuration();
                int lvEvt = event.getEffectInstance().getAmplifier();
                if (lv >= lvEvt){
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}

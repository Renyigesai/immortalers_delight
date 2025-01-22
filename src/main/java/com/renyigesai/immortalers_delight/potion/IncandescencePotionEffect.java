package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class IncandescencePotionEffect {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            ItemStack stack = event.getItem();
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (stack.getItem().isEdible() && livingEntity.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get())) {
                    int lv = livingEntity.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get()).getAmplifier() : 0;
                    int time = livingEntity.hasEffect(MobEffects.DAMAGE_BOOST) ? livingEntity.getEffect(MobEffects.DAMAGE_BOOST).getDuration() : 0;
//                    livingEntity.removeEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get());
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, time + 100, lv));
                }
            }
        }
    }
}

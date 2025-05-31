package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.GoldenFabricArmor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GasPoisonPotionEffect {

    @SubscribeEvent
    public static void onRemoveFromEntity(MobEffectEvent.Remove event) {
        if (event != null && event.getEntity() != null) {
            LivingEntity entity = event.getEntity();

            if (!entity.getCommandSenderWorld().isClientSide
                    && entity.hasEffect(ImmortalersDelightMobEffect.GAS_POISON.get())
                    && !entity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())
                    && !(entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GoldenFabricArmor)) {
                if (event.getEffectInstance() != null
                    && (event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.GAS_POISON.get()
                        || event.getEffectInstance().getEffect() == MobEffects.HUNGER
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

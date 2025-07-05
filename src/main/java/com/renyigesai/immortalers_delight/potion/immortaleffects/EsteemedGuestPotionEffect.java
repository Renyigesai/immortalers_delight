package com.renyigesai.immortalers_delight.potion.immortaleffects;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EsteemedGuestPotionEffect {
    @SubscribeEvent
    public static void PiglinIgnore(LivingEvent.LivingTickEvent event) {

        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof AbstractPiglin piglin) {
            if (piglin.getTarget() != null) {
                if (piglin.getTarget().hasEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get())) {
                    piglin.setTarget(null);
                    piglin.setAggressive(false);
                }
            }
        }
    }
}

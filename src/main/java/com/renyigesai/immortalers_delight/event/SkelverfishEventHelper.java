package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SkelverfishEventHelper {

    @SubscribeEvent
    public static void onExplosionDamage(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_EXPLOSION) && event.getSource().getEntity() instanceof SkelverfishBomber bomber) {
            float f = event.getEntity().getRandom().nextFloat();
            float extraDamage = (bomber.getMaxFireDamage() +
                    (float)(bomber.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0.0F : bomber.getAttribute(Attributes.ATTACK_DAMAGE).getValue())) *
                    (bomber.isPowered() ? 4.0F : 2.0F) +
                    (bomber.level().getDifficulty() == Difficulty.EASY ? 2.0F + f * 1.5F : 0.0F) +
                    (bomber.level().getDifficulty() == Difficulty.NORMAL ? 5.0F + f * 4.0F : 0.0F) +
                    (bomber.level().getDifficulty() == Difficulty.HARD ? 6.0F + f * 3.0F : 0.0F);
            event.setAmount(event.getAmount() + extraDamage); // 提升爆炸伤害，造成攻击力200%或400%爆炸伤害
        }
    }
}

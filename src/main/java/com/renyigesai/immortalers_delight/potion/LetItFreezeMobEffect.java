package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.FreezeEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class LetItFreezeMobEffect extends BaseMobEffect {
    public LetItFreezeMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 5337801);
    }

    @Override
    public boolean isDurationEffectTickInControl(int pDuration, int pAmplifier) { return true;}
    //等级控制在特殊状态里面实现
    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class LetItFreezeEvents {
        @SubscribeEvent
        public static void freezeAttack(LivingAttackEvent event) {
            if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity attacker) {
                if (attacker.hasEffect(ImmortalersDelightMobEffect.LET_IT_FREEZE.get())) {
                    LivingEntity hurtOne = event.getEntity();
                    int amplifier = attacker.getEffect(ImmortalersDelightMobEffect.LET_IT_FREEZE.get()).getAmplifier();
                    FreezeEffect.applyImmortalEffect(hurtOne, 100 + amplifier * 20, amplifier);
                }
            }
        }
    }
}

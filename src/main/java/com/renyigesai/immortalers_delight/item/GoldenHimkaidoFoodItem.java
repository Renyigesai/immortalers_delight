package com.renyigesai.immortalers_delight.item;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class GoldenHimkaidoFoodItem extends EnchantAbleFoodItem{
    private static final int EAT_DURATION = 20;
    private final int reverseDuration;

    private final int reverseAmplifier;

    public MobEffect FD_Nourished = vectorwing.farmersdelight.common.registry.ModEffects.NOURISHMENT.get();
    public MobEffect FD_Comfit = vectorwing.farmersdelight.common.registry.ModEffects.COMFORT.get();

    public Map<MobEffect,MobEffect> reverseEffect = (new ImmutableMap.Builder<MobEffect,MobEffect>())
            .put(MobEffects.BAD_OMEN,MobEffects.HERO_OF_THE_VILLAGE)
            .put(MobEffects.UNLUCK,MobEffects.LUCK)
            .put(MobEffects.GLOWING,MobEffects.INVISIBILITY)
            .put(MobEffects.MOVEMENT_SLOWDOWN,MobEffects.MOVEMENT_SPEED)
            .put(MobEffects.LEVITATION,MobEffects.SLOW_FALLING)
            .put(MobEffects.BLINDNESS,MobEffects.NIGHT_VISION)
            .put(MobEffects.DARKNESS,MobEffects.CONDUIT_POWER)
            .put(MobEffects.DIG_SLOWDOWN,MobEffects.DIG_SPEED)
            .put(MobEffects.WEAKNESS,MobEffects.DAMAGE_BOOST)
            .put(MobEffects.POISON,FD_Comfit)
            .put(MobEffects.WITHER,MobEffects.REGENERATION)
            .put(MobEffects.HUNGER,FD_Nourished)
            .put(MobEffects.HARM,MobEffects.HEAL)
            .build();
    public GoldenHimkaidoFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomToolTip, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomToolTip, isFoil);
        reverseDuration = 0;
        reverseAmplifier = 0;
    }
    public GoldenHimkaidoFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean isFoil, int reverseLevel, int reverseTime) {
        super(properties, hasFoodEffectTooltip, true, isFoil);
        reverseDuration = reverseTime;
        reverseAmplifier = reverseLevel;
    }

    public int getReverseDuration() {return this.reverseDuration;}

    public int getReverseAmplifier() {return this.reverseAmplifier;}
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        ItemStack outStack = pStack.copy();
        if (!pLevel.isClientSide) {
            if (outStack.getItem() instanceof GoldenHimkaidoFoodItem thisItem) {
                if (thisItem.getReverseDuration() > 0 && thisItem.getReverseAmplifier() > 0) {
                    reverseEffect(pLivingEntity, thisItem.getReverseAmplifier());
                    new ScheduledExecuteTask(1, 1) {
                        private int tick = 0;

                        @Override
                        public void run() {
                            if (++tick > thisItem.reverseDuration || !pLivingEntity.isAlive()) {
                                if (pLivingEntity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
                                    pLivingEntity.removeEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get());
                                }
                                this.cancel();
                                return;
                            }
                            if (pLivingEntity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
                                reverseEffect(pLivingEntity, thisItem.getReverseAmplifier());
                            } else if (tick > 1){
                                this.cancel();
                            }
                        }

                    }.start();
                }
            }
        }
//        if (pLivingEntity instanceof Player && !((Player)pLivingEntity).getAbilities().instabuild) {
//            outStack.shrink(1);
//        }
        return super.finishUsingItem(outStack,pLevel,pLivingEntity);
    }

    public int getUseDuration(ItemStack p_42933_) {
        return EAT_DURATION;
    }

    public UseAnim getUseAnimation(ItemStack p_42931_) {
        return UseAnim.EAT;
    }

    public InteractionResultHolder<ItemStack> use(Level p_42927_, Player p_42928_, InteractionHand p_42929_) {
        return ItemUtils.startUsingInstantly(p_42927_, p_42928_, p_42929_);
    }

    public void reverseEffect(LivingEntity pEntity, int amplifier) {
            /*
            对反胃进行特殊处理，其将变为20分之1时间的饱和。
             */
        if (pEntity.hasEffect(MobEffects.CONFUSION)){
            int lv = pEntity.hasEffect(MobEffects.CONFUSION)? Objects.requireNonNull(pEntity.getEffect(MobEffects.CONFUSION)).getAmplifier():0;
            int time = pEntity.hasEffect(MobEffects.CONFUSION)? Objects.requireNonNull(pEntity.getEffect(MobEffects.CONFUSION)).getDuration():0;
            time /= 20;
            MobEffectInstance Saturation = new MobEffectInstance(
                    MobEffects.SATURATION,time,lv);
            pEntity.addEffect(Saturation);
            pEntity.removeEffect(MobEffects.CONFUSION);
        }
            /*
            获取实体的EffectMap进行遍历
             */
        Map<MobEffect, MobEffectInstance> effectsMap = pEntity.getActiveEffectsMap();
        Iterator<MobEffect> iterator = effectsMap.keySet().iterator();
        try {
            while(iterator.hasNext()) {
                MobEffect mobeffect = iterator.next();
                    /*
                    如果不是正面效果，比较是否可以逆转，随后逐条进行删除
                     */
                if (!mobeffect.isBeneficial()) {
                        /*
                         获取当前效果的等级和时间，并对对反转后的效果等级进行限制
                         */
                    MobEffectInstance mobeffectinstance = effectsMap.get(mobeffect);
                    int time = mobeffectinstance.getDuration();
                    int lv = mobeffectinstance.getAmplifier();

                        /*
                        如果逆转用的map收录了当前效果
                         */
                    if (reverseEffect.containsKey(mobeffect)){
                            /*
                            获取逆转后的效果
                             */
                        MobEffect reversedEffect =reverseEffect.get(mobeffect);
                            /*
                            添加逆转后的效果
                             */
                        int tureLv = mobeffectinstance.getAmplifier() > amplifier ? amplifier : mobeffectinstance.getAmplifier();
                        MobEffectInstance buffToAdd = new MobEffectInstance(
                                reversedEffect,time,tureLv);
                        pEntity.addEffect(buffToAdd);
                    }
                        /*
                        无论是否可以逆转，令负面效果的等级降低lv级
                         */
                    pEntity.removeEffect(mobeffect);
                    if (lv > amplifier) {
                        MobEffectInstance lowEffect = new MobEffectInstance(mobeffect,time,lv - amplifier);
                        pEntity.addEffect(lowEffect);
                    }
                }
            }
        } catch (ConcurrentModificationException concurrentmodificationexception) {
        }
    }

}

package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

public class BurnTheBoatsMobEffect extends BaseMobEffect {
    public BurnTheBoatsMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11829585);
    }

    //实体数据，用于记录破釜额外回血
    static HashMap<UUID, Float> entityWithHeal = new HashMap<UUID, Float>();
    //实现生命恢复II的回血效果,并管理属性修饰符
    protected void applyEffectTickInControl(LivingEntity entity, int amplifier) {
        float health = 2 << amplifier;
        this.removeAttributeModifiers(entity, entity.getAttributes(), amplifier);
        //判断当前是否满足破釜条件
        if (isBurnTheBoats(entity,0)) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            //普通模式下与生命恢复互斥
            if (!isPowerful) {
                removeEffects(entity, amplifier);
            }
            //力量急迫速度属性加成
            this.addAttributeModifiers(entity, entity.getAttributes(), amplifier);
            //判断本次回血是否会导致生命值溢出
            if (isBurnTheBoats(entity,health)) {
                //若不溢出，生命回复回血
                entity.heal(health);
            } else {
                //若溢出，生命回复不回血,改为将本次回血量累积到实体数据中，以免buff自身回血导致破釜失效，普通模式只能累计一次
                if (isPowerful) {
                    health += entityWithHeal.getOrDefault(entity.getUUID(), 0.0F);
                }
                entityWithHeal.put(entity.getUUID(), health);
            }
        }
    }
    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        /*不频繁执行，每2.5秒执行一次*/
        return duration % 50 == 0;
    }
    //获取属性修饰符的数值
    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        if (pModifier.getOperation().toValue() == 2 || pModifier.getOperation().toValue() == 0) {
            return pModifier.getAmount() * (double)(pAmplifier + 3);
        }
        return pModifier.getAmount() * (double)(pAmplifier + 1);
    }
    //生效时去除旧版中所派生的效果
    //I级破釜的效果为力量III、速度III、急迫III、跳跃提升III、抗性III、生命恢复II，普通模式为加算等级，否则为乘算等级
    static void removeEffects(LivingEntity entity, int pAmplifier){
        entity.removeEffect(MobEffects.DAMAGE_BOOST);
        entity.removeEffect(MobEffects.MOVEMENT_SPEED);
        entity.removeEffect(MobEffects.DIG_SPEED);
        entity.removeEffect(MobEffects.JUMP);
        entity.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        entity.removeEffect(MobEffects.REGENERATION);
    }
    //判断破釜当前是否生效
    public static boolean isBurnTheBoats(LivingEntity entity,float extraCount) {
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        MobEffectInstance burnTheBoats = entity.getEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS.get());
        if (burnTheBoats != null && burnTheBoats.getEffect() instanceof BaseMobEffect) {
            int lv = burnTheBoats.getAmplifier();
            int truthLv = ((BaseMobEffect) burnTheBoats.getEffect()).getTruthUsingAmplifier(lv);
            lv++;
            truthLv++;
            float useHealth = entity.getHealth() + extraCount;
            float workProgress = entity.getMaxHealth() * truthLv / (2 * (truthLv + 1));
            float workHealth = 3 * (truthLv + 1);
            if (isPowerful) {
                return useHealth <= Math.max(workProgress, workHealth);
            } else {
                return useHealth <= Math.min(workProgress, workHealth);
            }
        }
        return false;
    }
    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class BurnTheBoatsPotionEffect {
        //实现急迫III加挖掘速度的效果
        @SubscribeEvent
        public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            // 判断是否满足破釜效果
            if (!BurnTheBoatsMobEffect.isBurnTheBoats(player,0)) {
                return;
            }

            // 检查玩家是否有破釜效果
            MobEffectInstance thisEffect = player.getEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS.get());
            if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect baseMobEffect) {

                int amplifier = baseMobEffect.getTruthUsingAmplifier(thisEffect.getAmplifier());

                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                //普通模式下与急迫互斥
                if (!isPowerful) {
                    removeEffects(player, amplifier);
                }

                float multiplier = 1.0F + (isPowerful ? 0.2F * (amplifier * 3 + 3) : 0.2F * (amplifier + 3));

                // 设置新速度（原速度 × 倍率）
                event.setNewSpeed(event.getNewSpeed() * multiplier);
            }
        }
        //实现跳跃提升III加跳跃高度的效果
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void addJumpSpeed(LivingEvent.LivingJumpEvent event) {
            LivingEntity entity = event.getEntity();
            // 判断是否满足破釜效果
            if (!BurnTheBoatsMobEffect.isBurnTheBoats(entity,0)) {
                return;
            }
            //判断是否有破釜效果
            MobEffectInstance thisEffect = entity.getEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS.get());
            if (thisEffect != null && thisEffect.getEffect() instanceof BaseMobEffect effect && (entity instanceof Player || !entity.level().isClientSide())) {
                int lv = 1;
                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                //普通模式下与跳跃提升互斥
                if (!isPowerful) {
                    removeEffects(entity, lv);
                }
                //获取buff等级，注意此处原版就是有+1的
                lv += effect.getTruthUsingAmplifier(lv);
                //计算跳跃提升III获取的额外动量
                float vx = 0.3F;
                float vy = 0.2F + 0.1f * (float)lv;
                float f = entity.getYRot() * ((float)Math.PI / 180F);
                entity.setDeltaMovement(entity.getDeltaMovement().add((double)(-Mth.sin(f) * vx), (double)vy, (double)(Mth.cos(f) *vx)));
            }
        }
        //实现抗性提升III的减伤效果与跳跃提升的减摔落伤害效果
        @SubscribeEvent
        public static void onCreatureHurt(LivingHurtEvent evt) {
            if (evt.isCanceled() ) {
                return;
            }
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = evt.getEntity();
            //判断buff存在
            MobEffectInstance burnTheBoats = hurtOne.getEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS.get());
            if (burnTheBoats != null && burnTheBoats.getEffect() instanceof BaseMobEffect baseMobEffect) {
                //获取buff等级
                int lv = baseMobEffect.getTruthUsingAmplifier(burnTheBoats.getAmplifier());
                //判断破釜当前是否生效
                if (!BurnTheBoatsMobEffect.isBurnTheBoats(hurtOne,0)) return;
                //普通模式下与抗性提升互斥
                if (!isPowerful) {
                    removeEffects(hurtOne, lv);
                }
                if (lv >= 0) {
                    //跳跃提升减伤效果
                    float damage = evt.getAmount();
                    if (evt.getSource().is(DamageTypeTags.IS_FALL)) damage -= 3F + lv;
                    //抗性提升减伤效果
                    float buffer = 1.0F;
                    for (int i = 0; i <= (lv + 2); i++) {
                        buffer = buffer * 0.8F;
                    }
                    evt.setAmount(Math.min(damage * buffer, damage));
                }
            }
        }
        //实现破釜的一次性免死效果
        @SubscribeEvent
        public static void onCreatureLostHealth(LivingDamageEvent evt) {
            if (evt.isCanceled() ) {
                return;
            }
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = evt.getEntity();
            //判断是否有破釜效果
            MobEffectInstance burnTheBoats = hurtOne.getEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS.get());
            if (burnTheBoats != null && burnTheBoats.getEffect() instanceof BaseMobEffect) {
                //受到伤害时取出缓存的治疗量减伤
                if (entityWithHeal.containsKey(hurtOne.getUUID())) {
                    float heal = entityWithHeal.get(hurtOne.getUUID());
                    evt.setAmount(evt.getAmount() - heal);
                    entityWithHeal.remove(hurtOne.getUUID());
                    if (evt.getAmount() <= 0) evt.setCanceled(true);
                }
                //破釜未触发时，受到致命伤害可以消耗破釜免死一次
                float finalHealth = entityWithHeal.getOrDefault(hurtOne.getUUID(), 1.0F);
                if (isPowerful
                        && !BurnTheBoatsMobEffect.isBurnTheBoats(hurtOne,0)
                        && hurtOne.getHealth() <= evt.getAmount()
                        && finalHealth > 0) {
                    hurtOne.removeEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS.get());
                    evt.setAmount(0);
                    hurtOne.setHealth(finalHealth);
                    entityWithHeal.put(hurtOne.getUUID(), -1.0F);
                    evt.setCanceled(true);
                }
            }
        }
        //                int lv = burnTheBoats.getAmplifier();
//                int truthLv = ((BaseMobEffect) burnTheBoats.getEffect()).getTruthUsingAmplifier(lv);
//                lv++;
//                truthLv++;
//                float workHealth = (hurtOne.getMaxHealth() * truthLv) / (2 * (truthLv + 1)) > 3 * (truthLv + 1) ? 3 * (truthLv + 1) : (hurtOne.getMaxHealth() * truthLv / (2 * (truthLv + 1)));
//
//                if (isPowerful && hurtOne.getHealth() - evt.getAmount() < workHealth) {
//
//                } else if (hurtOne.getHealth() < workHealth) {
//
//                }
//                if (isPowerful) {
//                    if (hurtOne.getHealth() - evt.getAmount() < workHealth) {
//
//                        int lvStrong = hurtOne.hasEffect(MobEffects.DAMAGE_BOOST) ? Objects.requireNonNull(hurtOne.getEffect(MobEffects.DAMAGE_BOOST)).getAmplifier() + 1 : 0;
//                        int lvSpeed = hurtOne.hasEffect(MobEffects.MOVEMENT_SPEED)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.MOVEMENT_SPEED)).getAmplifier()+1 :0;
//                        int lvDigSpeed = hurtOne.hasEffect(MobEffects.DIG_SPEED)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.DIG_SPEED)).getAmplifier()+1 :0;
//                        int lvJump = hurtOne.hasEffect(MobEffects.JUMP)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.JUMP)).getAmplifier()+1 :0;
//                        MobEffectInstance strong = new MobEffectInstance(MobEffects.DAMAGE_BOOST,300, lvStrong >= lv * 3 ? lv * 3 : lvStrong + 2);
//                        MobEffectInstance speed = new MobEffectInstance(MobEffects.MOVEMENT_SPEED,300, lvSpeed >= lv * 3 ? lv * 3 : lvSpeed + 2);
//                        MobEffectInstance dig = new MobEffectInstance(MobEffects.DIG_SPEED,300, lvDigSpeed >= lv * 3 ? lv * 3 : lvDigSpeed + 2);
//                        MobEffectInstance jump = new MobEffectInstance(MobEffects.JUMP,300, lvJump >= lv * 3 ? lv * 3 : lvJump + 2);
//                        MobEffectInstance resist = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300, lv > 1 ? 3 : 2);
//                        hurtOne.addEffect(strong);
//                        hurtOne.addEffect(speed);
//                        hurtOne.addEffect(dig);
//                        hurtOne.addEffect(jump);
//                        hurtOne.addEffect(resist);
//                        //ImmortalersDelightMod.LOGGER.info("伤害来源是：" + Objects.requireNonNull(evt.getSource().getEntity()).getName().toString());
//                        if (evt.getSource().getEntity()==null || !(evt.getSource().getEntity() instanceof LivingEntity) || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)){
//                            int lvRegen = hurtOne.hasEffect(MobEffects.REGENERATION)? Objects.requireNonNull(hurtOne.getEffect(MobEffects.REGENERATION)).getAmplifier()+1 :0;
//                            MobEffectInstance regen = new MobEffectInstance(MobEffects.REGENERATION,100,(lvRegen > lv ? lv : lvRegen) + 1 );
//                            hurtOne.addEffect(regen);
//                        }
//                    }
//                } else if (hurtOne.getHealth() < workHealth) {
//                    hurtOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,300, 2 ));
//                    hurtOne.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,300, 2 ));
//                    hurtOne.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,300, 2 ));
//                    hurtOne.addEffect(new MobEffectInstance(MobEffects.JUMP,300, 2 ));
//                    hurtOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,300, 2 ));
//                    if (evt.getSource().getEntity()==null || !(evt.getSource().getEntity() instanceof LivingEntity) || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)){
//                        hurtOne.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100,1));
//                    }
//                }
    }
}

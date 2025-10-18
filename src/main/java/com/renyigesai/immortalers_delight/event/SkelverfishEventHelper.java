package com.renyigesai.immortalers_delight.event;


import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.mobbase.ImmortalersMob;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBase;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.init.ImmortalersTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

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

    @SubscribeEvent
    public static void onAmbusherAttack(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType());
            if (entityId != null && !attacker.level().isClientSide) {
                String idString = entityId.toString();
                if (idString.equals(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.getId().toString())) {
                    int hurtArmor = event.getEntity().getArmorValue();
                    if (hurtArmor > 0) {
                        if (hurtArmor > 20) hurtArmor = 20;
                        float damageBuffer = (1/(1-(hurtArmor * 0.04f))) > 3.0f ? 3.0f : (1/(1-(hurtArmor * 0.04f)));
                        event.setAmount(event.getAmount() * damageBuffer);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onThrasherJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Sniffer sniffer) {
            sniffer.goalSelector.addGoal(3, new TemptGoal(sniffer, 3.0D, Ingredient.of(ImmortalersDelightItems.SACHETS.get()), false));
        }
//        if (event.getEntity() instanceof SkelverfishThrasher skelverfishThrasher) {
//            skelverfishThrasher.goalSelector.removeGoal(new MeleeAttackGoal(skelverfishThrasher, 1.0D, false));
//        }
    }

    @SubscribeEvent
    public static void onSilverfishsIntoStone(EntityMobGriefingEvent event) {
        if (event.getEntity() instanceof SkelverfishBase skelverfishBase) {
            event.setResult(Event.Result.DENY);
        }
//        if (event.getEntity() instanceof SkelverfishThrasher skelverfishThrasher) {
//            skelverfishThrasher.goalSelector.removeGoal(new MeleeAttackGoal(skelverfishThrasher, 1.0D, false));
//        }
    }

    /**
     *  超凡模式下，怪物的伤害随目标血量提升，作用类似百分比伤害。
     *  但其实此处最大仅能造成3倍的伤害，限伤的存在实际使得过高的增伤意义不大。
     *  保底伤害的触发在这个方法的时机之前，所以这个增伤不影响保底伤害。
     */
    @SubscribeEvent
    public static void ImmortalrsMobAttackProgressDamage(LivingHurtEvent event) {
        LivingEntity hurtOne = event.getEntity();
        if (!hurtOne.level().isClientSide) {
            if (event.getSource().getEntity() instanceof LivingEntity attacker) {
                if (DifficultyModeHelper.isPowerBattleMode()) {
                    if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)
                            || attacker.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)
                            || attacker.getType().is(ImmortalersDelightTags.IMMORTAL_MINI_BOSS)
                    ) {
                        float oldDamage = event.getAmount();
                        float buffer = Math.min(3,1 + hurtOne.getMaxHealth() * 0.02F);
                        event.setAmount(Math.max(oldDamage * buffer, 0.0F));
                    }
                }

                if (attacker.level().getDifficulty().getId() >= 3) event.setAmount(event.getAmount() * 1.25F);
            }

            if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)
                    || hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)
                    || hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_MINI_BOSS)
            ) {
                if (hurtOne.level().getDifficulty().getId() >= 3) event.setAmount(event.getAmount() * 0.8F);
            }
        }

    }

    /**
     *  保底伤害：在本模组生物发出攻击行为时触发，直接进行一个改血攻击以制裁逆天减伤。
     *  保底伤害为伤害值*0.2，精英怪以及以上等级怪物额外造成目标生命上限5%的伤害。
     *  该事件触发时Forge的事件系统尚无法对伤害值进行修改，但生物自身hurt方法生效更快，可以修改伤害值
     *  该攻击不会致死
     */
    @SubscribeEvent
    public static void ImmortalrsMobAttackMinDamage(LivingAttackEvent event) {
        if (DifficultyModeHelper.isPowerBattleMode()) {
            if (event.getSource().getEntity() instanceof LivingEntity attacker && !attacker.level().isClientSide) {
                LivingEntity hurtOne = event.getEntity();
                if (hurtOne.getHealth() < 1.0F) return;

                float minDamage = event.getAmount() * 0.2F;

                boolean needMinDamage = false;
                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)) {
                    float damageProgress = 0;
                    if (attacker instanceof ImmortalersMob immMob) {
                        damageProgress = immMob.getAttackProportion();
                        minDamage += hurtOne.getMaxHealth() * damageProgress;
                    }
                    needMinDamage = true;
                }

                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)) {
                    float damageProgress = 0.05F;
                    if (attacker instanceof ImmortalersMob immMob) {
                        damageProgress = immMob.getAttackProportion();
                    }
                    if (attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) > 0) {
                        float buffer = (float) (event.getAmount() / attacker.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        if (buffer < 1.0F) damageProgress *= buffer;
                    }
                    minDamage += hurtOne.getMaxHealth() * damageProgress;
                    needMinDamage = true;
                }

                if (attacker.getType().is(ImmortalersDelightTags.IMMORTAL_MINI_BOSS)) {
                    float damageProgress = 0.05F;
                    if (attacker instanceof ImmortalersMob immMob) {
                        damageProgress = immMob.getAttackProportion();
                    }
                    if (attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) > 0) {
                        float buffer = (float) (event.getAmount() / attacker.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        damageProgress *= buffer;
                    }
                    minDamage += hurtOne.getMaxHealth() * damageProgress;
                    needMinDamage = true;
                }

                if (needMinDamage) hurtOne.setHealth(Math.max(hurtOne.getHealth() - minDamage, 0.01F));
            }
        }

    }

    /**
     *  伤害衰减：伤害大于x时，将衰减为[原伤害值/(1+(原伤害值*0.01x))]。
     *  根据怪物品级的不同，分母的上限分别为7,11,15。
     *  该事件触发时生物自定义hurt方法、Forge伤害事件、原版护甲法抗已经完成计算
     */
    @SubscribeEvent
    public static void ImmortalrsMobHurtDamageDecay(LivingDamageEvent event) {
        if (DifficultyModeHelper.isPowerBattleMode()) {
            LivingEntity hurtOne = event.getEntity();
            if (!hurtOne.level().isClientSide) {
                float oldDamage = event.getAmount();
                float damage = oldDamage;
                float damageDivisor = 0;

                boolean needLimitDamage = false;
                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_NORMAL_MOBS)) {
                    damageDivisor = 0.04f;
                    if (hurtOne instanceof ImmortalersMob immMob) {
                        damageDivisor = immMob.getDamageDivisor();
                    }
                    if (oldDamage > 100*damageDivisor) {
                        float buffer = Math.min(7,1 + damageDivisor * (oldDamage - 100 * damageDivisor));
                        damage = oldDamage / buffer;
                        needLimitDamage = true;
                    }
                }

                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_ELITE_MOBS)) {
                    damageDivisor = 0.05f;
                    if (hurtOne instanceof ImmortalersMob immMob) {
                        damageDivisor = immMob.getDamageDivisor();
                    }
                    if (oldDamage > 100*damageDivisor) {
                        float buffer = Math.min(11,1 + damageDivisor * (oldDamage - 100 * damageDivisor));
                        damage = oldDamage / buffer;
                        needLimitDamage = true;
                    }
                }

                if (hurtOne.getType().is(ImmortalersDelightTags.IMMORTAL_MINI_BOSS)) {
                    damageDivisor = 0.08f;
                    if (hurtOne instanceof ImmortalersMob immMob) {
                        damageDivisor = immMob.getDamageDivisor();
                    }
                    if (oldDamage > 100*damageDivisor) {
                        float buffer = Math.min(15,1 + damageDivisor * (oldDamage - 100 * damageDivisor));
                        damage = oldDamage / buffer;
                        needLimitDamage = true;
                    }
                }
                if (needLimitDamage) event.setAmount(damage);
            }
        }
    }
}

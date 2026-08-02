package com.renyigesai.immortalers_delight.potion;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.particle.ScreenLayerParticle;
//import com.renyigesai.immortalers_delight.client.particle.ScreenLayerParticleOption;
import com.renyigesai.immortalers_delight.client.particle.ScreenLayerParticleOption;
import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticles;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.message.KeyAuxiliaryMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class InfernalForgingMobEffect extends BaseMobEffect {

    //表示上次攻击的时间
    private static final Map<UUID,Long> entitiesLastAttack = new HashMap<>();
    //表示攻速叠层
    private static final Map<UUID,Byte> entitiesWithCharges = new HashMap<>();
    //表示伤害叠层
    private static final Map<UUID,Byte> entitiesWithStacks = Maps.newHashMap();
    public static HashMap<UUID,Byte> getEntitiesWithStacks() {return new HashMap<>(entitiesWithStacks);}
    public float tipBuffer = 1;

    public InfernalForgingMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 11674146);
    }

    /*================================实现客户端动态切换每层数据显示与叠满数据显示======================================*/


    @Override
    public @NotNull MobEffect addAttributeModifier(@NotNull Attribute pAttribute, @NotNull String pUuid, double pAmount, AttributeModifier.@NotNull Operation pOperation) {
        return super.addAttributeModifier(pAttribute, pUuid, pAmount, pOperation);
    }

    /*===========================实现叠层后一段时间不打架持续掉层===============================*/
    //根据等级判断是否要掉层，并返回掉的层数
    private static byte needReduceCharges(LivingEntity living, int amplifier) {

        HashMap<UUID,Long> bufferL = new HashMap<>(entitiesLastAttack);
        Long lastTime = bufferL.get(living.getUUID());
        if (lastTime == null) return (byte) 99;
        if (entitiesWithCharges.get(living.getUUID()) == null && entitiesWithStacks.get(living.getUUID()) == null) return (byte) 0;

        byte mode = 0;
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (amplifier <= 2) {
            mode = (byte) 1;
        } else if (amplifier <= 6) {
            mode = (byte) 2;
        } else if (amplifier <= 14) {
            mode = (byte) 3;
        } else {mode = (byte) 4;}
        if (isPowerful) mode--;

        Long time = TimekeepingTask.getImmortalTickTime();

        if (mode == 0) {
            return (byte) 0;
        } else if (mode == 1) {
            return (byte) ((time - lastTime >= 4000) ? 1 : 0);
        } else if (mode == 2) {
            return (byte) ((time - lastTime >= 3500) ? 1 : 0);
        } else if (mode == 3) {
            return (byte) ((time % 1000 > 500) ? 1 : 0);
        } else {
            return (byte) ((time - lastTime >= 4000) ? 99 : 0);
        }
    }

    //生效时执行的方法（主要用于掉层）
    //双端执行
    @Override
    protected void applyEffectTickInControl(LivingEntity livingEntity, int amplifier) {
        byte b = needReduceCharges(livingEntity,amplifier);
        //掉层
        if (!livingEntity.level().isClientSide() && b > 0) {
            //缓存要查的表，避免并发修改错误
            HashMap<UUID,Byte> bufferC = new HashMap<>(entitiesWithCharges);
            HashMap<UUID,Byte> bufferS = new HashMap<>(entitiesWithStacks);

            byte charge = bufferC.getOrDefault(livingEntity.getUUID(),(byte)0);
            byte buffStack = bufferS.getOrDefault(livingEntity.getUUID(),(byte)0);
            boolean needChange = false;
            if (charge > 0) {
                if (charge - b > 0) entitiesWithCharges.put(livingEntity.getUUID(), (byte) (charge - b));
                else entitiesWithCharges.remove(livingEntity.getUUID());
                needChange = true;
            }
            else if (buffStack > 0) {
                if (buffStack - b > 0) entitiesWithStacks.put(livingEntity.getUUID(), (byte) (buffStack - b));
                else entitiesWithStacks.remove(livingEntity.getUUID());
                needChange = true;
            }

            //玩家特殊处理：向客户端同步数据
            if (needChange && livingEntity instanceof ServerPlayer player) {
                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();

                //实现灼热联动
                float buffer = 1;
                if (!player.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get())) buffer = 0.6f;

                int maxCharge = getMaxCharges(amplifier,buffer);
                //计算当前叠层进度：超凡模式两项一起叠所以只算攻速叠层，普通模式两项总计叠层所以相加
                int progress = isPowerful ? charge - b : charge + buffStack - b;
                float apl = (float) progress / maxCharge;
                if (apl > 1) apl = 1;

                ImmortalersNetwork.sendNonLocal(new ImmortalersEffectMessage(2,apl),player);
            }

            //更新掉层后的属性加成
            this.addAttributeModifiers(livingEntity, livingEntity.getAttributes(), amplifier);
        }

    }

    //每秒生效，用于刷新属性加成
    @Override
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    /*====实现基础功能支持：刷新攻速属性、在buff去除时清除缓存数据、两端分别计算属性值、获取每级buff叠层上限====*/
    //buff去除时清除缓存数据
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        entitiesWithStacks.remove(livingEntity.getUUID());
        entitiesWithCharges.remove(livingEntity.getUUID());
        entitiesLastAttack.remove(livingEntity.getUUID());
        super.removeAttributeModifiers(livingEntity,pAttributeMap,pAmplifier);

        //玩家特殊处理：向客户端同步数据
        if (livingEntity instanceof ServerPlayer player && !player.level().isClientSide()) {

            float apl = 0;
            ImmortalersNetwork.sendNonLocal(new ImmortalersEffectMessage(2,apl),player);
        }
    }

    //实际用于更新属性加成
    //加攻击实际不生效，仅用于客户端显示对火伤的加成
    //用于叠攻速
    @Override
    public void addAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {

        //缓存要查的表，避免并发修改错误
        HashMap<UUID,Byte> bufferC = new HashMap<>(entitiesWithCharges);

        for(Map.Entry<Attribute, AttributeModifier> entry : this.getAttributeModifiersSimple().entrySet()) {

            if (entry.getKey() == Attributes.ATTACK_DAMAGE) continue;

            Byte charges = bufferC.get(pLivingEntity.getUUID());
            if (charges == null) return;

            AttributeInstance attributeinstance = pAttributeMap.getInstance(entry.getKey());
            if (attributeinstance != null && charges > 0) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier);
                double amount = this.getAttributeModifierValueSimple(pAmplifier, attributemodifier) * charges;
//                System.out.println("火成添加攻速" + amount);
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), this.getDescriptionId() + " " + pAmplifier, amount, attributemodifier.getOperation()));
            } //else System.out.println("火成添加攻速失败1");
        }
    }

    public @NotNull Map<Attribute, AttributeModifier> getAttributeModifiersSimple() {
        return super.getAttributeModifiers();
    }
    //计算属性加成，这个属性加成仅用于客户端显示
    @Override
    public double getAttributeModifierValue(int pAmplifier, @NotNull AttributeModifier pModifier) {
        double d = super.getAttributeModifierValue(pAmplifier + 1,pModifier);
        if (System.currentTimeMillis() % 4000 > 2000) d *= getMaxCharges(pAmplifier,tipBuffer);
        return d * Config.infernal_forging_attribute_multiplier;
    }
    //计算属性加成，这个属性加成获取每层的数据
    public double getAttributeModifierValueSimple(int pAmplifier, @NotNull AttributeModifier pModifier) {
        return super.getAttributeModifierValue(pAmplifier + 1,pModifier) * Config.infernal_forging_attribute_multiplier;
    }

    //计算当前效果等级的叠层上限
    public static int getMaxCharges(int lv,float buffer) {
        int highestBit = Integer.highestOneBit(lv + 1); // 取 lv+1 的最高位权值
        int value = (48 + highestBit - 1) / highestBit; // 向上取整除法
        float output = Math.max(1, value) * buffer;
        return Math.round(output);
    }

    /*========实现攻击叠层与火焰伤害加伤、根据叠层向客户端发送数据辅助显示全屏特效========*/

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = Dist.CLIENT)
    public static class InfernalForgingClientEffect {
        @SubscribeEvent
        public static void onPlayerTickStart(TickEvent.PlayerTickEvent evt) {
            Player player1 = Minecraft.getInstance().player;
            //实现灼热联动
            float buffer = 1;
            if (player1 != null) {
                if (!player1.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get())) buffer = 0.6f;
            }

            if (ImmortalersDelightMobEffect.INFERNAL_FORGING.get() instanceof InfernalForgingMobEffect effect) {
                effect.tipBuffer = buffer;
            }
        }
    }

    @Mod.EventBusSubscriber
    public static class InfernalForgingMobPotionEffect {
        //表示吃到火焰伤害加成的实体
        private static final Map<UUID,Float> entitiesFearFire = new HashMap<>();
        //表示需要发包同步的玩家
        private static final Map<UUID,Boolean> playersNeedMSG = new HashMap<>();
        //火成效果的具体实现：在此处实现攻速/火伤叠层，并处理火焰伤害的加伤
        @SubscribeEvent
        public static void onPlayerTickStart(TickEvent.PlayerTickEvent evt) {
            Player player = evt.player;

            if (!player.level().isClientSide() && playersNeedMSG.get(player.getUUID()) != null) {
                //缓存要查的表，避免并发修改错误
                HashMap<UUID,Byte> bufferC = new HashMap<>(entitiesWithCharges);
                HashMap<UUID,Byte> bufferS = new HashMap<>(entitiesWithStacks);

                byte charge = bufferC.getOrDefault(player.getUUID(),(byte)0);
                byte buffStack = bufferS.getOrDefault(player.getUUID(),(byte)0);

                //玩家特殊处理：向客户端同步数据
                MobEffectInstance effectInstance = player.getEffect(ImmortalersDelightMobEffect.INFERNAL_FORGING.get());
                if (effectInstance != null && effectInstance.getEffect() instanceof BaseMobEffect effect) {
                    //拿生效等级
                    int amplifier = effect.getTruthUsingAmplifier(effectInstance.getAmplifier());

                    //实现灼热联动
                    float buffer = 1;
                    if (!player.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get())) buffer = 0.6f;

                    //发包
                    if (player instanceof ServerPlayer serverPlayer) {
                        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                        int maxCharge = getMaxCharges(amplifier,buffer);
                        //计算当前叠层进度：超凡模式两项一起叠所以只算攻速叠层，普通模式两项总计叠层所以相加
                        int progress = isPowerful ? charge : charge + buffStack;
                        float apl = (float) progress / maxCharge;
                        if (apl > 1) apl = 1;
                        ImmortalersNetwork.sendNonLocal(new ImmortalersEffectMessage(2,apl),serverPlayer);
                    }
                }

                playersNeedMSG.remove(player.getUUID());
            }
        }
        @SubscribeEvent
        public static void onCreatureAttack(LivingHurtEvent evt) {
            if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
                return;
            }
            LivingEntity hurtOne = evt.getEntity();
            LivingEntity attacker = null;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();

            //实现叠攻速/火伤
            if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
                attacker = livingEntity;
            }

            //缓存要查的表，避免并发修改错误
            HashMap<UUID,Byte> bufferC = new HashMap<>(entitiesWithCharges);
            HashMap<UUID,Byte> bufferS = new HashMap<>(entitiesWithStacks);

            if (!hurtOne.level().isClientSide && attacker != null) {

                //触发附加火焰伤害要求伤害值不能过低，以限制连点器
                if (!isPowerful) {
                    AttributeInstance attr = attacker.getAttribute(Attributes.ATTACK_DAMAGE);
                    double atk = attr == null ? 0 : attr.getValue();
                    if (evt.getAmount() < 1 || (evt.getAmount() < atk)) return;
                }

                MobEffectInstance effectInstance = attacker.getEffect(ImmortalersDelightMobEffect.INFERNAL_FORGING.get());
                if (effectInstance != null && effectInstance.getEffect() instanceof InfernalForgingMobEffect effect) {

                    //获取生效等级
                    int amplifier = effect.getTruthUsingAmplifier(effectInstance.getAmplifier());
                    //实现灼热联动
                    float buffer = 1;
                    if (!attacker.hasEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get())) buffer = 0.6f;

                    //计算最大叠层
                    byte maxCharges = (byte) getMaxCharges(amplifier,buffer);

                    byte charge = bufferC.getOrDefault(attacker.getUUID(),(byte)0);
                    byte buffStack = bufferS.getOrDefault(attacker.getUUID(),(byte)0);
                    //叠层：非超凡，如果攻速达到4，改为叠火伤，二者总计可以叠加到上限层数
                    //超凡则改为每次都叠一层攻速和火伤
                    if (isPowerful) {
                        if (charge + 1 <= maxCharges) entitiesWithCharges.put(attacker.getUUID(), (byte) (charge + 1));
                        if (buffStack + 1 <= maxCharges) entitiesWithStacks.put(attacker.getUUID(), (byte) (buffStack + 1));
                    } else {
                        //判断是否叠满
                        byte sum = (byte) (charge + buffStack);
                        if (sum + 1 > maxCharges) return;

                        //判断应该叠火伤还是攻速，并叠层
                        AttributeInstance attributeinstance = attacker.getAttribute(Attributes.ATTACK_SPEED);
                        if (attributeinstance != null && attributeinstance.getValue() < 4.0f) {
                            entitiesWithCharges.put(attacker.getUUID(), (byte) (charge + 1));
                        } else entitiesWithStacks.put(attacker.getUUID(), (byte) (buffStack + 1));
                    }
                    //刷新重置时间
                    entitiesLastAttack.put(attacker.getUUID(), TimekeepingTask.getImmortalTickTime());

//                    System.out.println("服务端火伤叠层：" + charge);
                    //标记目标，用于火焰伤害加伤
                    AttributeModifier attackDamage = effect.getAttributeModifiersSimple().get(Attributes.ATTACK_DAMAGE);
                    if (attackDamage != null) {
                        float amount = (float) (effect.getAttributeModifierValueSimple(amplifier, attackDamage) * buffStack);
                        entitiesFearFire.put(hurtOne.getUUID(), amount);
                    }
                    //刷新属性
                    effect.addAttributeModifiers(attacker,attacker.getAttributes(),amplifier);

                    //玩家特殊处理：将玩家标记为向客户端发送数据，让渲染特效
                    if (attacker instanceof Player player) {
                        playersNeedMSG.put(player.getUUID(),true);
                    }

//                    AttributeInstance attributeinstance = attacker.getAttribute(Attributes.ATTACK_SPEED);
//                    if (attributeinstance != null) {
//                        System.out.println("当前攻速：" + attributeinstance.getValue());
//                    }
                }
            }

            HashMap<UUID,Float> bufferF = new HashMap<>(entitiesFearFire);
            //实现火焰伤害按叠层增伤
            if (!hurtOne.level().isClientSide && evt.getSource().is(DamageTypeTags.IS_FIRE)) {
                if (bufferF.get(hurtOne.getUUID()) != null) {
                    float amount = bufferF.get(hurtOne.getUUID());
                    //System.out.println("现在的火伤倍率是：" + amount);
                    evt.setAmount(evt.getAmount() * (1 + amount));

                    entitiesFearFire.remove(hurtOne.getUUID());
                }
            }

        }


    }
}

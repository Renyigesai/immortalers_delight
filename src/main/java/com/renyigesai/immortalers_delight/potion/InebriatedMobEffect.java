package com.renyigesai.immortalers_delight.potion;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.food.InebriatedToxicFoodItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class InebriatedMobEffect extends BaseMobEffect {

    private static Map<UUID,Long> entitiesNeedExempt = new HashMap<>();
    private static Long timeNeedExempt = 0L;
    public InebriatedMobEffect() {
        super(MobEffectCategory.HARMFUL, 4959736);
    }

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == INEBRIATED.get() && !pEntity.level().isClientSide()) {
            int time = pEntity.hasEffect(INEBRIATED.get()) ? Objects.requireNonNull(pEntity.getEffect(INEBRIATED.get()).getDuration()):0;
            if (time > 3600) {
                super.applyEffectTick(pEntity, amplifier);
                if (Config.useBetterStun) {
                    InebriatedToxicFoodItem.addEffectWithoutCanBeAffected(pEntity, new MobEffectInstance(WEAK_POISON.get(), time, amplifier), null);
                }else pEntity.addEffect(new MobEffectInstance(WEAK_POISON.get(), time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, time, amplifier));
                pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, time, amplifier));

            }
        }
    }
    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        //获取实体的最大生命与生命上限的基础属性
        float maxHealth = pEntity.getMaxHealth();
        double baseHealth = 20;
        //玩家的默认基础生命为20，同时处理一些不具有原版血条的特殊生物
        if (!(pEntity instanceof Player) && pEntity.getAttribute(Attributes.MAX_HEALTH) != null) {
            baseHealth = pEntity.getAttributeBaseValue(Attributes.MAX_HEALTH);
        }
        //计算伤害：伤害为固定值，按照目标的生命增益倍率放大
        float damage = (float) (1.6 * maxHealth / baseHealth);
        if (!isPowerful && damage > 8 + 4 * amplifier) {
            damage = 8+4*amplifier;
        }
        boolean isOP = pEntity instanceof Player player && player.isCreative();
        if (!isOP || isPowerful) {
            pEntity.invulnerableTime = 0;
            pEntity.hurt(new DamageSource(pEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:drunk")))), damage);
            pEntity.invulnerableTime = 0;
        }
    }

    @Override
    public boolean isDurationEffectTickInControl(int pDuration, int pAmplifier) {
        int j = 64 >> pAmplifier;
        if (j > 0) {
            return pDuration % j == 0;
        } else {
            return true;
        }
    }


    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class InebriatedPotionEffect {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onOpUseCommand(CommandEvent event) {
            if (event.isCanceled()) return;
//            String rawInput = event.getParseResults().getReader().getString();
//            System.out.println(rawInput);
//
//            // 判断是否是 effect clear
//            if (rawInput.startsWith("effect clear")) {
//                System.out.println("Somebody use the effect clear command. ");
//                CommandSourceStack source = event.getParseResults().getContext().getSource();
//                Player player = source.getPlayer(); // 获取执行者
//                if (player != null) {
//                    System.out.println("Who use the command? "+player.getUUID());
//                }
//            }
            List<ParsedCommandNode<CommandSourceStack>> nodes =
                    event.getParseResults().getContext().getNodes();

            if (nodes.size() < 2) return;

            // 第一个节点是 "effect"，第二个节点是 "clear"
            String rootNode = nodes.get(0).getNode().getName();
            String subNode = nodes.get(1).getNode().getName();

            // 确认是 /effect clear 命令
            if ("effect".equals(rootNode) && "clear".equals(subNode)) {
                //System.out.println("Somebody use the effect clear command. ");
                ParseResults<CommandSourceStack> parse = event.getParseResults();

                // 获取命令发送者信息
//                CommandSourceStack source = parse.getContext().getSource();
//                String senderName = source.getTextName();
//                ServerLevel level = source.getLevel();
//                System.out.println("Who use the command? "+ senderName);
//                System.out.println("Is client? "+ level.isClientSide());
                boolean hasTargets = nodes.size() >= 3 && "targets".equals(nodes.get(2).getNode().getName());
                if (!hasTargets) {return;}

                // 将目标添加到豁免列表
                CommandContext<CommandSourceStack> ctx = parse.getContext().build(parse.getReader().getString());
                try {
                    // 读取目标实体
                    Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");

                    // 调试信息
                    String sender = ctx.getSource().getTextName();
                    System.out.println(sender + " 对 " + targets.size() + " 个目标清除效果");

                    //将目标添加到豁免实体表
                    for (Entity entity : targets) {
                        if (entity instanceof LivingEntity living) {
                            entitiesNeedExempt.put(living.getUUID(), TimekeepingTask.getImmortalTickTime());
                        }
                    }
                } catch (CommandSyntaxException ignored) {

                }
            }
        }

        private static final Map<UUID,Float> entityDamage = new HashMap<UUID,Float>();
        //对玩家与效用生物，使用事件强化伤害
        //在伤害结算流程的开始记录伤害
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onLivingAttack(LivingAttackEvent event) {

            if (event.getSource().is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:drunk")))) {
                if (event.isCanceled()) event.setCanceled(false);
                LivingEntity pEntity = event.getEntity();

                if (pEntity instanceof Enemy) return;

                float health = pEntity.getHealth();
                float damage = event.getAmount();

                entityDamage.put(pEntity.getUUID(), health - damage);
            }
        }
        //对玩家与效用生物，使用事件强化伤害
        //在伤害结算流程中取最高伤害
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onLivingHurt(LivingHurtEvent event) {

            if (event.getSource().is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:drunk")))) {
                if (event.isCanceled()) event.setCanceled(false);

                boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
                if (isPowerful) {
                    LivingEntity pEntity = event.getEntity();

                    if (pEntity instanceof Enemy) return;

                    float health = pEntity.getHealth();
                    float damage = event.getAmount();

                    if ((health - damage) < entityDamage.get(pEntity.getUUID())) {
                        entityDamage.put(pEntity.getUUID(), health - damage);
                    }
                }
            }
        }
        //对玩家与效用生物，使用事件强化伤害
        //如果被玩家减伤，修正伤害
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onLivingDamage(LivingDamageEvent event) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity pEntity = event.getEntity();

            if (pEntity instanceof Enemy) return;

            if (entityDamage.containsKey(pEntity.getUUID())) {
                float health = pEntity.getHealth();
                float needHealth = entityDamage.get(pEntity.getUUID());

                if (isPowerful && health - event.getAmount() > needHealth) {
                    pEntity.setHealth(needHealth < 0.0F ? 0.0F : needHealth);
                    event.setAmount(0.0F);
                }
                entityDamage.remove(pEntity.getUUID());
            }
        }
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onAddToEntity(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                LivingEntity entity = event.getEntity();
                if (!entity.getCommandSenderWorld().isClientSide
                        && event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get()
                        && !entity.hasEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get())) {
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
        @SubscribeEvent
        public static void onRemoveFromEntity(MobEffectEvent.Remove event) {
            if (event != null && event.getEntity() != null) {
                LivingEntity entity = event.getEntity();

                if (entity instanceof Player player && player.isCreative()) return;


                if (!entity.getCommandSenderWorld().isClientSide) {
                    MobEffectInstance inebriated = entity.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get());
                    MobEffectInstance magicalReverse = entity.getEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get());
                    int maxTime = magicalReverse == null ? 0 : (magicalReverse.getDuration() * 100) << magicalReverse.getAmplifier();
                    if (inebriated != null && inebriated.getDuration() > maxTime) {
                        if (event.getEffectInstance() != null
                                && (event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get()
                                || event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.WEAK_POISON.get()
                                || event.getEffectInstance().getEffect() == MobEffects.POISON
                                || event.getEffectInstance().getEffect() == MobEffects.BLINDNESS
                                || event.getEffectInstance().getEffect() == MobEffects.CONFUSION
                                || event.getEffectInstance().getEffect() == MobEffects.MOVEMENT_SLOWDOWN
                                || event.getEffectInstance().getEffect() == MobEffects.WEAKNESS)) {
                            if (!needExemptEntity(entity)) event.setCanceled(true);
                        }
                    }
                }
            }
        }

        //通过豁免实体列表来判定是否可以解除
        private static boolean needExemptEntity(LivingEntity entity) {
            //如果不在豁免列表中，直接返回否定
            if (entitiesNeedExempt.get(entity.getUUID()) != null) {
                boolean needBreak = false;
                //如果在且是新鲜的解除指令，改为可以解除
                if (Math.abs(entitiesNeedExempt.get(entity.getUUID()) - TimekeepingTask.getImmortalTickTime()) < 100) {
                    needBreak = true;
                }
                //如果是已超时的解除指令，清表
                if (!needBreak) entitiesNeedExempt.remove(entity.getUUID());
                return needBreak;
            }
            return false;
        }
    }
}

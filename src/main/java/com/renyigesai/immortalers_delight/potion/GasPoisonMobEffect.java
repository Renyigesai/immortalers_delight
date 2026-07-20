package com.renyigesai.immortalers_delight.potion;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.weapon.GoldenFabricArmor;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.*;

public class GasPoisonMobEffect extends BaseMobEffect {
    private static Map<UUID,Long> entitiesNeedExempt = new HashMap<>();
    public GasPoisonMobEffect() {
        super(MobEffectCategory.HARMFUL, 9574964);
    }
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        super.applyEffectTick(pEntity, amplifier);
        int i = pEntity.getRandom().nextInt(5);
        switch (i) {
            case 0 -> pEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, amplifier));
            case 1 -> pEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, amplifier));
            case 2 -> pEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, amplifier));
            case 3 -> pEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, amplifier));
            case 4 -> pEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, amplifier));
        }
    }
    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
        if (this == GAS_POISON.get() && !pEntity.level().isClientSide()) {
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            //获取实体的最大生命与生命上限的基础属性
            float maxHealth = pEntity.getMaxHealth();
            double baseHealth = 20;
            //玩家的默认基础生命为20，同时处理一些不具有原版血条的特殊生物
            if (!(pEntity instanceof Player) && pEntity.getAttribute(Attributes.MAX_HEALTH) != null) {
                baseHealth = pEntity.getAttributeBaseValue(Attributes.MAX_HEALTH);
            }
            //计算伤害：伤害为固定值，按照目标的生命增益倍率放大
            float damage = (float) (1.2 * maxHealth / baseHealth);
            if (!isPowerful && damage > 6 + 3 * amplifier) {
                damage = 6+3*amplifier;
            }
            boolean isOP = pEntity instanceof Player player && player.isCreative();
            if (!isOP || isPowerful) {
                pEntity.invulnerableTime = 0;
                pEntity.hurt(getDamageSource(pEntity, null), damage);
                pEntity.invulnerableTime = 0;
            }
        }
    }

    public static DamageSource getDamageSource(Entity hurtOne, @Nullable Entity attacker) {
        if (attacker != null) {
            return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:gas"))), attacker);
        }
        return new DamageSource(hurtOne.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("immortalers_delight:gas"))));
    }

    @Override
    /**
     * 判断效果是否应该刻更新
     * @param pDuration 效果剩余持续时间
     * @param pAmplifier 效果的放大等级
     * @return 如果效果应该刻更新则返回true，否则返回false
     */
    public boolean isDurationEffectTickInControl(int pDuration, int pAmplifier) {
        // 计算更新间隔，基础值为32，随着放大等级增加而增大
        int j = 32 >> pAmplifier;
        // 如果计算出的间隔大于0
        if (j > 0) {
            // 当剩余时间对间隔取余为0时，表示应该更新效果
            return pDuration % j == 0;
        } else {
            // 如果间隔小于等于0，则始终更新效果
            return true;
        }
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class GasPoisonPotionEffect {
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
        @SubscribeEvent
        public static void onRemoveFromEntity(MobEffectEvent.Remove event) {
            if (event != null && event.getEntity() != null) {
                LivingEntity entity = event.getEntity();

                if (entity instanceof Player player && player.isCreative()) return;
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
                        if (!needExemptEntity(entity)) event.setCanceled(true);
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

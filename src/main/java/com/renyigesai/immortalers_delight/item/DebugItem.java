package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.Config;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffectTask;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
//import com.renyigesai.immortalers_delight.util.datautil.EffectData;
//import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
//import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.MagicalReverseMapSaveLoadHelper;
import com.renyigesai.immortalers_delight.api.ILivingEntityExtension;
import com.renyigesai.immortalers_delight.entities.living.PiecesHitboxEntity;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import com.renyigesai.immortalers_delight.util.LivingDamageUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;

public class DebugItem extends Item {
    public DebugItem(Properties p_41383_) {
        super(p_41383_);
    }

    public static final int DEBUG_ITEM_MODEL = 0;
    public static final int KI_BLAST_MODEL = 1;
    public static final int LARGE_COLUMN_MODEL = 2;
    public static final int BONE_KNIFE_MODEL = 3;
    public static final int JENG_NANU_MODEL = 4;

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        Class<BlockEntity> blockEntityClass = BlockEntity.class;
        Field[] fields = blockEntityClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        IntArrayTag tag = NbtUtils.createUUID(BASE_ATTACK_DAMAGE_UUID);
        System.out.println("攻击力" + tag);
        System.out.println("攻击力：" + tag.getAsString());

        tag = NbtUtils.createUUID(BASE_ATTACK_SPEED_UUID);
        System.out.println("攻击速度" + tag);
        System.out.println("攻击速度：" + tag.getAsString());

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public InteractionResult useOn(UseOnContext pContext) {
        Map<MobEffect, Float[]> map = EffectUtils.getMobEffectWithLevelConfig(Config.EFFECTS_USING_LINEAR_GROWTH.get());
        for (MobEffect effect : map.keySet()) {
            System.out.println(effect.getDescriptionId() + ":" + map.get(effect)[0] + ":" + map.get(effect)[1]);
        }

//        if (!pContext.getLevel().isClientSide() && pContext.getPlayer() != null) {
//
//            PiecesHitboxEntity xia = new PiecesHitboxEntity(
//                    pContext.getLevel(),
//                    pContext.getClickedPos().getX(),
//                    pContext.getClickedPos().getY() + 1,
//                    pContext.getClickedPos().getZ(),
//                    pContext.getPlayer().yHeadRot,
//                    pContext.getPlayer()
//            );
//            //设置攻击力
//            AttributeInstance atk = xia.getAttribute(Attributes.ATTACK_DAMAGE);
//            if (atk != null) atk.setBaseValue(pContext.getPlayer().getAttributeValue(Attributes.ATTACK_DAMAGE));
//            //设置范围
//            xia.setRadius(2);
//            pContext.getLevel().addFreshEntity(xia);
//        }
//        if (!pContext.getLevel().isClientSide()) {
//            BlockState blockState = pContext.getLevel().getBlockState(pContext.getClickedPos());
//            List<Property<?>> list = blockState.getProperties().stream().toList();
//            Property<?> needProperty = null;
//            for (Property<?> property : list) {
//                String info = "这是从配方输入的ID";
//                if (property.getName().equals(info)) {
//                    System.out.println("这是从方块状态中获取的ID：" + property.getName() + "可以满足要求");
//                    needProperty = property;
//                }
//            }
//
//            if (needProperty != null && blockState.hasProperty(needProperty)) {
//                blockState.
//            }
//        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        System.out.println("hurtEnemy");
        if (!target.level().isClientSide()) {
            LivingDamageUtil.callActuallyHurt(target, attacker.damageSources().mobAttack(attacker), target.getMaxHealth());
            if (target.isDeadOrDying()) {
                System.out.println("target is dead");
                target.die(attacker.damageSources().mobAttack(attacker));
            }
        }


        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        System.out.println("onLeftClickEntity");
        if (entity instanceof LivingEntity target && player instanceof ServerPlayer attacker) {
            if (!target.level().isClientSide()) {
//                LivingDamageUtil.callActuallyHurt(target, attacker.damageSources().mobAttack(attacker), target.getHealth());
//                if (target.isDeadOrDying()) {
//                    System.out.println("target is dead");
//                    target.die(attacker.damageSources().mobAttack(attacker));
//                }
                CompoundTag nbt = target.getPersistentData();
                nbt.putBoolean("immortalers_delight_zi_wen_gui_tian",true);
                if (target instanceof ILivingEntityExtension tool) tool.immortalers_delight$ziWenGuiTian(true);
                target.die(attacker.damageSources().mobAttack(attacker));
            }
        }

        return super.onLeftClickEntity(stack, player, entity);
    }
    @Override
    public ItemStack finishUsingItem (ItemStack pStack, Level level, LivingEntity pLivingEntity) {
//        //CustomDataUsageExample.saveCustomInfo(level, "Hello, Minecraft!");
//        // 读取自定义信息
//        ExitTimeSaveLoadHelper.saveExitTime(level,System.currentTimeMillis());
//        Long info = ExitTimeSaveLoadHelper.loadExitTime(level);
//        System.out.println("Loaded info: " + info);
//
//        UUID playerID = pLivingEntity.getUUID();
//        System.out.println("Players UUID: " + playerID);
//
//        Map<UUID, EffectData> map = MagicalReverseMapSaveLoadHelper.loadEntityHasEffect(level);
//        // 输出读取到的 Map
//        for (Map.Entry<UUID, EffectData> entry : map.entrySet()) {
//            UUID uuid = entry.getKey();
//            EffectData effectData = entry.getValue();
//            System.out.println("UUID: " + uuid + ", Effect Level: " + effectData.getAmplifier() +
//                    ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
//        }
//        MobEffectInstance gas = new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),100,0);
//        pLivingEntity.addEffect(gas);
        //GasPoisonEffect.applyImmortalEffect(pLivingEntity,5.0,0);
//        MobEffectInstance gas1 = new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get(),1200,0);
//        pLivingEntity.addEffect(gas1);
//        BaseImmortalEffect.applyImmortalEffect(pLivingEntity,50.0,0);
        return pStack;
    }
}

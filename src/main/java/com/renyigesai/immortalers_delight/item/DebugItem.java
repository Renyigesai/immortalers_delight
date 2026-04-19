package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.Config;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffectTask;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
//import com.renyigesai.immortalers_delight.util.datautil.EffectData;
//import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
//import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.MagicalReverseMapSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public InteractionResult useOn(UseOnContext pContext) {
        Map<MobEffect, Float[]> map = EffectUtils.getMobEffectWithLevelConfig(Config.EFFECTS_USING_LINEAR_GROWTH.get());
        for (MobEffect effect : map.keySet()) {
            System.out.println(effect.getDescriptionId() + ":" + map.get(effect)[0] + ":" + map.get(effect)[1]);
        }

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

package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffectTask;
import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.MagicalReverseMapSaveLoadHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.UUID;

public class DebugItem extends Item {
    public DebugItem(Properties p_41383_) {
        super(p_41383_);
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
        MobEffectInstance gas = new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),100,0);
        pLivingEntity.addEffect(gas);
        GasPoisonEffect.applyImmortalEffect(pLivingEntity,5.0,0);
//        MobEffectInstance gas1 = new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get(),1200,0);
//        pLivingEntity.addEffect(gas1);
//        BaseImmortalEffect.applyImmortalEffect(pLivingEntity,50.0,0);
        return pStack;
    }
}

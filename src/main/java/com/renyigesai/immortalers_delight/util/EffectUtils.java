package com.renyigesai.immortalers_delight.util;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.*;

public class EffectUtils {
    /**
     *检查指定名称的 MobEffect 是否存在
     * @ param effectName 要检查的 MobEffect 的注册名
     * @ return 如果存在则返回 true, 否则返回 false
     */
//    public static MobEffect get0therModMobEffect(String effectName) {
//// 遍历所有已注册的 MobEffect
//        for (MobEffect effect : ForgeRegistries.MOB_EFFECTS) {
//// 获取当前 MobEffect 的注册名
//            ResourceLocation registryName = Objects. requireNonNull(ForgeRegistries. MOB_EFFECTS. getKey(effect));
////将注册名转换为字符串
//            String currentEffectName = registryName. toString();
////比较注册名和指定的字符串
//            if (currentEffectName. equals(effectName)) {
//                return effect;
//            }
//        }
//        return ImmortalersDelightMobEffect.LINGERING_FLAVOR. get();
//    }

    public static Map<MobEffect, MobEffect> get0therModMobEffect(List<? extends List<?>> source)
    {
        Map<MobEffect, MobEffect> map = new HashMap<>();
        for (List<?> entry : source)
        {
            String inputEffectName = (String) entry.get(0);
            String outputEffectName = (String) entry.get(1);
            MobEffect effectInput = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(inputEffectName));
            MobEffect effectOutput = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(outputEffectName));

            if (effectInput != null && effectOutput != null) map.put(effectInput,effectOutput);
        }
        return map;
    }
}

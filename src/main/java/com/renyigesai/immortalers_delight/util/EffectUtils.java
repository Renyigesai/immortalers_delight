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

     /** 计算经典的调和级数
     * 这个方法会将n作为项数求对应的调和级数(1+1/2+1/3+......的前n项和)
     * 它是快速收敛的，n=5与n=10的计算结果已经非常接近
     * 当输入n为最大值(255)时，结果约为6.039
      */
    public static float convertClassicalHarmonicSeries(int n) {
        // 存储调和级数前n项和
        float sum = 0.0f;

        // 循环累加 1/1 + 1/2 + ... + 1/n
        for (int i = 1; i <= n; i++) {
            sum += 1.0 / i;
        }
        //System.out.println("调和级数前 " + n + " 项和为：" + sum + ", 对应2的次方的数值为：" + (float) Math.pow(2, sum));
        return sum;
    }

     /** 计算平缓的调和级数
     * 这个方法会将n作为项数求对应的调和级数(1+1/2+1/2+1/3+1/3+1/3+......的前n项和)
     * 它是较慢收敛的，同时在n=4以内对于每级翻倍的效果，会使其期望近乎变为线性增长
     * 当输入n为最大值(255)时，结果约为22.086
      */
    public static float convertGradualHarmonicSeries(int n) {
        float sum = 0.0f;  // 数列和
        int k = 1;         // 当前分母：1,2,3...
        int count = 0;     // 记录最后一组不够k项时，剩余需要累加的次数
        int remaining = n; // 还剩下多少项需要计算（初始值 = 要求的前n项）

        // 循环：只要还有剩余项没算完，就继续
        while (remaining > 0) {
            // 如果当前这一组的分母k比剩余项数还多，说明最后一组不够k项，只能取剩下的全部项
            if (k > remaining) count = remaining;
            // 把当前这一整组k项全部减掉（批量处理，效率高）
            remaining -= k;
            // 如果减掉后剩余项数 >=0，说明这一整组都能完整加上
            // 每一组 1/k 加k次，总和正好是 1.0
            if (remaining >= 0) sum += 1.0;
            // 切换下一个分母
            k++;
        }
        // 最后处理：如果有不够一组的剩余项（count>0）
        // 把这些剩余项直接累加：count * (1/(k-1))
        if (count > 0 && k > 1) sum += (float) count / (k - 1);
        //System.out.println("平缓的调和级数前 " + n + " 项和为：" + sum + ", 对应2的次方的数值为：" + (float) Math.pow(2, sum));
        return sum;
    }
}

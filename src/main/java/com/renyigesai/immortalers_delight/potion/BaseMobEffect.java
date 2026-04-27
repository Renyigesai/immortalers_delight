package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.EffectUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BaseMobEffect extends MobEffect {
    public static Map<MobEffect,Float[]> effectsUsingLinearGrowth = new HashMap<MobEffect,Float[]>();

    public static Map<MobEffect,Float[]> effectsUsingClassicalHarmonicSeriesGrowth = new HashMap<MobEffect,Float[]>();

    public static Map<MobEffect,Float[]> effectsUsingGradualHarmonicSeriesGrowth = new HashMap<MobEffect,Float[]>();
    public BaseMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public int getTruthUsingAmplifier(int amplifier) {
        //输出值
        float number = 0;
        //默认值
        int truthAmplifier = amplifier;
        Random random = ImmortalersDelightMod.RANDOM;
        //默认参数
        float add = 0;
        float multiple = 1;
        //读表从config文件获取自定义值
        if (effectsUsingLinearGrowth.containsKey(this)) {
            Float[] tools = effectsUsingLinearGrowth.get(this);
            if (tools[0] != null) {
                add = tools[0];
            }
            if (tools[1] != null) {
                multiple = tools[1];
            }
            //运算
            number = add + multiple * amplifier;
            truthAmplifier = (int) number;
        }
        //读表从config文件获取自定义值
        if (effectsUsingClassicalHarmonicSeriesGrowth.containsKey(this)) {
            Float[] tools = effectsUsingClassicalHarmonicSeriesGrowth.get(this);
            if (tools[0] != null) {
                add = tools[0];
            }
            if (tools[1] != null) {
                multiple = tools[1];
            }
            //运算
            number = add + multiple * EffectUtils.convertClassicalHarmonicSeries(amplifier);
            truthAmplifier = (int) number;
        }
        //读表从config文件获取自定义值
        if (effectsUsingGradualHarmonicSeriesGrowth.containsKey(this)) {
            Float[] tools = effectsUsingGradualHarmonicSeriesGrowth.get(this);
            if (tools[0] != null) {
                add = tools[0];
            }
            if (tools[1] != null) {
                multiple = tools[1];
            }
            //运算
            number = add + multiple * EffectUtils.convertGradualHarmonicSeries(amplifier);
            truthAmplifier = (int) number;
        }
        //取小数
        float decimalPart = number % 1;
        if (random.nextFloat() < decimalPart) truthAmplifier++;

        //校验
        if (truthAmplifier < 0) truthAmplifier = 0;
        return truthAmplifier;
    }
    //将配置文件中的参数应用到效果中
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        int truthAmplifier = getTruthUsingAmplifier(amplifier);
        applyEffectTickInControl(pEntity, truthAmplifier);
    }
    // 拆分工具方法方便子类重写。这里收到的 amplifier 已经是换算后的真实等级，
    // 子类不要在此处再调用 super.applyEffectTick(...)，否则会再次回到本方法形成递归。
    protected void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {

    }

    //将配置文件中的参数应用到效果中
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int truthAmplifier = getTruthUsingAmplifier(amplifier);
        return isDurationEffectTickInControl(duration, truthAmplifier);
    }
    //拆分工具方法方便子类重写
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        return false;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class BaseEffectEvent {
        //初始化
        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void onWorldLoad(LevelEvent.Load event) {
           if (effectsUsingLinearGrowth.isEmpty()) {
               effectsUsingLinearGrowth = EffectUtils.getMobEffectWithLevelConfig(Config.EFFECTS_USING_LINEAR_GROWTH.get());
           }
            if (effectsUsingClassicalHarmonicSeriesGrowth.isEmpty()) {
                effectsUsingClassicalHarmonicSeriesGrowth = EffectUtils.getMobEffectWithLevelConfig(Config.EFFECTS_USING_CLASSICAL_HARMONIC_SERIES_GROWTH.get());
            }
            if (effectsUsingGradualHarmonicSeriesGrowth.isEmpty()) {
                effectsUsingGradualHarmonicSeriesGrowth = EffectUtils.getMobEffectWithLevelConfig(Config.EFFECTS_USING_GRADUAL_HARMONIC_SERIES_GROWTH.get());
            }
        }
    }
}

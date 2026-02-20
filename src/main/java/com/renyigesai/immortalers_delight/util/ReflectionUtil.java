package com.renyigesai.immortalers_delight.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.CoralPlantBlock;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * 反射工具类
 */
public class ReflectionUtil {
    //===========================用于访问珊瑚方块的私有字段deadBlock============================//
    // 缓存反射获取的Field对象（避免重复反射，提升性能）
    private static Field CORAL_DEAD_BLOCK_FIELD;

    static {
        // 静态代码块初始化反射字段（仅执行一次）
        try {
            // 1. 获取CoralBlock类中的deadBlock字段
            CORAL_DEAD_BLOCK_FIELD = CoralBlock.class.getDeclaredField("deadBlock");
            // 2. 突破private访问限制
            CORAL_DEAD_BLOCK_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            // 反射失败时打印异常（避免游戏崩溃，便于调试）
            e.printStackTrace();
        }
    }

    /**
     * 获取CoralBlock实例对应的deadBlock字段值
     * @param coralBlock CoralBlock实例（如原版的珊瑚方块对象）
     * @return 该珊瑚对应的死亡态方块（deadBlock），失败返回null
     */
    @Nullable
    public static Block getCoralDeadBlock(CoralBlock coralBlock) {
        if (CORAL_DEAD_BLOCK_FIELD == null) {
            return null;
        }
        try {
            // 3. 读取私有字段的值
            return (Block) CORAL_DEAD_BLOCK_FIELD.get(coralBlock);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * （未启用）修改CoralBlock的deadBlock字段值
     * @param coralBlock CoralBlock实例
     * @param newDeadBlock 新的死亡态方块
     */
//    public static void setCoralDeadBlock(CoralBlock coralBlock, Block newDeadBlock) {
//        if (CORAL_DEAD_BLOCK_FIELD == null) {
//            return;
//        }
//        try {
//            // 3. 修改私有字段的值
//            CORAL_DEAD_BLOCK_FIELD.set(coralBlock, newDeadBlock);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
    //===========================用于访问珊瑚扇方块的私有字段deadBlock============================//
    // 缓存反射获取的Field对象（避免重复反射，提升性能）
    private static Field CORAL_PLANT_DEAD_BLOCK_FIELD;

    static {
        // 静态代码块初始化反射字段（仅执行一次）
        try {
            // 1. 获取CoralBlock类中的deadBlock字段
            CORAL_PLANT_DEAD_BLOCK_FIELD = CoralPlantBlock.class.getDeclaredField("deadBlock");
            // 2. 突破private访问限制
            CORAL_PLANT_DEAD_BLOCK_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            // 反射失败时打印异常（避免游戏崩溃，便于调试）
            e.printStackTrace();
        }
    }

    /**
     * 获取CoralBlock实例对应的deadBlock字段值
     * @param coralBlock CoralBlock实例（如原版的珊瑚方块对象）
     * @return 该珊瑚对应的死亡态方块（deadBlock），失败返回null
     */
    @Nullable
    public static Block getPlantCoralDeadBlock(CoralPlantBlock coralBlock) {
        if (CORAL_PLANT_DEAD_BLOCK_FIELD == null) {
            return null;
        }
        try {
            // 3. 读取私有字段的值
            return (Block) CORAL_PLANT_DEAD_BLOCK_FIELD.get(coralBlock);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * （未启用）修改CoralPlantBlock的deadBlock字段值
     * @param coralBlock CoralBlock实例
     * @param newDeadBlock 新的死亡态方块
     */
//    public static void setCoralDeadBlock(CoralBlock coralBlock, Block newDeadBlock) {
//        if (CORAL_DEAD_BLOCK_FIELD == null) {
//            return;
//        }
//        try {
//            // 3. 修改私有字段的值
//            CORAL_DEAD_BLOCK_FIELD.set(coralBlock, newDeadBlock);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
}

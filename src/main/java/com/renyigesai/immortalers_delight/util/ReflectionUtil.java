package com.renyigesai.immortalers_delight.util;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.UUID;

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

    //===========================用于访问第二种珊瑚扇方块的私有字段deadBlock============================//
    // 缓存反射获取的Field对象（避免重复反射，提升性能）
    private static Field CORAL_FAN_DEAD_BLOCK_FIELD;

    static {
        // 静态代码块初始化反射字段（仅执行一次）
        try {
            // 1. 获取CoralFanBlock类中的deadBlock字段
            CORAL_FAN_DEAD_BLOCK_FIELD = CoralFanBlock.class.getDeclaredField("deadBlock");
            // 2. 突破private访问限制
            CORAL_FAN_DEAD_BLOCK_FIELD.setAccessible(true);
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
    public static Block getCoralFanDeadBlock(CoralFanBlock coralBlock) {
        if (CORAL_FAN_DEAD_BLOCK_FIELD == null) {
            return null;
        }
        try {
            // 3. 读取私有字段的值
            return (Block) CORAL_FAN_DEAD_BLOCK_FIELD.get(coralBlock);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    //===========================用于访问第二种珊瑚扇方块的私有字段deadBlock============================//
    // 缓存反射获取的Field对象（避免重复反射，提升性能）
    private static Field CORAL_WALL_FAN_DEAD_BLOCK_FIELD;

    static {
        // 静态代码块初始化反射字段（仅执行一次）
        try {
            // 1. 获取CoralWallFanBlock类中的deadBlock字段
            CORAL_WALL_FAN_DEAD_BLOCK_FIELD = CoralWallFanBlock.class.getDeclaredField("deadBlock");
            // 2. 突破private访问限制
            CORAL_WALL_FAN_DEAD_BLOCK_FIELD.setAccessible(true);
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
    public static Block getCoralWallFanDeadBlock(CoralWallFanBlock coralBlock) {
        if (CORAL_WALL_FAN_DEAD_BLOCK_FIELD == null) {
            return null;
        }
        try {
            // 3. 读取私有字段的值
            return (Block) CORAL_WALL_FAN_DEAD_BLOCK_FIELD.get(coralBlock);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    //===========================用于访问Item的私有字段中工具属性两UUID============================//
    public static final UUID BASE_ATTACK_DAMAGE_UUID;
    public static final UUID BASE_ATTACK_SPEED_UUID;

    static {
        UUID damageUuid;
        UUID speedUuid;
        try {
            // ObfuscationReflectionHelper.getPrivateValue 会根据当前运行环境自动在 SRG 名和反混淆名之间匹配
            // 参数1: 目标类 class
            // 参数2: 目标实例 (静态字段传 null)
            // 参数3: 目标字段的 SRG 名称 (开发环境和生产环境通用)
            damageUuid = ObfuscationReflectionHelper.getPrivateValue(Item.class, null, "f_41374_");
            speedUuid  = ObfuscationReflectionHelper.getPrivateValue(Item.class, null, "f_41375_");
        } catch (Exception e) {
            // 提供硬编码回退值（兜底保障，避免因映射异常导致模组崩溃）
            damageUuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
            speedUuid  = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
        }

        BASE_ATTACK_DAMAGE_UUID = damageUuid;
        BASE_ATTACK_SPEED_UUID  = speedUuid;
    }
}

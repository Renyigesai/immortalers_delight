package com.renyigesai.immortalers_delight.api.annotation;


import java.lang.annotation.*;
/**
 * 自定义注解，用于标记和配置物品数据
 * 该注解可以应用于字段上，提供物品的多语言名称、类型、模型等信息
 * 不要改了，需要移到千古乐事使用!!!
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ItemData {
    /**
     * 中文描述
     * @return 物品的中文描述
     */
    String zhCn();
    /**
     * 英文描述（可选，默认为空字符串）
     * @return 物品的英文描述
     * 如果物品英文名就是物品注册名可以省略不写
     */
    String enUs() default "";
    /**
     * 物品类型（可选，默认为ITEM）
     * @return 物品的类型枚举
     */
    ItemType itemType() default ItemType.ITEM;
    /**
     * 模型类型（可选，默认为GENERAL）
     * @return 物品的模型类型枚举
     * 如果是物品可以省略不写
     */
    ModelType model() default ModelType.GENERAL;
    /**
     * 物品分组（可选，默认为"bakeries_main"）
     * @return 物品所属的分组名称
     * 在主创造物品栏中可以省略不写
     */
    String group() default "bakeries_main";

/**
 * 物品类型枚举类
 * 用于定义不同类型的物品分类
 */
    enum ItemType {
    // 普通物品类型
        ITEM,
    // 方块类型
        BLOCK,
    // 自定义物品类型
        CUSTOM_ITEM,
    // 自定义方块物品类型
        CUSTOM_BLOCK,
    }

/**
 * 枚举类型ModelType，用于表示不同的模型类型
 */
    enum ModelType {
    // 通用模型类型
        GENERAL,
    // 块模型类型
        BLOCK,
    // 工具模型类型
        TOOL,
        BREAD,
    // 自定义模型类型
        CUSTOM
    }
}
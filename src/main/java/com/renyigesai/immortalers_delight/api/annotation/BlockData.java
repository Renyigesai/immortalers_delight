package com.renyigesai.immortalers_delight.api.annotation;

import java.lang.annotation.*;
/**
 * 自定义注解，用于标记和配置方块数据
 * 对于需要普通掉落的方块打上@BlockData注解，不需要参数，运行DataGen时会自动处理.
 * 对于需要特殊掉落的方块，可以加上CUSTOM参数，有CUSTOM参数后DataGen不会自动处理，
 * 可手写Json或到BlockLootTable类下的customDrop()方法中填写逻辑.
 * */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BlockData {

    DropType dropType() default DropType.GENERAL;

    enum DropType{
        GENERAL,
        CUSTOM,
    }

}

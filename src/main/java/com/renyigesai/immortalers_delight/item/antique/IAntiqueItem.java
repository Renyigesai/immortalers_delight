package com.renyigesai.immortalers_delight.item.antique;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 古物物品接口。
 * <p>
 * 实现此接口的物品可以放入古董袋，并随装备/卸下触发相应回调，
 * 同时每 tick 对持有者产生效果。
 */
public interface IAntiqueItem {

    /**
     * 返回该古物提供的属性修饰符（例如攻击力、移速加成）。
     * 玩家装备古物时，这些修饰符会应用到玩家身上。
     *
     * @return 属性到修饰符的映射
     */
    Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers();

    /**
     * 古物被放入古物袋时调用。
     * 用于给予玩家初始 buff、播放音效、发射粒子等。
     *
     * @param player 放入古物的玩家
     * @param level  当前世界
     */
    void onPlace(Player player, Level level);

    /**
     * 古物被取出古物袋时调用。
     * 用于移除放入时给予的 buff、清理状态等。
     *
     * @param player 取出古物的玩家
     * @param level  当前世界
     */
    void onTake(Player player, Level level);

    /**
     * 古物在古董袋中时每 tick 调用。
     * 用于持续施加效果、检测条件触发等。
     *
     * @param player 持有古董袋的玩家
     * @param level  当前世界
     */
    void onTick(Player player, Level level);

    default boolean isTick(Player player, Level level){
        return false;
    }
}
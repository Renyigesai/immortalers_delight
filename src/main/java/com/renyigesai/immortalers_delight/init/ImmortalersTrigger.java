package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.advancement.GameplayTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ImmortalersTrigger {
    public static final GameplayTrigger OVERLOAD_SOUL_INFUSER = new GameplayTrigger("overload_soul_infuser");
    public static final GameplayTrigger GLACIER_FIRES_REFUND = new GameplayTrigger("glacier_fires_refund");
    public static final GameplayTrigger COAT_PILLAGER_KNIFE = new GameplayTrigger("coat_pillager_knife");
    public static final GameplayTrigger THROW_BLAZING_OBSIDIAN_WALNUT = new GameplayTrigger("throw_blazing_obsidian_walnut");
    public static final GameplayTrigger BETTER_PIGLIN_BARTERING = new GameplayTrigger("better_piglin_bartering");

    public static void register() {
        CriteriaTriggers.register(OVERLOAD_SOUL_INFUSER);
        CriteriaTriggers.register(GLACIER_FIRES_REFUND);
        CriteriaTriggers.register(COAT_PILLAGER_KNIFE);
        CriteriaTriggers.register(THROW_BLAZING_OBSIDIAN_WALNUT);
        CriteriaTriggers.register(BETTER_PIGLIN_BARTERING);
    }
}

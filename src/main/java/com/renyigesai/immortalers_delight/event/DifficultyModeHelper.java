package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DifficultyModeHelper {

    private static boolean isPowerBattleMode = false;
    @SubscribeEvent
    public static void onPlayerAttackOrHurt(LivingHurtEvent evt) {
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        if (hurtOne instanceof Player player) {
            hurtOne = player;
            if (hurtOne.getMaxHealth() >= 80
                    || ((hurtOne.getAttribute(Attributes.ARMOR) == null ? 0.0F : hurtOne.getAttribute(Attributes.ARMOR).getValue()) >= 30)
                    || ((hurtOne.getAttribute(Attributes.ARMOR_TOUGHNESS) == null ? 0.0F : hurtOne.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()) >= 20)
            ) {
                isPowerBattleMode = true;
            }
        }
        if (evt.getSource().getEntity() instanceof Player player){
            attacker = player;
            if (((attacker.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0.0F : attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) >= 40)
                    || evt.getAmount() > 60) {
                isPowerBattleMode = true;
            }
        }
    }

    public static boolean isPowerBattleMode() {
        if (Config.powerBattleMode != null && Config.powerBattleMode.equals("true")) return true;
        if (Config.powerBattleMode != null && Config.powerBattleMode.equals("false")) return false;
        return isPowerBattleMode;
    }
}

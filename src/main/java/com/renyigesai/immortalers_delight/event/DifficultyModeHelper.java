package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DifficultyModeHelper {

    private static boolean isPowerBattleMode = false;
    private static byte damageProgress = 0;
    private static byte healthProgress = 0;
    private static byte armorProgress = 0;
    private static byte armorToughnessProgress = 0;
    @SubscribeEvent
    public static void onPlayerAttackOrHurt(LivingHurtEvent evt) {
        LivingEntity hurtOne = evt.getEntity();
        Entity attacker = evt.getSource().getEntity();
        if (hurtOne instanceof Player player) {
            if (player.getMaxHealth() >= 200) {
                healthProgress = 2;
            } else if (hurtOne.getMaxHealth() >= 80) {
                healthProgress = 1;
            }
            if (player.getArmorValue() >= 40) {
                armorProgress = 2;
            } else if (player.getArmorValue() >= 30) {
                armorProgress = 1;
            }
            if (player.getAttributeValue(Attributes.ARMOR_TOUGHNESS) >= 20) {
                armorToughnessProgress = 1;
            }
        }
        if (attacker instanceof Player player){
            double damage = player.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0.0F : player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            if (damage >= 100 || evt.getAmount() >= 200) {
                damageProgress = 2;
            } else if (damage >= 40 || evt.getAmount() > 60) {
                damageProgress = 1;
            }
        }
        if (damageProgress + healthProgress + armorProgress + armorToughnessProgress >= 4) {
            isPowerBattleMode = true;
        }
    }

    public static boolean isPowerBattleMode() {
        if (Config.powerBattleMode != null && Config.powerBattleMode.equals("true")) return true;
        if (Config.powerBattleMode != null && Config.powerBattleMode.equals("false")) return false;
        return isPowerBattleMode;
    }
}

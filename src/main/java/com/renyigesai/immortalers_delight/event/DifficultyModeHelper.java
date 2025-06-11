package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.event.entity.EntityMountEvent;
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
        //System.out.println("这里是难度控制");
        if (hurtOne instanceof Player player) {
            if (player.getMaxHealth() >= 200) {
                healthProgress = 2;
                //System.out.println( "玩家的血量等级" + healthProgress);
            } else if (hurtOne.getMaxHealth() >= 80) {
                healthProgress = 1;
                //System.out.println( "玩家的血量等级" + healthProgress);
            }
            if (player.getArmorValue() >= 40) {
                armorProgress = 2;
                //System.out.println( "玩家的护甲等级" + armorProgress);
            } else if (player.getArmorValue() >= 30) {
                armorProgress = 1;
                 //System.out.println( "玩家的护甲等级" + armorProgress);
            }
            if (player.getAttributeValue(Attributes.ARMOR_TOUGHNESS) >= 20) {
                armorToughnessProgress = 1;
            }
        }
        if (attacker instanceof Player player){
            double damage = player.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0.0F : player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            if (damage >= 100 || evt.getAmount() >= 200) {
                damageProgress = 2;
                //System.out.println( "玩家的伤害等级" + damageProgress);
            } else if (damage >= 40 || evt.getAmount() > 60) {
                damageProgress = 1;
                //System.out.println( "玩家的伤害等级" + damageProgress);
            }
        }
         //System.out.println("玩家总得分" +  (damageProgress + healthProgress + armorProgress + armorToughnessProgress));
        if (damageProgress + healthProgress + armorProgress + armorToughnessProgress >= 4) {
            isPowerBattleMode = true;
        }
    }

    // 修改船类的canAddPassenger方法
//    public static boolean canAddPassengerOverride(Boat boat, Entity passenger) {
//        // 原有的乘客添加逻辑
//        boolean originalLogic = boat.getPassengers().size() < 1 && !boat.canBoatInFluid(boat.getEyeInFluidType());
//        // 如果乘客是嗅探兽，允许其成为乘客
//        if (passenger instanceof Sniffer) {
//            return true;
//        }
//        return originalLogic;
//    }
//
//    // 监听实体尝试乘坐船的事件
//    @SubscribeEvent
//    public static void onEntityMount(EntityMountEvent event) {
//        if (event.getEntityBeingMounted() instanceof Boat) {
//            Boat boat = (Boat) event.getEntityBeingMounted();
//            Entity passenger = event.getEntity();
//            // 调用修改后的canAddPassenger方法
//            if (!canAddPassengerOverride(boat, passenger)) {
//                event.setCanceled(true);
//            }
//        }
//    }

    public static boolean isPowerBattleMode() {
        if (Config.powerBattleMode != null && Config.powerBattleMode.equals("true")) return true;
        if (Config.powerBattleMode != null && Config.powerBattleMode.equals("false")) return false;
        return isPowerBattleMode;
    }
}

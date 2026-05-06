package com.renyigesai.immortalers_delight.item.weapon;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@EventBusSubscriber
public class GoldenFabricArmor extends ArmorItem {

    public GoldenFabricArmor(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @SubscribeEvent
    public static void onCreatureHurt(LivingDamageEvent.Pre evt) {
        LivingEntity hurtOne = evt.getEntity();
        ItemStack itemStackHelm = hurtOne.getItemBySlot(EquipmentSlot.HEAD);
        if (!(itemStackHelm.getItem() instanceof GoldenFabricArmor)) return;
        //GasPoisonEffect.removeImmortalEffect(hurtOne);
        hurtOne.removeEffect(ImmortalersDelightMobEffect.GAS_POISON);
        if (evt.getSource().getEntity() instanceof LivingEntity attacker){
            if (attacker == null) return;
            evt.setNewDamage(evt.getNewDamage() * 0.9F);
        }
    }
}

package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GoldenFabricArmor extends ArmorItem {

    public GoldenFabricArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
        LivingEntity hurtOne = evt.getEntity();
        ItemStack itemStackHelm = hurtOne.getItemBySlot(EquipmentSlot.HEAD);
        if (!(itemStackHelm.getItem() instanceof GoldenFabricArmor)) return;
        GasPoisonEffect.removeImmortalEffect(hurtOne);
        hurtOne.removeEffect(ImmortalersDelightMobEffect.GAS_POISON.get());
        if (evt.getSource().getEntity() instanceof LivingEntity attacker){
            if (attacker == null) return;
            evt.setAmount(evt.getAmount() * 0.9F);
        }
    }
}

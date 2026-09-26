package com.renyigesai.immortalers_delight.item.weapon;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class GoldenFabricArmor extends ArmorItem {

    public GoldenFabricArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack,pLevel,pTooltip,pFlag);
        pTooltip.add(Component.translatable("tooltip.immortalers_delight." + this).withStyle(ChatFormatting.YELLOW));
    }
    //实现类似属性修饰符的显示
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {

        if (event.getItemStack().is(ImmortalersDelightItems.GOLDEN_FABRIC_VEIL.get())) {

            List<Component> tooltip = event.getToolTip();
            // 要插入的文本
            Component customLine = DifficultyModeUtil.isPowerBattleMode()
                    ? Component.translatable("tooltip.immortalers_delight.golden_fabric_veil_p").withStyle(ChatFormatting.BLUE)
                    : Component.translatable("tooltip.immortalers_delight.golden_fabric_veil_n").withStyle(ChatFormatting.BLUE);

            //属性修饰符
            // 遍历列表，找到所有属性修饰符标题行
            for (int i = 0; i < tooltip.size(); i++) {
                Component comp = tooltip.get(i);
                // 检测是否为 TranslatableComponent，且键以 "item.modifiers." 开头
                if (comp.getContents() instanceof net.minecraft.network.chat.contents.TranslatableContents translatable) {
                    String key = translatable.getKey();
                    if (key != null && key.startsWith("item.modifiers.")) {
                        // 在标题行之后插入一行（注意位置：i+1）
                        // 但要注意防止插入后导致索引错乱，故从后往前插入或用迭代器
                        tooltip.add(i + 1, customLine);
                        i++; // 跳过刚插入的行，避免死循环
                    }
                }
            }
        }

    }

    //通用效果
    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
        LivingEntity hurtOne = evt.getEntity();
        ItemStack itemStackHelm = hurtOne.getItemBySlot(EquipmentSlot.HEAD);
        if (!(itemStackHelm.getItem() instanceof GoldenFabricArmor)) return;
        //GasPoisonEffect.removeImmortalEffect(hurtOne);
        hurtOne.removeEffect(ImmortalersDelightMobEffect.GAS_POISON.get());
        if (evt.getSource().getEntity() instanceof LivingEntity attacker){

            evt.setAmount(evt.getAmount() * 0.9F);
        }
    }

    //超凡模式减伤
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if ( DifficultyModeUtil.isPowerBattleMode()) {
            if (event.getSource().is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) return;

            boolean fullSet = event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem()
                    instanceof GoldenFabricArmor;

            if (fullSet) {
                float originalDamage = event.getAmount();
                float reducedDamage = originalDamage * 0.82f;
                event.setAmount(reducedDamage);
            }
        }
    }
}

package com.renyigesai.immortalers_delight.item.weapon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class BakaArmor extends ArmorItem {
    public BakaArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity,
                              int slot, boolean selected) {
        //耐久自恢复
        if (!level.isClientSide
                && entity instanceof LivingEntity living
                && living.tickCount % 40 == 0
                && stack.getDamageValue() > 0) {
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - 4));
        }

        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;
        if (player.getItemBySlot(EquipmentSlot.HEAD) != stack) return;

        //提供夜视效果，解除黑暗和失明效果
        //超凡模式下，提供永续饱食度回血(无视禁疗)
        if (player.tickCount % 10 == 0) {

            player.addEffect(new MobEffectInstance(
                    MobEffects.NIGHT_VISION,310,0,true,false,false
            ));

            if (DifficultyModeUtil.isPowerBattleMode()) {

                if (player.tickCount % 80 == 0) player.heal(1);

                float currentHealth = player.getHealth();
                float maxHealth = player.getMaxHealth();

                if (currentHealth < maxHealth && currentHealth > 0) {
                    float newHealth = Math.min(currentHealth + 0.125F, maxHealth);
                    player.setHealth(newHealth);
                }

            }

        }


        if (player.hasEffect(MobEffects.BLINDNESS)) {
            player.removeEffect(MobEffects.BLINDNESS);
        }
        if (player.hasEffect(MobEffects.DARKNESS)) {
            player.removeEffect(MobEffects.DARKNESS);
        }
    }
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        Multimap<Attribute, AttributeModifier> old = super.getDefaultAttributeModifiers(equipmentSlot);

        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (!isPowerful) {
            for (Map.Entry<Attribute, AttributeModifier> entry : old.entries()) {
                if (entry.getKey() == Attributes.ARMOR) {
                    AttributeModifier newOne = new AttributeModifier(
                            entry.getValue().getId(),
                            entry.getValue().getName(),
                            entry.getValue().getAmount() - 2,
                            entry.getValue().getOperation()
                    );
                    multimap.put(entry.getKey(),newOne);
                }else if (entry.getKey() == Attributes.ARMOR_TOUGHNESS) {
                    AttributeModifier newOne = new AttributeModifier(
                            entry.getValue().getId(),
                            entry.getValue().getName(),
                            entry.getValue().getAmount() - 1,
                            entry.getValue().getOperation()
                    );
                    multimap.put(entry.getKey(),newOne);
                }else if(entry.getKey() == Attributes.KNOCKBACK_RESISTANCE){
                    AttributeModifier newOne = new AttributeModifier(
                            entry.getValue().getId(),
                            entry.getValue().getName(),
                            entry.getValue().getAmount() - 0.5,
                            entry.getValue().getOperation()
                    );
                    multimap.put(entry.getKey(),newOne);
                } else {
                    multimap.put(entry.getKey(),entry.getValue());
                }
            }
            return multimap;
        }

        return old;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack,pLevel,pTooltip,pFlag);
        pTooltip.add(Component.translatable("tooltip.immortalers_delight." + this).withStyle(ChatFormatting.BLUE));
        if (DifficultyModeUtil.isPowerBattleMode()){
//            pTooltip.add(Component.translatable("tooltip.immortalers_delight." + this + "_n").withStyle(ChatFormatting.YELLOW));
            pTooltip.add(Component.translatable("tooltip.immortalers_delight." + this + "_p").withStyle(ChatFormatting.YELLOW));
        }
    }
}

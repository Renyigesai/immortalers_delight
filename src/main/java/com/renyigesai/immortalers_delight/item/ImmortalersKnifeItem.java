package com.renyigesai.immortalers_delight.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.event.ImmortalersDelightEvent;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class ImmortalersKnifeItem extends KnifeItem {
    public static final String ANCIENT_KNIFE_COMBO_SKILL = ImmortalersDelightMod.MODID + "_ancient_knife_combo_skill";
    private int type_id;
    private final float attackDamage;
    private final float attackSpeed;
    public int getTypeId() {return this.type_id;}
    public ImmortalersKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        this.type_id = type;
        this.attackDamage = attackDamage + tier.getAttackDamageBonus();
        this.attackSpeed = attackSpeed;
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        int type = this.type_id;
        if (type == 2) {
            if (enchantment == Enchantments.MOB_LOOTING) level += isPowerful ? 4 : 2;
        }
        return level;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double)this.attackDamage + (isPowerful ? 2.0F : 0), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)attackSpeed, AttributeModifier.Operation.ADDITION));
            return multimap;
        }
//        else if (equipmentSlot == EquipmentSlot.OFFHAND) {
//            boolean flag = stack.getOrCreateTag().contains(ANCIENT_KNIFE_COMBO_SKILL, Tag.TAG_BYTE)
//                    && stack.getOrCreateTag().getBoolean(ANCIENT_KNIFE_COMBO_SKILL);
//            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", flag ? (isPowerful ? 2.125F : 1.333F) : 0, AttributeModifier.Operation.MULTIPLY_TOTAL));
//            return multimap;
//        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            if (this.type_id == 1) {
                MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }
            if (this.type_id == 2) {
                for (int i = 0; i < 3; i++) {
                    MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this + "." + ((i == 2 && DifficultyModeHelper.isPowerBattleMode()) ? "power" : i), new Object[0]);
                    if(i == 1) {
                        tooltip.add(textEmpty.withStyle(ChatFormatting.GRAY));
                    } else if (i == 2) {
                        tooltip.add(textEmpty.withStyle(ChatFormatting.DARK_GREEN));
                    } else {
                        tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
                    }
                }
            }
        }

    }

//    @Override
//    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
//        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
//        System.out.println("这里是inventoryTick方法，当前物品为：" + ForgeRegistries.ITEMS.getKey(pStack.getItem()));
//        if (pStack.getItem() instanceof ImmortalersKnifeItem immKnife && immKnife.getTypeId() == 2) {
//            if (pEntity instanceof LivingEntity livingEntity) {
//                if (pIsSelected && !livingEntity.level().isClientSide()) {
//                    CompoundTag tag = pStack.getOrCreateTag();
//                    if (livingEntity.getItemInHand(InteractionHand.MAIN_HAND) == pStack) {
//                        tag.putBoolean(ANCIENT_KNIFE_COMBO_SKILL,true);
//                        if (livingEntity.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ImmortalersKnifeItem immKnife1
//                                && immKnife1.getTypeId() == 2) {
//                            livingEntity.getItemInHand(InteractionHand.OFF_HAND).getOrCreateTag().putBoolean(ANCIENT_KNIFE_COMBO_SKILL,true);
//                        }
//                    }
//
//                    if (!tag.contains(ANCIENT_KNIFE_COMBO_SKILL, Tag.TAG_BYTE)) {
//                        tag.putBoolean(ANCIENT_KNIFE_COMBO_SKILL,false);
//                    } else if (tag.getBoolean(ANCIENT_KNIFE_COMBO_SKILL)) {
//                        if (!(livingEntity.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ImmortalersKnifeItem immKnife2
//                                && immKnife2.getTypeId() == 2)) {
//                            tag.putBoolean(ANCIENT_KNIFE_COMBO_SKILL,false);
//                        }
//                    }
//                }
//            }
//        }
//    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class ImmortalersKnifeEvents {
        @SubscribeEvent
        public static void ImmortalersKnifeAttack(LivingHurtEvent event) {
            if (event.isCanceled()) return;

            boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
            LivingEntity hurtOne = event.getEntity();
            if (hurtOne.level().isClientSide) return;

            if (event.getSource().getEntity() instanceof LivingEntity attacker){
                ItemStack toolStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                if (!toolStack.isEmpty() && toolStack.getItem() instanceof ImmortalersKnifeItem knife) {
                    if (knife.getTypeId() == 1) {
                        hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.WEAK_WITHER.get(), 320, isPowerful ? 2 : 1));
                    }
                    if (knife.getTypeId() == 2) {
                        ItemStack toolStack2 = attacker.getItemInHand(InteractionHand.OFF_HAND);
                        if (!toolStack2.isEmpty() && toolStack2.is(ImmortalersDelightItems.ANCIENT_BLADE.get())) {
                            float buffer = event.getAmount() * 0.6F;
                            if (!isPowerful && buffer > 4) buffer = 4;
                            event.setAmount(event.getAmount() + buffer);
                            if (attacker instanceof ServerPlayer serverPlayer && !serverPlayer.getAbilities().instabuild) {
                                toolStack2.hurtAndBreak(1, serverPlayer, (action) -> {
                                    action.broadcastBreakEvent(InteractionHand.OFF_HAND);
                                });
                            }
                        }
                    }
                }
            }

        }

    }
}

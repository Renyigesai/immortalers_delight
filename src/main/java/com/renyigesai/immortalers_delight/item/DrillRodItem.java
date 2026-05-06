package com.renyigesai.immortalers_delight.item;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

import static net.neoforged.neoforge.common.Tags.Items.ENCHANTING_FUELS;

public class DrillRodItem extends DiggerItem {
    private final TagKey<Block> extraBlocks;
    private final int tooltipCount;
    public DrillRodItem(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, TagKey<Block> pBlocks, TagKey<Block> pExtraBlocks, Properties pProperties, int tooltipCount) {
        super(pTier, pBlocks, ImmortalersDelightItems.withTierToolAttributes(pProperties, pTier, pAttackDamageModifier, pAttackSpeedModifier));
        this.extraBlocks = pExtraBlocks;
        this.tooltipCount = tooltipCount;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        float speed = super.getDestroySpeed(pStack, pState);
        return pState.is(this.extraBlocks) ? Math.max(speed, this.getTier().getSpeed()) : speed;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers base = super.getDefaultAttributeModifiers(stack);
        if (!DifficultyModeUtil.isPowerBattleMode()) {
            return base;
        }
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        for (ItemAttributeModifiers.Entry entry : base.modifiers()) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }
        builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "drill_power_bonus"), 4.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        return builder.build();
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack pStack, Level pLevel, @NotNull BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            if (pEntityLiving.getOffhandItem().is(ENCHANTING_FUELS)) {
                pEntityLiving.getOffhandItem().shrink(1);
            } else {
                pStack.hurtAndBreak(1, pEntityLiving, EquipmentSlot.MAINHAND);
            }

            if (pEntityLiving.getHealth() > 0.7 * pEntityLiving.getMaxHealth()) {
                if (pEntityLiving.getMaxHealth() < 20) {
                    pEntityLiving.addEffect(new MobEffectInstance(MobEffects.HARM ,1));
                } else {
                    pEntityLiving.hurt(pLevel.damageSources().magic(), (float) (pEntityLiving.getHealth() - 0.7 * pEntityLiving.getMaxHealth()));
                }

            }
        }

        return true;
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.SMITE)
                || enchantment.is(Enchantments.BANE_OF_ARTHROPODS)
                || enchantment.is(Enchantments.FIRE_ASPECT)
                || enchantment.is(Enchantments.LOOTING)) {
            return true;
        }
        if (enchantment.is(Enchantments.MENDING)) {
            return false;
        }
        return enchantment.value().canEnchant(stack);
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (enchantment.is(Enchantments.FORTUNE)) level += isPowerful ? 15 : 5;
        if (enchantment.is(Enchantments.LOOTING)) level += isPowerful ? 10 : 4;
        return level;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.tooltipCount > 0) {
                for (int i = 0; i < this.tooltipCount; i++) {
                    MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this + "." + i, new Object[0]);
                    tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
                }
            }
        }

        super.appendHoverText(stack, context, tooltip, isAdvanced);
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class DrillRodEvents {
        @SubscribeEvent
        public static void drillRodArmorShatterAttack(LivingDamageEvent.Pre event) {
            LivingEntity hurtOne = event.getEntity();
            LivingEntity attacker = event.getEntity().getKillCredit();
            ItemStack toolStack = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (!toolStack.isEmpty() && toolStack.getItem() instanceof DrillRodItem) {
                boolean noArmor = !hurtOne.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                        || !hurtOne.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                        || !hurtOne.getItemBySlot(EquipmentSlot.LEGS).isEmpty()
                        || !hurtOne.getItemBySlot(EquipmentSlot.FEET).isEmpty();
                if (attacker != null && attacker.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                    if (noArmor) {
                        event.setNewDamage((float) (event.getNewDamage() + (isPowerful ? 10 + attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F : 5)));
                        hurtOne.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50 + hurtOne.getRandom().nextInt(11), 3));
                    } else if (hurtOne.getArmorValue() > 10) {
                        event.setNewDamage((float) (event.getNewDamage() + (isPowerful ? 15 + attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75F : 7.5)));
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onKnifeKnockback(LivingKnockBackEvent event) {
            LivingEntity backOne = event.getEntity();
            LivingEntity attacker = event.getEntity().getKillCredit();
            ItemStack toolStack = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
            if (!toolStack.isEmpty() && toolStack.getItem() instanceof DrillRodItem) {
                double resistance = backOne.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                if (resistance < 1.0D) {
                    float extractKnockback = (1.0F / (1.0F - (float) resistance)) > 4 ? 4 : (1.0F / (1.0F - (float) resistance));
                    event.setStrength(event.getOriginalStrength() + extractKnockback);
                }
            }

        }
    }
}

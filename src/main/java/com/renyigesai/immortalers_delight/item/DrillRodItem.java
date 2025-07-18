package com.renyigesai.immortalers_delight.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static net.minecraftforge.common.Tags.Items.ENCHANTING_FUELS;

public class DrillRodItem extends DiggerItem {
    private final TagKey<Block> extraBlocks;
    private final int tooltipCount;
    private final float attackDamage;
    private final float attackSpeed;
    public DrillRodItem(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, TagKey<Block> pBlocks, TagKey<Block> pExtraBlocks, Properties pProperties, int tooltipCount) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, pBlocks, pProperties);
        this.extraBlocks = pExtraBlocks;
        this.tooltipCount = tooltipCount;
        this.attackDamage = pAttackDamageModifier + pTier.getAttackDamageBonus();
        this.attackSpeed = pAttackSpeedModifier;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(this.extraBlocks) ? this.speed : super.getDestroySpeed(pStack, pState);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double)this.attackDamage + (isPowerful ? 4.0F : 0), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)attackSpeed, AttributeModifier.Operation.ADDITION));
            return multimap;
        } else return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack pStack, Level pLevel, @NotNull BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            if (pEntityLiving.getOffhandItem().is(ENCHANTING_FUELS)) {
                pEntityLiving.getOffhandItem().shrink(1);
            } else {
                pStack.hurtAndBreak(1, pEntityLiving, (user) -> {
                    user.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
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

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        Set<Enchantment> ALLOWED_ENCHANTMENTS = Sets.newHashSet(
                Enchantments.SMITE,
                Enchantments.BANE_OF_ARTHROPODS,
                Enchantments.FIRE_ASPECT,
                Enchantments.MOB_LOOTING);
        if (ALLOWED_ENCHANTMENTS.contains(enchantment)) {
            return true;
        } else {
            Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(
                    Enchantments.MENDING
            );
            return DENIED_ENCHANTMENTS.contains(enchantment) ? false : enchantment.category.canEnchant(stack.getItem());
        }
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
        boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
        if (enchantment == Enchantments.BLOCK_FORTUNE) level += isPowerful ? 15 : 5;
        if (enchantment == Enchantments.MOB_LOOTING) level += isPowerful ? 10 : 4;
        return level;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            if (this.tooltipCount > 0) {
                for (int i = 0; i < this.tooltipCount; i++) {
                    MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this + "." + i, new Object[0]);
                    tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
                }
            }
        }

    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class DrillRodEvents {
        @SubscribeEvent
        public static void drillRodArmorShatterAttack(LivingHurtEvent event) {
            LivingEntity hurtOne = event.getEntity();
            LivingEntity attacker = event.getEntity().getKillCredit();
            ItemStack toolStack = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
            boolean isPowerful = DifficultyModeHelper.isPowerBattleMode();
            if (!toolStack.isEmpty() && toolStack.getItem() instanceof DrillRodItem) {
                boolean noArmor = !hurtOne.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                        || !hurtOne.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                        || !hurtOne.getItemBySlot(EquipmentSlot.LEGS).isEmpty()
                        || !hurtOne.getItemBySlot(EquipmentSlot.FEET).isEmpty();
                if (noArmor) {
                    event.setAmount((float) (event.getAmount() + (isPowerful ? 10 + attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F : 5)));
                    hurtOne.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50 + hurtOne.getRandom().nextInt(11), 3));
                } else if (hurtOne.getArmorValue() > 10) {
                    event.setAmount((float) (event.getAmount() + (isPowerful ? 15 + attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75F : 7.5)));
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

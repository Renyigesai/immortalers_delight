package com.renyigesai.immortalers_delight.item;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PillagersKnifeItem extends ImmortalersKnifeItem{
    public PillagersKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(type, tier, attackDamage, attackSpeed, properties);
    }

    public PillagersKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, float extra_attackDamage, float extra_attackSpeed, Properties properties) {
        super(type, tier, attackDamage, attackSpeed, extra_attackDamage, extra_attackSpeed, properties);
    }

    public @NotNull ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON);
    }

    /**
     * Allows items to add custom lines of information to the mouseover description.
     */
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if ((Boolean) Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
            pTooltip.add(textEmpty.withStyle(ChatFormatting.DARK_PURPLE));
            PotionUtils.addPotionTooltip(pStack, pTooltip, 0.125F);
        }
        if (this.type_id == PILLAGER_KNIFE_TYPE) {
            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this + ".default_enchantment." + (DifficultyModeUtil.isPowerBattleMode() ? "power." + 1 : 1), new Object[0]);
            pTooltip.add(textEmpty.withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
//    public String getDescriptionId(ItemStack pStack) {
//        return PotionUtils.getPotion(pStack).getName(this.getDescriptionId() + ".effect.");
//    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        boolean b = super.hurtEnemy(stack, target, attacker);
        if (b && !target.level().isClientSide()) {
            Potion potion = Potions.EMPTY;
            Set<MobEffectInstance> effects = Sets.newHashSet();

            potion = PotionUtils.getPotion(stack);
            Collection<MobEffectInstance> collection = PotionUtils.getCustomEffects(stack);
            if (!collection.isEmpty()) {
                for(MobEffectInstance mobeffectinstance : collection) {
                    effects.add(new MobEffectInstance(mobeffectinstance));
                }
            }

            for(MobEffectInstance mobeffectinstance : potion.getEffects()) {
                target.addEffect(new MobEffectInstance(
                        mobeffectinstance.getEffect(),
                        Math.max(mobeffectinstance.mapDuration((p_268168_) -> {return p_268168_ / 8;}), 1),
                        mobeffectinstance.getAmplifier(),
                        mobeffectinstance.isAmbient(),
                        mobeffectinstance.isVisible()),
                        attacker);
            }

            if (!effects.isEmpty()) {
                for(MobEffectInstance mobeffectinstance1 : effects) {
                    target.addEffect(mobeffectinstance1, attacker);
                }
            }
        }
        return b;
    }

}

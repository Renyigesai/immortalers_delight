package com.renyigesai.immortalers_delight.item;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PillagersKnifeItem extends ImmortalersKnifeItem{
    public static final String MAX_POTION_COUNT = "potion_coating_count";
    public static final String USED_POTION_COUNT = "pillagers_knife_attack_count";
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
        if (pStack.getOrCreateTag().contains(MAX_POTION_COUNT) && pStack.getOrCreateTag().getInt(MAX_POTION_COUNT) > 0) {
            int max = pStack.getOrCreateTag().getInt(MAX_POTION_COUNT);
            int progress = max - (pStack.getOrCreateTag().contains(USED_POTION_COUNT) ? pStack.getOrCreateTag().getInt(USED_POTION_COUNT) : 0);
            MutableComponent textValue = Component.translatable(
                    "tooltip." +ImmortalersDelightMod.MODID+ ".potion_coating_count", // 翻译键
                    progress,
                    max// 替换%d占位符
            );
            pTooltip.add(textValue.withStyle(ChatFormatting.YELLOW));
        }
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
        int damageCount = stack.getDamageValue();
        boolean b = super.hurtEnemy(stack, target, attacker);
        if (b && !target.level().isClientSide() && stack.getDamageValue() != damageCount) {
            CompoundTag tag = stack.getOrCreateTag();
            if (PotionUtils.getPotion(tag) != Potions.EMPTY || PotionUtils.getCustomEffects(stack).size() > 0) {
                if (!tag.contains(MAX_POTION_COUNT)) {
                    tag.putInt(MAX_POTION_COUNT, 8);
                }
                if (tag.contains(USED_POTION_COUNT)) {
                    int count = tag.getInt(USED_POTION_COUNT);
                    tag.putInt(USED_POTION_COUNT, count + 1);
                    if (count >= tag.getInt(MAX_POTION_COUNT)) {
                        stack.removeTagKey(USED_POTION_COUNT);
                        stack.removeTagKey(MAX_POTION_COUNT);
                        PotionUtils.setPotion(stack, Potions.EMPTY);
                        stack.removeTagKey("CustomPotionEffects");
                    }
                } else {
                    tag.putInt(USED_POTION_COUNT, 1);
                }
            }
        }
        return b;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class ImmortalersKnifeEvents {
        @SubscribeEvent
        public static void PillagersKnifeAttack(LivingHurtEvent event) {
            if (event.isCanceled()) return;

            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity target = event.getEntity();
            if (target.level().isClientSide) return;

            if (event.getSource().getEntity() instanceof LivingEntity attacker){
                ItemStack stack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                if (!stack.isEmpty() && stack.getItem() instanceof PillagersKnifeItem knife) {
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
                        if (mobeffectinstance.getEffect().isInstantenous()) target.invulnerableTime = 0;
                    }

                    if (!effects.isEmpty()) {
                        for(MobEffectInstance mobeffectinstance1 : effects) {
                            target.addEffect(mobeffectinstance1, attacker);
                            if (mobeffectinstance1.getEffect().isInstantenous()) target.invulnerableTime = 0;
                        }
                    }
                }
            }
        }
    }

}

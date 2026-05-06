package com.renyigesai.immortalers_delight.item.weapon;
import net.neoforged.fml.common.EventBusSubscriber;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.item.ImmortalersKnifeItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PillagersKnifeItem extends ImmortalersKnifeItem {
    public static final String MAX_POTION_COUNT = "potion_coating_count";
    public static final String USED_POTION_COUNT = "pillagers_knife_attack_count";

    public static boolean hasPotionCoating(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            return false;
        }
        if (!contents.customEffects().isEmpty()) {
            return true;
        }
        return contents.potion().filter(h -> !h.is(Potions.WATER)).isPresent();
    }
    public PillagersKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(type, tier, attackDamage, attackSpeed, properties);
    }

    public PillagersKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, float extra_attackDamage, float extra_attackSpeed, Properties properties) {
        super(type, tier, attackDamage, attackSpeed, extra_attackDamage, extra_attackSpeed, properties);
    }

    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
        return stack;
    }

    /**
     * Allows items to add custom lines of information to the mouseover description.
     */
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltip, TooltipFlag pFlag) {
        CompoundTag tag = pStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(MAX_POTION_COUNT) && tag.getInt(MAX_POTION_COUNT) > 0) {
            int max = tag.getInt(MAX_POTION_COUNT);
            int progress = max - (tag.contains(USED_POTION_COUNT) ? tag.getInt(USED_POTION_COUNT) : 0);
            MutableComponent textValue = Component.translatable(
                    "tooltip." +ImmortalersDelightMod.MODID+ ".potion_coating_count", // 翻译键
                    progress,
                    max// 替换%d占位符
            );
            pTooltip.add(textValue.withStyle(ChatFormatting.YELLOW));
        }
        if (Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
            pTooltip.add(textEmpty.withStyle(ChatFormatting.DARK_PURPLE));
            PotionContents contents = pStack.get(DataComponents.POTION_CONTENTS);
            if (contents != null) {
                contents.addPotionTooltip(pTooltip::add, 0.125F, 20.0F);
            }
        }
        if (this.type_id == PILLAGER_KNIFE_TYPE) {
            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this + ".default_enchantment." + (DifficultyModeUtil.isPowerBattleMode() ? "power." + 1 : 1), new Object[0]);
            pTooltip.add(textEmpty.withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(pStack, context, pTooltip, pFlag);
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
            if (hasPotionCoating(stack)) {
                CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                if (!tag.contains(MAX_POTION_COUNT)) {
                    tag.putInt(MAX_POTION_COUNT, 8);
                }
                if (tag.contains(USED_POTION_COUNT)) {
                    int count = tag.getInt(USED_POTION_COUNT);
                    tag.putInt(USED_POTION_COUNT, count + 1);
                    if (count >= tag.getInt(MAX_POTION_COUNT)) {
                        tag.remove(MAX_POTION_COUNT);
                        tag.remove(USED_POTION_COUNT);
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        stack.remove(DataComponents.POTION_CONTENTS);
                    } else {
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    }
                } else {
                    tag.putInt(USED_POTION_COUNT, 1);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
        }
        return b;
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class ImmortalersKnifeEvents {
        @SubscribeEvent
        public static void PillagersKnifeAttack(LivingDamageEvent.Pre event) {

            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity target = event.getEntity();
            if (target.level().isClientSide) return;

            if (event.getSource().getEntity() instanceof LivingEntity attacker){
                ItemStack stack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                if (!stack.isEmpty() && stack.getItem() instanceof PillagersKnifeItem knife) {
                    PotionContents pc = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                    Holder<Potion> potionHolder = pc.potion().orElse(Potions.WATER);
                    Set<MobEffectInstance> effects = Sets.newHashSet();

                    Collection<MobEffectInstance> collection = pc.customEffects();
                    if (!collection.isEmpty()) {
                        for(MobEffectInstance mobeffectinstance : collection) {
                            effects.add(new MobEffectInstance(mobeffectinstance));
                        }
                    }

                    for(MobEffectInstance mobeffectinstance : potionHolder.value().getEffects()) {
                        target.addEffect(new MobEffectInstance(
                        mobeffectinstance.getEffect(),
                        Math.max(mobeffectinstance.mapDuration((p_268168_) -> {return p_268168_ / 8;}), 1),
                        mobeffectinstance.getAmplifier(),
                        mobeffectinstance.isAmbient(),
                        mobeffectinstance.isVisible()),
                        attacker);
                        if (mobeffectinstance.getEffect().value().isInstantenous()) target.invulnerableTime = 0;
                    }

                    if (!effects.isEmpty()) {
                        for(MobEffectInstance mobeffectinstance1 : effects) {
                            target.addEffect(mobeffectinstance1, attacker);
                            if (mobeffectinstance1.getEffect().value().isInstantenous()) target.invulnerableTime = 0;
                        }
                    }
                }
            }
        }
    }

}

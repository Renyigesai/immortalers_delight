package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.potion.CulturalLegacyMobEffect;
import com.renyigesai.immortalers_delight.potion.VulnerableMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ImmortalersHammerItem extends DiggerItem {

    protected final float attackDamage;
    protected final float attackSpeed;
    protected final float extra_attackDamage;
    protected final float extra_attackSpeed;
    protected int type_id;

    public static final int[] GIDDINESS_TIME = new int[]{16, 20, 24, 28, 32, 36, 40, 44};
    public ImmortalersHammerItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, ImmortalersDelightTags.MINEABLE_HAMMER, pProperties);
        this.attackDamage = pAttackDamageModifier + pTier.getAttackDamageBonus();
        this.attackSpeed = pAttackSpeedModifier;
        this.extra_attackDamage = 0;
        this.extra_attackSpeed = 0;
    }

    public ImmortalersHammerItem(int type, Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, float pExtraAttackDamage, float pExtraAttackSpeed, Properties pProperties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, ImmortalersDelightTags.MINEABLE_HAMMER, pProperties);
        this.attackDamage = pAttackDamageModifier + pTier.getAttackDamageBonus();
        this.attackSpeed = pAttackSpeedModifier;
        this.extra_attackDamage = pExtraAttackDamage;
        this.extra_attackSpeed = pExtraAttackSpeed;
        this.type_id = type;
    }

    /**
     * 物品的破坏速度，对于松肉锤，挖掘硬度大于其挖掘等级的方块时，其效率会减半
     * @param pStack
     * @param pState
     * @return
     */
    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        float speed = super.getDestroySpeed(pStack, pState);
        if (speed > 1.0F && pState.getBlock().defaultDestroyTime() > this.getTier().getLevel()) {
            speed *= 0.5F;
        }
        return speed;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getBlock() == Blocks.MELON) {
            //System.out.println("Melon");
        }

        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {

        if (stack.getItem() instanceof ImmortalersHammerItem) {
            MutableComponent textValue1 = Component.translatable(
                    "tooltip." +ImmortalersDelightMod.MODID+ ".hammers_skill_title_1"
            );
            MutableComponent textValue2 = Component.translatable(
                    "tooltip." +ImmortalersDelightMod.MODID+ ".hammers_skill_title_2"
            );

            if (Screen.hasShiftDown()) {
                MutableComponent textValueS1 = Component.translatable(
                        "tooltip." +ImmortalersDelightMod.MODID+ ".hammers_skill_1"
                );
                tooltip.add(textValue1.withStyle(ChatFormatting.YELLOW));
                tooltip.add(textValueS1.withStyle(ChatFormatting.GRAY));
                MutableComponent textValueS2 = Component.translatable(
                        "tooltip." +ImmortalersDelightMod.MODID+ ".hammers_skill_2"
                );
                tooltip.add(textValue2.withStyle(ChatFormatting.YELLOW));
                tooltip.add(textValueS2.withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(textValue1.withStyle(ChatFormatting.YELLOW));
                tooltip.add(textValue2.withStyle(ChatFormatting.YELLOW));
            }
        }
        super.appendHoverText(stack, level, tooltip, isAdvanced);
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class ImmortalersHammerEvents {
        //玩家使用松肉锤满蓄力攻击，可以使生物眩晕。对已眩晕的生物再次攻击无效。
        @SubscribeEvent
        public static void PlayerDizziness(AttackEntityEvent event) {
            if (event.getEntity() == null || event.getTarget() == null) return;
            if (!event.getEntity().level().isClientSide() && event.getTarget() instanceof LivingEntity hurtOne) {
                Player attacker = event.getEntity();

                ItemStack toolStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                if (toolStack.getItem() instanceof ImmortalersHammerItem hammer) {
                    float attackInterval = attacker.getAttackStrengthScale(0.5F);
                    if (attackInterval >= 0.99F){
                        StunEffect.applyImmortalEffect(hurtOne, GIDDINESS_TIME[hammer.getTier().getLevel()], 0);
                    }
                }
            }

        }

        @SubscribeEvent
        public static void VulnerableAndMobDizziness(LivingDamageEvent event) {
            if (event.getEntity() == null || event.getSource().getEntity() == null) return;
            LivingEntity hurtOne = event.getEntity();
            if (!hurtOne.level().isClientSide() && event.getSource().getEntity() instanceof LivingEntity attacker) {
                if (attacker.getMainHandItem().getItem() instanceof ImmortalersHammerItem hammer) {
                    //易伤
//                    VulnerableMobEffect.addEffectWithFrequencyLimit(
//                            hurtOne,
                            hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.VULNERABLE.get(), GIDDINESS_TIME[hammer.getTier().getLevel()] + 50, 1));
//                            (byte) 1
//                    );

                    //非玩家攻击眩晕
                    if (StunEffect.getEntityMap().get(hurtOne.getUUID()) != null || attacker instanceof Player) return;
                    float f = (float) GIDDINESS_TIME[hammer.getTier().getLevel()] / 50;
                    if (attacker.getRandom().nextFloat() < f) {
                        StunEffect.applyImmortalEffect(hurtOne, GIDDINESS_TIME[hammer.getTier().getLevel()], 0);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();

            if (stack.is(ImmortalersDelightTags.IMMORTAL_KNIVES)) {
                MutableComponent textValue = Component.translatable(
                        "tooltip." +ImmortalersDelightMod.MODID+ ".is_immortalers_knives"
                );
                event.getToolTip().add(textValue.withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ImmortalersDelightTags.IMMORTAL_HAMMERS)) {
                MutableComponent textValue = Component.translatable(
                        "tooltip." +ImmortalersDelightMod.MODID+ ".is_immortalers_hammers"
                );
                event.getToolTip().add(textValue.withStyle(ChatFormatting.BLUE));
            }
            if (stack.is(ImmortalersDelightTags.STRAW)) {
                MutableComponent textValue = Component.translatable(
                        "tooltip." +ImmortalersDelightMod.MODID+ ".is_straw"
                );
                event.getToolTip().add(textValue.withStyle(ChatFormatting.BLUE));
            }
        }
    }
}

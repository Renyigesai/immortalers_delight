package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.event.SnifferEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SachetsItem extends EnchantAbleFoodItem{
    public SachetsItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
    }

    public SachetsItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, isFoil);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
        super.releaseUsing(pStack, pLevel, pEntityLiving, pTimeLeft);
        if (pEntityLiving instanceof Player player) {
            if (pLevel instanceof ServerLevel serverLevel && !serverLevel.isClientSide) {
                if (pTimeLeft >= 6) {
                    int itemDamage = 0;
                    List<LivingEntity> entities = serverLevel.getEntitiesOfClass(
                            LivingEntity.class,
                            player.getBoundingBox().inflate(pTimeLeft > 16 ? 16 : pTimeLeft)
                    );
                    for (LivingEntity entity : entities) {
                        if (entity instanceof Sniffer sniffer) {
                            CompoundTag tag = sniffer.getPersistentData();
                            boolean flag = !tag.contains(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN, Tag.TAG_INT) || tag.getInt(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN) <= 0;
                            sniffer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 900,flag ? 1 : 3));
                            sniffer.getNavigation().moveTo(player, 3.0);
                            itemDamage += 1;
                        }
                    }

                    if (!player.getAbilities().instabuild) {
                        EquipmentSlot equipmentslot = pStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                        pStack.hurtAndBreak(itemDamage, player, (action) -> {
                            action.broadcastBreakEvent(equipmentslot);
                        });
                    }
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BRUSH;
    }

    /**
     * Called to trigger the item's "innate" right click behavior.
     * To handle when this item is used on a Block
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}

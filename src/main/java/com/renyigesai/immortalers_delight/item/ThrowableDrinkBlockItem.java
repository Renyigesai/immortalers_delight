package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.entities.projectile.ToxicGasGrenadeEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ThrowableDrinkBlockItem extends DrinkItem{
    public ThrowableDrinkBlockItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip, boolean hasCustomToolTip) {
        super(pBlock, pProperties, hasPotionEffectTooltip, hasCustomToolTip);
    }

    public ThrowableDrinkBlockItem(Block pBlock, Properties pProperties, boolean hasPotionEffectTooltip) {
        super(pBlock, pProperties, hasPotionEffectTooltip);
    }

    public ThrowableDrinkBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }
    public void releaseUsing(ItemStack itemstack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        int i = this.getUseDuration(itemstack) - pTimeLeft;
        if (i < 0) return;
        float f = getPowerForTime(i);
        if (f > 0.9f) {
            pLevel.playSound((Player)null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                ToxicGasGrenadeEntity arrow = new ToxicGasGrenadeEntity(pLevel, pEntityLiving);
                arrow.setItem(itemstack);
                arrow.shootFromRotation(pEntityLiving, pEntityLiving.getXRot(), pEntityLiving.getYRot(), 0.0F, 1.5F, 1.0F);
                pLevel.addFreshEntity(arrow);
            }
            if (pEntityLiving instanceof Player pPlayer && !pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
        }
    }
    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}

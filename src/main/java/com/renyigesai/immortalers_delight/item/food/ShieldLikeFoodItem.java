package com.renyigesai.immortalers_delight.item.food;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.AntiFeedingFoodItem;
import com.renyigesai.immortalers_delight.api.PlateBaseBlock;
import com.renyigesai.immortalers_delight.block.StackedFoodBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.item.PowerfulAbleFoodItem;
import com.renyigesai.immortalers_delight.potion.immortaleffects.DeathlessEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class ShieldLikeFoodItem extends PowerfulAbleFoodItem implements AntiFeedingFoodItem {
    public final int type;
    @javax.annotation.Nullable
    private final FoodProperties aheadFoodProperties;

    @javax.annotation.Nullable
    private final FoodProperties poweredAheadFoodProperties;

    public ShieldLikeFoodItem(Properties properties, @Nullable FoodProperties powerFoodProperties,
                              @Nullable FoodProperties aheadFoodProperties, @Nullable FoodProperties poweredAheadFoodProperties,
                              boolean hasFoodEffectTooltip, boolean hasCustomTooltip, int type) {
        super(properties, powerFoodProperties, true, true);
        this.aheadFoodProperties = aheadFoodProperties;
        this.poweredAheadFoodProperties = poweredAheadFoodProperties;
        this.type = type;
    }

    public ShieldLikeFoodItem(Properties properties, @Nullable FoodProperties powerFoodProperties,
                              @Nullable FoodProperties aheadFoodProperties, @Nullable FoodProperties poweredAheadFoodProperties,
                              boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil, int type) {
        super(properties, powerFoodProperties, true, true, isFoil);
        this.aheadFoodProperties = aheadFoodProperties;
        this.poweredAheadFoodProperties = poweredAheadFoodProperties;
        this.type = type;
    }

    public BlockState getPlaceState(Level level, BlockPos blockpos) {
        Block block = Blocks.AIR;
        Block block1 = ImmortalersDelightBlocks.EVOLUTCORN_HARD_CANDY.get();
        Block block2 = ImmortalersDelightBlocks.KU_MESH_NON.get();
        if (type == 1) block=block1;
        if (type == 2) block=block2;
        if (block instanceof StackedFoodBlock stackedFoodBlock) return stackedFoodBlock.defaultBlockState().setValue(StackedFoodBlock.BITES, stackedFoodBlock.getMaxBites() - stackedFoodBlock.getPileBitesPerItem());
        return Blocks.AIR.defaultBlockState();
    }
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.getBlock() instanceof PlateBaseBlock plateBaseBlock && plateBaseBlock.isEmptyPlate(blockstate)) {
            level.playSound(player, blockpos, SoundEvents.WOOD_FALL, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            BlockState blockstate1 = this.getPlaceState(level, blockpos);
            level.setBlock(blockpos, blockstate1, 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
            ItemStack itemstack = pContext.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                itemstack.shrink(1);
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }
    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 300;
    }

    public @Nullable FoodProperties getAheadFoodProperties() {
        return DifficultyModeUtil.isPowerBattleMode() ? this.poweredAheadFoodProperties : this.aheadFoodProperties;
    }

    private void addAheadFoodEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (stack.getItem() instanceof ShieldLikeFoodItem shieldLikeFoodItem && shieldLikeFoodItem.getAheadFoodProperties() != null) {
            for (Pair<MobEffectInstance, Float> pair : shieldLikeFoodItem.getAheadFoodProperties().getEffects()) {
                if (!level.isClientSide() && pair.getFirst() != null) {
                    livingEntity.addEffect(pair.getFirst());
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("tooltip." + ImmortalersDelightMod.MODID + ".can_place_on_plate").withStyle(ChatFormatting.GRAY));
        if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {

            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
            if (this.type == 1) tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            if (this.type == 2) tooltip.add(textEmpty.withStyle(ChatFormatting.GRAY));

            this.addUsedEffectTooltip(stack, tooltip,1.0f);
        }
    }

    //==============================以下部分是为了继承接口而需要实现的方法===============================//

    @Override
    public FoodProperties getWholeFoodStats(ItemStack itemIn) {
        return DifficultyModeUtil.isPowerBattleMode() ? this.poweredFoodProperties : super.getFoodProperties();
    }
    @javax.annotation.Nullable
    private FoodProperties noEffectFoodProperties = null;

    @Override
    public FoodProperties getFoodProperties() {
        if (this.noEffectFoodProperties != null) {
            return this.noEffectFoodProperties;
        } else if (super.getFoodProperties() != null) {
            this.noEffectFoodProperties = this.notEffectFood(super.getFoodProperties());
            return this.noEffectFoodProperties;
        }
        return super.getFoodProperties();
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        //判断在使用物品
        if (pRemainingUseDuration >= 0 && !pLivingEntity.level().isClientSide()) {
            if (pStack.getItem() instanceof ShieldLikeFoodItem && pRemainingUseDuration >= 14) {
                if ((this.getUseDuration(pStack) - pRemainingUseDuration + 1) % 14 == 1) {
                    addAheadFoodEffect(pStack, pLivingEntity.level(), pLivingEntity);
                }
            }
        }
        //结束使用物品
        if (pRemainingUseDuration == 1) {
            this.addEatEffect(pStack, pLevel, pLivingEntity);
            //玉黍硬糖吃完后2s无敌帧
            if (this.type == 1) DeathlessEffect.applyImmortalEffect(pLivingEntity, 40, 0);
        }
    }
}

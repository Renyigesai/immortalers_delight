package com.renyigesai.immortalers_delight.item.weapon;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.PlateBaseBlock;
import com.renyigesai.immortalers_delight.block.StackedFoodBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.ImmortalersShieldItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class PlaceableShieldItem extends ImmortalersShieldItem {
    public final int type;
    public PlaceableShieldItem(Properties pProperties, int type) {
        super(pProperties);
        this.type = type;
    }
    public BlockState getPlaceState(Level level, BlockPos blockpos) {
        Block block1 = ImmortalersDelightBlocks.LARGE_COLUMN.get();
        if (type == 1 && block1 instanceof StackedFoodBlock stackedFoodBlock) return stackedFoodBlock.defaultBlockState().setValue(StackedFoodBlock.BITES, stackedFoodBlock.getMaxBites() - stackedFoodBlock.getPileBitesPerItem());
        return Blocks.AIR.defaultBlockState();
    }
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
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("tooltip." + ImmortalersDelightMod.MODID + ".can_place_on_plate").withStyle(ChatFormatting.GRAY));
        if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {

            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + this, new Object[0]);
            if (this.type == 1) tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));

        }
    }
    //实现列巴格挡掉列巴片以及格挡回饥饿
    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class PlaceableShieldEvents {
        public static final String DAMAGE_TAG = ImmortalersDelightMod.MODID + "_bites";
        @SubscribeEvent(priority = EventPriority.LOWEST)
            public static void onBlockedDamage(ShieldBlockEvent event) {
            LivingEntity hurtOne = event.getEntity();
            if (!hurtOne.level().isClientSide() && !event.isCanceled()) {
                ItemStack shield = hurtOne.getUseItem();
                if (shield.getItem() instanceof PlaceableShieldItem placeableShieldItem) {
                    float damage = event.getBlockedDamage();
                    //普通模式下，列巴最多抵挡30点伤害，若受到超过30点的单次伤害，多余伤害仍传递给使用者
                    if (damage > 30) damage = 30;
                    if (!DifficultyModeUtil.isPowerBattleMode()) event.setBlockedDamage(30);
                    //判断是否会被破盾，ojng这个盾牌机制全是散装的写起来太赤石了
                    boolean isBroken = false;
                    Entity source = event.getDamageSource().getDirectEntity();
                    if (source instanceof LivingEntity attacker) isBroken = willBeDisableShield(attacker,hurtOne, shield);
                    //检查是否有mod特殊机制设置了当前伤害忽视盾牌
                    if (event.shieldTakesDamage()) {
                        //列巴没耐久，每个列巴固定能抗2下(记录在使用者)
                        CompoundTag tag = hurtOne.getPersistentData();
                        if (tag.contains(DAMAGE_TAG, Tag.TAG_INT) && tag.getInt(DAMAGE_TAG) < 1) {
                            if (isBroken) {
                                onBreakShield(hurtOne, shield, placeableShieldItem.type, damage);
                            } else {
                                tag.putInt(DAMAGE_TAG, tag.getInt(DAMAGE_TAG) + 1);
                                dropSlice(hurtOne, placeableShieldItem.type, damage);
                                healUser(hurtOne,1);
                            }
                        } else if (!tag.contains(DAMAGE_TAG, Tag.TAG_INT)) {
                            if (isBroken) {
                                onBreakShield(hurtOne, shield, placeableShieldItem.type, damage);
                            } else {
                                tag.putInt(DAMAGE_TAG, 1);
                                dropSlice(hurtOne, placeableShieldItem.type, damage);
                                healUser(hurtOne,1);
                            }
                        } else {
                            tag.remove(DAMAGE_TAG);
                            dropSlice(hurtOne, placeableShieldItem.type, damage);
                            healUser(hurtOne,5);
                            if (hurtOne instanceof ServerPlayer serverPlayer && !serverPlayer.isCreative()) {
                                shield.shrink(1);
                            }
                        }
                    }
                }
            }
        }

        public static boolean willBeDisableShield(LivingEntity attacker, LivingEntity hurtOne, ItemStack shield) {
            return attacker.canDisableShield() || attacker.getMainHandItem().canDisableShield(shield, hurtOne, attacker);
        }
        public static void healUser(LivingEntity hurtOne,int buffer) {
            //列巴每次格挡时恢复少量饥饿或者血量，列巴在损坏时恢复5倍的饥饿或血量
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (!hurtOne.level().isClientSide()) {
                for (int i = 0; i < buffer; i++) {
                    if (hurtOne instanceof Player player){
                        if (isPowerful) player.getFoodData().eat(2,1);
                        else player.getFoodData().eat(1,1);
                    } else hurtOne.heal(isPowerful ? 8 : 2);
                }
            }
        }
        public static void onBreakShield(LivingEntity hurtOne,ItemStack stack,int type, float damage) {
            Level level = hurtOne.level();
            if (!level.isClientSide()) {
                int i = 2;
                CompoundTag tag = hurtOne.getPersistentData();
                if (tag.contains(PlaceableShieldEvents.DAMAGE_TAG, Tag.TAG_INT) && tag.getInt(PlaceableShieldEvents.DAMAGE_TAG) > 0) i -= tag.getInt(PlaceableShieldEvents.DAMAGE_TAG);
                for (int j = 0; j < i; ++j) {dropSlice(hurtOne, type, damage);}
                healUser(hurtOne,5);
                stack.shrink(1);
                tag.remove(PlaceableShieldEvents.DAMAGE_TAG);
            }
        }
        public static void dropSlice(LivingEntity living, int type, float damage) {
            Direction direction = living.getDirection().getOpposite();
            Level level = living.level();
            BlockPos pos = living.blockPosition();
            //列巴每次格挡有几率掉列巴片，单次伤害越高掉率越低，伤害为15时正好为50%概率，伤害达到30后则不再掉落
            if (level.isClientSide() || level.random.nextFloat() * 30.0F < damage ) return;
            if (type == 1) ItemUtils.spawnItemEntity(level,
                    new ItemStack(ImmortalersDelightItems.LARGE_COLUMN_SLICE.get()),
                    (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5,
                    (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        }
    }
}

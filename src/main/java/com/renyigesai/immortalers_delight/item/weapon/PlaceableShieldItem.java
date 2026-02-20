package com.renyigesai.immortalers_delight.item.weapon;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.PlateBaseBlock;
import com.renyigesai.immortalers_delight.block.StackedFoodBlock;
import com.renyigesai.immortalers_delight.entities.projectile.EffectCloudBaseEntity;
import com.renyigesai.immortalers_delight.entities.projectile.GasCloudEntity;
import com.renyigesai.immortalers_delight.entities.projectile.WarpedLaurelHitBoxEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightPotions;
import com.renyigesai.immortalers_delight.item.ImmortalersShieldItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

import javax.annotation.Nullable;
import java.util.List;

public class PlaceableShieldItem extends ImmortalersShieldItem {
    public final int type;
    public final int maxDamage;
    public PlaceableShieldItem(Properties pProperties, int type) {
        super(pProperties);
        this.type = type;
        int[] maxDamages = {0, 1, 3};
        this.maxDamage = this.type >= maxDamages.length ? maxDamages[0] : maxDamages[type];
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
            if (this.type == 2) tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity consumer, int timeLeft) {
        if (this.type == 2 && timeLeft <= this.getUseDuration(stack) - 32) {
            CompoundTag nbt = consumer.getPersistentData();
            if (nbt.contains(PlaceableShieldEvents.DAMAGE_TAG, Tag.TAG_INT)) {
                int lv = nbt.getInt(PlaceableShieldEvents.DAMAGE_TAG);
                makeAreaOfEffectCloud(level, consumer.blockPosition(), lv, consumer);
                nbt.remove(PlaceableShieldEvents.DAMAGE_TAG);
            }
        }
        super.releaseUsing(stack, level, consumer, timeLeft);
    }


    private static void makeAreaOfEffectCloud(Level level, BlockPos pPos, int lv, @Nullable LivingEntity caster) {
        if (level.isClientSide()) return;
        WarpedLaurelHitBoxEntity effectCloud = new WarpedLaurelHitBoxEntity(level, pPos.getX() + 0.5D, pPos.getY() + 0.05D, pPos.getZ() + 0.5D);

        effectCloud.setDuration(64);
        effectCloud.setRadius(2.0F + lv * 0.5f);
        effectCloud.setRadiusOnUse(0.0F);
        effectCloud.setWaitTime(10);
        effectCloud.setRadiusPerTick(0.0f);
        effectCloud.setParticle(ParticleTypes.SOUL_FIRE_FLAME);
        effectCloud.setOwner(caster);

        effectCloud.setDangerous(true);
        effectCloud.addEffect(new MobEffectInstance(MobEffects.DARKNESS,100));

        effectCloud.setDamageAmp(lv);

        level.addFreshEntity(effectCloud);
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
                        //列巴没耐久，每个列巴固定能抗2下(记录在使用者)，战争面包则是抗4下
                        CompoundTag tag = hurtOne.getPersistentData();
                        if (tag.contains(DAMAGE_TAG, Tag.TAG_INT) && tag.getInt(DAMAGE_TAG) < placeableShieldItem.maxDamage) {
                            doOnUsing(damage, tag, shield, hurtOne, isBroken);
                        } else if (!tag.contains(DAMAGE_TAG, Tag.TAG_INT)) {
                            doOnUsing(damage, tag, shield, hurtOne, isBroken);
                        } else {
                            doOnExhaust(damage, shield, hurtOne);
                            tag.remove(DAMAGE_TAG);
                        }
                    }
                }
            }
        }
        public static void doOnUsing(float damage,CompoundTag tag,ItemStack shield, LivingEntity hurtOne, boolean isBroken) {
            if (shield.getItem() instanceof PlaceableShieldItem placeableShieldItem) {
                if (isBroken) {
                    onBreakShield(hurtOne, shield, placeableShieldItem.type, damage);
                } else {
                    tag.putInt(DAMAGE_TAG, tag.contains(DAMAGE_TAG, Tag.TAG_INT) ? tag.getInt(DAMAGE_TAG) + 1 : 1);
                    dropSlice(hurtOne, placeableShieldItem.type, damage);
                    if (placeableShieldItem.type == 1) healUser(hurtOne,1);
                }
            }
        }
        public static void doOnExhaust(float damage,ItemStack shield, LivingEntity consumer) {
            if (shield.getItem() instanceof PlaceableShieldItem placeableShieldItem) {
                dropSlice(consumer, placeableShieldItem.type, damage);
                if (placeableShieldItem.type == 1) healUser(consumer,5);

                if (placeableShieldItem.type == 2) {
                    CompoundTag nbt = consumer.getPersistentData();
                    if (nbt.contains(PlaceableShieldEvents.DAMAGE_TAG, Tag.TAG_INT)) {
                        //这里的条件与取消使用的不同是因为耐久损耗的值不会达到最大耐久，导致打了4下耐久损耗为3，所以加一
                        int lv = nbt.getInt(PlaceableShieldEvents.DAMAGE_TAG) + 1;
                        makeAreaOfEffectCloud(consumer.level(), consumer.blockPosition(), lv, consumer);
                    }
                }

                if (consumer instanceof ServerPlayer serverPlayer && !serverPlayer.isCreative()) {
                    shield.shrink(1);
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
            if (!level.isClientSide() && stack.getItem() instanceof PlaceableShieldItem placeableShieldItem) {
                int i = 2;
                CompoundTag tag = hurtOne.getPersistentData();
                if (tag.contains(PlaceableShieldEvents.DAMAGE_TAG, Tag.TAG_INT) && tag.getInt(PlaceableShieldEvents.DAMAGE_TAG) > 0) i -= tag.getInt(PlaceableShieldEvents.DAMAGE_TAG);
                for (int j = 0; j < i; ++j) {dropSlice(hurtOne, type, damage);}

                doOnExhaust(damage, stack, hurtOne);
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

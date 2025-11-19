package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.screen.GrindstoneWithNoBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class GrindstoneHammerItem extends ImmortalersHammerItem{
    private static final Component CONTAINER_TITLE = Component.translatable("container.grindstone_title");

    public GrindstoneHammerItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public GrindstoneHammerItem(int type, Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, float pExtraAttackDamage, float pExtraAttackSpeed, Properties pProperties) {
        super(type, pTier, pAttackDamageModifier, pAttackSpeedModifier, pExtraAttackDamage, pExtraAttackSpeed, pProperties);
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack pStack) {
        return 7200;
    }

    //这是使用物品右键方块时触发的方法
    //这个方法理论上在方块的use方法之后触发，但方块的use方法中疑似会判断这个方法的返还值，如果返回CONSUME则不会触发方块的use方法
    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        HitResult hitResult = this.calculateHitResult(player);
        if (player != null && hitResult.getType() == HitResult.Type.BLOCK) {
            if (player.level().getBlockState(pContext.getClickedPos()).getBlock() instanceof BaseEntityBlock) {
                return InteractionResult.PASS;
            }
            player.startUsingItem(pContext.getHand());
        }
        return InteractionResult.CONSUME;
    }
    //这是使用物品右键实体时触发的方法
    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (this.calculateHitResult(pPlayer).getType() == HitResult.Type.ENTITY) {
            pPlayer.startUsingItem(pUsedHand);
        }

        return InteractionResult.CONSUME;
    }
    //这是右键使用物品时触发的方法，理论上说这个方法在右键实体、方块、空气(空挥)时都会触发
    //这个方法的触发时机在方块的use方法之前
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack item = player.getItemInHand(hand);
//        InteractionHand otherhand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
//        ItemStack otheritem = player.getItemInHand(otherhand);
//        if (otheritem.canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK) && !player.getCooldowns().isOnCooldown(otheritem.getItem())) {
//            return InteractionResultHolder.fail(item);
//        }else{
//            player.startUsingItem(hand);
//            //level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SHREDDER_START.get(), SoundSource.PLAYERS, 1.5f, 1F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
//            return InteractionResultHolder.consume(item);
//        }
//    }

    //长按右键时每tick的逻辑，不论对方块对实体还是空挥都在这处理，客户端服务端都是如此
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        super.onUseTick(level, entity, stack, time);
        //这里实现对实体长按的效果：持续造成伤害
//        if (time > 0 && time % 10 == 0 && level instanceof ServerLevel) {
//            HitResult hitResult = this.calculateHitResult(entity);
//            if (hitResult instanceof EntityHitResult entityHitResult && hitResult.getType() == HitResult.Type.ENTITY) {
//                if (entityHitResult.getEntity() instanceof LivingEntity target) {
//                    //target.hurt(level.damageSources().mobAttack(entity), 1.0F)
//                    target.hurt(level.damageSources().indirectMagic(entity, entity), 2.5F);
//                }
//            }
//        }
    }
//
//    //松开右键的效果，这里用来打开砂轮的gui
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int time) {
        //对方块长按的效果，打开砂轮的gui
        if (time < this.getUseDuration(stack) - 32 && entity instanceof Player player) {
            HitResult hitresult = this.calculateHitResult(player);
            if (hitresult.getType() == HitResult.Type.BLOCK && hitresult instanceof BlockHitResult blockhitresult) {
                BlockPos pos = blockhitresult.getBlockPos();
                //BlockState state = level.getBlockState(pos);
                ItemStack heldStack = player.getUseItem();
                if (heldStack.getItem() instanceof GrindstoneHammerItem thisHammer) {
                    if (!player.level().isClientSide()) {
                        stack.hurtAndBreak(2, player, (p_289501_) -> {
                            p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        player.openMenu(thisHammer.getMenuProvider(player.level(), pos));
                        player.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
                    }
                }
            }
        }
    }

    public MenuProvider getMenuProvider(Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((p_53812_, p_53813_, p_53814_) -> {
            return new GrindstoneWithNoBlockMenu(p_53812_, p_53813_, ContainerLevelAccess.create(pLevel, pPos));
        }, CONTAINER_TITLE);
    }

    //获取方块或实体的点击结果，我也说不清具体什么原理
    //这个方法在右键方块、实体、空气(空挥)时都会触发，也能在长按中调用
    private HitResult calculateHitResult(LivingEntity pEntity) {
        return ProjectileUtil.getHitResultOnViewVector(pEntity, (p_281111_) -> {
            return !p_281111_.isSpectator() && p_281111_.isPickable();
        }, (Math.sqrt(ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE) - 1.0D));
    }
}

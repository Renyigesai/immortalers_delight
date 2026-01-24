package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.block.food.EmptyPlateBlock;
import com.renyigesai.immortalers_delight.entities.boat.AncientWoodBoat;
import com.renyigesai.immortalers_delight.entities.boat.AncientWoodChestBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class BoatsEventHelper {
    @SubscribeEvent
    public static void buildLargeBoat(PlayerInteractEvent.EntityInteractSpecific event) {
//        if (event.getEntity() != null && event.getTarget() instanceof ImmortalersBoat boat){
//            Player player = event.getEntity();
//            Level level = player.level();
//            if (!(level instanceof ServerLevel serverLevel)) return;
//            if (boat.getBoatVariant() == ImmortalersBoat.Type.ANCIENT_WOOD) {
//                ItemStack itemStack = event.getItemStack();
//                ItemStack itemStackInOtherHand = player.getItemInHand(event.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
//                if (itemStack.is(ItemTags.LOGS) && itemStack.getCount() >= 5 && itemStackInOtherHand.is(Items.WOODEN_SHOVEL)) {
//                    AncientWoodBoat largeBoat = new AncientWoodBoat(serverLevel, boat.getX(), boat.getY(), boat.getZ());
//                    largeBoat.setVariant(boat.getBoatVariant());
//                    serverLevel.addFreshEntity(largeBoat);
//                    boat.discard();
//                }
//                if (!player.getAbilities().instabuild) {
//                    itemStackInOtherHand.shrink(1);
//                    itemStack.shrink(5);
//                }
//            }
//        }

        if (event.getEntity() != null && event.getTarget() instanceof ImmortalersChestBoat chestboat){
            Player player = event.getEntity();
            Level level = player.level();
            if (!(level instanceof ServerLevel serverLevel)) return;
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(chestboat.getType());
            if (entityId == null || level.isClientSide) return;
            if (entityId.toString().equals(ImmortalersDelightEntities.IMMORTAL_CHEST_BOAT.getId().toString())
                    && chestboat.getBoatVariant() == ImmortalersChestBoat.Type.ANCIENT_WOOD) {
                ItemStack itemStack = event.getItemStack();
                ItemStack itemStackInOtherHand = player.getItemInHand(event.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                boolean flag = buildLargeChestBoat(itemStack, itemStackInOtherHand, player, serverLevel, chestboat);
                if (!flag) {
                    buildLargeChestBoat(itemStackInOtherHand, itemStack, player, serverLevel, chestboat);
                }
            }
            //AidSupportAbility.onItemUse(player, itemStack);
        }

    }


    private static boolean buildLargeChestBoat(ItemStack itemStack, ItemStack itemStackInOtherHand, Player player, ServerLevel serverLevel, ImmortalersChestBoat chestboat) {
        if (itemStack.is(ImmortalersDelightTags.ANCIENT_CHEST_BOAT_NEED_1) && itemStack.getCount() >= Config.ancientChestBoatNeeded_1
                && itemStackInOtherHand.is(ImmortalersDelightTags.ANCIENT_CHEST_BOAT_NEED_2) && itemStackInOtherHand.getCount() >= Config.ancientChestBoatNeeded_2) {
            AncientWoodChestBoat largeBoat = new AncientWoodChestBoat(serverLevel, chestboat.getX(), chestboat.getY(), chestboat.getZ());
            largeBoat.setVariant(chestboat.getBoatVariant());
            largeBoat.setCanopies(true);
            largeBoat.level().playLocalSound(largeBoat.getX(),largeBoat.getY(),largeBoat.getZ(), SoundEvents.WOOD_BREAK, SoundSource.BLOCKS,0.8F,0.8F,false);
            serverLevel.addFreshEntity(largeBoat);
            if (!player.getAbilities().instabuild) {
                itemStackInOtherHand.shrink(Config.ancientChestBoatNeeded_2);
                itemStack.shrink(Config.ancientChestBoatNeeded_1);
            }
            chestboat.discard();
            return true;
        } else return false;
    }


    @SubscribeEvent
    public static void buildEmptyPlate(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack itemStack = event.getItemStack();
        @NotNull InteractionHand hand = event.getHand();
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if (itemStack.is(ImmortalersDelightItems.SPOON.get()) && player.getItemInHand(otherHand).is(Items.BOWL)) {
            BlockPos blockPos = event.getPos();
            if (event.getFace() != null && player instanceof ServerPlayer serverPlayer) {
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.is(ImmortalersDelightBlocks.EMPTY_PLATE.get()) && blockState.hasProperty(EmptyPlateBlock.TYPES)) {
                    int type = blockState.getValue(EmptyPlateBlock.TYPES);
                    type++;
                    if (type > 3) type = 0;
                    level.setBlock(blockPos, blockState.setValue(EmptyPlateBlock.TYPES, type), 3);
                } else {
                    BlockPos blockpos1 = blockPos.relative(event.getFace());
                    if (level.getBlockState(blockpos1).isAir()) {
                        level.setBlockAndUpdate(blockpos1, ImmortalersDelightBlocks.EMPTY_PLATE.get().defaultBlockState());
                        if (!player.getAbilities().instabuild) {
                            player.getItemInHand(otherHand).shrink(1);
                            itemStack.hurtAndBreak(125, serverPlayer, (action) -> {
                                action.broadcastBreakEvent(event.getHand());
                            });
                        }
                    }
                }
            }
        }
    }
}

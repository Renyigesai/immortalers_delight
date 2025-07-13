package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.entities.boat.AncientWoodBoat;
import com.renyigesai.immortalers_delight.entities.boat.AncientWoodChestBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersBoat;
import com.renyigesai.immortalers_delight.entities.boat.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class BoatsEventHelper {
    @SubscribeEvent
    public static void buildLargeBoat(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getEntity() != null && event.getTarget() instanceof ImmortalersBoat boat){
            Player player = event.getEntity();
            Level level = player.level();
            if (!(level instanceof ServerLevel serverLevel)) return;
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(boat.getType());
            if (entityId == null || level.isClientSide) return;
            if (entityId.toString().equals(ImmortalersDelightEntities.IMMORTAL_BOAT.getId().toString())
                    && boat.getBoatVariant() == ImmortalersBoat.Type.ANCIENT_WOOD) {
                ItemStack itemStack = event.getItemStack();
                ItemStack itemStackInOtherHand = player.getItemInHand(event.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                if (itemStack.is(ImmortalersDelightTags.ANCIENT_BOAT_NEED_1) && itemStack.getCount() >= 5
                        && itemStackInOtherHand.is(ImmortalersDelightTags.ANCIENT_BOAT_NEED_2)) {
                    AncientWoodBoat largeBoat = new AncientWoodBoat(serverLevel, boat.getX(), boat.getY(), boat.getZ());
                    largeBoat.setVariant(boat.getBoatVariant());
                    serverLevel.addFreshEntity(largeBoat);
                    if (!player.getAbilities().instabuild) {
                        itemStackInOtherHand.shrink(1);
                        itemStack.shrink(5);
                    }
                    boat.discard();
                }
            }
            //AidSupportAbility.onItemUse(player, itemStack);
        }
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
                if (itemStack.is(ImmortalersDelightTags.ANCIENT_CHEST_BOAT_NEED_1) && itemStack.getCount() >= 5
                        && itemStackInOtherHand.is(ImmortalersDelightTags.ANCIENT_CHEST_BOAT_NEED_2)) {
                    AncientWoodChestBoat largeBoat = new AncientWoodChestBoat(serverLevel, chestboat.getX(), chestboat.getY(), chestboat.getZ());
                    largeBoat.setVariant(chestboat.getBoatVariant());
                    serverLevel.addFreshEntity(largeBoat);
                    if (!player.getAbilities().instabuild) {
                        itemStackInOtherHand.shrink(1);
                        itemStack.shrink(5);
                    }
                    chestboat.discard();
                }
            }
            //AidSupportAbility.onItemUse(player, itemStack);
        }
    }
}

package com.renyigesai.immortalers_delight.event;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import com.renyigesai.immortalers_delight.block.brushable.ModBrushableBlock;
import com.renyigesai.immortalers_delight.block.brushable.ModBrushableBlockEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.mixin.SnifferMixin;
import com.renyigesai.immortalers_delight.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
public class SnifferEvent {
    @SubscribeEvent
    public static void onDropSeed(SnifferDropSeedEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = event.getBlockPos();
            Holder<Biome> biomeHolder = event.getLevel().getBiome(pos);
            for (Map.Entry<TagKey<Biome>, List<ItemStack>> key : getSeeds(serverLevel,pos).entrySet()) {
                TagKey<Biome> tagKey = key.getKey();
                if (biomeHolder.is(tagKey)){
                    if (Math.random() < Config.mininProbability) {//根据配置文件的概率决定是否替换掉落物
                        HashMap<TagKey<Biome>, List<ItemStack>> seeds = getSeeds(serverLevel, pos);
                        event.setStacks(seeds.get(tagKey));
                    }
                }
            }
        }
    }

    private static HashMap<TagKey<Biome>, List<ItemStack>> getSeeds(Level level,BlockPos pos){
        HashMap<TagKey<Biome>, List<ItemStack>> itemStackHashMap = new HashMap<>();
        itemStackHashMap.put(BiomeTags.IS_JUNGLE, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_jungle",level),level,pos));
        itemStackHashMap.put(Tags.Biomes.IS_PLAINS, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_plains",level),level,pos));
        itemStackHashMap.put(BiomeTags.IS_FOREST, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_forest",level),level,pos));
        itemStackHashMap.put(BiomeTags.IS_RIVER, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_river",level),level,pos));
        itemStackHashMap.put(ImmortalersDelightTags.IS_CRIMSON_FOREST, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_crimson_forest",level),level,pos));
        itemStackHashMap.put(ImmortalersDelightTags.IS_WARPED_FOREST, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_warped_forest",level),level,pos));
        itemStackHashMap.put(ImmortalersDelightTags.IS_ICE_SPIKES, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_ice_spikes",level),level,pos));
        itemStackHashMap.put(ImmortalersDelightTags.IS_NETHER_WASTES, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_nether_wastes",level),level,pos));
        itemStackHashMap.put(BiomeTags.IS_MOUNTAIN, WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:gameplay/id_sniffer_mountain",level),level,pos));
        return itemStackHashMap;
    }

    public static final String SNIFFER_BRUSHING_COOLDOWN = ImmortalersDelightMod.MODID + "_sniffer_brushing_cooldown";
    public static final String SNIFFER_TAIL_REGENERATION_COOLDOWN = ImmortalersDelightMod.MODID + "_tail_regeneration_cooldown";
    @SubscribeEvent
    public static void snifferPrune(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getEntity() != null && event.getTarget() instanceof Sniffer sniffer){
            Player player = event.getEntity();
            Level level = player.level();
            int outputNumber = 0;
            CompoundTag tag = sniffer.getPersistentData();
            if (tag.get(SNIFFER_BRUSHING_COOLDOWN) == null){
                tag.putInt(SNIFFER_BRUSHING_COOLDOWN, 0);
            }
            if (tag.get(SNIFFER_TAIL_REGENERATION_COOLDOWN) == null){
                tag.putInt(SNIFFER_TAIL_REGENERATION_COOLDOWN, 0);
            }

            boolean flag = false;
            if ((level instanceof ServerLevel) && (player instanceof ServerPlayer serverPlayer)) {
                ItemStack itemStack = event.getItemStack();
                if (itemStack.getItem() instanceof ShearsItem) {
                    BlockPos pos = player.getOnPos().above();
                    if (tag.getInt(SNIFFER_TAIL_REGENERATION_COOLDOWN) == 0){
                        vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,new ItemStack(ImmortalersDelightItems.RAW_SNIFFER_TAIL.get()),
                                pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                        if (!player.getAbilities().instabuild){
                            tag.putInt(SNIFFER_TAIL_REGENERATION_COOLDOWN, 144000);
                            itemStack.hurtAndBreak(1, serverPlayer, event.getHand() == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
                        }
                        flag = true;
                    }
                    if (tag.getInt(SNIFFER_BRUSHING_COOLDOWN) == 0){
                        outputNumber += 1 + player.getRandom().nextInt(3);
                        if (!player.getAbilities().instabuild) {
                            tag.putInt(SNIFFER_BRUSHING_COOLDOWN, 10140);
                            itemStack.hurtAndBreak(125, serverPlayer, event.getHand() == InteractionHand.MAIN_HAND ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
                        }
                        if (outputNumber > 0) {
                            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,new ItemStack(ImmortalersDelightItems.SNIFFER_FUR.get(), outputNumber),
                                    pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                        }
                        flag = true;
                    }
                }
            }
            if (flag){
                level.playSound(null,sniffer.getOnPos(),SoundEvents.SHEEP_SHEAR,SoundSource.BLOCKS);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public static void snifferBrushing(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() != null && event.getTarget() instanceof Sniffer sniffer){
            Player player = event.getEntity();
            Level level = player.level();
            CompoundTag tag = sniffer.getPersistentData();
            if (tag.get(SNIFFER_BRUSHING_COOLDOWN) == null) tag.putInt(SNIFFER_BRUSHING_COOLDOWN, 0);

            if ((level instanceof ServerLevel serverLevel) && (player instanceof ServerPlayer serverPlayer)) {
                if (!tag.contains(SNIFFER_BRUSHING_COOLDOWN, Tag.TAG_INT) || tag.getInt(SNIFFER_BRUSHING_COOLDOWN) <= 0) {
                    ItemStack oldStack = event.getItemStack();
                    if (oldStack.getItem() == Items.BRUSH) {
                        player.setItemInHand(event.getHand(), oldStack.transmuteCopy(ImmortalersDelightItems.BRUSH.get()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void snifferCooldown(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof Sniffer sniffer) {
            CompoundTag tag = sniffer.getPersistentData();
            if (tag.contains(SNIFFER_BRUSHING_COOLDOWN, Tag.TAG_INT) && tag.getInt(SNIFFER_BRUSHING_COOLDOWN) > 0) {
                tag.putInt(SNIFFER_BRUSHING_COOLDOWN, tag.getInt(SNIFFER_BRUSHING_COOLDOWN) - 1);
            }
            if (tag.contains(SNIFFER_TAIL_REGENERATION_COOLDOWN, Tag.TAG_INT) && tag.getInt(SNIFFER_TAIL_REGENERATION_COOLDOWN) > 0) {
                tag.putInt(SNIFFER_TAIL_REGENERATION_COOLDOWN, tag.getInt(SNIFFER_TAIL_REGENERATION_COOLDOWN) - 1);
            }
        }

        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof Player player) {
            if (player.getUseItemRemainingTicks() <= 0) {
                ItemStack oldStack = new ItemStack(Items.AIR);
                if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == ImmortalersDelightItems.BRUSH.get()) {
                    oldStack = player.getItemInHand(InteractionHand.OFF_HAND);
                }
                if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ImmortalersDelightItems.BRUSH.get()) {
                    oldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                }
                if (!oldStack.isEmpty()) {
                    EquipmentSlot equipmentslot = oldStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    player.setItemSlot(equipmentslot, oldStack.transmuteCopy(Items.BRUSH));
                }
            }

        }
    }


    @SubscribeEvent
    public static void onModBrushAbleInteraction(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Block block = state.getBlock();
        if (level instanceof ServerLevel serverLevel && block instanceof ModBrushableBlock) {
            ItemStack oldStack = event.getItemStack();
            if (oldStack.getItem() == Items.BRUSH) {
                event.getEntity().setItemInHand(event.getHand(), oldStack.transmuteCopy(ImmortalersDelightItems.BRUSH.get()));
            }
            //event.setCancellationResult(InteractionResult.SUCCESS);
            //event.setCanceled(true);
        }
    }

}

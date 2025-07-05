package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class SnifferEvent {
    @SubscribeEvent
    public static void onDropSeed(SnifferDropSeedEvent event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = event.getBlockPos();
            Holder<Biome> biomeHolder = event.getLevel().getBiome(pos);
            for (Map.Entry<TagKey<Biome>, ItemStack> key : addSeedEntity().entrySet()) {
                TagKey<Biome> tagKey = key.getKey();
                if (biomeHolder.is(tagKey)){
                    ItemStack stack = addSeedEntity().get(tagKey);
                    ItemEntity itemEntity = new ItemEntity(event.getLevel(), pos.getX(), pos.getY(), pos.getZ(), stack);
                    serverLevel.addFreshEntity(itemEntity);
                }
            }
        }
    }

    private static HashMap<TagKey<Biome>, ItemStack> addSeedEntity() {
        HashMap<TagKey<Biome>, ItemStack> itemStackHashMap = new HashMap<>();
        itemStackHashMap.put(BiomeTags.IS_JUNGLE, new ItemStack(ImmortalersDelightItems.PEARLIPEARL.get()));
        itemStackHashMap.put(Tags.Biomes.IS_PLAINS, new ItemStack(ImmortalersDelightItems.EVOLUTCORN_GRAINS.get()));
        itemStackHashMap.put(BiomeTags.IS_FOREST, new ItemStack(ImmortalersDelightItems.HIMEKAIDO_SEED.get()));
        itemStackHashMap.put(BiomeTags.IS_RIVER, new ItemStack(ImmortalersDelightItems.CONTAINS_TEA_LEISAMBOO.get()));
        itemStackHashMap.put(ImmortalersDelightTags.IS_CRIMSON_FOREST, new ItemStack(ImmortalersDelightItems.KWAT_WHEAT_SEEDS.get()));
        return itemStackHashMap;
    }
    public static final String SNIFFER_BRUSHING_COOLDOWN = ImmortalersDelightMod.MODID + "_sniffer_brushing_cooldown";
    @SubscribeEvent
    public static void snifferPrune(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getEntity() != null && event.getTarget() instanceof Sniffer sniffer){
            Player player = event.getEntity();
            Level level = player.level();
            int outputNumber = 0;
            CompoundTag tag = sniffer.getPersistentData();
            if (tag.get(SNIFFER_BRUSHING_COOLDOWN) == null) tag.putInt(SNIFFER_BRUSHING_COOLDOWN, 0);

            if ((level instanceof ServerLevel serverLevel) && (player instanceof ServerPlayer serverPlayer)) {

                if (!tag.contains(SNIFFER_BRUSHING_COOLDOWN, Tag.TAG_INT) || tag.getInt(SNIFFER_BRUSHING_COOLDOWN) <= 0) {
                    ItemStack itemStack = event.getItemStack();
                    if (itemStack.getItem() instanceof ShearsItem) {
                        outputNumber += 1 + player.getRandom().nextInt(3);
                        if (!player.getAbilities().instabuild) {
                            tag.putInt(SNIFFER_BRUSHING_COOLDOWN, 10140);
                            itemStack.hurtAndBreak(125, serverPlayer, (action) -> {
                                action.broadcastBreakEvent(event.getHand());
                            });
                        }
                    }
                }
            }
            if (outputNumber > 0) {
                BlockPos pos = player.getOnPos().above();
                vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,new ItemStack(ImmortalersDelightItems.SNIFFER_FUR.get(), outputNumber),
                        pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
            }
            //AidSupportAbility.onItemUse(player, itemStack);
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
                        ItemStack newStack = new ItemStack(ImmortalersDelightItems.BRUSH.get(), oldStack.getCount(), getItemStackCapNBT(oldStack));
                        newStack.setPopTime(oldStack.getPopTime());
                        if (oldStack.getTag() != null) {
                            newStack.setTag(oldStack.getTag());
                        }
                        player.setItemInHand(event.getHand(), newStack);
                    }
                }
            }
            //AidSupportAbility.onItemUse(player, itemStack);
        }
    }

    @SubscribeEvent
    public static void snifferCooldown(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Sniffer sniffer) {
            CompoundTag tag = sniffer.getPersistentData();
            if (tag.contains(SNIFFER_BRUSHING_COOLDOWN, Tag.TAG_INT) && tag.getInt(SNIFFER_BRUSHING_COOLDOWN) > 0) {
                tag.putInt(SNIFFER_BRUSHING_COOLDOWN, tag.getInt(SNIFFER_BRUSHING_COOLDOWN) - 1);
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
                    ItemStack newStack = new ItemStack(Items.BRUSH, oldStack.getCount(), getItemStackCapNBT(oldStack));
                    newStack.setPopTime(oldStack.getPopTime());
                    if (oldStack.getTag() != null) {
                        newStack.setTag(oldStack.getTag());
                    }
                    player.setItemSlot(equipmentslot, newStack);
                }
            }

        }
    }

    private static CompoundTag getItemStackCapNBT(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        try {
            // 获取private字段
            Method method = CapabilityProvider.class.getDeclaredMethod("serializeCaps");
            method.setAccessible(true); // 允许访问private成员

            // 读取值
            CompoundTag value = (CompoundTag) method.invoke(itemStack); // 通过实例访问private字段
            if (value != null) {
                tag = value.copy();
                //System.out.println("获取capNBT字段成功");
            } else System.out.println("我们get到了null值，怎么会这样呢？");
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.out.println("获取capNBT字段失败");
            e.printStackTrace();
        }
        return tag;
    }

//    private static CompoundTag getItemStackCapNBT(ItemStack itemStack) {
//        // 获取父类的private字段
//        Field capNBT = ItemStack.class.getDeclaredField("capNBT");
//        capNBT.setAccessible(true); // 允许访问private成员
//
//        // 读取值
//        CompoundTag value = (CompoundTag) capNBT.get(this); // 通过子类实例访问父类private字段
//        //Sy//stem.out.println("通过反射获取父类private字段值：" + value);
//    }
}

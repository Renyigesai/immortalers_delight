package com.renyigesai.immortalers_delight.item.antique;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.renyigesai.immortalers_delight.screen.AntiqueBagMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class AntiqueBagItem extends Item {

    public static final int MAX_TOOL_SLOT = 3;
    public static final int MAX_ANTIQUEL_SLOT = 8;
    public static final String KEY_TOOL = "ToolSlot";
    public static final String KEY_ANTIQUE = "AntiqueSlot";
    public static final String KEY_LEVEL = "Level";

    public final int level;

    public AntiqueBagItem(Properties pProperties, int level) {
        super(pProperties);
        this.level = level;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide){
            ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
            if (itemInHand.getItem() instanceof AntiqueBagItem antiqueBag){
                if (is(itemInHand)){
                    //初始化等级和存储槽
                    initLevel(itemInHand);
                    initInventory(itemInHand);
                }
                NetworkHooks.openScreen(
                        (ServerPlayer) pPlayer,
                        getMenuProvider(antiqueBag.getToolSlot(itemInHand),antiqueBag.getAntiqueSlot(itemInHand)),
                        byteBuf -> byteBuf.writeItem(itemInHand));
                return InteractionResultHolder.success(itemInHand);
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public MenuProvider getMenuProvider(ItemStackHandler toolSlots,ItemStackHandler antiqueSlots) {
        return new SimpleMenuProvider((comstructor,title,player) -> new AntiqueBagMenu(comstructor,title,toolSlots,antiqueSlots), Component.translatable("container.immortalers_delight.antique_bag"));
    }


    public void initInventory(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(KEY_TOOL, Tag.TAG_COMPOUND)) {
            tag.put(KEY_TOOL, new ItemStackHandler(MAX_TOOL_SLOT).serializeNBT());
        }

        int level = getLevel(stack);
        int maxSlot = 0;
        switch (level){//获取各等级对应的槽数量
            case 1 -> maxSlot = 1;
            case 2 -> maxSlot = 2;
            case 3 -> maxSlot = 4;
            case 4 -> maxSlot = 6;
            case 5 -> maxSlot = 7;
            case 6 -> maxSlot = 8;
        }
        if (!tag.contains(KEY_ANTIQUE, Tag.TAG_COMPOUND)) {
            tag.put(KEY_ANTIQUE, new ItemStackHandler(maxSlot).serializeNBT());
        }
    }

    public void initLevel(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        if (stack.getItem() instanceof AntiqueBagItem antiqueBagItem){
            tag.putInt(KEY_LEVEL,antiqueBagItem.level);
        }
    }

    public int getLevel(ItemStack stack){
        return stack.getOrCreateTag().getInt(KEY_LEVEL);
    }

    /** ★ 返回绑定到物品 NBT 的 handler */
    public ItemStackHandler getToolSlot(ItemStack stack) {
        return new BagItemHandler(stack, KEY_TOOL, MAX_TOOL_SLOT);
    }

    /** ★ 返回绑定到物品 NBT 的 handler */
    public ItemStackHandler getAntiqueSlot(ItemStack stack) {
        return new BagItemHandler(stack, KEY_ANTIQUE, MAX_ANTIQUEL_SLOT);
    }

    public boolean is(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return !tag.contains(KEY_TOOL, Tag.TAG_COMPOUND) || !tag.contains(KEY_ANTIQUE, Tag.TAG_COMPOUND) || !tag.contains(KEY_LEVEL);
    }

    public Multimap<Attribute, AttributeModifier> getAllAttributeModifier(ItemStack bag){
        Multimap<Attribute, AttributeModifier> map = ArrayListMultimap.create();
        if (bag.getItem() instanceof AntiqueBagItem bagItem){
            ItemStackHandler antiqueSlot = bagItem.getAntiqueSlot(bag);
            for (int i = 0; i < antiqueSlot.getSlots(); i++) {
                ItemStack stackInSlot = antiqueSlot.getStackInSlot(i);
                if (stackInSlot.getItem() instanceof IAntiqueItem antiqueItem){
                    map.putAll(antiqueItem.getDefaultAttributeModifiers());
                }
            }
        }
        return map;
    }

    public void onAllAntiqueTick(ItemStack bag,Player player,Level level){
        if (bag.getItem() instanceof AntiqueBagItem bagItem){
            ItemStackHandler antiqueSlot = bagItem.getAntiqueSlot(bag);
            for (int i = 0; i < antiqueSlot.getSlots(); i++) {
                ItemStack stackInSlot = antiqueSlot.getStackInSlot(i);
                if (stackInSlot.getItem() instanceof IAntiqueItem antiqueItem){
                    if (antiqueItem.isTick(player,level)){
                        antiqueItem.onTick(player,level);
                    }
                }
            }
        }
    }

    public boolean containsAntique(ItemStack bag, Item item) {
        if (!(bag.getItem() instanceof AntiqueBagItem bagItem)) {
            return false;
        }
        ItemStackHandler antiqueSlot = bagItem.getAntiqueSlot(bag);
        for (int i = 0; i < antiqueSlot.getSlots(); i++) {
            if (antiqueSlot.getStackInSlot(i).is(item)) {
                return true;
            }
        }
        return false;
    }

    /** ★ 返回古物包物品的等级 */
    public int getLevel() {
        return level;
    }
}

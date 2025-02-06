package com.renyigesai.immortalers_delight.block.enchantal_cooler;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightRecipeTypes;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerMenu;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;

import java.util.Arrays;
import java.util.Optional;

public class EnchantalCoolerBlockEntity extends BaseContainerBlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(5); // 5 个槽位
    public int cookingTotalTime;
    private ResourceLocation[] lastRecipeIDs;

    public EnchantalCoolerBlockEntity(BlockPos pos, BlockState state) {
        super(ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(), pos, state);
        lastRecipeIDs = new ResourceLocation[3];

    }

    public void addItem(ItemStack item) {
        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                this.inventory.setStackInSlot(i, item.split(1));
                setChanged();
                return;
            }
        }
    }

    public void getItem(Player player) {
        ItemStack stack = this.inventory.getStackInSlot(4);
        if (!stack.isEmpty()) {
            this.inventory.setStackInSlot(4, ItemStack.EMPTY);
            ItemUtils.givePlayerItem(player, stack);
            setChanged();
        }
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.enchantal_cooler"); // GUI 标题
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new EnchantalCoolerMenu(containerId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return inventory.getSlots(); // 返回槽位数量
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = inventory.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(tag.getCompound("Inventory"));
        }
        cookingTotalTime = tag.getInt("CookingTotalTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("CookingTotalTime", cookingTotalTime);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                (double) this.worldPosition.getY() + 0.5D,
                (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    public static void updateBlock(EnchantalCoolerBlockEntity enchantalCoolerBlockEntity) {
        Level world = enchantalCoolerBlockEntity.getLevel();
        BlockPos pos = enchantalCoolerBlockEntity.getBlockPos();
        BlockState state = world.getBlockState(pos);
        setChanged(world, pos, state);
        world.sendBlockUpdated(pos, state, state, 3);
    }

    public static void craftTick(Level level, BlockPos pos, BlockState state, EnchantalCoolerBlockEntity blockEntity) {
        blockEntity.craftItem(level, pos, state, blockEntity);
        setChanged(level, pos, state);
        if (!level.isClientSide) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private void craftItem(Level level, BlockPos pos, BlockState state, EnchantalCoolerBlockEntity blockEntity){
        int size = this.inventory.getSlots() - 1;
        boolean craft = true;
        Optional<EnchantalCoolerRecipe> recipe = Optional.empty();
        ItemStack[] recipeArr;
        for (int i = 0; i < size; i++) {
            ItemStack slotStack = inventory.getStackInSlot(i);
            if (!slotStack.isEmpty()){
                recipe = getCurrentRecipe(i);
                if (recipe.isPresent()&& !recipe.get().getIngredients().isEmpty()){
                    if (recipe.get().getIngredients().get(0).test(slotStack)){
                        recipeArr = recipe.get().getIngredients().get(0).getItems();
                        if (!recipeArr[i].is(slotStack.getItem())){
                            craft = false;
                            break;
                        }
                    }else {
                        craft = false;
                    }
                }else {
                    craft = false;
                }
            }else {
                craft = false;
            }
        }
        if (craft){
            ItemStack resultItemTemp = recipe.get().getResultItem(null);
            ItemStack resultItem = new ItemStack(resultItemTemp.getItem(), resultItemTemp.getCount());
            if (!resultItem.isEmpty()){
                if (cookingTotalTime < 200){
                    cookingTotalTime ++;
                }else {
                    cookingTotalTime = 0;
                    for (int i = 0; i < size; i++) {
                        ItemStack removeItem = this.inventory.getStackInSlot(i);
                        removeItem.shrink(1);
                    }
                    this.inventory.setStackInSlot(4, resultItem);
                    updateBlock(blockEntity);
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }
    }

//    public Optional<CampfireCookingRecipe> getMatchingRecipe(Container recipeWrapper, int slot) {
//        if (level == null) return Optional.empty();
//
//        if (lastRecipeIDs[slot] != null) {
//            Recipe<Container> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
//                    .getRecipeMap(ImmortalersDelightRecipeTypes.SERIALIZERS)
//                    .get(lastRecipeIDs[slot]);
//            if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, level)) {
//                return Optional.of((CampfireCookingRecipe) recipe);
//            }
//        }
//
//        return level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeWrapper, level);
//    }


    private Optional<EnchantalCoolerRecipe> getCurrentRecipe(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        if (!stack.isEmpty()) {
            return level.getRecipeManager().getRecipeFor(EnchantalCoolerRecipe.Type.INSTANCE, new SimpleContainer(stack), level);
        }
        return Optional.empty();
    }


}
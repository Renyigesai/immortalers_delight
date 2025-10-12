package com.renyigesai.immortalers_delight.block.enchantal_cooler;

import com.renyigesai.immortalers_delight.block.WrappedHandler;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerMenu;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnchantalCoolerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private final ItemStackHandler inventory = new ItemStackHandler(7);// 7个槽位
    public int cookingTotalTime;
    public int residualDye;
    public int CONTAINER_SLOT = 4;
    public int OUTPUT_SLOT = 5;
    public int FUEL_SLOT = 6;
    private static final int[] INPUT_SLOTS = new int[]{0,1,2,3};
    private static final int[] OUTPUT_SLOTS = new int[]{5};
    private static final int[] FUEL_SLOTS = new int[]{6};
    private static final int[] CONTAINER_SLOTS = new int[]{4};
    private boolean newVersion = false;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final EnumMap<Direction, LazyOptional<WrappedHandler>> directionHandlers = new EnumMap<>(Direction.class);

    public EnchantalCoolerBlockEntity(BlockPos pos, BlockState state) {
        super(ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.enchantal_cooler"); // GUI 标题
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new EnchantalCoolerMenu(containerId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
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

    private boolean hasInput() {
        for(int i = 0; i < 4; ++i) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean fuelSupplement(){
        return this.residualDye < 3;
    }

    private boolean isFuelEmpty(){
        return this.residualDye == 0;
    }

    public boolean isFuel(ItemStack fuel){
        return fuel.is(Items.LAPIS_LAZULI) || fuel.is(ItemTags.create(new ResourceLocation("immortalers_delight:enchantal_cooler_fuel")));
    }

    public boolean getIntList(int i,int[] intList){
        for (int j = 0; j < intList.length; j++) {
            if (intList[j] == i){
                return true;
            }
        }
        return false;
    }

    private Direction getDirection(){
        return this.getBlockState().getValue(EnchantalCoolerBlock.FACING);
    }

    public void drops(EnchantalCoolerBlockEntity blockEntity) {
        SimpleContainer inventory = new SimpleContainer(blockEntity.inventory.getSlots());
        for (int i = 0; i < blockEntity.inventory.getSlots(); i++) {
            inventory.setItem(i, blockEntity.inventory.getStackInSlot(i));
        }
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }
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
    public void onLoad() {
        super.onLoad();
        Direction facing = getDirection();

        lazyItemHandler = LazyOptional.of(() -> inventory);

        directionHandlers.put(Direction.UP, LazyOptional.of(
                () -> new WrappedHandler(inventory, (i) -> false, (i, s) -> getIntList(i,INPUT_SLOTS) && canPlaceItem(i,s))));

        directionHandlers.put(Direction.DOWN, LazyOptional.of(
                () -> new WrappedHandler(inventory, (i) -> getIntList(i,OUTPUT_SLOTS), (i, s) -> false)));

        directionHandlers.put(facing, LazyOptional.of(
                () -> new WrappedHandler(inventory, (i) -> false, (i,s) -> getIntList(i,FUEL_SLOTS))));

        for (Direction dir : Direction.values()) {
            if (dir != Direction.UP && dir != Direction.DOWN && dir != facing) {
                directionHandlers.put(dir, LazyOptional.of(
                        () -> new WrappedHandler(inventory, (i) -> false, (i, s) -> getIntList(i,CONTAINER_SLOTS) && canPlaceItem(i,s))));
            }
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (!newVersion){
            ItemStackHandler newInventory = new ItemStackHandler(7);
            ItemStackHandler oldInventory = new ItemStackHandler(5);
            ItemStackHandler oldFuel = new ItemStackHandler(1);
            ItemStackHandler oldContainerslot = new ItemStackHandler(1);
            if (tag.contains("Inventory")) {
                oldInventory.deserializeNBT(tag.getCompound("Inventory"));
                inventory.deserializeNBT(newInventory.serializeNBT());
            }
            if (tag.contains("Containerslot")) {
                oldContainerslot.deserializeNBT(tag.getCompound("Containerslot"));
            }
            if (tag.contains("Fuelslot")) {
                oldFuel.deserializeNBT(tag.getCompound("Fuelslot"));
            }
            inventory.setStackInSlot(0,oldInventory.getStackInSlot(0));
            inventory.setStackInSlot(1,oldInventory.getStackInSlot(1));
            inventory.setStackInSlot(2,oldInventory.getStackInSlot(2));
            inventory.setStackInSlot(3,oldInventory.getStackInSlot(3));
            inventory.setStackInSlot(OUTPUT_SLOT,oldInventory.getStackInSlot(4));
            inventory.setStackInSlot(CONTAINER_SLOT,oldContainerslot.getStackInSlot(0));
            inventory.setStackInSlot(FUEL_SLOT,oldFuel.getStackInSlot(0));
            newVersion = true; 
        }else {
            if (tag.contains("Inventory")) {
                inventory.deserializeNBT(tag.getCompound("Inventory"));
            }
        }
        cookingTotalTime = tag.getInt("CookingTotalTime");
        residualDye = tag.getInt("ResidualDye");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("CookingTotalTime", cookingTotalTime);
        tag.putInt("ResidualDye", residualDye);
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

    public static void craftTick(Level level, BlockPos pos, BlockState state, EnchantalCoolerBlockEntity blockEntity) {
        boolean flag = false;
        if (!blockEntity.isFuelEmpty() && blockEntity.hasInput()){
            flag = true;
            blockEntity.craftItem();
        }
        if (blockEntity.fuelSupplement()){
            flag = true;
            blockEntity.fillFuel();
        }
        if (flag){
            setChanged(level, pos, state);
            if (!level.isClientSide) {
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    private boolean isContainer(){
        Optional<EnchantalCoolerRecipe> recipeOptional = getCurrentRecipe();
        if (recipeOptional.isPresent()){
            EnchantalCoolerRecipe recipe = recipeOptional.get();
            if (recipe.getContainer().is(this.inventory.getStackInSlot(CONTAINER_SLOT).getItem())){
                return true;
            }
            return recipe.getContainer().isEmpty();
        }
        return false;
    }
    private Optional<EnchantalCoolerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(4);
        List<ItemStack> inputs = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }

        for (int i = 0; i < inputs.size(); i++) {
            inventory.setItem(i, inputs.get(i));
        }

        return level.getRecipeManager()
                .getRecipeFor(EnchantalCoolerRecipe.Type.INSTANCE, inventory, level);
    }

    private void craftItem() {
        Optional<EnchantalCoolerRecipe> recipeOptional = getCurrentRecipe();
        if (recipeOptional.isEmpty()) {
            cookingTotalTime = 0;
            return;
        }
        EnchantalCoolerRecipe recipe = recipeOptional.get();
        ItemStack resultItem = recipe.getResultItem(level.registryAccess()).copy();
        ItemStack outputStack = inventory.getStackInSlot(5);

        if (!canCraft(resultItem, outputStack) || !isContainer()) {
            cookingTotalTime = 0;
            return;
        }

        List<Ingredient> ingredientsToConsume = new ArrayList<>(recipe.getIngredients());
        List<Integer> slotsToConsume = new ArrayList<>();

        outer:
        for (Ingredient ingredient : ingredientsToConsume) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty() && ingredient.test(stack) && !slotsToConsume.contains(i)) {
                    slotsToConsume.add(i);
                    continue outer;
                }
            }
            cookingTotalTime = 0;
            return;
        }

        if (cookingTotalTime < 100) {
            cookingTotalTime++;
        } else {
            for (int slot : slotsToConsume) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (!stack.is(Items.WATER_BUCKET)) {
                    if (stack.hasCraftingRemainingItem()) {
                        ejectIngredientRemainder(stack.getCraftingRemainingItem());
                    }
                    inventory.extractItem(slot, 1, false);
                }
            }

            if (!recipe.getContainer().isEmpty()) {
                inventory.extractItem(CONTAINER_SLOT,1,false);
            }

            if (outputStack.isEmpty()) {
                inventory.setStackInSlot(5, resultItem);
            } else {
                outputStack.grow(resultItem.getCount());
            }
            residualDye --;
            cookingTotalTime = 0;
        }
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.5;
        double z = worldPosition.getZ() + 0.5;
        ItemUtils.spawnItemEntity(this.level,remainderStack,x,y,z,0.0f,0.0f,0.0f);
    }

    private void fillFuel(){
        ItemStack stack = inventory.getStackInSlot(FUEL_SLOT);
        if (isFuel(stack)) {
            this.residualDye++;
            stack.shrink(1);
        }
    }

    private boolean canCraft(ItemStack resultItem,ItemStack outputStack){
        if (outputStack.isEmpty()){
            return true;
        }
        if (resultItem.is(outputStack.getItem()) && outputStack.getCount() != outputStack.getMaxStackSize()){
            return true;
        }
        return false;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction pSide) {
        if (pSide == getDirection()){
            return FUEL_SLOTS;
        } else if (pSide == Direction.DOWN) {
            return OUTPUT_SLOTS;
        }
        return pSide == Direction.UP?INPUT_SLOTS:CONTAINER_SLOTS;
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return pIndex != OUTPUT_SLOT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return this.canPlaceItem(pIndex,pItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
        if (side == null) {
            return lazyItemHandler.cast();
        }
        return directionHandlers.getOrDefault(side, LazyOptional.empty()).cast();
    }
    return super.getCapability(cap, side);
    }
}
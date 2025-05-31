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

    private final ItemStackHandler inventory = new ItemStackHandler(5);// 5个槽位
    private final ItemStackHandler containerslot = new ItemStackHandler(1);// 1 个容器槽位
    private final ItemStackHandler fuelslot = new ItemStackHandler(1);// 1 个燃料槽位
    public int cookingTotalTime;
    public int residualDye;
    private static final int[] INPUT_SLOTS = new int[]{0,1,2,3};
    private static final int[] OUTPUT_SLOTS = new int[]{4};
    private static final int[] FUEL_SLOTS = new int[]{0};
    private static final int[] CONTAINER_SLOTS = new int[]{0};

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final EnumMap<Direction, LazyOptional<WrappedHandler>> directionHandlers = new EnumMap<>(Direction.class);

    public EnchantalCoolerBlockEntity(BlockPos pos, BlockState state) {
        super(ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public ItemStackHandler getFuelslot() {
        return this.fuelslot;
    }

    public ItemStackHandler getContainerSlot() {
        return this.containerslot;
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
        SimpleContainer containerslot = new SimpleContainer(blockEntity.containerslot.getSlots());
        SimpleContainer fuelslot = new SimpleContainer(blockEntity.fuelslot.getSlots());
        for (int i = 0; i < blockEntity.inventory.getSlots(); i++) {
            inventory.setItem(i, blockEntity.inventory.getStackInSlot(i));
        }
        containerslot.setItem(0,blockEntity.containerslot.getStackInSlot(0));
        fuelslot.setItem(0,blockEntity.fuelslot.getStackInSlot(0));
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, inventory);
            Containers.dropContents(this.level, this.worldPosition, containerslot);
            Containers.dropContents(this.level, this.worldPosition, fuelslot);
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
                () -> new WrappedHandler(fuelslot, (i) -> false, (i,s) -> getIntList(i,FUEL_SLOTS))));

        for (Direction dir : Direction.values()) {
            if (dir != Direction.UP && dir != Direction.DOWN && dir != facing) {
                directionHandlers.put(dir, LazyOptional.of(
                        () -> new WrappedHandler(containerslot, (i) -> false, (i, s) -> getIntList(i,CONTAINER_SLOTS) && canPlaceItem(i,s))));
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
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(tag.getCompound("Inventory"));
        }
        if (tag.contains("Containerslot")) {
            containerslot.deserializeNBT(tag.getCompound("Containerslot"));
        }
        if (tag.contains("Fuelslot")) {
            fuelslot.deserializeNBT(tag.getCompound("Fuelslot"));
        }
        cookingTotalTime = tag.getInt("CookingTotalTime");
        residualDye = tag.getInt("ResidualDye");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.put("Containerslot", containerslot.serializeNBT());
        tag.put("Fuelslot", fuelslot.serializeNBT());
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

    public static void updateBlock(EnchantalCoolerBlockEntity enchantalCoolerBlockEntity) {
        Level world = enchantalCoolerBlockEntity.getLevel();
        BlockPos pos = enchantalCoolerBlockEntity.getBlockPos();
        BlockState state = world.getBlockState(pos);
        setChanged(world, pos, state);
        world.sendBlockUpdated(pos, state, state, 3);
    }

    public static void craftTick(Level level, BlockPos pos, BlockState state, EnchantalCoolerBlockEntity blockEntity) {
        blockEntity.craftItem();
        blockEntity.fillFuel();
        setChanged(level, pos, state);
        if (!level.isClientSide) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private boolean isContainer(){
        Optional<EnchantalCoolerRecipe> recipeOptional = getCurrentRecipe();
        if (recipeOptional.isPresent()){
            EnchantalCoolerRecipe recipe = recipeOptional.get();
            if (recipe.getContainer().is(this.containerslot.getStackInSlot(0).getItem())){
                return true;
            }
            return recipe.getContainer().isEmpty();
        }
        return false;
    }
    private Optional<EnchantalCoolerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(5);
        List<ItemStack> inputs = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
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
            cookingTotalTime = 0; // 重置进度
            return;
        }

        EnchantalCoolerRecipe recipe = recipeOptional.get();
        ItemStack resultItem = recipe.getResultItem(level.registryAccess()).copy();
        ItemStack outputStack = inventory.getStackInSlot(4);

        if (!canCraft(resultItem, outputStack) || !isContainer()) {
            cookingTotalTime = 0;
            return;
        }

        List<Ingredient> ingredientsToConsume = new ArrayList<>(recipe.getIngredients());
        List<Integer> slotsToConsume = new ArrayList<>();

        outer:
        for (Ingredient ingredient : ingredientsToConsume) {
            for (int i = 0; i < 9; i++) {
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
                if (stack.hasCraftingRemainingItem()) {
                    ejectIngredientRemainder(stack.getCraftingRemainingItem());
                }
                inventory.extractItem(slot, 1, false);
            }

            if (!recipe.getContainer().isEmpty()) {
                containerslot.extractItem(0,1,false);
            }

            if (outputStack.isEmpty()) {
                inventory.setStackInSlot(4, resultItem);
            } else {
                outputStack.grow(resultItem.getCount());
            }

            cookingTotalTime = 0;
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.5;
        double z = worldPosition.getZ() + 0.5;
        ItemUtils.spawnItemEntity(this.level,remainderStack,x,y,z,0.0f,0.0f,0.0f);
    }

    private void fillFuel(){
        if (this.residualDye < 3){
            ItemStack stack = this.fuelslot.getStackInSlot(0);
            if (stack.is(Items.LAPIS_LAZULI)){
                this.residualDye ++;
                stack.shrink(1);
            }
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
        return pIndex != 4;
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return canPlaceItem(pIndex,pItemStack);
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
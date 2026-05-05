package com.renyigesai.immortalers_delight.block.enchantal_cooler;

import com.renyigesai.immortalers_delight.block.WrappedHandler;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.PillagerKnifeAddPotionRecipe;
import com.renyigesai.immortalers_delight.recipe.SimpleContainerRecipeInput;
import com.renyigesai.immortalers_delight.screen.EnchantalCoolerMenu;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BiomeTags;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnchantalCoolerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final TagKey<Item> ENCHANTAL_COOLER_FUEL = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("immortalers_delight", "enchantal_cooler_fuel"));

    private final ItemStackHandler inventory = new ItemStackHandler(7);// 7个槽位
    public int cookingTotalTime;
    public int residualDye;
    public int loadVersion = 11;
    public static final int CONTAINER_SLOT = 4;
    public static final int OUTPUT_SLOT = 5;
    public static final int FUEL_SLOT = 6;
    private static final int[] INPUT_SLOTS = new int[]{0,1,2,3};
    private static final int[] OUTPUT_SLOTS = new int[]{5};
    private static final int[] FUEL_SLOTS = new int[]{6};
    private static final int[] CONTAINER_SLOTS = new int[]{4};
    private boolean newVersion = false;

    public EnchantalCoolerBlockEntity(BlockPos pos, BlockState state) {
        super(ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.immortalers_delight.enchantal_cooler");
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
        return fuel.is(Items.LAPIS_LAZULI) || fuel.is(ENCHANTAL_COOLER_FUEL);
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
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> list = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getSlots(); i++) {
            list.set(i, inventory.getStackInSlot(i));
        }
        return list;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, i < items.size() ? items.get(i) : ItemStack.EMPTY);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (isMigration(tag)){
            ItemStackHandler oldInventory = new ItemStackHandler(5);
            ItemStackHandler oldFuel = new ItemStackHandler(1);
            ItemStackHandler oldContainerslot = new ItemStackHandler(1);
            if (tag.contains("Inventory")) {
                oldInventory.deserializeNBT(registries, tag.getCompound("Inventory"));
            }
            if (tag.contains("Containerslot")) {
                oldContainerslot.deserializeNBT(registries, tag.getCompound("Containerslot"));
            }
            if (tag.contains("Fuelslot")) {
                oldFuel.deserializeNBT(registries, tag.getCompound("Fuelslot"));
            }
            inventory.setStackInSlot(0,oldInventory.getStackInSlot(0));
            inventory.setStackInSlot(1,oldInventory.getStackInSlot(1));
            inventory.setStackInSlot(2,oldInventory.getStackInSlot(2));
            inventory.setStackInSlot(3,oldInventory.getStackInSlot(3));
            inventory.setStackInSlot(OUTPUT_SLOT,oldInventory.getStackInSlot(4));
            inventory.setStackInSlot(CONTAINER_SLOT,oldContainerslot.getStackInSlot(0));
            inventory.setStackInSlot(FUEL_SLOT,oldFuel.getStackInSlot(0));
        }else {
            if (tag.contains("Inventory")) {
                inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
            }
        }
        cookingTotalTime = tag.getInt("CookingTotalTime");
        residualDye = tag.getInt("ResidualDye");
        loadVersion = tag.getInt("LoadVersion");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("CookingTotalTime", cookingTotalTime);
        tag.putInt("ResidualDye", residualDye);
        tag.putInt("LoadVersion", 11);
    }

    private boolean isMigration(CompoundTag tag){
        if (!tag.contains("LoadVersion")){
            return true;
        }
        if (tag.getInt("LoadVersion") == loadVersion){
            return false;
        }
        return tag.getInt("LoadVersion") < loadVersion;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("CookingTotalTime", cookingTotalTime);
        tag.putInt("ResidualDye", residualDye);
        tag.putInt("LoadVersion", loadVersion);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        super.onDataPacket(net, pkt, registries);
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

    private Optional<EnchantalCoolerRecipe> getCurrentRecipe() {
        SimpleContainer inv = getInput(true);
        if (level == null) {
            return Optional.empty();
        }
        return level.getRecipeManager()
                .getRecipeFor(EnchantalCoolerRecipe.Type.INSTANCE, new SimpleContainerRecipeInput(inv), level)
                .map(RecipeHolder::value)
                .filter(r -> !(r instanceof PillagerKnifeAddPotionRecipe));
    }

    private Optional<PillagerKnifeAddPotionRecipe> findSpecialRecipe() {
        SimpleContainer inv = getInput(true);
        if (level == null) {
            return Optional.empty();
        }
        return level.getRecipeManager()
                .getRecipeFor(PillagerKnifeAddPotionRecipe.Type.INSTANCE, new SimpleContainerRecipeInput(inv), level)
                .map(RecipeHolder::value);
    }

    private SimpleContainer getInput(boolean needContainer) {
        SimpleContainer inventory = new SimpleContainer(needContainer ? 5 : 4);
        List<ItemStack> inputs = new ArrayList<>();

        if(needContainer) inventory.setItem(CONTAINER_SLOT,this.inventory.getStackInSlot(CONTAINER_SLOT));

        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }

        for (int i = 0; i < inputs.size(); i++) {
            inventory.setItem(i, inputs.get(i));
        }

        return inventory;
    }

    private void craftItem() {
        Optional<PillagerKnifeAddPotionRecipe> specialRecipe = findSpecialRecipe();
        Optional<EnchantalCoolerRecipe> recipeOptional = getCurrentRecipe();
        if (specialRecipe.isEmpty() && recipeOptional.isEmpty()) {
            cookingTotalTime = 0;
            return;
        }
        EnchantalCoolerRecipe recipe =specialRecipe.isEmpty() ? recipeOptional.get() : specialRecipe.get();
        SimpleContainer inputs = getInput(true);
        ItemStack resultItem = recipe.assemble(new SimpleContainerRecipeInput(inputs), level.registryAccess()).copy();
        ItemStack outputStack = inventory.getStackInSlot(5);

        if (!canCraft(resultItem, outputStack)) {
            cookingTotalTime = 0;
            return;
        }
        if (cookingTotalTime < 100) {
            cookingTotalTime++;
        } else {
            for (int slot = 0; slot < 4; slot ++) {
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

            if (!ejectIngredientJvav(level)){
                if (outputStack.isEmpty()) {
                    inventory.setStackInSlot(5, resultItem);
                } else {
                    outputStack.grow(resultItem.getCount());
                }
            }
            residualDye --;
            cookingTotalTime = 0;
        }
    }
    private boolean ejectIngredientJvav(Level level){
        if (level.getBiome(worldPosition).is(BiomeTags.IS_JUNGLE)){
            if (Math.random() < 0.00947) {
                ItemUtils.spawnItemEntity(this.level, new ItemStack(ImmortalersDelightItems.JVAV_OFFEE.get()), worldPosition.getX() + 0.5, worldPosition.getY() + 0.5625, worldPosition.getZ() + 0.5, 0.0f, 0.25f, 0.0f);
                return true;
            }
        }
        return false;
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

    public IItemHandler getAutomationHandler(@Nullable Direction side) {
        if (side == null) {
            return inventory;
        }
        Direction facing = getDirection();
        if (side == Direction.UP) {
            return new WrappedHandler(inventory, i -> false, (i, s) -> getIntList(i, INPUT_SLOTS) && canPlaceItem(i, s));
        }
        if (side == Direction.DOWN) {
            return new WrappedHandler(inventory, i -> getIntList(i, OUTPUT_SLOTS), (i, s) -> false);
        }
        if (side == facing) {
            return new WrappedHandler(inventory, i -> false, (i, s) -> getIntList(i, FUEL_SLOTS));
        }
        return new WrappedHandler(inventory, i -> false, (i, s) -> getIntList(i, CONTAINER_SLOTS) && canPlaceItem(i, s));
    }
}
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.renyigesai.immortalers_delight.api.farmersdelight;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.utility.ItemUtils;

public abstract class AbstractStoveBlockEntity extends BlockEntity implements Clearable {
    private final ItemStackHandler items;
    private final int[] cookingProgress;
    private final int[] cookingTime;
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickRecipeLookup;

    protected AbstractStoveBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        super(blockEntityType, blockPos, blockState);
        int inventorySlotCount = this.getInventorySlotCount();
        this.items = createHandler(inventorySlotCount);
        this.cookingProgress = new int[inventorySlotCount];
        this.cookingTime = new int[inventorySlotCount];
        this.quickRecipeLookup = RecipeManager.createCheck(recipeType);
    }

    protected abstract int getInventorySlotCount();

    public abstract Vec2 getStoveItemOffset(int var1);

    public ItemStackHandler getItems() {
        return this.items;
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag inventoryTag;
        if (tag.contains("Inventory")) {
            inventoryTag = tag.getCompound("Inventory");
        } else {
            inventoryTag = tag;
        }

        this.items.deserializeNBT(inventoryTag);
        int[] arrayCookingTimesTotal;
        if (tag.contains("CookingTimes", 11)) {
            arrayCookingTimesTotal = tag.getIntArray("CookingTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, arrayCookingTimesTotal.length));
        }

        if (tag.contains("CookingTotalTimes", 11)) {
            arrayCookingTimesTotal = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, arrayCookingTimesTotal.length));
        }

    }

    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", this.items.serializeNBT());
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("Inventory", this.items.serializeNBT());
        return tag;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractStoveBlockEntity stoveEntity) {
        if (!stoveEntity.isEmpty()) {
            if (stoveEntity.shouldDropItems()) {
                stoveEntity.dropAllItems();
                stoveEntity.setChanged();
            } else {
                if ((Boolean)state.getValue(AbstractStoveBlock.LIT)) {
                    stoveEntity.cookAndOutputItems();
                } else {
                    stoveEntity.coolItems();
                }

            }
        }
    }

    private void cookAndOutputItems() {
        assert this.level != null;

        boolean didChange = false;

        for(int i = 0; i < this.items.getSlots(); ++i) {
            ItemStack ingredient = this.items.getStackInSlot(i);
            if (!ingredient.isEmpty()) {
                didChange = true;
                int var10002 = this.cookingProgress[i]++;
                if (this.cookingProgress[i] >= this.cookingTime[i]) {
                    Container container = new SimpleContainer(new ItemStack[]{ingredient});
                    ItemStack result = (ItemStack)this.quickRecipeLookup.getRecipeFor(container, this.level).map((recipe) -> {
                        return recipe.assemble(container, this.level.registryAccess());
                    }).orElse(ingredient);
                    if (result.isItemEnabled(this.level.enabledFeatures())) {
                        ItemUtils.spawnItemEntity(this.level, result.copy(), (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.0, (double)this.worldPosition.getZ() + 0.5, this.level.random.nextGaussian() * 0.009999999776482582, 0.10000000149011612, this.level.random.nextGaussian() * 0.009999999776482582);
                        this.items.setStackInSlot(i, ItemStack.EMPTY);
                        BlockState state = this.getBlockState();
                        this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
                        this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, Context.of(state));
                    }
                }
            }
        }

        if (didChange) {
            this.setChanged();
        }

    }

    private void coolItems() {
        assert this.level != null;

        boolean didChange = false;

        for(int i = 0; i < this.items.getSlots(); ++i) {
            int thisItemCookingProgress = this.cookingProgress[i];
            if (thisItemCookingProgress > 0) {
                didChange = true;
                this.cookingProgress[i] = Mth.clamp(thisItemCookingProgress - 2, 0, this.cookingTime[i]);
            }
        }

        if (didChange) {
            this.setChanged();
        }

    }

    public Optional<? extends AbstractCookingRecipe> getCookingRecipe(ItemStack itemStack) {
        assert this.level != null;

        return this.quickRecipeLookup.getRecipeFor(new SimpleContainer(new ItemStack[]{itemStack}), this.level);
    }

    public int getNextEmptySlot() {
        return IntStream.range(0, this.items.getSlots()).filter((i) -> {
            return this.items.getStackInSlot(i).isEmpty();
        }).findFirst().orElse(-1);
    }

    public boolean placeFood(@Nullable Entity entity, ItemStack foodStackToPlace, int foodCookingTime) {
        assert this.level != null;

        int emptySlotIndex = this.getNextEmptySlot();
        if (emptySlotIndex < 0) {
            return false;
        } else {
            assert this.items.getStackInSlot(emptySlotIndex).isEmpty();

            this.cookingTime[emptySlotIndex] = foodCookingTime;
            this.cookingProgress[emptySlotIndex] = 0;
            this.items.setStackInSlot(emptySlotIndex, foodStackToPlace.split(1));
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, Context.of(entity, state));
            this.setChanged();
            return true;
        }
    }

    public boolean shouldDropItems() {
        return this.level == null ? false : AbstractStoveBlock.isStoveTopCovered(this.level, this.worldPosition, this.getBlockState());
    }

    public Stream<ItemStack> streamItems() {
        IntStream var10000 = IntStream.range(0, this.items.getSlots());
        ItemStackHandler var10001 = this.items;
        Objects.requireNonNull(var10001);
        return var10000.mapToObj(var10001::getStackInSlot);
    }

    public boolean isEmpty() {
        return this.streamItems().allMatch(ItemStack::isEmpty);
    }

    public boolean isFull() {
        return this.streamItems().noneMatch(ItemStack::isEmpty);
    }

    public void dropAllItems() {
        if (this.level != null) {
            ItemUtils.dropItems(this.level, this.worldPosition, this.items);
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
            this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, Context.of(state));
        }
    }

    public void extinguish() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.setChanged();
        }
    }

    public void ignite() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.setChanged();
        }
    }

    public void clearContent() {
        this.streamItems().forEach((stack) -> {
            stack.setCount(0);
        });
    }

    private static ItemStackHandler createHandler(int slotCount) {
        return new ItemStackHandler(slotCount) {
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }
}

package com.renyigesai.immortalers_delight.block.ancient_stove;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Optional;

public class AncientStoveBlockEntity extends SyncedBlockEntity {

    private static final VoxelShape GRILLING_AREA = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
    private static final int INVENTORY_SLOT_COUNT = 6;
    private final ItemStackHandler inventory = this.createHandler();
    private final int[] cookingTimes = new int[6];
    private final int[] cookingTimesTotal = new int[6];
    public AncientStoveBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType) ImmortalersDelightBlocks.ANCIENT_STOVE_ENTITY.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        if (compound.contains("Inventory")) {
            this.inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
        } else {
            this.inventory.deserializeNBT(registries, compound);
        }

        int[] arrayCookingTimesTotal;
        if (compound.contains("CookingTimes", 11)) {
            arrayCookingTimesTotal = compound.getIntArray("CookingTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTimes, 0, Math.min(this.cookingTimesTotal.length, arrayCookingTimesTotal.length));
        }

        if (compound.contains("CookingTotalTimes", 11)) {
            arrayCookingTimesTotal = compound.getIntArray("CookingTotalTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTimesTotal, 0, Math.min(this.cookingTimesTotal.length, arrayCookingTimesTotal.length));
        }

    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.put("Inventory", this.inventory.serializeNBT(registries));
        compound.putIntArray("CookingTimes", this.cookingTimes);
        compound.putIntArray("CookingTotalTimes", this.cookingTimesTotal);
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, AncientStoveBlockEntity stove) {
        boolean isStoveLit = (Boolean)state.getValue(AncientStoveBlock.LIT);
        if (stove.isStoveBlockedAbove()) {
            if (!stove.isEmpty()) {
                ItemUtils.dropItems(level, pos, stove.inventory);
                stove.inventoryChanged();
            }
        } else if (isStoveLit) {
            stove.cookAndOutputItems();
        } else {
            for(int i = 0; i < stove.inventory.getSlots(); ++i) {
                if (stove.cookingTimes[i] > 0) {
                    stove.cookingTimes[i] = Mth.clamp(stove.cookingTimes[i] - 2, 0, stove.cookingTimesTotal[i]);
                }
            }
        }

    }

    public boolean isEmpty(){
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, AncientStoveBlockEntity stove) {
        for(int i = 0; i < stove.inventory.getSlots(); ++i) {
            if (!stove.inventory.getStackInSlot(i).isEmpty() && level.random.nextFloat() < 0.2F) {
                Vec2 stoveItemVector = stove.getStoveItemOffset(i);
                Direction direction = (Direction)state.getValue(StoveBlock.FACING);
                int directionIndex = direction.get2DDataValue();
                Vec2 offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2(stoveItemVector.y, stoveItemVector.x);
                double x = (double)pos.getX() + 0.5 - (double)((float)direction.getStepX() * offset.x) + (double)((float)direction.getClockWise().getStepX() * offset.x);
                double y = (double)pos.getY() + 1.0;
                double z = (double)pos.getZ() + 0.5 - (double)((float)direction.getStepZ() * offset.y) + (double)((float)direction.getClockWise().getStepZ() * offset.y);

                for(int k = 0; k < 3; ++k) {
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
                }
            }
        }
    }

    private void cookAndOutputItems() {
        if (this.level != null) {
            boolean didInventoryChange = false;

            for(int i = 0; i < this.inventory.getSlots(); ++i) {
                ItemStack stoveStack = this.inventory.getStackInSlot(i);
                if (!stoveStack.isEmpty()) {
                    int var10002 = this.cookingTimes[i]++;
                    if (this.cookingTimes[i] >= this.cookingTimesTotal[i]) {
                        SingleRecipeInput recipeInput = new SingleRecipeInput(stoveStack);
                        Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(recipeInput, i);
                        if (recipe.isPresent()) {
                            ItemStack resultStack = recipe.get().assemble(recipeInput, this.level.registryAccess());
                            if (!resultStack.isEmpty()) {
                                ItemUtils.spawnItemEntity(this.level, resultStack.copy(), (double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 1.0, (double)this.worldPosition.getZ() + 0.5, this.level.random.nextGaussian() * 0.009999999776482582, 0.10000000149011612, this.level.random.nextGaussian() * 0.009999999776482582);
                            }
                        }

                        this.inventory.setStackInSlot(i, ItemStack.EMPTY);
                        didInventoryChange = true;
                    }
                }
            }

            if (didInventoryChange) {
                this.inventoryChanged();
            }

        }
    }

    public int getNextEmptySlot() {
        for(int i = 0; i < this.inventory.getSlots(); ++i) {
            ItemStack slotStack = this.inventory.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public boolean addItem(ItemStack itemStackIn, CampfireCookingRecipe recipe, int slot) {
        if (0 <= slot && slot < this.inventory.getSlots()) {
            ItemStack slotStack = this.inventory.getStackInSlot(slot);
            if (slotStack.isEmpty()) {
                this.cookingTimesTotal[slot] = recipe.getCookingTime();
                this.cookingTimes[slot] = 0;
                this.inventory.setStackInSlot(slot, itemStackIn.split(1));
                this.inventoryChanged();
                return true;
            }
        }

        return false;
    }

    public Optional<CampfireCookingRecipe> getMatchingRecipe(SingleRecipeInput recipeInput, int slot) {
        if (this.level == null) {
            return Optional.empty();
        }
        return this.level.getRecipeManager()
                .getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeInput, this.level)
                .map(RecipeHolder::value);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public boolean isStoveBlockedAbove() {
        if (this.level != null) {
            BlockState above = this.level.getBlockState(this.worldPosition.above());
            return Shapes.joinIsNotEmpty(GRILLING_AREA, above.getShape(this.level, this.worldPosition.above()), BooleanOp.AND);
        } else {
            return false;
        }
    }

    public Vec2 getStoveItemOffset(int index) {
        float X_OFFSET = 0.3F;
        float Y_OFFSET = 0.2F;
        Vec2[] OFFSETS = new Vec2[]{new Vec2(0.3F, 0.2F), new Vec2(0.0F, 0.2F), new Vec2(-0.3F, 0.2F), new Vec2(0.3F, -0.2F), new Vec2(0.0F, -0.2F), new Vec2(-0.3F, -0.2F)};
        return OFFSETS[index];
    }

    private void addParticles() {
        if (this.level != null) {
            for(int i = 0; i < this.inventory.getSlots(); ++i) {
                if (!this.inventory.getStackInSlot(i).isEmpty() && this.level.random.nextFloat() < 0.2F) {
                    Vec2 stoveItemVector = this.getStoveItemOffset(i);
                    Direction direction = (Direction)this.getBlockState().getValue(StoveBlock.FACING);
                    int directionIndex = direction.get2DDataValue();
                    Vec2 offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2(stoveItemVector.y, stoveItemVector.x);
                    double x = (double)this.worldPosition.getX() + 0.5 - (double)((float)direction.getStepX() * offset.x) + (double)((float)direction.getClockWise().getStepX() * offset.x);
                    double y = (double)this.worldPosition.getY() + 1.0;
                    double z = (double)this.worldPosition.getZ() + 0.5 - (double)((float)direction.getStepZ() * offset.y) + (double)((float)direction.getClockWise().getStepZ() * offset.y);

                    for(int k = 0; k < 3; ++k) {
                        this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
                    }
                }
            }

        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag compound = super.getUpdateTag(registries);
        compound.put("Inventory", this.inventory.serializeNBT(registries));
        return compound;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(6) {
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }
}

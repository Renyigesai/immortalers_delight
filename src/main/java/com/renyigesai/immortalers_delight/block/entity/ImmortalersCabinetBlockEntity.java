package com.renyigesai.immortalers_delight.block.entity;

import com.renyigesai.immortalers_delight.block.ImmortalersCabinetBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.TextUtils;

/**
 * FD {@link vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity} always registers as
 * {@code farmersdelight:cabinet}, which breaks structure placement on mod cabinet blocks.
 */
public class ImmortalersCabinetBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            ImmortalersCabinetBlockEntity.this.playSound(state, (SoundEvent) ModSounds.BLOCK_CABINET_OPEN.get());
            ImmortalersCabinetBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            ImmortalersCabinetBlockEntity.this.playSound(state, (SoundEvent)ModSounds.BLOCK_CABINET_CLOSE.get());
            ImmortalersCabinetBlockEntity.this.updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu menu) {
                return menu.getContainer() == ImmortalersCabinetBlockEntity.this;
            }
            return false;
        }
    };

    public ImmortalersCabinetBlockEntity(BlockPos pos, BlockState state) {
        super(ImmortalersDelightBlocks.CABINET_BLOCK_ENTITY.get(), pos, state);
    }

    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.items, registries);
        }

    }

    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compound)) {
            ContainerHelper.loadAllItems(compound, this.items, registries);
        }

    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected Component getDefaultName() {
        return TextUtils.container("cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return ChestMenu.threeRows(containerId, playerInventory, this);
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void updateBlockState(BlockState state, boolean open) {
        if (this.level != null) {
            this.level.setBlock(this.getBlockPos(), (BlockState)state.setValue(ImmortalersCabinetBlock.OPEN, open), 3);
        }

    }

    private void playSound(BlockState state, SoundEvent sound) {
        if (this.level != null) {
            Vec3i cabinetFacingVector = ((Direction)state.getValue(ImmortalersCabinetBlock.FACING)).getNormal();
            double x = (double)this.worldPosition.getX() + 0.5 + (double)cabinetFacingVector.getX() / 2.0;
            double y = (double)this.worldPosition.getY() + 0.5 + (double)cabinetFacingVector.getY() / 2.0;
            double z = (double)this.worldPosition.getZ() + 0.5 + (double)cabinetFacingVector.getZ() / 2.0;
            this.level.playSound((Player)null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}

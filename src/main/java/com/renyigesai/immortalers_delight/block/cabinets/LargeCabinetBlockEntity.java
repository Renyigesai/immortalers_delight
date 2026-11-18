package com.renyigesai.immortalers_delight.block.cabinets;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.screen.LargeCabinetMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class LargeCabinetBlockEntity extends RandomizableContainerBlockEntity {
    public static final int SIZE = 108; // 9 行 * 12 列 = 108
    private NonNullList<ItemStack> contents;
    private final ContainerOpenersCounter openersCounter;

    public LargeCabinetBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ImmortalersDelightBlocks.TRAVASTRUGGLER_CABINET_ENTITY.get(), pos, state);
        this.contents = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        this.openersCounter = new ContainerOpenersCounter() {
            @Override
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                LargeCabinetBlockEntity.this.playSound(state, (SoundEvent) ModSounds.BLOCK_CABINET_OPEN.get());
                LargeCabinetBlockEntity.this.updateBlockState(state, true);
            }

            @Override
            protected void onClose(Level level, BlockPos pos, BlockState state) {
                LargeCabinetBlockEntity.this.playSound(state, (SoundEvent) ModSounds.BLOCK_CABINET_CLOSE.get());
                LargeCabinetBlockEntity.this.updateBlockState(state, false);
            }

            @Override
            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
            }

            @Override
            protected boolean isOwnContainer(Player player) {
                // 校验玩家打开的是否是 LargeCabinetMenu
                if (player.containerMenu instanceof LargeCabinetMenu largeCabinetMenu) {
                    Container container = largeCabinetMenu.getContainer();
                    return container == LargeCabinetBlockEntity.this;
                }
                return false;
            }
        };
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.contents);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.contents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compound)) {
            ContainerHelper.loadAllItems(compound, this.contents);
        }
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.contents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.contents = itemsIn;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container."+ ImmortalersDelightMod.MODID +".large_cabinet");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new LargeCabinetMenu(id, player, this);
    }

    @Override
    public void startOpen(Player player) {
        if (this.level != null && !this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.level, this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (this.level != null && !this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.level, this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (this.level != null && !this.remove) {
            this.openersCounter.recheckOpeners(this.level, this.getBlockPos(), this.getBlockState());
        }
    }

    void updateBlockState(BlockState state, boolean open) {
        if (this.level != null && state.hasProperty(CabinetBlock.OPEN)) {
            this.level.setBlock(this.getBlockPos(), state.setValue(CabinetBlock.OPEN, open), 3);
        }
    }

    private void playSound(BlockState state, SoundEvent sound) {
        if (this.level != null && state.hasProperty(CabinetBlock.FACING)) {
            Vec3i facingVector = state.getValue(CabinetBlock.FACING).getNormal();
            double x = (double) this.worldPosition.getX() + 0.5 + (double) facingVector.getX() / 2.0;
            double y = (double) this.worldPosition.getY() + 0.5 + (double) facingVector.getY() / 2.0;
            double z = (double) this.worldPosition.getZ() + 0.5 + (double) facingVector.getZ() / 2.0;
            this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }
}

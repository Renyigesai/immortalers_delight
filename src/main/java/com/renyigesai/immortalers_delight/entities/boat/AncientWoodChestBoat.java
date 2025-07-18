package com.renyigesai.immortalers_delight.entities.boat;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AncientWoodChestBoat extends ImmortalersChestBoat{
    private static final int CONTAINER_SIZE = 54;
    private static final EntityDataAccessor<Boolean> DATA_HAS_CANOPIES = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
    private NonNullList<ItemStack> bigItemStacks = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    @Nullable
    private ResourceLocation lootTable;
    public AncientWoodChestBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public AncientWoodChestBoat(Level level, double x, double y, double z) {
        this(ImmortalersDelightEntities.ANCIENT_WOOD_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HAS_CANOPIES, false);
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.entityData.get(DATA_HAS_CANOPIES)) {
            compoundTag.putBoolean("HasCanopies", true);
        }
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(DATA_HAS_CANOPIES, compoundTag.getBoolean("HasCanopies"));
    }
    public boolean hasCanopies() {
        return this.entityData.get(DATA_HAS_CANOPIES);
    }
    public void setCanopies(boolean canopies) {
        this.entityData.set(DATA_HAS_CANOPIES, canopies);
    }
    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return this.bigItemStacks;
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }
    @Override
    public void clearItemStacks() {
        this.bigItemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        if (this.lootTable == null || !player.isSpectator()) {
            this.unpackLootTable(inventory.player);
            return ChestMenu.sixRows(i, inventory, this);
        }
        return null;
    }
    @Override
    public void destroy(DamageSource damageSource) {
        this.spawnAtLocation(new ItemStack(ImmortalersDelightItems.ANCIENT_WOOD_LOG.get(),5));
        this.chestVehicleDestroyed(damageSource, this.level(), this);
    }
    @Override
    @Nullable
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }
    @Override
    public void setLootTable(@Nullable ResourceLocation resourceLocation) {
        this.lootTable = resourceLocation;
    }

    @Override
    public boolean hasEnoughSpaceFor(Entity pEntity) {
        return pEntity.getBbWidth() < 1.375F;
    }
    @Override
    protected float getSinglePassengerXOffset() {
        return -1.45f;
    }
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        super.positionRider(pPassenger, pCallback);
        if (this.hasPassenger(pPassenger)) {
            float f = this.getSinglePassengerXOffset();
            float f1 = (float)((this.isRemoved() ? (double)0.01F : this.getPassengersRidingOffset()) + pPassenger.getMyRidingOffset());
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(pPassenger);
                if (i == 0) {
                    f = 1.2F;
                } else {
                    f = -1.45F;
                }
            }

            Vec3 vec3 = (new Vec3((double)f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            pCallback.accept(pPassenger, this.getX() + vec3.x, this.getY() + (double)f1, this.getZ() + vec3.z);

        }
    }
    @Override
    public double getPassengersRidingOffset() {
        return 0.15D;
    }
    @Override
    protected int getMaxPassengers() {
        return 2;
    }
}

package com.renyigesai.immortalers_delight.entities.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class BladeEnergy extends Entity implements IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<Float> DATA_PROGRESS = SynchedEntityData.defineId(BladeEnergy.class, EntityDataSerializers.FLOAT);

    public BladeEnergy(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        if (level().isClientSide){
            return;
        }
        if (getAge() <= 1f){
            setAge(getAge() + 0.05f);
        }else {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_PROGRESS, 0f);
    }

    public float getAge() {
        return this.entityData.get(DATA_PROGRESS);
    }

    public void setAge(float age) {
        this.entityData.set(DATA_PROGRESS, age);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setAge(compoundTag.getFloat("Progress"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("Progress", getAge());
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

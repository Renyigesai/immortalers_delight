package com.renyigesai.immortalers_delight.entities;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.IntFunction;

public class ImmortalersBoat extends Boat {

    public static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(ImmortalersBoat.class, EntityDataSerializers.INT);

    public ImmortalersBoat(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ImmortalersBoat(Level level, double pX, double pY, double pZ) {
        this(ImmortalersDelightEntities.HIMEKAIDO_BOAT.get(), level);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }
    @Override
    public Item getDropItem() {
        return switch (getBoatVariant()) {
            case HIMEKAIDO -> ImmortalersDelightItems.HIMEKAIDO_BOAT.get();
        };
    }
    public void setVariant(Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }

    public Type getBoatVariant() {
        return Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, Type.HIMEKAIDO.ordinal());
    }
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getVariant().getSerializedName());
    }
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 8)) {
            this.setVariant(Type.byName(pCompound.getString("Type")));
        }
    }
    public static enum Type implements StringRepresentable {
        HIMEKAIDO(ImmortalersDelightBlocks.HIMEKAIDO_PLANKS.get(), "himekaido");

        private final String name;
        private final Block planks;
        public static final EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        Type(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }

        public String getSerializedName() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public String toString() {
            return this.name;
        }

        public static Type byId(int pId) {
            return BY_ID.apply(pId);
        }

        public static Type byName(String pName) {
            return CODEC.byName(pName, HIMEKAIDO);
        }
    }
}

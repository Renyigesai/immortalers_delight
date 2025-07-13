package com.renyigesai.immortalers_delight.entities.boat;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class ImmortalersBoat extends Boat {
    protected float deltaRotation;

    public static final EntityDataAccessor<Integer> DATA_ID_MOD_TYPE = SynchedEntityData.defineId(ImmortalersBoat.class, EntityDataSerializers.INT);

    public ImmortalersBoat(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ImmortalersBoat(Level level, double pX, double pY, double pZ) {
        this(ImmortalersDelightEntities.IMMORTAL_BOAT.get(), level);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }
    @Override
    public Item getDropItem() {
        return switch (getBoatVariant()) {
            case HIMEKAIDO -> ImmortalersDelightItems.HIMEKAIDO_BOAT.get();
            case ANCIENT_WOOD -> ImmortalersDelightItems.ANCIENT_WOOD_BOAT.get();
            case LEISAMBOO -> Items.BIRCH_BOAT;
            case PEARLIP_SHELL -> ImmortalersDelightItems.PEARLIP_SHELL_BOAT.get();
        };
    }
    @Override
    public void destroy(DamageSource damageSource) {
        if (this.getBoatVariant() == Type.ANCIENT_WOOD) {
            Level level = this.level();
            BlockPos pos = this.blockPosition();
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                    new ItemStack(Items.STICK,2),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
            this.spawnAtLocation(new ItemStack(ImmortalersDelightItems.ANCIENT_WOOD_PLANKS.get(),3));
        } else super.destroy(damageSource);
    }

    public void setVariant(Type pVariant) {
        this.entityData.set(DATA_ID_MOD_TYPE, pVariant.ordinal());
    }

    public Type getBoatVariant() {
        return Type.byId(this.entityData.get(DATA_ID_MOD_TYPE));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_MOD_TYPE, Type.HIMEKAIDO.ordinal());
    }
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("ModType", this.getBoatVariant().getSerializedName());
    }
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("ModType", 8)) {
            this.setVariant(Type.byName(pCompound.getString("ModType")));
        }
    }
    public enum Type implements StringRepresentable {
        HIMEKAIDO(ImmortalersDelightBlocks.HIMEKAIDO_PLANKS.get(), "himekaido"),
        LEISAMBOO(ImmortalersDelightBlocks.LEISAMBOO_PLANKS.get(), "leisamboo"),

        PEARLIP_SHELL(ImmortalersDelightBlocks.PEARLIP_SHELL_PLANKS.get(), "pearlip_shell"),

        ANCIENT_WOOD(ImmortalersDelightBlocks.ANCIENT_WOOD_PLANKS.get(), "ancient_wood");

        private final String name;
        private final Block planks;
        public static final EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        Type(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }

        public @NotNull String getSerializedName() {
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


    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (this.getBoatVariant() == Type.ANCIENT_WOOD){
            ItemStack hand = pPlayer.getItemInHand(pHand);
            if (hand.is(ItemTags.LOGS) && hand.getCount() >= 5){
                AncientWoodBoat largeBoat = new AncientWoodBoat(this.level(), this.getX(), this.getY(), this.getZ());
                if (!pPlayer.getAbilities().instabuild) {
                    hand.shrink(5);
                }
                largeBoat.setVariant(this.getBoatVariant());
                this.level().addFreshEntity(largeBoat);
                this.level().playLocalSound(this.getX(),this.getY(),this.getZ(), SoundEvents.WOOD_BREAK, SoundSource.BLOCKS,0.8F,0.8F,false);
                this.discard();
                return InteractionResult.SUCCESS;
            }else {
                return super.interact(pPlayer,pHand);
            }
        }
        return super.interact(pPlayer,pHand);
    }

    public InteractionResult imm$Interact(Player pPlayer, InteractionHand pHand){
        return super.interact(pPlayer,pHand);
    }

}

package com.renyigesai.immortalers_delight.entities.boat;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class ImmortalersChestBoat extends ChestBoat {
    private NonNullList<ItemStack> modItemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
    @Nullable
    private ResourceLocation lootTable;
    private long lootTableSeed;

    public ImmortalersChestBoat(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    public ImmortalersChestBoat(Level level, double x, double y, double z) {
        this(ImmortalersDelightEntities.IMMORTAL_CHEST_BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected float getSinglePassengerXOffset() {
        return 0.15f;
    }

    @Override
    protected int getMaxPassengers() {
        return 1;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addChestVehicleSaveData(compoundTag);
        compoundTag.putString("ModType", this.getBoatVariant().getSerializedName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readChestVehicleSaveData(compoundTag);
        if (compoundTag.contains("ModType", 8)) {
            this.setVariant(ImmortalersChestBoat.Type.byName(compoundTag.getString("ModType")));
        }
    }

    @Override
    public Item getDropItem() {
        return switch (getBoatVariant()) {
            case HIMEKAIDO -> ImmortalersDelightItems.HIMEKAIDO_CHEST_BOAT.get();
            case ANCIENT_WOOD -> ImmortalersDelightItems.ANCIENT_WOOD_CHEST_BOAT.get();
            case LEISAMBOO -> Items.BIRCH_BOAT;
            case PEARLIP_SHELL -> ImmortalersDelightItems.PEARLIP_SHELL_CHEST_BOAT.get();
        };
    }
    @Override
    public void destroy(DamageSource damageSource) {
        if (this.getBoatVariant() == ImmortalersChestBoat.Type.ANCIENT_WOOD) {
            Level level = this.level();
            BlockPos pos = this.blockPosition();
            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,
                    new ItemStack(Items.STICK,2),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
            this.spawnAtLocation(new ItemStack(ImmortalersDelightItems.ANCIENT_WOOD_PLANKS.get(),3));
        } else super.destroy(damageSource);
        this.chestVehicleDestroyed(damageSource, this.level(), this);
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        if (!this.level().isClientSide && removalReason.shouldDestroy()) {
            Containers.dropContents(this.level(), this, (Container)this);
        }
        super.remove(removalReason);
    }

//    @Override
//    public InteractionResult interact(Player player, InteractionHand interactionHand) {
//        if (!this.canAddPassenger(player) || player.isSecondaryUseActive()) {
//            InteractionResult interactionResult = this.interactWithContainerVehicle(player);
//            if (interactionResult.consumesAction()) {
//                this.gameEvent(GameEvent.CONTAINER_OPEN, player);
//                PiglinAi.angerNearbyPiglins(player, true);
//            }
//            return interactionResult;
//        }
//        return super.interact(player, interactionHand);
//    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        player.openMenu(this);
        if (!player.level().isClientSide) {
            this.gameEvent(GameEvent.CONTAINER_OPEN, player);
            PiglinAi.angerNearbyPiglins(player, true);
        }
    }

    @Override
    public void clearContent() {
        this.clearChestVehicleContent();
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.getChestVehicleItem(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return this.removeChestVehicleItem(i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return this.removeChestVehicleItemNoUpdate(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.setChestVehicleItem(i, itemStack);
    }

    @Override
    public SlotAccess getSlot(int i) {
        return this.getChestVehicleSlot(i);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isChestVehicleStillValid(player);
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        if (this.lootTable == null || !player.isSpectator()) {
            this.unpackLootTable(inventory.player);
            return ChestMenu.threeRows(i, inventory, this);
        }
        return null;
    }

    public void unpackLootTable(@Nullable Player player) {
        this.unpackChestVehicleLootTable(player);
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
    public long getLootTableSeed() {
        return this.lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long l) {
        this.lootTableSeed = l;
    }

    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return this.modItemStacks;
    }

    @Override
    public void clearItemStacks() {
        this.modItemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public void stopOpen(Player player) {
        this.level().gameEvent(GameEvent.CONTAINER_CLOSE, this.position(), GameEvent.Context.of(player));
    }



    // ---------------------------------------------------------------------------------------------------------------------
    //下面是关于船的变种的代码
    public static final EntityDataAccessor<Integer> DATA_ID_MOD_TYPE = SynchedEntityData.defineId(ImmortalersChestBoat.class, EntityDataSerializers.INT);
    public void setVariant(ImmortalersChestBoat.Type pVariant) {
        this.entityData.set(DATA_ID_MOD_TYPE, pVariant.ordinal());
    }

    public ImmortalersChestBoat.Type getBoatVariant() {
        return Type.byId(this.entityData.get(DATA_ID_MOD_TYPE));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_MOD_TYPE, ImmortalersChestBoat.Type.HIMEKAIDO.ordinal());
    }
    public static enum Type implements StringRepresentable {
        HIMEKAIDO(ImmortalersDelightBlocks.HIMEKAIDO_PLANKS.get(), "himekaido"),
        LEISAMBOO(ImmortalersDelightBlocks.LEISAMBOO_PLANKS.get(), "leisamboo"),

        PEARLIP_SHELL(ImmortalersDelightBlocks.PEARLIP_SHELL_PLANKS.get(), "pearlip_shell"),

        ANCIENT_WOOD(ImmortalersDelightBlocks.ANCIENT_WOOD_PLANKS.get(), "ancient_wood");

        private final String name;
        private final Block planks;
        public static final EnumCodec<ImmortalersChestBoat.Type> CODEC = StringRepresentable.fromEnum(ImmortalersChestBoat.Type::values);
        private static final IntFunction<ImmortalersChestBoat.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

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

        public static ImmortalersChestBoat.Type byId(int pId) {
            return BY_ID.apply(pId);
        }

        public static ImmortalersChestBoat.Type byName(String pName) {
            return CODEC.byName(pName, HIMEKAIDO);
        }

    }
}

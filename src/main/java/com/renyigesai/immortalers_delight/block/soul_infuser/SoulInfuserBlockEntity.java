package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.WrappedHandler;
import com.renyigesai.immortalers_delight.entities.projectile.KiBlastEntity;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireballEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.recipe.SoulInfuserRecipe;
import com.renyigesai.immortalers_delight.screen.SoulInfuserMenu;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

public class SoulInfuserBlockEntity extends BaseContainerBlockEntity {
    // 0-8: 原料, 9: 输出, 10: 燃料
    public static final int OUTPUT_SLOT = 9;
    public static final int FUEL_SLOT = 10;
    private static final int[] INPUT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};

    private final ItemStackHandler inventory = new ItemStackHandler(11);
    public int progress = 0;
    public int maxProgress = 200;
    public int charge = 0;          // 剩余充能
    private int fuelTimer = 0;      // 秒燃料充能计时器

    protected final ContainerData dataAccess;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final EnumMap<Direction, LazyOptional<WrappedHandler>> directionHandlers = new EnumMap<>(Direction.class);

    public SoulInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ImmortalersDelightBlocks.SOUL_INFUSER_ENTITY.get(), pos, state);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SoulInfuserBlockEntity.this.progress;
                    case 1 -> SoulInfuserBlockEntity.this.maxProgress;
                    case 2 -> SoulInfuserBlockEntity.this.charge;
                    case 3 -> SoulInfuserBlockEntity.this.fuelTimer;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SoulInfuserBlockEntity.this.progress = value;
                    case 1 -> SoulInfuserBlockEntity.this.maxProgress = value;
                    case 2 -> SoulInfuserBlockEntity.this.charge = value;
                    case 3 -> SoulInfuserBlockEntity.this.fuelTimer = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ContainerData getDataAccess() {
        return this.dataAccess;
    }

    public boolean isFuel(ItemStack stack) {
        return stack.is(ImmortalersDelightItems.MOON_OIL.get());
    }


    private int animationTime = 0;//当前动画进行的时间，这个值仅客户端，注意这个值每次切换动画都重置

    public int getAnimationTime() {return animationTime;}

    public static void animationTick(Level level, BlockPos pos, BlockState state, SoulInfuserBlockEntity entity) {
        if (!level.isClientSide) return;
//        System.out.println("灵魂炸锅客户端tick：" + entity.animationTime);
        if (state.getValue(SoulInfuserBlock.AGE) != 1) {
            SoulInfuserAttackHelper.AnimationTick(level,pos, entity.animationTime);
            entity.animationTime++;
        } else entity.animationTime=0;
    }
    private int attackTime = 0;//当前战斗已开始的时间，这个值仅服务端
    public static void craftTick(Level level, BlockPos pos, BlockState state, SoulInfuserBlockEntity entity) {
//        System.out.println("灵魂炸锅服务端tick：" + entity.attackTime);
        if (level.isClientSide) return;

        // 检查过载爆炸
        if (entity.charge > 64) {
            //启动攻击
            if (entity.attackTime == 0) {
                // 先销毁容器内所有物品
                for (int i = 0; i < entity.inventory.getSlots(); i++) {
                    entity.inventory.setStackInSlot(i, ItemStack.EMPTY);
                }
                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2.0F, Level.ExplosionInteraction.BLOCK);
                entity.attackTime++;
                //利用方块状态同步到客户端，动画状态切换为出场爆炸
                level.setBlockAndUpdate(pos, state.setValue(SoulInfuserBlock.AGE,0));
                setChanged(level, pos, state);
            } else {
                //攻击进行
                SoulInfuserAttackHelper.AttackTick(level,entity.worldPosition,entity.attackTime);
                entity.attackTime++;
                //攻击结束
                if (entity.attackTime >= 100) {
                    entity.attackTime=0;
                    entity.charge=0;
                    //利用方块状态同步到客户端，动画状态切换为无动画
                    level.setBlockAndUpdate(pos, state.setValue(SoulInfuserBlock.AGE,1));
                    setChanged(level, pos, state);
                }
            }

            return;
        }


        //危险状态发射灵魂弹
        if (state.getValue(SoulInfuserBlock.LIT) && entity.charge >= 48) {
            if (level.getGameTime() % 6 == 0){
                if (level.getRandom().nextInt(5) != 0) return;
                SoulInfuserAttackHelper.shootSoulFireBallRandom(level,pos);
            }
        }


        //没有沙子不工作
        boolean hasSand = state.getValue(SoulInfuserBlock.FILLING);
        if (!hasSand) {
            if (state.getValue(SoulInfuserBlock.LIT)) level.setBlock(pos, state.setValue(SoulInfuserBlock.LIT, false), 3);
            return;
        }

        Optional<SoulInfuserRecipe> recipeOpt = entity.getCurrentRecipe();
        boolean hasRecipe = recipeOpt.isPresent() && entity.canCraft(recipeOpt.get().getResultItem(level.registryAccess()));

        // 每隔8秒（危险临界后12秒）吸收一次燃料
        if (hasRecipe) {
            entity.fuelTimer++;
            if (entity.fuelTimer >= (entity.charge > 48 ? 240 : 160)) {
                entity.fuelTimer = 0;
                ItemStack fuelStack = entity.inventory.getStackInSlot(FUEL_SLOT);
                if (entity.isFuel(fuelStack)) {
                    if (fuelStack.hasCraftingRemainingItem()) {
                        ItemUtils.spawnItemEntity(entity.level,fuelStack.getCraftingRemainingItem(),
                                pos.getX(),pos.getY(),pos.getZ(),0.0f,0.0f,0.0f);
                    }
                    fuelStack.shrink(1);
                    entity.charge += 15;
                    //进入危险状态时发出警告
                    if (entity.charge >= 48) {
                        List<ServerPlayer> targets = level.getEntitiesOfClass(
                                ServerPlayer.class, new AABB(pos).inflate(16),
                                e -> e.isAlive());
                        for (ServerPlayer player : targets) {
                            player.displayClientMessage(
                                    Component.translatable("message." + ImmortalersDelightMod.MODID + ".soul_infuser.warming", new Object[0]),
                                    true);
                        }
                    }
                    //吸收燃料活化
                    if (!state.getValue(SoulInfuserBlock.LIT)) level.setBlock(pos, state.setValue(SoulInfuserBlock.LIT, true), 3);
                }
            }
        } else {
            entity.fuelTimer = 0;
        }

        // 有充能且有可用配方时推进合成
        if (entity.charge > 0 && hasRecipe) {
            SoulInfuserRecipe recipe = recipeOpt.get();
            entity.maxProgress = recipe.getCookingTime();

            // 每秒消耗 1 点充能
            if (entity.progress % 20 == 0) {
                entity.charge--;

                //充能耗尽再次休眠，并清掉灵魂沙
                if (entity.charge == 0) {
                    level.setBlock(pos, state.setValue(SoulInfuserBlock.LIT, false)
                            .setValue(SoulInfuserBlock.FILLING, false), 3);
                }
            }

            entity.progress++;
            if (entity.progress >= entity.maxProgress) {
                entity.craftItem(recipe);
                entity.progress = 0;
            }
        } else {
            if (entity.progress > 0) {
                entity.progress = Math.max(0, entity.progress - 2); // 无充能时进度回退
            }
        }

        setChanged(level, pos, state);
    }

    private Optional<SoulInfuserRecipe> getCurrentRecipe() {
        SimpleContainer inv = new SimpleContainer(9);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        return level != null ? level.getRecipeManager().getRecipeFor(SoulInfuserRecipe.Type.INSTANCE, inv, level) : Optional.empty();
    }

    private boolean canCraft(ItemStack result) {
        ItemStack out = inventory.getStackInSlot(OUTPUT_SLOT);
        if (out.isEmpty()) return true;
        if (!out.is(result.getItem())) return false;
        return out.getCount() + result.getCount() <= out.getMaxStackSize();
    }

    private void craftItem(SoulInfuserRecipe recipe) {
        //从配方获取输出物品
        ItemStack result = recipe.assemble(new SimpleContainer(9), level.registryAccess());

        //遍历输入物，消耗
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.hasCraftingRemainingItem()) {
                    ejectIngredientRemainder(stack.getCraftingRemainingItem());
                }
                inventory.extractItem(i, 1, false);
            }
        }
        //获取输出栏是否已有物品
        ItemStack out = inventory.getStackInSlot(OUTPUT_SLOT);
        //输出
        if (out.isEmpty()) {
            inventory.setStackInSlot(OUTPUT_SLOT, result.copy());
        } else {
            out.grow(result.getCount());
        }
    }
    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.5;
        double z = worldPosition.getZ() + 0.5;
        ItemUtils.spawnItemEntity(this.level,remainderStack,x,y,z,0.0f,0.0f,0.0f);
    }
    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        if (level != null) {
            Containers.dropContents(level, worldPosition, inv);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> inventory);
        Direction facing = getBlockState().getValue(SoulInfuserBlock.FACING);

        // 顶面：输入槽；底面：输出槽；朝向面：燃料槽；其余侧面：容器/辅助
        directionHandlers.put(Direction.UP, LazyOptional.of(() -> new WrappedHandler(inventory, i -> false, (i, s) -> i < 9)));
        directionHandlers.put(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(inventory, i -> i == OUTPUT_SLOT, (i, s) -> false)));
        directionHandlers.put(facing, LazyOptional.of(() -> new WrappedHandler(inventory, i -> false, (i, s) -> i == FUEL_SLOT && isFuel(s))));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) return lazyItemHandler.cast();
            return directionHandlers.getOrDefault(side, LazyOptional.empty()).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putInt("Charge", charge);
        tag.putInt("FuelTimer", fuelTimer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Inventory")) inventory.deserializeNBT(tag.getCompound("Inventory"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        charge = tag.getInt("Charge");
        fuelTimer = tag.getInt("FuelTimer");
    }

    @Override
    public CompoundTag getUpdateTag() { return saveWithoutMetadata(); }
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) { load(pkt.getTag()); }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.immortalers_delight.soul_infuser");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new SoulInfuserMenu(containerId, playerInventory, this, this.dataAccess);
    }

    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this &&
                player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    // 实现 BaseContainerBlockEntity 兼容
    @Override public int getContainerSize() { return 11; }
    @Override public boolean isEmpty() { return false; }
    @Override public ItemStack getItem(int slot) { return inventory.getStackInSlot(slot); }
    @Override public ItemStack removeItem(int slot, int amount) { return inventory.extractItem(slot, amount, false); }
    @Override public ItemStack removeItemNoUpdate(int slot) { return inventory.extractItem(slot, 64, false); }
    @Override public void setItem(int slot, ItemStack stack) { inventory.setStackInSlot(slot, stack); }
    @Override public void clearContent() {}
}
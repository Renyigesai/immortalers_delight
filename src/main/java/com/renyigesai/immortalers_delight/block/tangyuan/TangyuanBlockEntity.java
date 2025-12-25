package com.renyigesai.immortalers_delight.block.tangyuan;

import com.renyigesai.immortalers_delight.block.WrappedHandler;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.recipe.TangyuanRecipe;
import com.renyigesai.immortalers_delight.screen.GrindstoneWithNoBlockMenu;
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
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModAdvancements;

import java.util.*;

/**
 * 附魔冷却器方块实体类
 * 继承自基础容器方块实体，实现了世界容器接口，用于处理方块的物品存储、配方合成和燃料管理等逻辑
 */
public class TangyuanBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    // 物品存储处理器，包含7个槽位
    private final ItemStackHandler inventory = new ItemStackHandler(7);
    // 烹饪总时间（合成进度）
    public boolean canProceed = false;
    // 剩余燃料量（青金石或其他燃料）
    public int residualProgress = 3;
    // 数据加载版本（用于版本迁移）
    public int loadVersion = 11;
    // 容器槽位索引（放置容器类物品）
    public static final int CONTAINER_SLOT = 4;
    // 输出槽位索引（存放合成结果）
    public static final int OUTPUT_SLOT = 5;
    // 燃料槽位索引（放置燃料物品）
    public static final int RESULT_CACHE_SLOT = 6;
    // 输入槽位索引数组（0-3，放置合成原料）
    private static final int[] INPUT_SLOTS = new int[]{0,1,2,3};
    // 输出槽位索引数组
    private static final int[] OUTPUT_SLOTS = new int[]{5};
    // 步骤缓存器槽位索引数组
    private static final int[] RESULT_CACHE_SLOTS = new int[]{6};
    // 容器槽位索引数组
    private static final int[] CONTAINER_SLOTS = new int[]{4};
    // 是否为新版本标识（未使用）
    private boolean newVersion = false;

    // 物品处理器的延迟可选实例（用于能力系统）
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    // 按方向存储的物品处理器包装器（用于不同方向的自动化交互）
    private final EnumMap<Direction, LazyOptional<WrappedHandler>> directionHandlers = new EnumMap<>(Direction.class);

    /**
     * 构造函数
     * @param pos 方块位置
     * @param state 方块状态
     */
    public TangyuanBlockEntity(BlockPos pos, BlockState state) {
        super(ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(), pos, state);
    }

    /**
     * 获取物品存储处理器
     * @return 物品处理器实例
     */
    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    /**
     * 获取容器默认名称（用于UI显示）
     * @return 容器名称组件
     */
    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.immortalers_delight.tangyuan");
    }

    /**
     * 创建容器菜单（用于玩家界面交互）
     * @param containerId 容器ID
     * @param playerInventory 玩家背包
     * @return 附魔冷却器容器菜单实例
     */
    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory) {
        return new GrindstoneWithNoBlockMenu(containerId, playerInventory, ContainerLevelAccess.create(playerInventory.player.level(), this.worldPosition));
    }

    /**
     * 获取容器槽位总数
     * @return 槽位数量
     */
    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    /**
     * 检查容器是否为空
     * @return 若所有槽位都为空则返回true，否则返回false
     */
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查输入槽位是否有物品
     * @return 若输入槽位有物品则返回true
     */
    private boolean hasInput() {
        for(int i = 0; i < 4; ++i) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 尝试输入物品
     * 检查是否还能输入物品，随后判断应该将物品置入哪一槽位
     */
    public boolean tryInput(ItemStack stack,boolean simulate){
        if (stack.isEmpty()) return false;
        if (this.isEmpty()) {
            // 只添加1个物品到槽位
            if (!simulate) this.inventory.setStackInSlot(0, stack.split(1));
            return true;
        } else if (this.hasInput()) {
            // 查找是否还有空槽位
            for (int i = 0; i < INPUT_SLOTS.length; i++) {
                //如果槽位为空
                if (this.inventory.getStackInSlot(i).isEmpty()) {
                    // 添加物品
                    if (!simulate) this.inventory.setStackInSlot(i, stack.split(1));
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack tryRemoveInput() {
        if (this.isEmpty()) return ItemStack.EMPTY;
        int id = 0;
        for (int i = 0; i < INPUT_SLOTS.length; i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                id = i;
            }
        }
        ItemStack stack = this.inventory.getStackInSlot(id).copy();
        this.inventory.setStackInSlot(id, ItemStack.EMPTY);
        return stack;
    }
//
//    /**
//     * 检查燃料是否需要补充（剩余燃料小于3）
//     * @return 若需要补充燃料则返回true
//     */
//    private boolean fuelSupplement(){
//        return this.residualProgress < 3;
//    }
//
//    /**
//     * 检查燃料是否为空
//     * @return 若燃料为空则返回true
//     */
//    private boolean isFuelEmpty(){
//        return this.residualProgress == 0;
//    }

    public boolean tryAddProgress(int progress){
        if (this.canProceed) {
            this.residualProgress -= progress;
            System.out.println("汤圆进度增加成功");
            return true;
        }
        System.out.println("汤圆进度增加失败");
        return false;
    }

    /**
     * 检查索引是否在指定数组中
     * @param i 待检查索引
     * @param intList 索引数组
     * @return 若存在则返回true
     */
    public boolean getIntList(int i,int[] intList){
        for (int j = 0; j < intList.length; j++) {
            if (intList[j] == i){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取方块朝向
     * @return 方块朝向方向
     */
    private Direction getDirection(){
        return this.getBlockState().getValue(UnfinishedTangyuanBlock.FACING);
    }

    /**
     * 方块被破坏时掉落物品栏中的物品
     * @param blockEntity 方块实体实例
     */
    public void drops(TangyuanBlockEntity blockEntity) {
        SimpleContainer inventory = new SimpleContainer(blockEntity.inventory.getSlots());
        for (int i = 0; i < blockEntity.inventory.getSlots(); i++) {
            inventory.setItem(i, blockEntity.inventory.getStackInSlot(i));
        }
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }
    }

    /**
     * 获取指定槽位的物品
     * @param slot 槽位索引
     * @return 槽位中的物品栈
     */
    @Override
    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    /**
     * 从指定槽位移除一定数量的物品
     * @param slot 槽位索引
     * @param amount 移除数量
     * @return 移除的物品栈
     */
    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = inventory.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(amount);
    }

    /**
     * 不更新状态地从指定槽位移除物品
     * @param slot 槽位索引
     * @return 移除的物品栈
     */
    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    /**
     * 向指定槽位设置物品
     * @param slot 槽位索引
     * @param stack 物品栈
     */
    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    /**
     * 清空所有槽位物品
     */
    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    /**
     * 方块实体加载时初始化能力系统（用于自动化设备交互）
     */
    @Override
    public void onLoad() {
        super.onLoad();
        Direction facing = getDirection();

        // 初始化基础物品处理器
        lazyItemHandler = LazyOptional.of(() -> inventory);

        // 向上方向：仅允许放入输入槽位物品
        directionHandlers.put(Direction.UP, LazyOptional.of(
                () -> new WrappedHandler(inventory, (i) -> false, (i, s) -> getIntList(i,INPUT_SLOTS) && canPlaceItem(i,s))));

        // 向下方向：仅允许取出输出槽位物品
        directionHandlers.put(Direction.DOWN, LazyOptional.of(
                () -> new WrappedHandler(inventory, (i) -> getIntList(i,OUTPUT_SLOTS), (i, s) -> false)));

        // 方块朝向方向：仅允许取出缓存槽位物品
        directionHandlers.put(facing, LazyOptional.of(
                () -> new WrappedHandler(inventory, (i) -> getIntList(i, RESULT_CACHE_SLOTS), (i,s) -> false)));

        // 其他方向：仅允许放入容器槽位物品
        for (Direction dir : Direction.values()) {
            if (dir != Direction.UP && dir != Direction.DOWN && dir != facing) {
                directionHandlers.put(dir, LazyOptional.of(
                        () -> new WrappedHandler(inventory, (i) -> false, (i, s) -> getIntList(i,CONTAINER_SLOTS) && canPlaceItem(i,s))));
            }
        }
    }

    /**
     * 使能力失效（方块卸载时调用）
     */
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    /**
     * 从NBT标签加载数据
     * @param tag NBT标签
     */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        // 检查是否需要版本迁移（旧版本数据结构转换）
        if (isMigration(tag)){
            ItemStackHandler newInventory = new ItemStackHandler(7);
            ItemStackHandler oldInventory = new ItemStackHandler(5);
            ItemStackHandler oldFuel = new ItemStackHandler(1);
            ItemStackHandler oldContainerslot = new ItemStackHandler(1);

            // 读取旧版本数据
            if (tag.contains("Inventory")) {
                oldInventory.deserializeNBT(tag.getCompound("Inventory"));
                inventory.deserializeNBT(newInventory.serializeNBT());
            }
            if (tag.contains("Containerslot")) {
                oldContainerslot.deserializeNBT(tag.getCompound("Containerslot"));
            }
            if (tag.contains("Fuelslot")) {
                oldFuel.deserializeNBT(tag.getCompound("Fuelslot"));
            }

            // 迁移到新的槽位结构
            inventory.setStackInSlot(0,oldInventory.getStackInSlot(0));
            inventory.setStackInSlot(1,oldInventory.getStackInSlot(1));
            inventory.setStackInSlot(2,oldInventory.getStackInSlot(2));
            inventory.setStackInSlot(3,oldInventory.getStackInSlot(3));
            inventory.setStackInSlot(OUTPUT_SLOT,oldInventory.getStackInSlot(4));
            inventory.setStackInSlot(CONTAINER_SLOT,oldContainerslot.getStackInSlot(0));
            inventory.setStackInSlot(RESULT_CACHE_SLOT,oldFuel.getStackInSlot(0));
        }else {
            // 直接读取新版本数据
            if (tag.contains("Inventory")) {
                inventory.deserializeNBT(tag.getCompound("Inventory"));
            }
        }
        // 加载其他数据
        //cookingTotalTime = tag.getInt("CookingTotalTime");
        residualProgress = tag.getInt("ResidualDye");
        loadVersion = tag.getInt("LoadVersion");
    }

    /**
     * 保存数据到NBT标签
     * @param tag NBT标签
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        //tag.putInt("CookingTotalTime", cookingTotalTime);
        tag.putInt("ResidualDye", residualProgress);
        tag.putInt("LoadVersion", 11); // 保存当前版本号
    }

    /**
     * 检查是否需要数据迁移（版本升级时）
     * @param tag NBT标签
     * @return 若需要迁移则返回true
     */
    private boolean isMigration(CompoundTag tag){
        if (!tag.contains("LoadVersion")){
            return true;
        }
        if (tag.getInt("LoadVersion") == loadVersion){
            return false;
        }
        return tag.getInt("LoadVersion") < loadVersion;
    }

    /**
     * 获取更新标签（用于客户端同步）
     * @return 包含实体数据的NBT标签
     */
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    /**
     * 获取更新数据包（用于服务器向客户端同步数据）
     * @return 客户端绑定的方块实体数据包
     */
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * 处理客户端接收到的数据包
     * @param net 网络连接
     * @param pkt 数据包
     */
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    /**
     * 检查玩家是否在有效交互范围内
     * @param player 玩家实体
     * @return 若在范围内则返回true
     */
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                (double) this.worldPosition.getY() + 0.5D,
                (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    /**
     * 每tick执行的合成逻辑
     * @param level 世界
     * @param pos 方块位置
     * @param state 方块状态
     * @param blockEntity 方块实体实例
     */
    public static void craftTick(Level level, BlockPos pos, BlockState state, TangyuanBlockEntity blockEntity) {
        System.out.println("tangyuan_tick");
        boolean flag = false;
        // 若有燃料且有输入物品，则进行合成
        if (blockEntity.hasInput()){
            flag = true;
            blockEntity.craftItem();
        }
        // 若状态有变化，则同步数据
        if (flag){
            setChanged(level, pos, state);
            if (!level.isClientSide) {
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    /**
     * 获取当前匹配的附魔冷却器配方
     * @return 配方的可选实例
     */
    private Optional<TangyuanRecipe> getCurrentRecipe() {
        SimpleContainer inventory = getInput();

        if (level != null) {
            return level.getRecipeManager()
                    .getRecipeFor(TangyuanRecipe.Type.INSTANCE, inventory, level);
        }
        return Optional.empty();
    }

    /**
     * 使用工具处理砧板上的物品（核心切割逻辑）
     * @param toolStack 工具物品栈
     * @param player 使用工具的玩家（可为空）
     * @return 是否成功处理物品
     */
//    public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
//        // 检查世界是否存在
//        if (this.level == null) {
//            return false;
//        }
//
//        // 查找匹配的切割配方
//        Optional<CuttingBoardRecipe> matchingRecipe = this.getMatchingRecipe(new RecipeWrapper(this.inventory), toolStack, player);
//        matchingRecipe.ifPresent(recipe -> {
//
//            // 消耗工具耐久度
//            if (player != null) {
//                toolStack.hurtAndBreak(1, player, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
//            } else if (toolStack.hurt(1, this.level.random, null)) {
//                toolStack.setCount(0); // 工具耐久耗尽则销毁
//            }
//        });
//
//        return matchingRecipe.isPresent();
//    }
//
//    /**
//     * 查找特殊配方（掠夺者小刀添加药水配方）
//     * @return 特殊配方的可选实例
//     */
//    private Optional<PillagerKnifeAddPotionRecipe> findSpecialRecipe() {
//        SimpleContainer inventory = getInput(true);
//
//        return level.getRecipeManager()
//                .getRecipeFor(PillagerKnifeAddPotionRecipe.Type.INSTANCE, inventory, level);
//    }

    /**
     * 获取输入物品容器（用于配方匹配）
     *
     * @return 简化的物品容器
     */
    private SimpleContainer getInput() {

        SimpleContainer inventory = new SimpleContainer(6);
        List<ItemStack> inputs = new ArrayList<>();

        inventory.setItem(CONTAINER_SLOT,this.inventory.getStackInSlot(CONTAINER_SLOT));
        inventory.setItem(RESULT_CACHE_SLOT,this.inventory.getStackInSlot(RESULT_CACHE_SLOT));
        // 收集输入槽位物品
        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }

        // 填充到简化容器中
        for (int i = 0; i < inputs.size(); i++) {
            inventory.setItem(i, inputs.get(i));
        }

        return inventory;
    }

    /**
     * 执行合成物品逻辑
     */
    private void craftItem() {
        // 优先检查特殊配方
        //Optional<PillagerKnifeAddPotionRecipe> specialRecipe = findSpecialRecipe();
        // 检查普通配方
        Optional<TangyuanRecipe> recipeOptional = getCurrentRecipe();

        // 若无匹配配方，重置合成进度
        if (//specialRecipe.isEmpty() && 
                recipeOptional.isEmpty()) {
            //cookingTotalTime = 0;
            residualProgress = 3;
            canProceed = false;
            System.out.println("No matching recipe found.");
            return;
        }

        // 确定使用的配方
        TangyuanRecipe recipe =//specialRecipe.isEmpty() ? 
                recipeOptional.get() 
                //: specialRecipe.get()
        ;
        SimpleContainer inputs = getInput();
        // 获取合成结果
        ItemStack resultItem = ItemStack.EMPTY;
        if (level != null) {
            resultItem = recipe.assemble(inputs,level.registryAccess()).copy();
        }
        ItemStack outputStack = inventory.getStackInSlot(recipe.isFinished() ? OUTPUT_SLOT : RESULT_CACHE_SLOT);

        if (!resultItem.isEmpty()) {
            canProceed = true;
        }

        // 检查是否可以合成（输出槽是否能容纳结果）
        if (!canCraft(resultItem, outputStack)) {
            //cookingTotalTime = 0;
            residualProgress = 3;
            canProceed = false;
            return;
        }

        // 判断剩余进度，达到0完成合成
        if (residualProgress <= 0) {
            // 消耗输入物品
            for (int slot = 0; slot < 4; slot ++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                // 处理合成剩余物品（如桶->空桶）
                if (stack.hasCraftingRemainingItem()) {
                    ejectIngredientRemainder(stack.getCraftingRemainingItem());
                }
                inventory.extractItem(slot, 1, false);
            }

            // 消耗容器物品（若配方需要）
            if (!recipe.getContainer().isEmpty()) {
                inventory.extractItem(CONTAINER_SLOT,1,false);
            }

            // 消耗缓存物品（若配方需要）
            if (!recipe.getCacheItem().isEmpty()) {
                //removeItemNoUpdate(RESULT_CACHE_SLOT);
                inventory.extractItem(RESULT_CACHE_SLOT,1,false);
            }

            // 放入合成结果
            // 如果没有结束，将结果放入缓存槽，否则放入输出槽
            int slot_id = recipe.isFinished() ? OUTPUT_SLOT : RESULT_CACHE_SLOT;
            if (outputStack.isEmpty()) {
                inventory.setStackInSlot(slot_id, resultItem);
            } else {
                outputStack.grow(resultItem.getCount());
            }

            // 重置合成进度
            residualProgress = 3;
            canProceed = false;
            //cookingTotalTime = 0;
        }
    }

    /**
     * 弹出合成剩余物品（如容器的剩余物）
     * @param remainderStack 剩余物品栈
     */
    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.5;
        double z = worldPosition.getZ() + 0.5;
        ItemUtils.spawnItemEntity(this.level,remainderStack,x,y,z,0.0f,0.0f,0.0f);
    }

    /**
     * 补充燃料（从燃料槽位消耗物品增加剩余燃料）
     */
//    private void fillFuel(){
//        ItemStack stack = inventory.getStackInSlot(RESULT_CACHE_SLOT);
//        if (isFuel(stack)) {
//            this.residualProgress++;
//            stack.shrink(1);
//        }
//    }

    /**
     * 检查是否可以合成（输出槽是否能容纳结果）
     * @param resultItem 合成结果物品
     * @param outputStack 输出槽当前物品
     * @return 若可以合成则返回true
     */
    private boolean canCraft(ItemStack resultItem,ItemStack outputStack){
        if (outputStack.isEmpty()){
            return true;
        }
        // 物品相同且未达到最大堆叠数
        if (resultItem.is(outputStack.getItem()) && outputStack.getCount() != outputStack.getMaxStackSize()){
            return true;
        }
        return false;
    }

    /**
     * 获取指定方向可访问的槽位
     * @param pSide 方向
     * @return 槽位索引数组
     */
    @Override
    public int @NotNull [] getSlotsForFace(Direction pSide) {
        if (pSide == getDirection()){
            return RESULT_CACHE_SLOTS;
        } else if (pSide == Direction.DOWN) {
            return OUTPUT_SLOTS;
        }
        return pSide == Direction.UP?INPUT_SLOTS:CONTAINER_SLOTS;
    }

    /**
     * 检查物品是否可以放入指定槽位
     * @param pIndex 槽位索引
     * @param pStack 物品栈
     * @return 若可以放入则返回true（输出槽不可放入）
     */
    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return pIndex != OUTPUT_SLOT;
    }

    /**
     * 检查物品是否可以通过指定方向放入指定槽位
     * @param pIndex 槽位索引
     * @param pItemStack 物品栈
     * @param pDirection 方向
     * @return 若可以放入则返回true
     */
    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return this.canPlaceItem(pIndex,pItemStack);
    }

    /**
     * 检查物品是否可以通过指定方向从指定槽位取出（未实现，固定返回false）
     * @param pIndex 槽位索引
     * @param pStack 物品栈
     * @param pDirection 方向
     * @return 固定返回false
     */
    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }

    /**
     * 获取能力接口（用于自动化设备交互）
     * @param cap 能力类型
     * @param side 方向
     * @return 能力的延迟可选实例
     */
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return lazyItemHandler.cast();
            }
            return directionHandlers.getOrDefault(side, LazyOptional.empty()).cast();
        }
        return super.getCapability(cap, side);
    }
}

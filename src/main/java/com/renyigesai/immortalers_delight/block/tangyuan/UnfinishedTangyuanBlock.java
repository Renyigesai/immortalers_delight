package com.renyigesai.immortalers_delight.block.tangyuan;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.recipe.TangyuanRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ModTags;

/**
 * 附魔冷却器方块类，继承自带实体的方块基类BaseEntityBlock
 * 实现了具有交互界面、状态管理和粒子效果的功能性方块
 */
public class UnfinishedTangyuanBlock extends BaseEntityBlock {
    // 方块的水平朝向属性（东/南/西/北）
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    // 方块的碰撞箱形状定义：从(2,0,2)到(14,9,14)的立方体（基于16x16x16方块坐标系统）
    private static final VoxelShape BOX = box(2.0, 0, 2.0, 14.0, 9.0, 14.0);

    /**
     * 构造方法，初始化方块属性
     * @param p_49224_ 方块的属性设置（硬度、抗性等）
     */
    public UnfinishedTangyuanBlock(Properties p_49224_) {
        super(p_49224_);
        // 注册默认方块状态，默认朝向为北方
        this.registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public static boolean canBePlacedAt(Level level, BlockPos blockpos1, Direction horizontalDirection) {
        return true;
    }

    /**
     * 获取方块的碰撞箱形状
     * @param pState 方块状态
     * @param pLevel 世界对象
     * @param pPos 方块位置
     * @param pContext 碰撞上下文
     * @return 预定义的碰撞箱形状BOX
     */
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BOX;
    }

    /**
     * 获取方块实体的更新逻辑处理器
     * 服务器端执行方块实体的tick逻辑，客户端不执行
     * @param pLevel 世界对象
     * @param pState 方块状态
     * @param pBlockEntityType 方块实体类型
     * @return 方块实体的更新处理器
     * @param <T> 方块实体泛型类型
     */
//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//        // 客户端返回null（不执行tick），服务器端绑定TangyuanBlockEntity的craftTick方法
//        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType,
//                ImmortalersDelightBlocks.UNFINISHED_TANGYUAN_ENTITY.get(),
//                TangyuanBlockEntity::craftTick);
//    }

    /**
     * 获取方块的渲染方式
     * @param pState 方块状态
     * @return 渲染方式为模型渲染（RenderShape.MODEL）
     */
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    /**
     * 客户端方块动画更新方法，用于生成粒子效果
     * @param pState 方块状态
     * @param pLevel 世界对象
     * @param pPos 方块位置
     * @param pRandom 随机数源
     */
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        // 获取当前位置的方块实体
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        // 若方块实体是TangyuanBlockEntity且处于工作状态（cookingTotalTime > 0）
        if (blockEntity instanceof TangyuanBlockEntity tangyuanBlockEntity && tangyuanBlockEntity.residualProgress > 0) {
            // 在方块中心位置生成烟雾粒子
            pLevel.addParticle(ParticleTypes.CLOUD,
                    pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5,
                    0.0, 0.0, 0.0);
        }
    }

    /**
     * 处理玩家与方块的交互逻辑
     * @param state 方块状态
     * @param level 世界对象
     * @param pos 方块位置
     * @param player 交互的玩家
     * @param hand 玩家使用的手（主手/副手）
     * @param hit 碰撞结果
     * @return 交互结果（成功/失败等）
     */
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        // 确保方块实体是切菜板实体
        if (tileEntity instanceof TangyuanBlockEntity tangyuanBlockEntity) {
            ItemStack heldStack = player.getItemInHand(hand); // 手持物品
            ItemStack offhandStack = player.getOffhandItem(); // 副手物品
            TangyuanRecipe tangyuanRecipe = tangyuanBlockEntity.fineRecipe();
            // 若有匹配的配方且物品使用工具
            if (tangyuanRecipe != null && tangyuanRecipe.getTool().test(heldStack)) {
                // 使用工具处理切菜板上的物品
                //ItemStack boardStack = tangyuanBlockEntity.getStoredItem().copy();
                if (tangyuanBlockEntity.tryCraftItem(1,pos,state)
                    //        tangyuanBlockEntity.processStoredItemUsingTool(heldStack, player)
                ) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
            // 如果还可以放入输入槽
            else if (tangyuanBlockEntity.tryInput(heldStack, true) || tangyuanBlockEntity.tryInput(offhandStack, true)) {
                // 处理副手物品的特殊情况（如装备类物品不能放置）
                if (!offhandStack.isEmpty()) {
                    if (hand.equals(InteractionHand.MAIN_HAND)
                            && !offhandStack.is(ModTags.OFFHAND_EQUIPMENT)
                            && !(heldStack.getItem() instanceof BlockItem)) {
                        return InteractionResult.PASS;
                    }
                    if (hand.equals(InteractionHand.OFF_HAND)
                            && offhandStack.is(ModTags.OFFHAND_EQUIPMENT)) {
                        return InteractionResult.PASS;
                    }
                }

                // 手持物品为空时不处理
                if (heldStack.isEmpty()) {
                    return InteractionResult.PASS;
                }

                // 将手持物品放置到切菜板上
                if (tangyuanBlockEntity.tryInput(player.getAbilities().instabuild ? heldStack.copy() : heldStack, false)) {
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                    return InteractionResult.SUCCESS;
                }
            } else {
                // 空手点击取回物品
                if (hand.equals(InteractionHand.MAIN_HAND) && heldStack.isEmpty()) {
                    // 优先尝试取出输出物品
                    ItemStack output = tangyuanBlockEntity.tryOutput();
                    if (!output.isEmpty()) {
                        if (!player.getInventory().add(output)) {
                            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), output);
                        }
                    } else if (!player.isCreative()) {
                        // 生存模式：放入背包，失败则掉落
                        if (!player.getInventory().add(tangyuanBlockEntity.tryRemoveInput())) {
                            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), tangyuanBlockEntity.tryRemoveInput());
                        }
                    } else {
                        // 创造模式：直接移除
                        tangyuanBlockEntity.tryRemoveInput();
                    }
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.25F, 0.5F);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

//    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
//        super.use(blockstate, world, pos, entity, hand, hit);
//        // 仅在服务器端处理界面打开逻辑
//        if (!world.isClientSide()) {
//            BlockEntity blockEntity = world.getBlockEntity(pos);
//            super.use(blockstate, world, pos, entity, hand, hit);
//            // 若方块实体是TangyuanBlockEntity，打开对应的GUI界面
//            if (blockEntity instanceof TangyuanBlockEntity ovenBlockEntity) {
//                InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ?
//                        InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
//                if (entity.getItemInHand(hand).is(ImmortalersDelightItems.SPOON.get())) {
//                    ovenBlockEntity.tryAddProgress(1);
//                }
//                ovenBlockEntity.tryAddProgress(1);
////                NetworkHooks.openScreen(((ServerPlayer) entity), ovenBlockEntity, pos);
////                return InteractionResult.CONSUME;
//            } else {
//                // 若方块实体不存在，抛出异常
//                throw new IllegalStateException("Our Container provider is missing!");
//            }
//        }
//        // 客户端返回成功
//        return InteractionResult.SUCCESS;
//    }

    /**
     * 获取方块的菜单提供器（用于GUI交互）
     * @param state 方块状态
     * @param worldIn 世界对象
     * @param pos 方块位置
     * @return 菜单提供器（方块实体自身）
     */
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        // 若方块实体是MenuProvider，则返回它，否则返回null
        //return null;
        return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    /**
     * 方块被移除时的处理逻辑
     * @param state 原方块状态
     * @param world 世界对象
     * @param pos 方块位置
     * @param newState 新方块状态
     * @param isMoving 是否正在移动（如活塞推动）
     */
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        // 仅当方块类型改变时执行（避免同类型方块替换时重复处理）
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            // 若方块实体是TangyuanBlockEntity，执行物品掉落并更新信号
            if (blockEntity instanceof TangyuanBlockEntity blenderBlockEntity) {
                blenderBlockEntity.drops(blenderBlockEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    /**
     * 注册方块的状态属性（此处注册朝向属性）
     * @param pBuilder 状态定义构建器
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    /**
     * 确定方块放置时的初始状态（基于玩家朝向）
     * @param pContext 方块放置上下文
     * @return 带有初始朝向的方块状态
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        // 方块朝向设置为玩家的水平朝向（与玩家面对方向一致）
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    /**
     * 创建并返回当前方块对应的方块实体
     * @param pos 方块位置
     * @param state 方块状态
     * @return 新的TangyuanBlockEntity实例
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TangyuanBlockEntity(pos, state);
    }


    /**
     * 工具雕刻事件监听器，处理潜行时使用工具在切菜板上雕刻的逻辑
     */
    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class ToolCarvingEvent {
        public ToolCarvingEvent() {
        }

        /**
         * 监听玩家右键方块事件，处理潜行时使用工具雕刻的逻辑
         * @param event 玩家交互事件
         */
        @SubscribeEvent
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();
            ItemStack heldStack = player.getMainHandItem();
            BlockEntity tileEntity = level.getBlockEntity(event.getPos());

            // 条件：玩家潜行、手持工具（有等级的工具/三叉戟/剪刀）、目标是切菜板实体
            if (player.isSecondaryUseActive() && !heldStack.isEmpty() && tileEntity instanceof TangyuanBlockEntity tangyuanBlockEntity) {
                // 执行雕刻逻辑
                tangyuanBlockEntity.setItem(TangyuanBlockEntity.CONTAINER_SLOT, heldStack);
                boolean success = !tangyuanBlockEntity.getItem(TangyuanBlockEntity.CONTAINER_SLOT).isEmpty();
                if (success) {
                    // 播放音效并取消原事件（避免触发其他交互）
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }
    }
}
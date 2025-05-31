package com.renyigesai.immortalers_delight.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 活塞基础方块类，继承自 DirectionalBlock，用于处理活塞方块的行为，包括放置、触发、移动方块等操作。
 */
public class MetalCaltropBlock extends DirectionalBlock {
    // 定义实体伤害值
    private float entity_damage = 1.0F;
    // 定义活塞的扩展状态属性，用于表示活塞是否处于扩展状态
    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;
    // 触发活塞扩展的事件编号
    public static final int TRIGGER_EXTEND = 0;
    // 触发活塞收缩的事件编号
    public static final int TRIGGER_CONTRACT = 1;
    // 触发活塞掉落的事件编号
    public static final int TRIGGER_DROP = 2;
    // 活塞平台的厚度
    public static final float PLATFORM_THICKNESS = 4.0F;
    // 活塞面向东方时的碰撞形状
    protected static final VoxelShape EAST_AABB = Block.box(1.0D, 1.0D, 1.0D, 12.0D, 15.0D, 15.0D);
    // 活塞面向西方时的碰撞形状
    protected static final VoxelShape WEST_AABB = Block.box(4.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    // 活塞面向南方时的碰撞形状
    protected static final VoxelShape SOUTH_AABB = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 12.0D);
    // 活塞面向北方时的碰撞形状
    protected static final VoxelShape NORTH_AABB = Block.box(1.0D, 1.0D, 4.0D, 15.0D, 15.0D, 15.0D);
    // 活塞面向上方时的碰撞形状
    protected static final VoxelShape UP_AABB = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 12.0D, 15.0D);
    // 活塞面向下方时的碰撞形状
    protected static final VoxelShape DOWN_AABB = Block.box(1.0D, 4.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    // 表示该活塞是否为粘性活塞
    private final boolean isSticky;

    /**
     * 构造函数，初始化活塞方块的属性。
     * @param pIsSticky 是否为粘性活塞
     * @param pProperties 方块的行为属性
     */
    public MetalCaltropBlock(boolean pIsSticky, Properties pProperties) {
        super(pProperties);
        // 注册默认的方块状态，包括面向方向和扩展状态
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EXTENDED, Boolean.valueOf(false)));
        this.isSticky = pIsSticky;
    }

    public MetalCaltropBlock(boolean pIsSticky, float entity_damage,Properties pProperties) {
        super(pProperties);
        // 注册默认的方块状态，包括面向方向和扩展状态
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(EXTENDED, Boolean.valueOf(false)));
        this.isSticky = pIsSticky;
        this.entity_damage = entity_damage;
    }

    /**
     * 根据方块状态返回其碰撞形状。
     * @param pState 方块状态
     * @param pLevel 方块所在的世界
     * @param pPos 方块的位置
     * @param pContext 碰撞上下文
     * @return 方块的碰撞形状
     */
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (!pState.getValue(EXTENDED)) {
            // 根据活塞的面向方向返回对应的碰撞形状
            switch ((Direction)pState.getValue(FACING)) {
                case DOWN:
                    return DOWN_AABB;
                case UP:
                default:
                    return UP_AABB;
                case NORTH:
                    return NORTH_AABB;
                case SOUTH:
                    return SOUTH_AABB;
                case WEST:
                    return WEST_AABB;
                case EAST:
                    return EAST_AABB;
            }
        } else {
            // 未扩展时返回完整方块的形状
            return Shapes.block();
        }
    }

    /**
     * 当方块被放置后调用，检查是否需要扩展活塞。
     * @param pLevel 方块所在的世界
     * @param pPos 方块的位置
     * @param pState 方块状态
     * @param pPlacer 放置方块的实体
     * @param pStack 放置方块的物品栈
     */
//    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
//        if (!pLevel.isClientSide) {
//            // 在服务器端检查是否需要扩展活塞
//            this.checkIfExtend(pLevel, pPos, pState);
//        }
//    }

    /**
     * 当邻居方块发生变化时调用，检查是否需要扩展或收缩活塞。
     * @param pState 方块状态
     * @param pLevel 方块所在的世界
     * @param pPos 方块的位置
     * @param pBlock 发生变化的邻居方块
     * @param pFromPos 邻居方块的位置
     * @param pIsMoving 方块是否正在移动
     */
//    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
//        if (!pLevel.isClientSide) {
//            // 在服务器端检查是否需要扩展或收缩活塞
//            this.checkIfExtend(pLevel, pPos, pState);
//        }
//    }

    /**
     * 当方块被放置时调用，检查是否需要扩展活塞。
     * @param pState 方块状态
     * @param pLevel 方块所在的世界
     * @param pPos 方块的位置
     * @param pOldState 旧的方块状态
     * @param pIsMoving 方块是否正在移动
     */
//    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
//        if (!pOldState.is(pState.getBlock())) {
//            if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) == null) {
//                // 在服务器端且方块实体为空时检查是否需要扩展活塞
//                this.checkIfExtend(pLevel, pPos, pState);
//            }
//        }
//    }

    /**
     * 获取方块放置时的状态。
     * @param pContext 方块放置上下文
     * @return 方块放置时的状态
     */
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getNearestLookingDirection().getOpposite())
                .setValue(EXTENDED, Boolean.valueOf(false));
    }

    /**
     * 检查活塞是否需要扩展或收缩。
     * @param pLevel 方块所在的世界
     * @param pPos 方块的位置
     * @param pState 方块状态
     */
//    private void checkIfExtend(Level pLevel, BlockPos pPos, BlockState pState) {
//        // 获取活塞的面向方向
//        Direction direction = pState.getValue(FACING);
//        // 检查活塞周围的方块是否提供信号
//        boolean flag = this.getNeighborSignal(pLevel, pPos, direction);
//        if (flag && !pState.getValue(EXTENDED)) {
//            // 如果有信号且活塞未扩展，尝试扩展活塞
//            if ((new PistonStructureResolver(pLevel, pPos, direction, true)).resolve()) {
//                pLevel.blockEvent(pPos, this, 0, direction.get3DDataValue());
//            }
//        } else if (!flag && pState.getValue(EXTENDED)) {
//            // 如果没有信号且活塞已扩展，尝试收缩活塞
//            BlockPos blockpos = pPos.relative(direction, 2);
//            BlockState blockstate = pLevel.getBlockState(blockpos);
//            int i = 1;
//            if (blockstate.is(Blocks.MOVING_PISTON) && blockstate.getValue(FACING) == direction) {
//                BlockEntity blockentity = pLevel.getBlockEntity(blockpos);
//                if (blockentity instanceof PistonMovingBlockEntity) {
//                    PistonMovingBlockEntity pistonmovingblockentity = (PistonMovingBlockEntity)blockentity;
//                    if (pistonmovingblockentity.isExtending() && (pistonmovingblockentity.getProgress(0.0F) < 0.5F || pLevel.getGameTime() == pistonmovingblockentity.getLastTicked() || ((ServerLevel)pLevel).isHandlingTick())) {
//                        i = 2;
//                    }
//                }
//            }
//            pLevel.blockEvent(pPos, this, i, direction.get3DDataValue());
//        }
//    }

    /**
     * 检查活塞周围的方块是否提供信号。
     * @param pSignalGetter 信号获取器
     * @param pPos 方块的位置
     * @param pDirection 活塞的面向方向
     * @return 是否有信号
     */
    private boolean getNeighborSignal(SignalGetter pSignalGetter, BlockPos pPos, Direction pDirection) {
        // 检查除面向方向外的其他方向是否有信号
        for(Direction direction : Direction.values()) {
            if (direction != pDirection && pSignalGetter.hasSignal(pPos.relative(direction), direction)) {
                return true;
            }
        }
        // 检查下方是否有信号
        if (pSignalGetter.hasSignal(pPos, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockpos = pPos.above();
            // 检查上方周围的方块是否有信号
            for(Direction direction1 : Direction.values()) {
                if (direction1 != Direction.DOWN && pSignalGetter.hasSignal(blockpos.relative(direction1), direction1)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 处理方块事件，包括活塞的扩展、收缩和掉落事件。
     * @param pState 方块状态
     * @param pLevel 方块所在的世界
     * @param pPos 方块的位置
     * @param pId 事件编号
     * @param pParam 事件参数
     * @return 是否处理成功
     */
//    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
//        // 获取活塞的面向方向
//        Direction direction = pState.getValue(FACING);
//        // 设置活塞为扩展状态
//        BlockState blockstate = pState.setValue(EXTENDED, Boolean.valueOf(true));
//        if (!pLevel.isClientSide) {
//            // 在服务器端检查是否有信号
//            boolean flag = this.getNeighborSignal(pLevel, pPos, direction);
//            if (flag && (pId == 1 || pId == 2)) {
//                // 如果有信号且是收缩或掉落事件，设置方块为扩展状态
//                pLevel.setBlock(pPos, blockstate, 2);
//                return false;
//            }
//            if (!flag && pId == 0) {
//                // 如果没有信号且是扩展事件，不处理
//                return false;
//            }
//        }
//        if (pId == 0) {
//            // 扩展事件
//            if (net.minecraftforge.event.ForgeEventFactory.onPistonMovePre(pLevel, pPos, direction, true)) return false;
//            if (!this.moveBlocks(pLevel, pPos, direction, true)) {
//                return false;
//            }
//            pLevel.setBlock(pPos, blockstate, 67);
//            pLevel.playSound((Player)null, pPos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, pLevel.random.nextFloat() * 0.25F + 0.6F);
//            pLevel.gameEvent(GameEvent.BLOCK_ACTIVATE, pPos, GameEvent.Context.of(blockstate));
//        } else if (pId == 1 || pId == 2) {
//            // 收缩或掉落事件
//            if (net.minecraftforge.event.ForgeEventFactory.onPistonMovePre(pLevel, pPos, direction, false)) return false;
//            BlockEntity blockentity1 = pLevel.getBlockEntity(pPos.relative(direction));
//            if (blockentity1 instanceof PistonMovingBlockEntity) {
//                ((PistonMovingBlockEntity)blockentity1).finalTick();
//            }
//            BlockState blockstate1 = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, direction).setValue(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
//            pLevel.setBlock(pPos, blockstate1, 20);
//            pLevel.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(pPos, blockstate1, this.defaultBlockState().setValue(FACING, Direction.from3DDataValue(pParam & 7)), direction, false, true));
//            pLevel.blockUpdated(pPos, blockstate1.getBlock());
//            blockstate1.updateNeighbourShapes(pLevel, pPos, 2);
//            if (this.isSticky) {
//                BlockPos blockpos = pPos.offset(direction.getStepX() * 2, direction.getStepY() * 2, direction.getStepZ() * 2);
//                BlockState blockstate2 = pLevel.getBlockState(blockpos);
//                boolean flag1 = false;
//                if (blockstate2.is(Blocks.MOVING_PISTON)) {
//                    BlockEntity blockentity = pLevel.getBlockEntity(blockpos);
//                    if (blockentity instanceof PistonMovingBlockEntity) {
//                        PistonMovingBlockEntity pistonmovingblockentity = (PistonMovingBlockEntity)blockentity;
//                        if (pistonmovingblockentity.getDirection() == direction && pistonmovingblockentity.isExtending()) {
//                            pistonmovingblockentity.finalTick();
//                            flag1 = true;
//                        }
//                    }
//                }
//                if (!flag1) {
//                    if (pId != 1 || blockstate2.isAir() || !isPushable(blockstate2, pLevel, blockpos, direction.getOpposite(), false, direction) || blockstate2.getPistonPushReaction() != PushReaction.NORMAL && !blockstate2.is(Blocks.PISTON) && !blockstate2.is(Blocks.STICKY_PISTON)) {
//                        pLevel.removeBlock(pPos.relative(direction), false);
//                    } else {
//                        this.moveBlocks(pLevel, pPos, direction, false);
//                    }
//                }
//            } else {
//                pLevel.removeBlock(pPos.relative(direction), false);
//            }
//            pLevel.playSound((Player)null, pPos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, pLevel.random.nextFloat() * 0.15F + 0.6F);
//            pLevel.gameEvent(GameEvent.BLOCK_DEACTIVATE, pPos, GameEvent.Context.of(blockstate1));
//        }
//        net.minecraftforge.event.ForgeEventFactory.onPistonMovePost(pLevel, pPos, direction, (pId == 0));
//        return true;
//    }

       /**
    * Checks if the piston can push the given BlockState.
    * 检查活塞是否可以推动给定的方块状态。
    * 
    * @param pState            要检查的方块状态
    * @param pLevel            方块所在的世界
    * @param pPos              方块的位置
    * @param pMovementDirection 活塞推动的方向
    * @param pAllowDestroy     是否允许破坏方块
    * @param pPistonFacing     活塞的朝向
    * @return 如果可以推动返回true，否则返回false
    */
//   public static boolean isPushable(BlockState pState, Level pLevel, BlockPos pPos, Direction pMovementDirection, boolean pAllowDestroy, Direction pPistonFacing) {
//      // 检查方块位置是否在世界的有效高度范围内，并且在世界边界内
//      if (pPos.getY() >= pLevel.getMinBuildHeight() && pPos.getY() <= pLevel.getMaxBuildHeight() - 1 && pLevel.getWorldBorder().isWithinBounds(pPos)) {
//         // 如果方块是空气，直接返回true，因为空气可以被推动
//         if (pState.isAir()) {
//            return true;
//         // 排除一些无法被推动的方块，如黑曜石、哭泣的黑曜石、重生锚和强化深板岩
//         } else if (!pState.is(Blocks.OBSIDIAN) && !pState.is(Blocks.CRYING_OBSIDIAN) && !pState.is(Blocks.RESPAWN_ANCHOR) && !pState.is(Blocks.REINFORCED_DEEPSLATE)) {
//            // 检查推动方向是否为向下且方块位于世界的最低高度，若是则不能推动
//            if (pMovementDirection == Direction.DOWN && pPos.getY() == pLevel.getMinBuildHeight()) {
//               return false;
//            // 检查推动方向是否为向上且方块位于世界的最高高度，若是则不能推动
//            } else if (pMovementDirection == Direction.UP && pPos.getY() == pLevel.getMaxBuildHeight() - 1) {
//               return false;
//            } else {
//               // 排除活塞方块，如果不是活塞方块
//               if (!pState.is(Blocks.PISTON) && !pState.is(Blocks.STICKY_PISTON)) {
//                  // 如果方块的破坏速度为 -1.0F，表示不可破坏，不能推动
//                  if (pState.getDestroySpeed(pLevel, pPos) == -1.0F) {
//                     return false;
//                  }
//                  // 根据方块的活塞推动反应类型进行判断
//                  switch (pState.getPistonPushReaction()) {
//                     // 若反应类型为 BLOCK，表示方块会阻挡推动，不能推动
//                     case BLOCK:
//                        return false;
//                     // 若反应类型为 DESTROY，根据是否允许破坏方块来决定是否可以推动
//                     case DESTROY:
//                        return pAllowDestroy;
//                     // 若反应类型为 PUSH_ONLY，只有推动方向与活塞朝向一致时才能推动
//                     case PUSH_ONLY:
//                        return pMovementDirection == pPistonFacing;
//                  }
//               // 如果是活塞方块且处于伸展状态，不能推动
//               } else if (pState.getValue(EXTENDED)) {
//                  return false;
//               }
//               // 若方块没有方块实体，则可以推动
//               return !pState.hasBlockEntity();
//            }
//         } else {
//            return false;
//         }
//      } else {
//         return false;
//      }
//   }

   /**
    * 移动方块的方法，用于处理活塞伸展或收缩时方块的移动操作。
    * 
    * @param pLevel    方块所在的世界
    * @param pPos      活塞的位置
    * @param pFacing   活塞的朝向
    * @param pExtending  是否为伸展操作
    * @return 如果方块移动成功返回true，否则返回false
    */
//   private boolean moveBlocks(Level pLevel, BlockPos pPos, Direction pFacing, boolean pExtending) {
//      // 获取活塞朝向的相邻方块位置
//      BlockPos blockpos = pPos.relative(pFacing);
//      // 如果是收缩操作且相邻方块是活塞头，则将其替换为空气
//      if (!pExtending && pLevel.getBlockState(blockpos).is(Blocks.PISTON_HEAD)) {
//         pLevel.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 20);
//      }
//      // 创建一个活塞结构解析器，用于解析需要推动和破坏的方块
//      PistonStructureResolver pistonstructureresolver = new PistonStructureResolver(pLevel, pPos, pFacing, pExtending);
//      // 解析需要推动和破坏的方块，如果解析失败则返回false
//      if (!pistonstructureresolver.resolve()) {
//         return false;
//      } else {
//         // 创建一个映射，用于存储方块位置和对应的方块状态
//         Map<BlockPos, BlockState> map = Maps.newHashMap();
//         // 获取需要推动的方块位置列表
//         List<BlockPos> list = pistonstructureresolver.getToPush();
//         // 创建一个列表，用于存储需要推动的方块状态
//         List<BlockState> list1 = Lists.newArrayList();
//         // 遍历需要推动的方块位置列表，将方块状态添加到列表中，并存储到映射中
//         for(int i = 0; i < list.size(); ++i) {
//            BlockPos blockpos1 = list.get(i);
//            BlockState blockstate = pLevel.getBlockState(blockpos1);
//            list1.add(blockstate);
//            map.put(blockpos1, blockstate);
//         }
//         // 获取需要破坏的方块位置列表
//         List<BlockPos> list2 = pistonstructureresolver.getToDestroy();
//         // 创建一个数组，用于存储所有需要处理的方块状态
//         BlockState[] ablockstate = new BlockState[list.size() + list2.size()];
//         // 根据是否伸展操作确定方块移动的方向
//         Direction direction = pExtending ? pFacing : pFacing.getOpposite();
//         int j = 0;
//         // 遍历需要破坏的方块位置列表，依次处理这些方块
//         for(int k = list2.size() - 1; k >= 0; --k) {
//            BlockPos blockpos2 = list2.get(k);
//            BlockState blockstate1 = pLevel.getBlockState(blockpos2);
//            // 获取方块实体，如果有的话
//            BlockEntity blockentity = blockstate1.hasBlockEntity() ? pLevel.getBlockEntity(blockpos2) : null;
//            // 掉落方块的资源
//            dropResources(blockstate1, pLevel, blockpos2, blockentity);
//            // 将方块替换为空气
//            pLevel.setBlock(blockpos2, Blocks.AIR.defaultBlockState(), 18);
//            // 触发方块破坏的游戏事件
//            pLevel.gameEvent(GameEvent.BLOCK_DESTROY, blockpos2, GameEvent.Context.of(blockstate1));
//            // 如果方块不是火，则添加方块破坏的效果
//            if (!blockstate1.is(BlockTags.FIRE)) {
//               pLevel.addDestroyBlockEffect(blockpos2, blockstate1);
//            }
//            // 将方块状态存储到数组中
//            ablockstate[j++] = blockstate1;
//         }
//         // 遍历需要推动的方块位置列表，依次处理这些方块
//         for(int l = list.size() - 1; l >= 0; --l) {
//            BlockPos blockpos3 = list.get(l);
//            BlockState blockstate5 = pLevel.getBlockState(blockpos3);
//            // 计算方块移动后的位置
//            blockpos3 = blockpos3.relative(direction);
//            // 从映射中移除移动后的位置
//            map.remove(blockpos3);
//            // 获取移动活塞方块的默认状态，并设置朝向
//            BlockState blockstate8 = Blocks.MOVING_PISTON.defaultBlockState().setValue(FACING, pFacing);
//            // 将方块设置为移动活塞方块
//            pLevel.setBlock(blockpos3, blockstate8, 68);
//            // 创建移动活塞方块实体
//            pLevel.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(blockpos3, blockstate8, list1.get(l), pFacing, pExtending, false));
//            // 将方块状态存储到数组中
//            ablockstate[j++] = blockstate5;
//         }
//         // 如果是伸展操作
//         if (pExtending) {
//            // 根据活塞是否为粘性活塞确定活塞头的类型
//            PistonType pistontype = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
//            // 获取活塞头方块的默认状态，并设置朝向和类型
//            BlockState blockstate4 = Blocks.PISTON_HEAD.defaultBlockState().setValue(PistonHeadBlock.FACING, pFacing).setValue(PistonHeadBlock.TYPE, pistontype);
//            // 获取移动活塞方块的默认状态，并设置朝向和类型
//            BlockState blockstate6 = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, pFacing).setValue(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
//            // 从映射中移除活塞朝向的相邻方块位置
//            map.remove(blockpos);
//            // 将活塞朝向的相邻方块设置为移动活塞方块
//            pLevel.setBlock(blockpos, blockstate6, 68);
//            // 创建移动活塞方块实体
//            pLevel.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(blockpos, blockstate6, blockstate4, pFacing, true, true));
//         }
//         // 获取空气方块的默认状态
//         BlockState blockstate3 = Blocks.AIR.defaultBlockState();
//         // 遍历映射中的所有方块位置，将这些位置的方块设置为空气
//         for(BlockPos blockpos4 : map.keySet()) {
//            pLevel.setBlock(blockpos4, blockstate3, 82);
//         }
//         // 遍历映射中的所有方块位置和状态，更新方块的邻居形状
//         for(Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
//            BlockPos blockpos5 = entry.getKey();
//            BlockState blockstate2 = entry.getValue();
//            blockstate2.updateIndirectNeighbourShapes(pLevel, blockpos5, 2);
//            blockstate3.updateNeighbourShapes(pLevel, blockpos5, 2);
//            blockstate3.updateIndirectNeighbourShapes(pLevel, blockpos5, 2);
//         }
//         j = 0;
//         // 遍历需要破坏的方块状态数组，更新方块的间接邻居形状和邻居方块
//         for(int i1 = list2.size() - 1; i1 >= 0; --i1) {
//            BlockState blockstate7 = ablockstate[j++];
//            BlockPos blockpos6 = list2.get(i1);
//            blockstate7.updateIndirectNeighbourShapes(pLevel, blockpos6, 2);
//            pLevel.updateNeighborsAt(blockpos6, blockstate7.getBlock());
//         }
//         // 遍历需要推动的方块状态数组，更新邻居方块
//         for(int j1 = list.size() - 1; j1 >= 0; --j1) {
//            pLevel.updateNeighborsAt(list.get(j1), ablockstate[j++].getBlock());
//         }
//         // 如果是伸展操作，更新活塞头方块的邻居方块
//         if (pExtending) {
//            pLevel.updateNeighborsAt(blockpos, Blocks.PISTON_HEAD);
//         }
//         // 方块移动成功，返回true
//         return true;
//      }
//   }

   public void entityInside(BlockState state, Level pLevel, BlockPos pPos, Entity pEntity) {
       if (pEntity instanceof LivingEntity livingentity) {
           //造成伤害并添加buff，对非玩家生物造成的伤害不会致死
           if (livingentity instanceof Player || livingentity.getHealth() > entity_damage) {
               livingentity.hurt(pLevel.damageSources().cactus(), entity_damage);
           }
       }
   }
   /**
    * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
    * blockstate.
    * 根据给定的旋转方向旋转方块状态。如果不适用，则返回传入的方块状态。
    * 
    * @param pState 要旋转的方块状态
    * @param pRot   旋转方向
    * @return 旋转后的方块状态
    * @deprecated call via {@link BlockStateBase#rotate} whenever
    * possible. Implementing/overriding is fine.
    */
   @Deprecated
   public BlockState rotate(BlockState pState, Rotation pRot) {
      // 根据旋转方向更新方块的朝向属性
      return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
   }

   /**
    * 根据给定的旋转方向旋转方块状态，考虑方块是否伸展的情况。
    * 
    * @param state     要旋转的方块状态
    * @param world     方块所在的世界访问器
    * @param pos       方块的位置
    * @param direction 旋转方向
    * @return 旋转后的方块状态
    */
   public BlockState rotate(BlockState state, net.minecraft.world.level.LevelAccessor world, BlockPos pos, Rotation direction) {
       // 如果方块处于伸展状态，则不进行旋转，直接返回原状态；否则调用父类的旋转方法
       return state.getValue(EXTENDED) ? state : super.rotate(state, world, pos, direction);
   }

   /**
    * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
    * blockstate.
    * 根据给定的镜像方式镜像方块状态。如果不适用，则返回传入的方块状态。
    * 
    * @param pState 要镜像的方块状态
    * @param pMirror 镜像方式
    * @return 镜像后的方块状态
    * @deprecated call via {@link BlockStateBase#mirror} whenever
    * possible. Implementing/overriding is fine.
    */
   @Deprecated
   public BlockState mirror(BlockState pState, Mirror pMirror) {
      // 根据镜像方式获取旋转方向，然后旋转方块状态
      return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
   }

   /**
    * 创建方块状态定义，添加方块的朝向和伸展属性。
    * 
    * @param pBuilder 方块状态定义构建器
    */
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
      // 向构建器中添加方块的朝向和伸展属性
      pBuilder.add(FACING, EXTENDED);
   }

   /**
    * 判断方块是否使用形状进行光照遮挡。
    * 
    * @param pState 方块状态
    * @return 如果方块处于伸展状态则返回true，否则返回false
    */
   public boolean useShapeForLightOcclusion(BlockState pState) {
      // 根据方块的伸展属性判断是否使用形状进行光照遮挡
      return pState.getValue(EXTENDED);
   }

   /**
    * 判断方块是否可用于路径计算。
    * 
    * @param pState 方块状态
    * @param pLevel 方块所在的世界
    * @param pPos   方块的位置
    * @param pType  路径计算类型
    * @return 始终返回false，表示方块不可用于路径计算
    */
   public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
      // 活塞方块不可用于路径计算，返回false
      return false;
   }
}
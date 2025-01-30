package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.world.tree.BigOakTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks.*;


public class HimekaidoShrubBlock extends EvolutcornBlock {
    public HimekaidoShrubBlock(Properties p_52247_) {
        super(p_52247_);
    }
    /*
    AABB（Axis-Aligned Bounding Box，轴对齐包围盒）的偏移量
     */
    protected static final int AABB_OFFSET = 1;
    /*
    仙人掌的碰撞形状
     */
    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    /*
    仙人掌的轮廓形状
     */
    protected static final VoxelShape OUTLINE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    /*
    获取仙人掌的碰撞形状
     */
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COLLISION_SHAPE;
    }

    /*
    获取仙人掌的轮廓形状
     */
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return OUTLINE_SHAPE;
    }
    /**
     * 巨大化
     */
    public void performBonemeal(ServerLevel p_221040_, RandomSource p_221041_, BlockPos p_221042_, BlockState p_221043_) {
        Random rand = new Random(p_221041_.nextLong());
        if (rand.nextInt(10) == 0) {
            BigOakTreeGrower tree = new BigOakTreeGrower(
                    HIMEKAIDO_LOG.get().defaultBlockState(),
                    HIMEKAIDO_LEAVES.get().defaultBlockState(),
                    HIMEKAIDO_SHRUB.get().defaultBlockState()
            );
            tree.setBigTrunkSize(true);
            tree.setHeight(4,8);
            tree.setLeafDistanceLimit(5);
            tree.generate(p_221040_, p_221041_, p_221042_);
        }
        else this.growCrops(p_221040_, p_221042_, p_221043_);
    }
    /**
     * 定义收获的行为逻辑，代码来源于玩家与南瓜方块交互的方法
     */
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        /*
        检查玩家手中物品是否可以执行雕刻操作（如剪刀）
         */
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.canPerformAction(net.minecraftforge.common.ToolActions.SHEARS_CARVE)) {
            if (!pLevel.isClientSide) {
                /*
                判断方块状态，如果不是指定的收获阶段，直接返回
                 */
                if (!(pState == this.defaultBlockState().setValue(AGE,MAX_AGE))) {
                    return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
                }
                /*
                在服务器端播放南瓜雕刻的声音
                 */
                pLevel.playSound((Player)null, pPos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                /*
                使方块回到指定的生长状态（默认从头开始生长)
                 */
                pLevel.setBlock(pPos, this.defaultBlockState().setValue(AGE, 0), 11);
                /*
                调用资源掉落方法
                 */
                dropResources(pState, pLevel, pPos);
                /*
                在指定位置生成一个包含 4 个南瓜种子的物品实体，并设置其运动速度和方向，这个方法已被弃用
                 */
//                ItemEntity itementity = new ItemEntity(pLevel, (double)pPos.getX() + 0.5D + (double)direction1.getStepX() * 0.65D, (double)pPos.getY() + 0.1D, (double)pPos.getZ() + 0.5D + (double)direction1.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
//                itementity.setDeltaMovement(0.05D * (double)direction1.getStepX() + pLevel.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getStepZ() + pLevel.random.nextDouble() * 0.02D);
//                pLevel.addFreshEntity(itementity);
                /*
                 对玩家手中的工具造成 1 点耐久损耗，并触发物品损坏事件
                 */
                itemstack.hurtAndBreak(1, pPlayer, (p_55287_) -> {
                    p_55287_.broadcastBreakEvent(pHand);
                });
                /*
                 触发游戏事件，表示玩家进行了雕刻操作
                 */
                pLevel.gameEvent(pPlayer, GameEvent.SHEAR, pPos);
                /*
                 给玩家增加使用剪刀的统计数据
                 */
                pPlayer.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            }

            /*
            根据是否是客户端返回相应的交互结果
             */
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            /*
            如果玩家手中物品不能雕刻南瓜，则调用父类的交互方法
             */
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }
}

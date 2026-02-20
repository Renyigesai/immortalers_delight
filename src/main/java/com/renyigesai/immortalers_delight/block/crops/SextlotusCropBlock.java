package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.ReflectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.RichSoilBlock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SextlotusCropBlock extends ReapCropBlock{

    private static final VoxelShape[] OUTLINE_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 16.0D, 15.9D),
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 16.0D, 15.9D),
            Block.box(0.1D, 0.0D, 0.1D, 15.9D, 16.0D, 15.9D),
    };

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Shapes.empty(),
            Shapes.empty(),
            Shapes.empty(),
            Shapes.empty(),
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 1.0D, 9.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 2.0D, 10.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 3.0D, 10.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D),
    };

    //上一次生长的时间，用于避免无尽沃土等高频催熟导致频繁遍历方块或实体，以免影响性能
    protected Long lastGrowTime;
    public Long getLastGrowTime() {return lastGrowTime;}

    public SextlotusCropBlock(Properties p_52247_) {
        super(p_52247_);
        lastGrowTime = 0L;
    }
    @Override
    protected @NotNull ItemLike getBaseSeedId() {return ImmortalersDelightItems.SEXTLOTUS_SEEDS.get();}
    @Override
    public VoxelShape getShape(BlockState p_51330_, BlockGetter p_51331_, BlockPos p_51332_, CollisionContext p_51333_) {
        return OUTLINE_SHAPE_BY_AGE[this.getAge(p_51330_)];
    }
    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[this.getAge(pState)];
    }
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        if (pState.getBlock() == this)
            return pLevel.getBlockState(blockpos).canSustainPlant(pLevel, blockpos, Direction.UP, this);
        return this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);
    }
    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.SOUL_SAND) || pState.is(Blocks.SOUL_SOIL);
    }
    public byte hasNearCrop(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighborState = level.getBlockState(pos.relative(direction));
            if (neighborState.getBlock() instanceof SextlotusCropBlock sextlotusCropBlock) {
                if (neighborState.hasProperty(SextlotusCropBlock.AGE) && neighborState.getValue(SextlotusCropBlock.AGE) >= sextlotusCropBlock.getMaxAge()) {
                    return 1;
                } else return  -1;
            }
        }
        return 0;
    }

    //吞噬转化周围植物方块
    public boolean devourEutrophic(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof SextlotusCropBlock) return false;
        if (state.getBlock() instanceof IPlantable) {
            if (!state.is(Blocks.DEAD_BUSH)) level.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), Block.UPDATE_CLIENTS);
            else level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return true;
        }
        if (state.is(BlockTags.LEAVES)) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return true;
        }
        if (state.is(BlockTags.DIRT) || state.getBlock() instanceof FarmBlock || state.getBlock() instanceof RichSoilBlock) {
            if (!state.is(Blocks.COARSE_DIRT)) level.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());
            else level.setBlockAndUpdate(pos, Blocks.SAND.defaultBlockState());
            return true;
        }
        if (state.getBlock() instanceof CoralBlock coralBlock) {
            Block deadBlock = ReflectionUtil.getCoralDeadBlock(coralBlock);
            if (deadBlock != null) {
                level.setBlockAndUpdate(pos, deadBlock.defaultBlockState());
                return true;
            } else return false;
        }
        return false;
    }


    // 存储坐标 + 对应曼哈顿距离的记录类（Record仅在Java 16+ 可用）
    private record PosWithDistance(BlockPos pos, int manhattanDistance) {}

    /**
     * 计算两个BlockPos之间的三维曼哈顿距离
     * @param pos 目标坐标
     * @param center 中心坐标
     * @return 曼哈顿距离（非负整数）
     */
    private static int calculateManhattanDistance(BlockPos pos, BlockPos center) {
        // 三维曼哈顿距离：|x1-x0| + |y1-y0| + |z1-z0|
        int dx = Math.abs(pos.getX() - center.getX());
        int dy = Math.abs(pos.getY() - center.getY());
        int dz = Math.abs(pos.getZ() - center.getZ());
        return dx + dy + dz;
    }

    //搜索相连的植物或土质方块
    public int findNeighborPlant(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int sum = 0;
        //列表记录待搜索的坐标与对应的曼哈顿距离
        List<PosWithDistance> posHigh = new ArrayList<>();
        List<PosWithDistance> posParallel = new ArrayList<>();
        List<PosWithDistance> posLow = new ArrayList<>();
        //通过8个"探针"确定搜索范围内的区块加载情况,每个探针决定是否要搜索对应的3*3*3区域
        for (int a = -1; a <= 1; ++a) {
            for (int b = -1; b <= 1; ++b) {
                BlockPos pos1 = pos.offset(a*3, 0, b*3);
                if (level.isAreaLoaded(pos1, 1)) {
                    //确定该区域在加载区块内，再搜索该区域
                    for (int i = -1; i <= 1; ++i) {
                        for (int j = -1; j <= 1; ++j) {
                            for (int k = -1; k <= 1; ++k) {
                                BlockPos pos2 = pos1.offset(i, j, k);
                                // 计算到中心的曼哈顿距离
                                int distance = calculateManhattanDistance(pos2, pos);
                                // 存入列表
                                if (j == 1) posHigh.add(new PosWithDistance(pos2, distance));
                                if (j == 0) posParallel.add(new PosWithDistance(pos2, distance));
                                if (j == -1) posLow.add(new PosWithDistance(pos2, distance));
                            }
                        }
                    }
                }
            }
        }

        // 按曼哈顿距离排序（升序：近→远；如需降序则反转Comparator）
        posHigh.sort(Comparator.comparingInt(PosWithDistance::manhattanDistance));
        posParallel.sort(Comparator.comparingInt(PosWithDistance::manhattanDistance));
        posLow.sort(Comparator.comparingInt(PosWithDistance::manhattanDistance));

        // 合并列表
        List<PosWithDistance> posList = new ArrayList<>();
        posList.addAll(posHigh);
        posList.addAll(posParallel);
        posList.addAll(posLow);

        // 按照曼哈顿距离的顺序进行吞噬，同时曼哈顿距离也决定吞噬的概率
        for (PosWithDistance pwd : posList) {
            BlockPos pos3 = pwd.pos();
            int distance = pwd.manhattanDistance();
            if (distance == 0) continue;//忽视本身
            float probability = 1.0F - distance * 0.1f;
            //进行吞噬
            if (random.nextFloat() < probability && devourEutrophic(level, pos3)) {
                sum += 1;
            }
            if (sum >= 31) return sum;
        }
        return sum;
    }

    /**
     * 随机刻逻辑（游戏随机触发的生长行为）
     * 注：@SuppressWarnings("deprecation") 屏蔽MC旧API的弃用警告
     * @param state 方块当前状态
     * @param worldIn 服务端世界实例（仅服务端执行随机刻）
     * @param pos 方块坐标
     * @param random 随机数源
     */
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        //判断基础生长条件：在已加载区块
        if (!worldIn.isAreaLoaded(pos, 1)) return;
        byte canGrow = hasNearCrop(worldIn, pos);
        //若无相邻的成熟作物，需要在暗处生长
        if (canGrow > 0 || worldIn.getRawBrightness(pos, 0) >= 9) return;
        //若有相邻的未成熟作物，则不进行生长
        if (canGrow < 0) return;

        int i = this.getAge(state);
        if (i < this.getMaxAge()) {
            //微调搜索频率，令 random.nextInt((int) (7 / sum) + 1) == 0 的期望0.46354调整为 random.nextInt((8 - sum) + 1) == 0 的期望0.33972
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(9999) < 7329)) {
                //记录吞噬的植物数量，初始值为1以免造成除0错误
                int sum = 1;
                //搜索3*3*3区域，分y层从上到下搜索，如果当前层搜索到有植物方块，则停止搜索
                for (int y = 1; y >= -1; --y) {
                    for (int x = 1; x >= -1; --x) {
                        for (int z = 1; z >= -1; --z) {
                            BlockPos pos1 = pos.offset(x, y, z);
                            if (x == 0 && y <= 0 && z == 0) continue; // 跳过中心
                            //进行吞噬
                            if (devourEutrophic(worldIn, pos1)) {
                                sum += 1;
                                //随机生长每次最多吞噬7个方块
                                if (sum >= 8) break;
                            }
                        }
                    }
                    if (sum > 1) break;
                }
                int newAge = i + 1;
                //初始值为1会导致不吞噬也有小概率生长，因此限定除非在满月，不吞噬不能完全成熟
                if (newAge >= this.getMaxAge() && worldIn.getMoonPhase() != 4 && sum <= 1) newAge = this.getMaxAge() - 1;
                //实际进行生长
                if (newAge != i && random.nextInt((7 / sum) + 1) == 0) {
                    worldIn.setBlock(pos, this.getStateForAge(i + 1), Block.UPDATE_CLIENTS);
                }
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
        }

    }

    // 催熟时的方法，保底生长一个阶段
    // 大范围搜索并吞噬植物方块，根据吞噬到的植物数量，额外生长1~4个阶段
    @Override
    public void growCrops(Level level, BlockPos pPos, BlockState pState) {
        //判断上一次成熟时间，避免高频率大范围遍历坐标导致性能问题
        if (level instanceof ServerLevel serverLevel && level.getGameTime() - this.lastGrowTime >= 50) {
            this.lastGrowTime = level.getGameTime();
            int currentAge = pState.getValue(AGE);
            int newAge = currentAge + 1;

            if (newAge < this.getMaxAge()) {
                float f = findNeighborPlant(pState, serverLevel, pPos, level.getRandom());
                if (f < 1) f = 1;
                if (level.getRandom().nextInt((int) (29.53F / f) + 1) == 0) {
                    int dAge = 1;
                    for (int i = 0; i <= (f - 29.53f); i++) dAge *= 2;
                    newAge += level.getRandom().nextInt(dAge) + 1;
                }
            }
            level.setBlock(pPos, pState.setValue(AGE, newAge), Block.UPDATE_CLIENTS);
        }
    }


//            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(pPos).inflate(5.0D, 5.0D, 5.0D));
//            if (!list.isEmpty()) {
//                List<LivingEntity> useList = new ArrayList<>();
//                for (LivingEntity livingentity : list) {
//                    if (!(livingentity instanceof Enemy)){
//                        useList.add(livingentity);
//                    }
//                    int k = useList.size();
//                    if (k > 0) {
//                        for (LivingEntity hurtOne : useList) {
//                            //将49点伤害分配给周围实体
//                            if (hurtOne instanceof Player player
//                                    && !player.isCreative()
//                                    && hurtOne.getHealth() > hurtOne.getMaxHealth() * 0.05f)
//                                hurtOne.setHealth(hurtOne.getHealth() - hurtOne.getMaxHealth() * 0.05f);
//                            hurtOne.hurt(level.damageSources().wither(), 49.0F / k);
//                            f = true;
//                        }
//                    }
//                }
//            }
}

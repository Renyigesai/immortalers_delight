package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.util.ReflectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

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
    protected long growProgress;
    public long getGrowProgress() {return growProgress;}

    public SextlotusCropBlock(Properties p_52247_) {
        super(p_52247_);
        growProgress = 0;
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
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        BlockState blockstate1 = pLevel.getBlockState(pCurrentPos.below());
        if(blockstate1.is(BlockTags.DIRT) || blockstate1.is(BlockTags.SAND) || blockstate1.getBlock() instanceof FarmBlock) return pState;
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public boolean canReap(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean flag = Config.rightClickHarvest;
        return (flag && state.getValue(AGE) == getMaxAge() - 1) || super.canReap(state, level, pos, player, hand, hitResult);
    }
//    @Override
//    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
//        BlockPos blockpos = pPos.below();
//        if (pState.getBlock() == this)
//            return pLevel.getBlockState(blockpos).canSustainPlant(pLevel, blockpos, Direction.UP, this);
//        return this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);
//    }
//    @Override
//    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
//        return pState.is(BlockTags.DIRT) || pState.is(BlockTags.SAND);
//    }

    public byte hasNearLantern(ServerLevel level, BlockPos pos) {
        byte i = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighborState = level.getBlockState(pos.relative(direction));
            if (neighborState.getBlock() == ImmortalersDelightBlocks.SEXTLOTUS_LANTERN.get()) return 2;
            if (neighborState.getBlock() instanceof SextlotusCropBlock sextlotusCropBlock) {
                if (neighborState.hasProperty(SextlotusCropBlock.AGE) && neighborState.getValue(SextlotusCropBlock.AGE) >= sextlotusCropBlock.getMaxAge()) {
                    return 1;
                } else i = -1;
            }
        }
        return i;
    }

    //吞噬转化周围植物方块
    public int devourEutrophic(ServerLevel level, BlockPos pos, float chance, int max) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof SextlotusCropBlock) return 0;

        if (max >= 3 && state.is(ImmortalersDelightTags.SEXTLOTUS_TRANSFORM_COAL)) {
            if (level.getRandom().nextFloat() < chance) level.setBlockAndUpdate(pos, Blocks.COAL_BLOCK.defaultBlockState());
            else level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            return 3;
        }
        if (max >= 1 && state.is(ImmortalersDelightTags.SEXTLOTUS_TRANSFORM_DIRT)) {
            level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            return 1;
        }
        if (max >= 1 && state.is(ImmortalersDelightTags.SEXTLOTUS_TRANSFORM_SAND) || state.is(Blocks.COARSE_DIRT)) {
            if (!state.is(Blocks.COARSE_DIRT)) level.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());
            else level.setBlockAndUpdate(pos, Blocks.SAND.defaultBlockState());
            return 1;
        }
        if (max >= 1 && state.is(ImmortalersDelightTags.SEXTLOTUS_TRANSFORM_AIR) || state.is(Blocks.DEAD_BUSH)) {
            if (!state.is(Blocks.DEAD_BUSH)) level.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), Block.UPDATE_CLIENTS);
            else level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return 1;
        }
        if (max >= 2 && state.is(ImmortalersDelightTags.SEXTLOTUS_TRANSFORM_SPECIAL)) {
            if (state.getBlock() instanceof CoralBlock coralBlock) {
                Block deadBlock = ReflectionUtil.getCoralDeadBlock(coralBlock);
                if (deadBlock != null) {
                    level.setBlockAndUpdate(pos, deadBlock.defaultBlockState());
                    return 2;
                } else return 0;
            }
            else if (state.getBlock() instanceof CoralPlantBlock coralBlock) {
                Block deadBlock = ReflectionUtil.getPlantCoralDeadBlock(coralBlock);
                if (deadBlock != null) {
                    level.setBlockAndUpdate(pos, deadBlock.defaultBlockState());
                    return 2;
                } else return 0;
            }
            else if (state.getBlock() instanceof CoralWallFanBlock coralBlock) {
                Block deadBlock = ReflectionUtil.getCoralWallFanDeadBlock(coralBlock);
                if (deadBlock != null) {
                    level.setBlockAndUpdate(pos, deadBlock.defaultBlockState());
                    return 2;
                } else return 0;
            }
            else if (state.getBlock() instanceof CoralFanBlock coralBlock) {
                Block deadBlock = ReflectionUtil.getCoralFanDeadBlock(coralBlock);
                if (deadBlock != null) {
                    level.setBlockAndUpdate(pos, deadBlock.defaultBlockState());
                    return 2;
                } else return 0;
            }
            else {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return 2;
            }
        }
        return 0;
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
    public int findNeighborPlant(BlockState state, ServerLevel level, BlockPos pos, boolean treeBreaker) {
        int sum = 0;
        RandomSource random = level.getRandom();
        //列表记录待搜索的坐标与对应的曼哈顿距离
        List<PosWithDistance> posList = new ArrayList<>();

        //通过8个"探针"确定搜索范围内的区块加载情况,每个探针决定是否要搜索对应的3*3*3区域
        for (int a = -1; a <= 1; ++a) {
            for (int b = -1; b <= 1; ++b) {
                BlockPos pos1 = pos.offset(a*3, 0, b*3);
                int minJ = -1;
                int maxJ = 1;
                if (a == 0) maxJ += 3;
                if (b == 0) maxJ += 3;
                //毁灭树木模式下，吸收范围将颠倒
                if (treeBreaker) {
                    minJ = 5;
                    maxJ = 7;
                    if (a == 0) minJ -= 3;
                    if (b == 0) minJ -= 3;
                }
                if (level.isAreaLoaded(pos1, 1)) {
                    //确定该区域在加载区块内，再搜索该区域
                    for (int i = -1; i <= 1; ++i) {
                        for (int j = minJ; j <= maxJ; ++j) {
                            for (int k = -1; k <= 1; ++k) {
                                BlockPos pos2 = pos1.offset(i, j, k);
                                // 计算到中心的曼哈顿距离
                                int distance = calculateManhattanDistance(pos2, pos);
                                //因为在9*9范围内，曼哈顿距离最大为9，因此减9j即可区分每个y层级并且按顺序排列
                                distance -= j * 9;
                                // 存入列表
                                posList.add(new PosWithDistance(pos2, distance));
                            }
                        }
                    }
                }
            }
        }

        // 按曼哈顿距离排序（升序：近→远；如需降序则反转Comparator）
        posList.sort(Comparator.comparingInt(PosWithDistance::manhattanDistance));

        // 按照曼哈顿距离的顺序进行吞噬，同时曼哈顿距离也决定吞噬的概率
        for (PosWithDistance pwd : posList) {
            BlockPos pos3 = pwd.pos();
            int distance = Math.abs(pwd.manhattanDistance()) % 9;
            if (level.getBlockState(pos3.above()).getBlock() == this) continue;//忽视本身
            float probability = 1.0F - distance * 0.1f;
            //进行吞噬
            if (random.nextFloat() < probability) {
                int diff = 31 - sum;
                sum += devourEutrophic(level, pos3,  (this.getAge(state) + 1.0F) / this.getMaxAge(), diff);
            }
            if (sum >= 31) return sum;
        }
        return sum;
    }

    public boolean isRightMoonAge(int age, Level worldIn) {
        int antiGrowMoonAge = age;
        if (antiGrowMoonAge >= 8) antiGrowMoonAge -= 8;
        return worldIn.getMoonPhase() != antiGrowMoonAge;
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
        int i = this.getAge(state);
        byte canGrow = hasNearLantern(worldIn, pos);
        //使用幻月灯可以无视月相生长(但也会造成其他影响)
        boolean isNeededMoonPhase = canGrow > 1;
        //若无相邻的成熟作物，需要在暗处生长
        if (canGrow <= 0 && i < this.getMaxAge() - 1 && worldIn.getRawBrightness(pos, 0) >= 9) return;
        //若有相邻的未成熟作物，则不进行生长
        if (canGrow < 0) return;
        //每个生长阶段有两个月相不能生长，且6阶段对应月相为7,0(满月与满月前一个月相)，因此植株将随着月相生长、在满月的后一天成熟
        if (!isNeededMoonPhase) isNeededMoonPhase = isRightMoonAge(i, worldIn) && isRightMoonAge(i + 1, worldIn);


        if (i < this.getMaxAge()) {

            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
                    isNeededMoonPhase && random.nextInt(833) > 19)) {
                //记录吞噬的植物数量，初始值为1以免造成除0错误
                int sum = 1;
                //搜索3*6*3区域，分y层从上到下搜索，如果当前层为3*3*3以内且搜索到有植物方块，则停止搜索
                for (int y = 4; y >= -1; --y) {
                    for (int x = 1; x >= -1; --x) {
                        for (int z = 1; z >= -1; --z) {
                            BlockPos pos1 = pos.offset(x, y, z);
                            if (x == 0 && y <= 0 && z == 0) continue; // 跳过中心
                            //进行吞噬
                            int diff = 8 - sum;
                            sum += devourEutrophic(worldIn, pos1, (this.getAge(state) + 1.0F) / this.getMaxAge(), diff);
                            //随机生长每次最多吞噬7个方块
                            if (sum >= 8) break;
                        }
                    }
                    if (y <= 1 && sum > 1) break;
                }
                //使用幻月灯的的代价：生长速度-25%同时不吞噬不再自然生长，以免幻月灯过于逃课
                if (canGrow > 1) sum -= 2;
                //实际进行生长
                if (sum > 0 && random.nextInt((7 / sum) + 1) == 0) {
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
        //判断当前生长进度为计时制(正数)或计数制(负数)
        if (level instanceof ServerLevel serverLevel && this.growProgress >= 0) {
            //计时制：避免高频率大范围遍历坐标导致性能问题
            if (level.getGameTime() - this.growProgress >= 50) {
                this.growProgress = level.getGameTime();
                int currentAge = pState.getValue(AGE);
                int newAge = currentAge + 1;
                float f = 1;

                if (newAge < this.getMaxAge()) {
                    f = Math.max(findNeighborPlant(pState, serverLevel, pPos, !level.canSeeSky(pPos)), 1.0F);

                    if (level.getRandom().nextInt((int) (29.53F / f) + 1) == 0) {
                        int dAge = 1;
                        for (int i = 0; i <= (f - 29.53f); i++) dAge *= 2;
                        newAge += level.getRandom().nextInt(dAge) + 1;
                    }
                }
                //如果吸收的方块数量不足7，则将下一次成熟的时间设置为-50以启动计数催熟模式
                if (f < 8) this.growProgress = -50;
                //实际的生长方法
                if (newAge > this.getMaxAge()) newAge = this.getMaxAge();
                level.setBlock(pPos, pState.setValue(AGE, newAge), Block.UPDATE_CLIENTS);
            }
            //计数制：每触发一次催熟，令进度+1直到进度恢复到0后可以正常生长
        } else this.growProgress += 1;
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

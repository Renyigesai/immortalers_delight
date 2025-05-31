package com.renyigesai.immortalers_delight.world.tree;

import java.util.*;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.RotatedPillarBlock.AXIS;

//just gen it in worldGenNew
/**
  * WorldGenTestTree类用于在游戏世界中生成特定的树，继承自WorldGenAbstractTree
 */
public class BigOakTreeGrower extends AbstractTreeGrower {

    /**
     * 生成树所用的原木、树叶与对应的树苗方块和种植所需的“耕地方块”
     */
    BlockState logBlock = Blocks.OAK_LOG.defaultBlockState();
    BlockState leavesBlock = Blocks.OAK_LEAVES.defaultBlockState();
    BlockState saplingBlock = Blocks.OAK_SAPLING.defaultBlockState();
    BlockState placeOnBlock = Blocks.DIRT.defaultBlockState();
    /**
     * 构造函数，调用父类构造函数并传入自定义的变种方块，使用空值构造将生成默认的橡树
     */
    public BigOakTreeGrower() {
        super();
    }
    public BigOakTreeGrower(BlockState logBlockIn, BlockState leavesBlockIn, BlockState saplingBlockIn) {
        super();
        logBlock = logBlockIn;
        leavesBlock = leavesBlockIn;
        saplingBlock = saplingBlockIn;
    }
    public BigOakTreeGrower(BlockState logBlockIn, BlockState leavesBlockIn, BlockState saplingBlockIn, BlockState placeOnBlockIn) {
        super();
        logBlock = logBlockIn;
        leavesBlock = leavesBlockIn;
        saplingBlock = saplingBlockIn;
        placeOnBlock = placeOnBlockIn;
    }

    /**
     * 实现父类中的抽象方法，但....我们不走数据包所以这其实很尴尬？
     */
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
        return null;
    }
    /**
     *用于生成树的随机数生成器
     */
    private Random rand;
    /**
     * 树生成的目标世界
     */
    private Level world;
    /**
     * 树的基础位置，初始化为原点
     */
    private BlockPos basePos = BlockPos.ZERO;
    /**
     * 树的高度限制，初始为0，会在生成过程中确定
     */
    int heightLimit;
    /**
     * 树的实际高度，根据高度限制和衰减系数计算得出
     */
    int height;
    /**
     * 树高度的衰减系数，用于调整树的高度
     */
    double heightAttenuation = 0.618D;
    /**
     * 树枝的倾斜角度系数，影响树枝的生长方向
     */
    double branchSlope = 0.381D;
    /**
     * 树的宽度缩放系数，影响树的整体宽度
     */
    double scaleWidth = 1.0D;
    /**
     * 树叶的密度系数，影响树叶的生成数量和分布
     */
    double leafDensity = 1.0D;
    /**
     * 树干的大小，默认为1，可用于生成更粗的树干
     */
    int trunkSize = 1;
    /**
     * 树高度限制的上限，防止树过高
     */
    int heightLimitLimit = 12;
    /**
     * 树叶距离限制，控制树叶生成的最远距离
     */
    int leafDistanceLimit = 4;
    /**
     * 存储树叶节点坐标的列表，用于生成树叶
     */
    public void setHeight (int baseHeight, int extraHeight) {
        this.heightLimit = baseHeight;
        this.heightLimitLimit = extraHeight;
    }
    public void setBigTrunkSize (boolean isBig) {
        this.trunkSize = isBig ? 2 : 1;
    }
    public void setLeafDistanceLimit (int pLeafDistanceLimit) {
        this.leafDistanceLimit = pLeafDistanceLimit;
    }
    List<FoliageCoordinates> foliageCoords;
    /**
     * 定义内部静态类 FoliageCoordinates，用于存储树叶节点的坐标和分支基础高度信息，继承自 BlockPos
     */
    static class FoliageCoordinates extends BlockPos
    {
        /**
         * 存储分支基础高度
         */
        private final int branchBase;

        /**
         * 构造函数，调用父类（BlockPos）构造函数初始化坐标，并设置分支基础高度
         */
        public FoliageCoordinates(BlockPos pos, int p_i45635_2_)
        {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.branchBase = p_i45635_2_;
        }

        /**
         * 获取分支基础高度的方法
         */
        public int getBranchBase()
        {
            return this.branchBase;
        }
    }

    /**
     * 生成树叶节点列表的方法
     */
    void generateLeafNodeList()
    {
        /*
        根据高度限制和衰减系数计算树的实际高度
         */
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);

        if (this.height >= this.heightLimit)
        {
            this.height = this.heightLimit - 1;
        }

        /*
        根据树叶密度和高度限制计算每层树叶的大致数量
         */
        int i = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D));

        if (i < 1)
        {
            i = 1;
        }

        /*
        计算树叶开始的高度
         */
        int j = this.basePos.getY() + this.height;
        /*
        计算距离树叶开始高度的距离限制
         */
        int k = this.heightLimit - this.leafDistanceLimit;
        /*
        初始化树叶节点坐标列表
         */
        this.foliageCoords = new ArrayList<>();
        /*
        添加第一个树叶节点坐标
         */
        this.foliageCoords.add(new FoliageCoordinates(this.basePos.above(k), j));

        /*
        循环生成其他树叶节点坐标
         */
        for (; k >= 0; --k)
        {
            /*
            获取当前层的大小
             */
            float f = this.layerSize(k);

            if (f >= 0.0F)
            {
                for (int l = 0; l < i; ++l)
                /*
                计算树叶节点的水平偏移量
                 */
                {
                    double d0 = this.scaleWidth * (double)f * ((double)this.rand.nextFloat() + 0.328D);
                    /*
                    计算树叶节点的角度
                     */
                    double d1 = (double)(this.rand.nextFloat() * 2.0F) * Math.PI;
                    /*
                    计算树叶节点在 x 轴的偏移量
                     */
                    double d2 = d0 * Math.sin(d1) + 0.5D;
                    /*
                    计算树叶节点在 z 轴的偏移量
                     */
                    double d3 = d0 * Math.cos(d1) + 0.5D;
                    /*
                    计算树叶节点的位置
                     */
                    BlockPos blockpos = this.basePos.offset((int)d2, (int)(k - 1), (int)d3);
                    /*
                    计算树叶节点上方的位置
                     */
                    BlockPos blockpos1 = blockpos.above(this.leafDistanceLimit);

                    /*
                    检查从树叶节点到上方位置的路径是否可行
                     */
                    if (this.checkBlockLine(blockpos, blockpos1) == -1)
                    {
                        int i1 = this.basePos.getX() - blockpos.getX();
                        int j1 = this.basePos.getZ() - blockpos.getZ();
                        /*
                        计算树叶节点的垂直偏移量
                         */
                        double d4 = (double)blockpos.getY() - Math.sqrt((double)(i1 * i1 + j1 * j1)) * this.branchSlope;
                        int k1 = d4 > (double)j ? j : (int)d4;
                        BlockPos blockpos2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());

                        /*
                        检查从计算出的垂直位置到树叶节点的路径是否可行
                         */
                        if (this.checkBlockLine(blockpos2, blockpos) == -1)
                        {
                            /*
                            如果可行，添加树叶节点坐标到列表
                             */
                            this.foliageCoords.add(new FoliageCoordinates(blockpos, blockpos2.getY()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 在指定位置生成树的横截面（用于生成树干或树枝）
     */
    void crosSection(BlockPos pos, float p_181631_2_, BlockState p_181631_3_)
    {
        int i = (int)((double)p_181631_2_ + 0.618D);

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                /*
                  检查当前位置是否在横截面范围内
                 */
                if (Math.pow((double)Math.abs(j) + 0.5D, 2.0D) + Math.pow((double)Math.abs(k) + 0.5D, 2.0D) <= (double)(p_181631_2_ * p_181631_2_))
                {
                    BlockPos blockpos = pos.offset(j, 0, k);
                    BlockState state = this.world.getBlockState(blockpos);

                    /*
                    如果当前位置是空气或树叶，则设置为指定的方块状态
                     */
                    if (state.isAir() || state.is(BlockTags.LEAVES))
                    {
                        world.setBlock(blockpos, p_181631_3_, 2);
                    }
                }
            }
        }
    }

    /**
     * 获取树的某一层的大致大小
     */
    float layerSize(int y)
    {
        /*
        如果当前层高度小于树高度限制的 30%，则返回 -1，表示不需要生成
         */
        if ((float)y < (float)this.heightLimit * 0.3F)
        {
            return -1.0F;
        }
        else
        {
            /*
            计算树高度的一半
             */
            float f = (float)this.heightLimit / 2.0F;
            /*
            计算当前层与树高度一半的差值
             */
            float f1 = f - (float)y;
            /*
            计算当前层的大小
             */
            float f2 = (float) Math.sqrt(f * f - f1 * f1);

            if (f1 == 0.0F)
            {
                f2 = f;
            }
            else if (Math.abs(f1) >= f)
            {
                return 0.0F;
            }

            return f2 * 0.5F;
        }
    }

    /**
     * 获取树叶在某一层的大小
     */
    float leafSize(int y)
    {
        /*
        如果当前层在树叶距离限制范围内，则根据位置返回不同的大小
         */
        if (y >= 0 && y < this.leafDistanceLimit)
        {
            return y != 0 && y != this.leafDistanceLimit - 1 ? 3.0F : 2.0F;
        }
        else
        {
            return -1.0F;
        }
    }

    /**
     * 生成单个树叶节点的树叶
     */
    void generateLeafNode(BlockPos pos)
    {
        for (int i = 0; i < this.leafDistanceLimit; ++i)
        {
            /*
            在指定位置生成树叶横截面，使用自定义的树叶方块状态
             */
            //this.crosSection(pos.up(i), this.leafSize(i), Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
            this.crosSection(pos.above(i), this.leafSize(i), leavesBlock);
        }
    }

    /*
    生成从一个位置到另一个位置的树枝
     */
    void limb(BlockPos p_175937_1_, BlockPos p_175937_2_, Block block)
    {
        BlockPos blockpos = p_175937_2_.offset(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;

        for (int j = 0; j <= i; ++j)
        {
            /*
            获取树枝的方块状态，根据方向设置属性
             */
            BlockPos blockpos1 = p_175937_1_.offset((int)(0.5F + (float)j * f), (int)(0.5F + (float)j * f1), (int)(0.5F + (float)j * f2));
            //BlockLog.EnumAxis blocklog$enumaxis = this.getLogAxis(p_175937_1_, blockpos1);
            BlockState blockState = getLogAxis(p_175937_1_, blockpos1, block);
            /*
            在指定位置设置树枝方块
             */
            world.setBlock(blockpos1, blockState, 2);
        }
    }

    /**
     * 获取 BlockPos 对象在三个维度上的最大距离
     */
    private int getGreatestDistance(BlockPos posIn)
    {
        int i = Math.abs(posIn.getX());
        int j = Math.abs(posIn.getY());
        int k = Math.abs(posIn.getZ());

        if (k > i && k > j)
        {
            return k;
        }
        else
        {
            return j > i ? j : i;
        }
    }

    /**
     * 获取从一个位置到另一个位置的原木的方块状态，根据方向设置属性
     */
    private BlockState getLogAxis(BlockPos p_175938_1_, BlockPos p_175938_2_, Block blockIn)
    {
        /*
        初始化为向上的方向
         */
        //blockState = blockIn.getDefaultState().withProperty(BlockLogBase.FACING, EnumFacing.UP);
        BlockState blockState = blockIn.defaultBlockState().setValue(AXIS, Direction.Axis.Y);
        int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
        int k = Math.max(i, j);

        if (k > 0)
        {
            if (i == k)
            {
                /*
                 如果 x 轴方向距离最大，设置为向西的方向
                 */
                blockState = blockIn.defaultBlockState().setValue(AXIS, Direction.Axis.X);
                //blockState = blockIn.getDefaultState().withProperty(BlockLogBase.FACING, EnumFacing.WEST);

            }
            else if (j == k)
            {
                /*
                如果 z 轴方向距离最大，设置为向北的方向
                 */
                blockState = blockIn.defaultBlockState().setValue(AXIS, Direction.Axis.Z);
                //blockState = blockIn.getDefaultState().withProperty(BlockLogBase.FACING, EnumFacing.NORTH);
            }
        }
        return blockState;
//        return blocklog$enumaxis;
    }

    /**
     * 生成所有树叶节点的树叶
     */
    void generateLeaves()
    {
        for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.foliageCoords)
        {
            this.generateLeafNode(worldgenbigtree$foliagecoordinates);
        }
    }

    /**
     * 检查树叶节点是否需要额外的树干支撑
     */
    boolean leafNodeNeedsBase(int p_76493_1_)
    {
        return (double)p_76493_1_ >= (double)this.heightLimit * 0.2D;
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    void generateTrunk()
    {
        BlockPos blockpos = this.basePos;
        BlockPos blockpos1 = this.basePos.above(this.height);
        Block block = logBlock.getBlock();
        /*
        生成树干
         */
        this.limb(blockpos, blockpos1, block);

        /*
        如果树干大小为 2，生成额外的树干部分
         */
        if (this.trunkSize == 2)
        {
            this.limb(blockpos.east(), blockpos1.east(), block);
            this.limb(blockpos.east().south(), blockpos1.east().south(), block);
            this.limb(blockpos.south(), blockpos1.south(), block);
        }
    }

    /**
     * 生成树叶节点的底部支撑树干
     */
    void generateLeafNodeBases()
    {
        for (FoliageCoordinates worldgenbigtree$foliagecoordinates : this.foliageCoords)
        {
            int i = worldgenbigtree$foliagecoordinates.getBranchBase();
            BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());

            if (!blockpos.equals(worldgenbigtree$foliagecoordinates) && this.leafNodeNeedsBase(i - this.basePos.getY()))
            {
                this.limb(blockpos, worldgenbigtree$foliagecoordinates, logBlock.getBlock());
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     * 检查从一个位置到另一个位置的方块线是否可行（是否遇到非空气、非树叶方块）
     */
    int checkBlockLine(BlockPos posOne, BlockPos posTwo)
    {
        BlockPos blockpos = posTwo.offset(-posOne.getX(), -posOne.getY(), -posOne.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float)blockpos.getX() / (float)i;
        float f1 = (float)blockpos.getY() / (float)i;
        float f2 = (float)blockpos.getZ() / (float)i;

        if (i == 0)
        {
            return -1;
        }
        else
        {
            for (int j = 0; j <= i; ++j)
            {
                BlockPos blockpos1 = posOne.offset((int)(0.5F + (float)j * f), (int)(0.5F + (float)j * f1), (int)(0.5F + (float)j * f2));

                if (!this.canGrowInto(world, blockpos1))
                {
                    return j;
                }
            }

            return -1;
        }
    }

    /*
    设置树生成的默认装饰参数（这里是树叶距离限制）
     */
    public void setDecorationDefaults()
    {
        this.leafDistanceLimit = 5;
    }

    /**
     * 生成树的主要方法，这一个是继承类而需要声明的方法
     */
    @Override
    public boolean growTree(ServerLevel worldIn, ChunkGenerator p_222906_, BlockPos position, BlockState p_222908_, RandomSource rand)
    {
        this.world = worldIn;
        this.basePos = position;
        this.rand = new Random(rand.nextLong());

        /*
        如果高度限制未设置，随机生成一个高度限制
         */
        if (this.heightLimit == 0)
        {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
        }

        /*
        如果树的位置无效，释放对世界的引用并返回 false
         */
        if (!this.validTreeLocation())
        {
            this.world = null; //Fix vanilla Mem leak, holds latest world
            return false;
        }
        else
        {
            /*
            引用上文方法，生成树叶节点列表、树叶、树干和树叶节点底部支撑
             */
            this.generateLeafNodeList();
            this.generateLeaves();
            this.generateTrunk();
            this.generateLeafNodeBases();
            this.world = null; //Fix vanilla Mem leak, holds latest world
            return true;
        }
    }
    /**
     * 生成树的主要方法，这一个是为了避免节外生枝
     */
    public boolean generate(Level worldIn, RandomSource rand, BlockPos position)
    {
        this.world = worldIn;
        this.basePos = position;
        this.rand = new Random(rand.nextLong());

        /*
        如果高度限制未设置，随机生成一个高度限制
         */
        if (this.heightLimit == 0)
        {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
        }

        /*
        如果树的位置无效，释放对世界的引用并返回 false
         */
        if (!this.validTreeLocation())
        {
            this.world = null; //Fix vanilla Mem leak, holds latest world
            return false;
        }
        else
        {
            /*
            引用上文方法，生成树叶节点列表、树叶、树干和树叶节点底部支撑
             */
            this.generateLeafNodeList();
            this.generateTrunk();
            this.generateLeaves();
            this.generateLeafNodeBases();
            this.world = null; //Fix vanilla Mem leak, holds latest world
            return true;
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.检查树的位置是否有效（检查下方方块是否为合适的土壤或特定方块，以及上方空间是否足够）
     */
    private boolean validTreeLocation()
    {
        /*
        获取树基础位置下方的方块位置
         */
        BlockPos down = this.basePos.below();
        /*
        获取下方方块的状态
         */
        BlockState state = this.world.getBlockState(down);
        /*
         检查下方方块是否能支持植物生长（这里应为对应树苗的生长条件）或者是否满足下文的自定义条件（用于互动其他方块）
         */
        //boolean isSoil = state.getBlock().canSustainPlant(state, this.world, down, Direction.UP, PlantType.DESERT);
        boolean isSoil = true;
        /*
        如果不满足上文条件，则树的位置无效，返回 false，此处应预留有一配置文件接口
         */
        if (!isSoil)
        {
            return false;
        }
        else
        {
            /*
            检查从树基础位置到指定高度限制位置的方块线是否可行（是否遇到非空气、非树叶方块）
             */
            int i = this.checkBlockLine(this.basePos, this.basePos.above(this.heightLimit - 1));

            /*
            如果没有遇到阻碍，则树的位置有效，返回 true
             */
            if (i == -1)
            {
                return true;
            }
            /*
            如果遇到阻碍且距离小于 6，则树的位置无效，返回 false
             */
            else if (i < 6)
            {
                return false;
            }
            else
            /*
            如果遇到阻碍且距离大于等于 6，则更新树的高度限制为遇到阻碍的位置，并返回 true
             */
            {
                this.heightLimit = i;
                return true;
            }
        }
    }

    /**
     * 判断给定的方块类型是否可以被树生长进入
     */
    public boolean canGrowInto(Level world, BlockPos pos)
    {
        BlockState material = world.getBlockState(pos);
        return material.isAir()
                || material.is(BlockTags.LEAVES)
                || material.is(BlockTags.SAND)
                || material.is(BlockTags.DIRT)
                || material.is(BlockTags.LOGS)
                || material.is(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)
                || material.is(BlockTags.SAPLINGS)
                || material.is(logBlock.getBlock())
                || material.is(leavesBlock.getBlock())
                || material.is(saplingBlock.getBlock())
                || material.is(BlockTags.SNOW)
                || material.is(BlockTags.CAVE_VINES);
    }

    /*
    重写父类方法，在给定世界和位置设置特定的方块（这里似乎是给2粗树干设置底部泥土用）
     */
    public void setDirtAt(Level worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos) != placeOnBlock)
        {
            worldIn.setBlock(pos, placeOnBlock, 2);
        }
    }
//    @Override
//    public boolean generate(World worldIn, Random rand, BlockPos position) {
//        return false;
//    }
}


package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.world.tree.BigOakTreeGrower;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;

import java.util.List;


public class HimekaidoShrubBlock extends ReapCropBlock {
    public static final int FRUITED_AGE = 3;
    public HimekaidoShrubBlock(Properties p_52247_) {
        super(p_52247_);
    }
    protected static final int EXTRA_HARVEST_AGE = 6;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    public VoxelShape getShape(BlockState p_51330_, BlockGetter p_51331_, BlockPos p_51332_, CollisionContext p_51333_) {
        return SHAPE_BY_AGE[this.getAge(p_51330_)];
    }

    /*
    生长逻辑，在生长时如果大于阶段3（默认结果状态），反而回退生长阶段
     */
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                if (i > FRUITED_AGE) {
                    pLevel.setBlock(pPos, this.getStateForAge(i - 1), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
                if (i < FRUITED_AGE) {
                    float f = getGrowthSpeed(this, pLevel, pPos);
                    if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(100.0F / f) + 1) == 0)) {
                        pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                    }
                }
            }
        }
    }
    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.HIMEKAIDO_SEED.get();
    }
    /**
     * 巨大化
     */
    public void performBonemeal(ServerLevel p_221040_, RandomSource p_221041_, BlockPos p_221042_, BlockState p_221043_) {
        /*
        巨大化条件：此处要求阶段6且生长速度不小于9
         */
        if (this.getAge(p_221043_) == EXTRA_HARVEST_AGE && getGrowthSpeed(this, p_221040_, p_221042_) >= 3.0f) {
            BigOakTreeGrower tree = new BigOakTreeGrower(
                    ImmortalersDelightBlocks.HIMEKAIDO_LOG.get().defaultBlockState(),
                    ImmortalersDelightBlocks.HIMEKAIDO_LEAVES.get().defaultBlockState(),
                    ImmortalersDelightBlocks.HIMEKAIDO_SHRUB.get().defaultBlockState()
            );
            tree.setHeight(8,5);
            tree.setLeafDistanceLimit(3);
            tree.generate(p_221040_, p_221041_, p_221042_);
        }
        else p_221040_.setBlock(p_221042_, this.getStateForAge( this.getAge(p_221043_) + 1), 2);
    }
    /**
     * 定义收获的行为逻辑，并给玩家进行巨大化操作的空间
     */@Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int age = state.getValue(AGE);
        if (age == FRUITED_AGE) {
            ItemStack hand_stack = player.getItemInHand(hand);
            /*
            在阶段3（正常生长的结果阶段），如果不是使用骨粉或生长速度小于5，使用收获方法
            也就是生长速度小于3收获方法会挤掉骨粉的操作，使得无法催大达到过度生长阶段
             */
            if (!(hand_stack.getItem() instanceof BoneMealItem) || getGrowthSpeed(this, level, pos) <= 3.0f) {
                harvest(state,level,pos,1);
                return InteractionResult.SUCCESS;
            }
            /*
            对if逻辑进行分析可以发现如果阶段3下满足了生长速度，那么使用骨粉时不会触发收获，也就是骨粉将生效，将灌木催大达到过度生长阶段
             */
        } else if (age == EXTRA_HARVEST_AGE || age == MAX_AGE) {
            /*
            如果是6阶段或7阶段，均可收获
             */
            ItemStack hand_stack = player.getItemInHand(hand);
            if (!(hand_stack.getItem() instanceof BoneMealItem)) {
                harvest(state,level,pos,1);
                return InteractionResult.SUCCESS;
            }
        }
        /*
        不满足收获条件，返回父类，骨粉的逻辑在此后处理
         */
        return super.use(state, level, pos, player, hand, hitResult);
    }
    /*
    收获时用于生成掉落物，以及设置到指定的生长阶段，在超生长阶段回到阶段3也就是能马上再收获一次
     */
    public void harvest(BlockState state, Level level, BlockPos pos,int pAge) {
        boolean temp = false;
        if (level instanceof ServerLevel level1) {
            /*
            遍历战利品表
             */
            List<ItemStack> stacks = getDrops(state, level1, pos, null);
            if (!stacks.isEmpty()){
                for (ItemStack stack : stacks) {
                    popResource(level, pos, stack);
                }
                temp = true;
            }
        }
        if (temp){
            /*
            设置收获后的生长阶段
             */
            level.setBlock(pos,state.setValue(AGE,pAge),3);
            //level.playSound((Player)null, pos, (SoundEvent) ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            return;
        }
        return;
     }
}

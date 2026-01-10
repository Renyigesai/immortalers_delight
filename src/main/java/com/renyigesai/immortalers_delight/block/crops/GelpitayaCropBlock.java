package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.client.particle.SnowFogParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GelpitayaCropBlock extends ReapCropBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    private static final VoxelShape[] COLLISION_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 3.0D, 10.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.box(5.0D, 0.0D, 5.0D, 11.0D, 5.0D, 11.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D)
    };
    public GelpitayaCropBlock(Properties p_52247_) {
        super(p_52247_);
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COLLISION_SHAPE_BY_AGE[pState.getValue(AGE)];
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[pState.getValue(AGE)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.GELPITAYA_SEEDS.get();
    }

    @Override
    public boolean canReap(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isCorrectToolForDrops(state)) {
            boolean flag = super.canReap(state, level, pos, player, hand, hitResult);
            if (flag && !player.getAbilities().instabuild && player instanceof ServerPlayer serverPlayer) {
                itemStack.hurtAndBreak(1, serverPlayer, (action) -> {
                    action.broadcastBreakEvent(hand);
                });
            }
            return flag;
        }
        return false;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate = pLevel.getBlockState(pPos.relative(direction));
            if (blockstate.isSolid() || pLevel.getFluidState(pPos.relative(direction)).is(FluidTags.LAVA)) {
                return false;
            }
        }

        BlockState blockstate1 = pLevel.getBlockState(pPos.below());
        return blockstate1.getBlock() instanceof CactusBlock && !pLevel.getBlockState(pPos.above()).liquid();
    }
    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.getBlock() instanceof CactusBlock;
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        int i = this.getAge(pState);
        if (i >= 3) {pEntity.hurt(pLevel.damageSources().cactus(), 1.0F);}

        if (i >= 5) {
            pEntity.setIsInPowderSnow(true);
            if (!pLevel.isClientSide) {
                pEntity.setSharedFlagOnFire(false);
            }
        }
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, pLevel, pPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(25.0F / f) + 1) == 0)) {
                    if (i == this.getMaxAge() - 1) {
                        spawnParticle(pLevel, pPos);
                        spawnSnow(pLevel, pPos, 1, 3, 1);
                    }
                    pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }

    public void spawnSnow(Level pLevel,BlockPos pPos,int x,int y,int z) {
        for (int i = -x; i <= x; i++) {
            for (int j = -y; j <= y; j++) {
                for (int k = -z; k <= z; k++) {
                    if (i != 0 || j > 0 || k != 0) {
                        BlockPos blockPos = pPos.offset(i,j,k);
                        BlockPos belowPos = blockPos.below();
                        BlockState blockState = pLevel.getBlockState(blockPos);
                        BlockState belowState = pLevel.getBlockState(belowPos);
                        if (blockState.isAir()
                        && isFaceFull(belowState.getCollisionShape(pLevel, belowPos), Direction.UP)
                        ) {
                            pLevel.setBlockAndUpdate(blockPos, Blocks.SNOW.defaultBlockState());
                        } else if (blockState.is(Blocks.MAGMA_BLOCK)) {
                            pLevel.setBlockAndUpdate(blockPos, Blocks.NETHERRACK.defaultBlockState());
                        } else if (blockState.is(Blocks.SNOW)) {
                            int layer = pLevel.getBlockState(blockPos).getValue(SnowLayerBlock.LAYERS);
                            if (layer < SnowLayerBlock.MAX_HEIGHT) pLevel.setBlock(blockPos,
                                    Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS,layer + 1), 2
                            );
                        } else if (blockState.is(Blocks.WATER)) {
                            pLevel.setBlockAndUpdate(blockPos, Blocks.ICE.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private void spawnParticle(Level level, BlockPos pPos) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
            double radius = 2.0;
            for (int i = 0; i < 32; i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y + r;
                if (i % radius == 0) {
                    SnowFogParticleOption particleOption = new SnowFogParticleOption((int) radius);
                    serverLevel.sendParticles(
                            particleOption, center.x, y, center.z, 1, 0, 0, 0, 0.025
                    );
                } else serverLevel.sendParticles(
                        ParticleTypes.SNOWFLAKE, x, y, z, 5, 0.25, 0.25, 0.25, 0.025
                );
            }
        }
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel);
        int j = this.getMaxAge();
        if (this.getAge(pState) == j-1) {
            i = j;
            spawnParticle(pLevel, pPos);
            spawnSnow(pLevel,pPos,1,3,1);
        } else if (i >= j) {
            i = j-1;
        }

        pLevel.setBlock(pPos, this.getStateForAge(i), 2);
    }

    @Override
    public int getMaxAge() {
        return 6;
    }
}

package com.renyigesai.immortalers_delight.block.food;

import com.google.common.collect.ImmutableList;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.brushable.ModBrushableBlockEntity;
import com.renyigesai.immortalers_delight.block.brushable.SuspiciousAshPileBlock;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NaanBakingPitBlock extends HorizontalDirectionalBlock {
    private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5D, 1.0D, 0.5D));
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final VoxelShape BOX = box(2.0D,0.0D,2.0D,14.0D,4.0D,14.0D);
    public static final VoxelShape BOX_LIT = box(1.0D,0.0D,1.0D,15.0D,10.0D,15.0D);
    public static final VoxelShape OUTLINE_BOX = box(1.9D,0.0D,1.9D,14.1D,4.2D,14.1D);
    public static final VoxelShape OUTLINE_BOX_LIT = box(0.1D,0.0D,0.1D,15.9D,12.0D,15.9D);

    public NaanBakingPitBlock(Properties pProperties) {
        super(pProperties);
        super.registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(PERSISTENT, Boolean.valueOf(false)).setValue(LIT, Boolean.valueOf(true)));
    }
    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide && pState.getValue(LIT)) {
            pEntity.hurt(pEntity.damageSources().hotFloor(), 1.1F);
            pEntity.setSecondsOnFire(8);
        }
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pState.getValue(LIT)) {
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            if (itemstack.getItem() == Items.MAGMA_BLOCK) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, Boolean.valueOf(true)));
                if (!pPlayer.isCreative()) itemstack.shrink(1);
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    @Override
    public boolean isRandomlyTicking(BlockState p_54449_) {
        return p_54449_.getValue(LIT) && !p_54449_.getValue(PERSISTENT);
    }
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(3) == 0)) {
            pLevel.setBlockAndUpdate(pPos, ImmortalersDelightBlocks.SUSPICIOUS_ASH_PILE.get().defaultBlockState().setValue(SuspiciousAshPileBlock.FACING, pState.getValue(FACING)));
            // 构建方块的实体数据（TileEntity NBT），设置自定义战利品表
            CompoundTag blockEntityTag = new CompoundTag();
            // 关键：设置战利品表路径（替换为你的自定义战利品表ID）
            blockEntityTag.putString("LootTable", ImmortalersDelightMod.MODID + ":archaeology/naan_baking_pit");
            // 可选：设置战利品表种子（随机数，保证每次掉落不同）
            blockEntityTag.putLong("LootTableSeed", pLevel.getRandom().nextLong());
            // 为方块实体附加NBT（关键：战利品表通过方块实体存储）
            if (pLevel.getBlockEntity(pPos) instanceof ModBrushableBlockEntity blockEntity) {
                blockEntity.load(blockEntityTag);
                blockEntity.setChanged(); // 标记方块实体数据变更
            }
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(LIT) ? OUTLINE_BOX_LIT : OUTLINE_BOX;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(LIT) ? BOX_LIT : BOX;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        if (!pContext.getItemInHand().getItem().getDescriptionId().equals(this.getDescriptionId()))return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(LIT, Boolean.valueOf(false));
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(PERSISTENT).add(LIT);
    }
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            this.getParticleOffsets(pState).forEach((p_220695_) -> {
                addParticlesAndSound(pLevel, p_220695_.add((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ()), pRandom);
            });
        }
    }
    protected Iterable<Vec3> getParticleOffsets(BlockState pState) {
        return PARTICLE_OFFSETS;
    }
    private static void addParticlesAndSound(Level pLevel, Vec3 pOffset, RandomSource pRandom) {
        float f = pRandom.nextFloat();
        if (f < 0.3F) {
            pLevel.addParticle(ParticleTypes.SMOKE, pOffset.x, pOffset.y, pOffset.z, 0.0D, 0.0D, 0.0D);
            if (f < 0.17F) {
                pLevel.playLocalSound(pOffset.x + 0.5D, pOffset.y + 0.5D, pOffset.z + 0.5D, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        pLevel.addParticle(ParticleTypes.SMALL_FLAME, pOffset.x, pOffset.y, pOffset.z, 0.0D, 0.0D, 0.0D);
    }
}

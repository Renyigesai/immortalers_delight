package com.renyigesai.immortalers_delight.block.food;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Map;
import java.util.Objects;

public class RotatingRoastMeatBlock extends BaseEntityBlock {
//    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,4);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,18.0D,15.0D);

    public RotatingRoastMeatBlock(Properties p_54120_) {
        super(p_54120_);
        super.registerDefaultState(defaultBlockState().setValue(BITES,0).setValue(LIT,false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ImmortalersDelightBlocks.ROTATING_ROAST_MEAT_ENTITY.get(),
                RotatingRoastMeatBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack hand_stack = player.getItemInHand(hand);
            if (hand_stack.is(ModTags.KNIVES)) {
                return cut(state, level, pos, player);
            }
            return super.use(state, level, pos, player, hand, hitResult);
    }

    public InteractionResult cut(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        Direction direction = player.getDirection().getOpposite();
        if (bites < 4){
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            ItemUtils.spawnItemEntity(level,new ItemStack(ImmortalersDelightItems.RAW_SNIFFER_SLICE.get(),2),(double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        }else {
            level.removeBlock(pos, false);
            ItemUtils.spawnItemEntity(level,new ItemStack(Items.IRON_BARS),(double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        }
        level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }
    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)){
            if (TimekeepingTask.getImmortalTickTime() % 5000 < 1000){

            }
        }
    }
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if (pState.getValue(LIT)) {
            if (pRandom.nextInt(10) == 0) {
                BlockPos blockpos = pPos.above();
                BlockState blockstate = pLevel.getBlockState(blockpos);
                if (!isFaceFull(blockstate.getCollisionShape(pLevel, blockpos), Direction.UP)) {
                    pLevel.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, (double) (pRandom.nextFloat() / 2.0F), 5.0E-5D, (double) (pRandom.nextFloat() / 2.0F));
                }
            }
        }
    }

    private boolean isHeated(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos.below());
        return state.is(BlockTags.create(new ResourceLocation("farmersdelight:heat_sources"))) && state.getBlock().getStateDefinition().getProperty("lit") instanceof BooleanProperty booleanProperty && state.getValue(booleanProperty);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES,LIT);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RotatingRoastMeatBlockEntity(pPos,pState);
    }
}

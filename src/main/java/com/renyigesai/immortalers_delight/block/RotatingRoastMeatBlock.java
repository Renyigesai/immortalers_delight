package com.renyigesai.immortalers_delight.block;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Map;
import java.util.Objects;

public class RotatingRoastMeatBlock extends HorizontalDirectionalBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites",0,9);
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    public static final VoxelShape BOX = box(1.0D,0.0D,1.0D,15.0D,18.0D,15.0D);

    public RotatingRoastMeatBlock(Properties p_54120_) {
        super(p_54120_);
        super.registerDefaultState(defaultBlockState().setValue(BITES,1).setValue(FACING, Direction.NORTH).setValue(STAGE, 1));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack hand_stack = player.getItemInHand(hand);
            if (hand_stack.is(ModTags.KNIVES) && state.getValue(RotatingRoastMeatBlock.STAGE) == 0) {
                if (!player.isShiftKeyDown()){
                    return eat(state, level, pos, player);
                }
                return cut(state, level, pos, player);
            } else {
                if (state.getValue(BITES) < 9){
                    level.setBlock(pos,state.rotate(level,pos, Rotation.CLOCKWISE_90),3);
                    return super.use(state, level, pos, player, hand, hitResult);
                } else {
                    level.destroyBlock(pos, false);
                    ItemUtils.spawnItemEntity(level,
                            new ItemStack(Items.IRON_BARS),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                    level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
                }
                return InteractionResult.SUCCESS;
            }
    }

    public InteractionResult eat(BlockState state, Level level, BlockPos pos, Player player){
            int bites = state.getValue(BITES);
            if (bites < 9){
                if (player.canEat(false)){
                    setBlockState(bites, false, state, level, pos);
                    player.getFoodData().eat(ImmortalersDelightItems.COOKED_SNIFFER_STEAK.get(), new ItemStack(ImmortalersDelightItems.COOKED_SNIFFER_STEAK.get()));
                    level.gameEvent(player, GameEvent.EAT, pos);
                    level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
                }else {
                    return InteractionResult.PASS;
                }
            }else {
                level.destroyBlock(pos, false);
                ItemUtils.spawnItemEntity(level,
                        new ItemStack(Items.IRON_BARS),pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                level.playSound(null,pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
            }
            return InteractionResult.SUCCESS;
    }

    public InteractionResult cut(BlockState state, Level level, BlockPos pos, Player player){
        int bites = state.getValue(BITES);
        Direction direction = player.getDirection().getOpposite();
        if (bites < 9){
            setBlockState(bites,false,state,level,pos);
            ItemUtils.spawnItemEntity(level,new ItemStack(ImmortalersDelightItems.ZEA_PANCAKE_SLICE.get()),(double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
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
        int stage = pState.getValue(STAGE);
        if (stage > 0 && TimekeepingTask.getImmortalTickTime() % 5000 < 1000){
            if (isHeated(pLevel,pPos)) {
                setStage(true,pState,pLevel,pPos);
            }
        }
    }
    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if (pRandom.nextInt(10) == 0 && isHeated(pLevel,pPos)) {
            BlockPos blockpos = pPos.above();
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (!isFaceFull(blockstate.getCollisionShape(pLevel, blockpos), Direction.UP)) {
                pLevel.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, (double)(pRandom.nextFloat() / 2.0F), 5.0E-5D, (double)(pRandom.nextFloat() / 2.0F));
            }
        }
    }
    public void setBlockState(int variate, boolean isCooked, BlockState state, Level level, BlockPos pos){
        level.setBlock(pos, state.setValue(BITES, variate + 1).setValue(STAGE,isCooked ? 0 : 1), 3);
    }
    public void setStage(boolean isCooked, BlockState state, Level level, BlockPos pos){
        level.setBlock(pos, state.setValue(STAGE, isCooked ? 0 : 1), 3);
    }
    private boolean isHeated(Level level, BlockPos pos) {
        Map<Integer,BlockPos> posMap = (new ImmutableMap.Builder<Integer,BlockPos>())
                .put(0,pos.north())
                .put(1,pos.south())
                .put(2,pos.west())
                .put(3,pos.east())
                .build();
        for (int i = 0;i < 4 && posMap.get(i) != null; i++) {
            BlockState stateBelow = level.getBlockState(Objects.requireNonNull(posMap.get(i)));
            if (stateBelow.is(ModTags.HEAT_SOURCES)) {
                return stateBelow.hasProperty(BlockStateProperties.LIT) ? (Boolean)stateBelow.getValue(BlockStateProperties.LIT) : true;
            } else {
                if (stateBelow.is(ModTags.HEAT_CONDUCTORS)) {
                    BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
                    if (stateFurtherBelow.is(ModTags.HEAT_SOURCES)) {
                        if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT)) {
                            return (Boolean)stateFurtherBelow.getValue(BlockStateProperties.LIT);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
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
        builder.add(BITES,FACING,STAGE);
    }
}

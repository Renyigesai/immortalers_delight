package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IForgeShearable;
import org.checkerframework.checker.units.qual.A;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;
import java.util.List;

public class OxygrapeCropBlock extends BushBlock implements LiquidBlockContainer, IForgeShearable,BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final VoxelShape BOX = box(2.0D,0.0D,2.0D,14.0D,16.0D,14.0D);
    public OxygrapeCropBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(DOWN,true));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Vec3 vec3 = pState.getOffset(pLevel, pPos);
        return BOX.move(vec3.x,vec3.y,vec3.z);
    }

    public BlockState updateShape(BlockState blockState, Direction p_154531_, BlockState p_154532_, LevelAccessor levelAccessor, BlockPos p_154534_, BlockPos p_154535_) {
        if (!canSurvive(blockState,levelAccessor,p_154534_)){
            return super.updateShape(blockState, p_154531_, p_154532_, levelAccessor, p_154534_, p_154535_);
        }
        boolean down = levelAccessor.getBlockState(p_154534_.below()).is(this);
        return blockState.setValue(DOWN, !down);
    }
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        FluidState fluid = pLevel.getFluidState(pPos);
        if (!fluid.is(FluidTags.WATER) || fluid.getAmount() != 8) {
            return false;
        }
        BlockPos abovePos = pPos.above();
        return mayPlaceOn(pState, pLevel, abovePos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_154539_, BlockGetter p_154540_, BlockPos p_154541_) {
        BlockState state = p_154540_.getBlockState(p_154541_);
        return state.isFaceSturdy(p_154540_, p_154541_, Direction.DOWN) || state.is(this);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (canReap(state, level, pos, player, hand, hitResult)) {
            boolean temp = false;
            if (level instanceof ServerLevel level1) {
                List<ItemStack> stacks = getDrops(state, level1, pos, null,player,player.getMainHandItem());
                if (!stacks.isEmpty()) {
                    for (ItemStack stack : stacks) {
                        popResource(level, pos, stack);
                    }
                    temp = true;
                }
            }
            if (temp) {
                level.setBlock(pos, state.setValue(AGE, 0), 3);
                level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    public boolean canReap(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean flag = Config.rightClickHarvest;
        return flag && state.getValue(AGE) == 3;
    }
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int age = pState.getValue(AGE);
        if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(35) == 0)) {
            grow(pState,pLevel,pPos,pRandom,age);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }

    public void grow(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom,int age){
        if (age < 3){
            if (pRandom.nextDouble() <= 0.5d && downGrow(pLevel,pPos)){
                return;
            }
            pLevel.setBlock(pPos,pState.setValue(AGE,age + 1),3);
        }else {
            downGrow(pLevel,pPos);
        }
    }

    public boolean downGrow(ServerLevel pLevel, BlockPos pPos){
        FluidState fluidState = pLevel.getFluidState(pPos.below());
        if (!pLevel.getBlockState(pPos.below()).is(this) && pLevel.getFluidState(pPos.below()).is(FluidTags.WATER) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8){
            pLevel.setBlock(pPos.below(),this.defaultBlockState(),3);
            return true;
        }
        return false;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_154503_) {
        FluidState fluidstate = p_154503_.getLevel().getFluidState(p_154503_.getClickedPos());
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(p_154503_) : null;
    }

    public FluidState getFluidState(BlockState p_154537_) {
        return Fluids.WATER.getSource(false);
    }

    public boolean canPlaceLiquid(BlockGetter p_154505_, BlockPos p_154506_, BlockState p_154507_, Fluid p_154508_) {
        return false;
    }

    public boolean placeLiquid(LevelAccessor p_154520_, BlockPos p_154521_, BlockState p_154522_, FluidState p_154523_) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE,DOWN);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return pState.getValue(AGE) != 3;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        int age = pState.getValue(AGE);
        grow(pState,pLevel,pPos,pRandom,age);
    }
}

package com.renyigesai.immortalers_delight.block.enchantal_cooler;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class EnchantalCoolerBlock extends BaseEntityBlock {

    private static final VoxelShape BOX = box(2.0,0,2.0,14.0,9.0,14.0);

    public EnchantalCoolerBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BOX;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get(),
                EnchantalCoolerBlockEntity::craftTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, entity, hand, hit);
        if(!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            super.use(blockstate, world, pos, entity, hand, hit);
            if (blockEntity instanceof EnchantalCoolerBlockEntity ovenBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer) entity), ovenBlockEntity, pos);
//                world.playSound(null, pos, BakeriesSounds.OVEN_OPEN.get(), SoundSource.PLAYERS);
//                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(BakeriesSounds.OVEN_OPEN.get(), 1.0F,1.5F));
                return InteractionResult.CONSUME;
            }else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnchantalCoolerBlockEntity(pos,state);
    }
}

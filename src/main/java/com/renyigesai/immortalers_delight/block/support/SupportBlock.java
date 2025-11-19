package com.renyigesai.immortalers_delight.block.support;

import com.mojang.serialization.MapCodec;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SupportBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape SHAPE = Block.box(7.0D, 5.0D, 7.0D, 9.0D, 7.0D, 9.0D);

    public SupportBlock(Properties pProperties) {
        super(pProperties);
        super.registerDefaultState(defaultBlockState().setValue(LIT,false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SupportBlockEntity(pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, net.minecraft.world.level.Level pLevel, BlockPos pPos, net.minecraft.world.entity.player.Player pPlayer, net.minecraft.world.InteractionHand pHand, net.minecraft.world.phys.BlockHitResult pHit) {
        if (!pLevel.isClientSide && pHand == net.minecraft.world.InteractionHand.MAIN_HAND) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SupportBlockEntity supportBlockEntity) {
                int delay = supportBlockEntity.increase();
                pPlayer.sendSystemMessage(Component.translatable("Spawn Delay:" + delay));
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ImmortalersDelightBlocks.SUPPORT_BLOCK_ENTITY.get(), SupportBlockEntity::serverTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}

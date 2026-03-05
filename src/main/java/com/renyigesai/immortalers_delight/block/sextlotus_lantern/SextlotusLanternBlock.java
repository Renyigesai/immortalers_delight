package com.renyigesai.immortalers_delight.block.sextlotus_lantern;

import com.renyigesai.immortalers_delight.block.warped_lantern.WarpedLanternBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.List;

public class SextlotusLanternBlock extends LanternBlock implements EntityBlock {

    /**
     * 红石供电状态属性：true=接收到红石信号（范围照明关闭），false=未接收（范围照明开启）
     */
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public SextlotusLanternBlock(Properties pProperties) {
        super(pProperties);
        // 设置默认状态
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HANGING, Boolean.valueOf(false))
                .setValue(WATERLOGGED, Boolean.valueOf(false))
                .setValue(POWERED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SextlotusLanternBlockEntity(pPos, pState);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HANGING, WATERLOGGED, POWERED);
    }
//    @Override
//    @Deprecated
//    public void neighborChanged(BlockState pState, Level pLevel, @NotNull BlockPos pCurrentPos, @NotNull Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
//        boolean isPoweredNow = pLevel.hasNeighborSignal(pCurrentPos);    // 当前是否接红石电
//        boolean wasPoweredBefore = pState.getValue(POWERED);                 // 之前是否接红石电
//
//        // 从断电→供电：关闭光源块
//        if (!wasPoweredBefore && isPoweredNow)
//            SextlotusLightHelper.castBlockEntity(pLevel.getBlockEntity(pCurrentPos), pCurrentPos, SextlotusLanternBlockEntity::turnOffLightSource);
//            // 从供电→断电：重新开启光源块
//        else if (wasPoweredBefore && !isPoweredNow)
//            SextlotusLightHelper.castBlockEntity(pLevel.getBlockEntity(pCurrentPos), pCurrentPos, SextlotusLanternBlockEntity::turnOnLightSource);
//    }
//
//    @Deprecated
//    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
//        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
//        if (pLevel != null && !pLevel.isClientSide()) {
//            // 优先按放置者视角更新光源，失败则用默认方向
//            updateSearchLight(pLevel, pPos, pState);
//        }
//    }
//
//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (player == null || level == null) return InteractionResult.FAIL;
//
//        // 服务端逻辑：调整光束方向并播放音效
//        if (!level.isClientSide() && updateSearchLight(level, pos, state))
//            level.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1, 0.4f);
//        return InteractionResult.SUCCESS;
//    }
//
//    @Override
//    @Deprecated
//    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
//        // 仅服务端执行：删除关联的光源块
//        if (!pLevel.isClientSide())
//            SextlotusLightHelper.castBlockEntity(pLevel.getBlockEntity(pPos), pPos, SextlotusLanternBlockEntity::deleteLightSource);
//        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
//    }
//
//    /**
//     * 核心工具方法：更新探照灯光束方向并重新生成光源块
//     * @param world 世界实例
//     * @param pos 探照灯坐标
//     * @param state 探照灯状态
//     * @return true=新光源块生成成功，false=失败
//     */
//    protected boolean updateSearchLight(Level world, BlockPos pos, BlockState state) {
//
//    }


}

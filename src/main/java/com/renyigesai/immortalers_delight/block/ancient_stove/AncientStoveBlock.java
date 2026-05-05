package com.renyigesai.immortalers_delight.block.ancient_stove;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AncientStoveBlock extends AbstractStoveBlock implements WeatheringCopper {

    private final WeatheringCopper.WeatherState weatherState;
    private final boolean waxed;
    private static final VoxelShape GRILLING_AREA;

    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder().put(ImmortalersDelightBlocks.ANCIENT_STOVE.get(),ImmortalersDelightBlocks.EXPOSED_ANCIENT_STOVE.get()).put(ImmortalersDelightBlocks.EXPOSED_ANCIENT_STOVE.get(),ImmortalersDelightBlocks.WEATHERED_ANCIENT_STOVE.get()).put(ImmortalersDelightBlocks.WEATHERED_ANCIENT_STOVE.get(),ImmortalersDelightBlocks.OXIDIZED_ANCIENT_STOVE.get()).build());
    Supplier<BiMap<Block, Block>> WAXED_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder().put(ImmortalersDelightBlocks.ANCIENT_STOVE.get(),ImmortalersDelightBlocks.WAXED_ANCIENT_STOVE.get()).put(ImmortalersDelightBlocks.EXPOSED_ANCIENT_STOVE.get(),ImmortalersDelightBlocks.WAXED_EXPOSED_ANCIENT_STOVE.get()).put(ImmortalersDelightBlocks.WEATHERED_ANCIENT_STOVE.get(),ImmortalersDelightBlocks.WAXED_WEATHERED_ANCIENT_STOVE.get()).put(ImmortalersDelightBlocks.OXIDIZED_ANCIENT_STOVE.get(),ImmortalersDelightBlocks.WAXED_OXIDIZED_ANCIENT_STOVE.get()).build());

    public AncientStoveBlock(Properties properties, WeatherState weatherState, boolean waxed) {
        super(properties);
        this.weatherState = weatherState;
        this.waxed = waxed;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        Item heldItem = heldStack.getItem();
        boolean instabuild = player.getAbilities().instabuild;

        if (!this.waxed && heldStack.is(Items.HONEYCOMB) && waxed(level,player,heldStack,state,pos,instabuild)){
            return InteractionResult.SUCCESS;
        }

        if (heldItem instanceof AxeItem){
            if (this.waxed && waxedOff(level,player,heldStack,hand,state,pos,instabuild)){
                return InteractionResult.SUCCESS;
            }
            Block scrape = getScrape(state.getBlock());
            if (scrape != null){
                BlockState newState = scrape.defaultBlockState().setValue(LIT,state.getValue(LIT)).setValue(FACING,state.getValue(FACING));
                level.setBlockAndUpdate(pos,newState);
                if (!instabuild) {
                    heldStack.hurtAndBreak(1, player, (p_150686_) -> {
                        p_150686_.broadcastBreakEvent(hand);
                    });
                }
                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(3005,pos,0);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        if (weatherState == WeatheringCopper.WeatherState.OXIDIZED){
            return InteractionResult.PASS;
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    private boolean waxed(Level level, Player player, ItemStack heldStack, BlockState state, BlockPos pos, boolean instabuild){
        Block block = this.WAXED_BY_BLOCK.get().get(this);
        if (block != null){
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, heldStack);
            }
            if (!instabuild){
                heldStack.shrink(1);
            }
            BlockState newState = block.defaultBlockState().setValue(LIT, state.getValue(LIT)).setValue(FACING, state.getValue(FACING));
            level.setBlockAndUpdate(pos, newState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, block.defaultBlockState()));
            level.levelEvent(player, 3003, pos, 0);
            return true;
        }
        return false;
    }

    private boolean waxedOff(Level level,Player player,ItemStack heldStack, InteractionHand hand, BlockState state,BlockPos pos,boolean instabuild){
        Block block = this.WAXED_BY_BLOCK.get().inverse().get(this);
        if (block != null){
            BlockState newState = block.defaultBlockState().setValue(LIT,state.getValue(LIT)).setValue(FACING,state.getValue(FACING));
            level.setBlockAndUpdate(pos,newState);
            if (!instabuild) {
                heldStack.hurtAndBreak(1, player, (p_150686_) -> {
                    p_150686_.broadcastBreakEvent(hand);
                });
            }
            level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3004, pos, 0);
            return true;
        }
        return false;
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (!this.waxed && blockEntity instanceof AncientStoveBlockEntity stoveBlock && stoveBlock.isEmpty()){
            this.onRandomTick(pState, pLevel, pPos, pRandom);
        }
    }

    public void onRandomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextFloat() < 0.05688889F) {
            this.applyChangeOverTime(pState, pLevel, pPos, pRandom);
        }
    }

    public void applyChangeOverTime(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int i = this.getAge().ordinal();
        int j = 0;
        int k = 0;

        for(BlockPos blockpos : BlockPos.withinManhattan(pPos, 4, 4, 4)) {
            int l = blockpos.distManhattan(pPos);
            if (l > 4) {
                break;
            }

            if (!blockpos.equals(pPos)) {
                BlockState blockstate = pLevel.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof ChangeOverTimeBlock) {
                    Enum<?> oenum = ((ChangeOverTimeBlock)block).getAge();
                    if (this.getAge().getClass() == oenum.getClass()) {
                        int i1 = oenum.ordinal();
                        if (i1 < i) {
                            return;
                        }

                        if (i1 > i) {
                            ++k;
                        } else {
                            ++j;
                        }
                    }
                }
            }
        }

        float f = (float)(k + 1) / (float)(k + j + 1);
        float f1 = f * f * this.getChanceModifier();
        if (pRandom.nextFloat() < f1) {
            this.getNext(pState).ifPresent((state) -> {
                boolean lit = !(state.getBlock() == ImmortalersDelightBlocks.OXIDIZED_ANCIENT_STOVE.get());
                pLevel.setBlockAndUpdate(pPos, state.setValue(LIT,lit));
            });
        }
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return getNext(pState.getBlock()).isPresent()/* && !pState.getValue(LIT)*/;
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }

    public Optional<Block> getNext(Block pBlock) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(pBlock));
    }

    @Override
    public Optional<BlockState> getNext(BlockState pState) {
        return getNext(pState.getBlock()).map((p_154896_) -> p_154896_.withPropertiesOf(pState));
    }

    public Block getScrape(Block value) {
        for (Map.Entry<Block, Block> entry : NEXT_BY_BLOCK.get().entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isStoveTopCovered(Level level, BlockPos pos, BlockState stoveState) {
        if (!(stoveState.getBlock() instanceof AncientStoveBlock)) {
            return false;
        } else {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            return Shapes.joinIsNotEmpty(GRILLING_AREA, aboveState.getShape(level, abovePos), BooleanOp.AND);
        }
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide && (Boolean)state.getValue(LIT) ? createTickerHelper(blockEntityType, ImmortalersDelightBlocks.ANCIENT_STOVE_ENTITY.get(), AncientStoveBlockEntity::particleTick) : createStoveTicker(level, blockEntityType, ImmortalersDelightBlocks.ANCIENT_STOVE_ENTITY.get());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AncientStoveBlockEntity(blockPos,blockState);
    }

    static {
        GRILLING_AREA = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
    }
}

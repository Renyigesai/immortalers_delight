package com.renyigesai.immortalers_delight.block.ancient_stove;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.MathUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class WeatheringAncientStoveBlock extends HorizontalDirectionalBlock implements WeatheringCopper {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final WeatheringCopper.WeatherState weatherState;

    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder().put(ImmortalersDelightBlocks.ANCIENT_STOVE.get(),ImmortalersDelightBlocks.EXPOSED_ANCIENT_STOVE.get()).put(ImmortalersDelightBlocks.EXPOSED_ANCIENT_STOVE.get(),ImmortalersDelightBlocks.WEATHERED_ANCIENT_STOVE.get()).put(ImmortalersDelightBlocks.WEATHERED_ANCIENT_STOVE.get(),ImmortalersDelightBlocks.OXIDIZED_ANCIENT_STOVE.get()).build());

    public WeatheringAncientStoveBlock(Properties pProperties, WeatheringCopper.WeatherState weatherState) {
        super(pProperties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT,false).setValue(FACING, Direction.NORTH));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, true);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.onRandomTick(pState, pLevel, pPos, pRandom);
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
            this.getNext(pState).ifPresent((p_153039_) -> pLevel.setBlockAndUpdate(pPos, p_153039_));
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        Item heldItem = heldStack.getItem();

        if (heldItem instanceof AxeItem){
            Block scrape = getScrape(state.getBlock());
            if (scrape != null){
                BlockState newState = scrape.defaultBlockState().setValue(LIT,state.getValue(LIT)).setValue(FACING,state.getValue(FACING));
                level.setBlockAndUpdate(pos,newState);
                if (!player.getAbilities().instabuild) {
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

        if (state.getValue(LIT)) {
            if (heldStack.canPerformAction(ToolActions.SHOVEL_DIG)) {
                this.extinguish(state, level, pos);
                heldStack.hurtAndBreak(1, player, (action) -> {
                    action.broadcastBreakEvent(hand);
                });
                return InteractionResult.SUCCESS;
            }

            if (heldItem == Items.WATER_BUCKET) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                this.extinguish(state, level, pos);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }

                return InteractionResult.SUCCESS;
            }
        } else {
            if (heldItem instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                heldStack.hurtAndBreak(1, player, (action) -> {
                    action.broadcastBreakEvent(hand);
                });
                return InteractionResult.SUCCESS;
            }

            if (heldItem instanceof FireChargeItem) {
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        boolean isLit = level.getBlockState(pos).getValue(LIT);
        if (isLit && !entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.hurt(ModDamageTypes.getSimpleDamageSource(level, ModDamageTypes.STOVE_BURN), 1.0F);
        }

        super.stepOn(level, pos, state, entity);
    }

    public void extinguish(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LIT, false), 2);
        double x = (double)pos.getX() + 0.5;
        double y = pos.getY();
        double z = (double)pos.getZ() + 0.5;
        level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F, false);
    }

    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if (stateIn.getValue(CampfireBlock.LIT)) {
            double x = (double)pos.getX() + 0.5;
            double y = pos.getY();
            double z = (double)pos.getZ() + 0.5;
            if (rand.nextInt(10) == 0) {
                level.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = stateIn.getValue(HorizontalDirectionalBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double horizontalOffset = rand.nextDouble() * 0.6 - 0.3;
            double xOffset = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : horizontalOffset;
            double yOffset = rand.nextDouble() * 6.0 / 16.0;
            double zOffset = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : horizontalOffset;
            level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
        }

    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return getNext(pState.getBlock()).isPresent() && !pState.getValue(LIT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT,FACING);
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
}

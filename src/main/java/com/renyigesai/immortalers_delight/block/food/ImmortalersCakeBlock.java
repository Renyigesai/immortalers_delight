package com.renyigesai.immortalers_delight.block.food;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ImmortalersCakeBlock extends CakeBlock {
    public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public final Supplier<Item> servingItem;

    // 1. 静态声明 21 种物品的 Supplier 列表（类加载时仅持有引用，不会提前解包 .get()）
    // 严格按顺序排列，Index (0 ~ 20) 对应 (BITES 0~6, CANDLES 2~4)
    private static final List<Supplier<? extends Item>> CANDLE_SUPPLIERS = List.of(
            () -> Items.CANDLE,
            () -> Items.WHITE_CANDLE,
            () -> Items.ORANGE_CANDLE,
            () -> Items.MAGENTA_CANDLE,
            () -> Items.LIGHT_BLUE_CANDLE,
            () -> Items.YELLOW_CANDLE,
            () -> Items.LIME_CANDLE,
            () -> Items.PINK_CANDLE,
            () -> Items.GRAY_CANDLE,
            () -> Items.LIGHT_GRAY_CANDLE,
            () -> Items.CYAN_CANDLE,
            () -> Items.PURPLE_CANDLE,
            () -> Items.BLUE_CANDLE,
            () -> Items.BROWN_CANDLE,
            () -> Items.GREEN_CANDLE,
            () -> Items.RED_CANDLE,
            () -> Items.BLACK_CANDLE,
            () -> Items.TORCHFLOWER,
            () -> Items.BLAZE_ROD,
            ImmortalersDelightItems.SEXTLOTUS_LEAF, // RegistryObject 本身就实现了 Supplier<Item>
            ImmortalersDelightItems.A_BUSH
    );
    public ImmortalersCakeBlock(Properties pProperties, Supplier<Item> servingItem) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BITES, Integer.valueOf(0))
                .setValue(CANDLES, Integer.valueOf(1))
                .setValue(LIT, Boolean.valueOf(false))
        );
        this.servingItem = servingItem;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BITES);
        pBuilder.add(CANDLES);
        pBuilder.add(LIT);
    }

    protected static final VoxelShape CAKE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
    protected static final VoxelShape CANDLE_SHAPE = Block.box(7.0D, 8.0D, 7.0D, 9.0D, 14.0D, 9.0D);
    protected static final VoxelShape SHAPES = Shapes.or(CAKE_SHAPE, CANDLE_SHAPE);
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(CANDLES) == 1) return super.getShape(pState,pLevel,pPos,pContext);
        return SHAPES;
    }
    /**
     * 安全地根据物品获取对应索引 (0~20)，如果不是合法蜡烛则返回 -1
     */
    private static int getCandleIndex(Item item) {
        for (int i = 0; i < CANDLE_SUPPLIERS.size(); i++) {
            if (CANDLE_SUPPLIERS.get(i).get() == item) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 安全地根据方块状态反查物品
     */
    @Nullable
    public static Item getCandleByState(BlockState state) {
        int candles = state.getValue(CANDLES);
        if (candles <= 1) return null;

        int bites = state.getValue(BITES);
        int index = (candles - 2) * 7 + bites;

        if (index >= 0 && index < CANDLE_SUPPLIERS.size()) {
            return CANDLE_SUPPLIERS.get(index).get();
        }
        return null;
    }

    protected boolean canAddCandle(BlockState state, ItemStack stack) {
        return state.getValue(BITES) == 0
                && state.getValue(CANDLES) == 1
                && getCandleIndex(stack.getItem()) != -1;
    }

    protected BlockState getStateByCandle(BlockState state, ItemStack stack) {
        int index = getCandleIndex(stack.getItem());
        if (index != -1) {
            int bitesValue = index % 7;         // 0 ~ 6
            int candlesValue = (index / 7) + 2;   // 2 ~ 4
            return state.setValue(BITES, bitesValue)
                    .setValue(CANDLES, candlesValue)
                    .setValue(LIT, false);
        }
        return state;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        boolean hasCandle = pState.getValue(CANDLES) > 1;

        // 1. 插蜡烛
        if (!hasCandle && this.canAddCandle(pState, itemstack)) {
            if (!pPlayer.isCreative()) {
                itemstack.shrink(1);
            }
            pLevel.playSound(null, pPos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.setBlockAndUpdate(pPos, this.getStateByCandle(pState, itemstack));
            pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
            pPlayer.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }

        // 2. 已插蜡烛的交互
        if (hasCandle) {
            // 点燃
            if (itemstack.is(Items.FLINT_AND_STEEL) || itemstack.is(Items.FIRE_CHARGE)) {
                if (!pState.getValue(LIT)) {
                    //改点燃状态
                    setLit(pLevel, pState, pPos, true);
                    pLevel.playSound(pPlayer, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);

                    //消耗物品
                    if (!pPlayer.isCreative()) {
                        if (itemstack.is(Items.FLINT_AND_STEEL)) {
                            itemstack.hurtAndBreak(1, pPlayer, (p) -> p.broadcastBreakEvent(pHand));
                        } else itemstack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                }
                return InteractionResult.PASS;
            }

            // 熄灭
            if (candleHit(pHit) && itemstack.isEmpty() && pState.getValue(LIT)) {
                extinguish(pPlayer, pState, pLevel, pPos);
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }

            // 拔下蜡烛并吃第一口
            if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
                Item candleItem = getCandleByState(pState);
                if (candleItem != null) {
                    popResource(pLevel, pPos, new ItemStack(candleItem));
                }

                // 重置为咬了1口的普通蛋糕 (BITES=1, CANDLES=1, LIT=false)
                InteractionResult eatResult = eatCake(pLevel, pPos, this.defaultBlockState().setValue(BITES, 1), pPlayer);
                if (eatResult.consumesAction()) {
                    pLevel.playSound(null, pPos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return eatResult;
            }

            return InteractionResult.PASS;
        }

        // 3. 普通未插蜡烛蛋糕正常食用
        return eatCake(pLevel, pPos, pState, pPlayer);
    }

    protected static InteractionResult eatCake(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pPlayer.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            pPlayer.awardStat(Stats.EAT_CAKE_SLICE);
            //pPlayer.getFoodData().eat(2, 0.1F);
            ItemStack serving = getServingItem(pState);
            serving.finishUsingItem(pLevel, pPlayer);
            int i = pState.getValue(BITES);
            pLevel.gameEvent(pPlayer, GameEvent.EAT, pPos);
            if (i < 6) {
                pLevel.setBlock(pPos, pState.setValue(BITES, Integer.valueOf(i + 1)), 3);
            } else {
                pLevel.removeBlock(pPos, false);
                pLevel.gameEvent(pPlayer, GameEvent.BLOCK_DESTROY, pPos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    public static ItemStack getServingItem(BlockState state) {
        if (state.getBlock() instanceof ImmortalersCakeBlock cakeBlock) {
            return new ItemStack((ItemLike)cakeBlock.servingItem.get());
        }
        return ItemStack.EMPTY;
    }

    private static boolean candleHit(BlockHitResult pHit) {
        return pHit.getLocation().y - (double)pHit.getBlockPos().getY() > 0.5D;
    }

    public static void setLit(LevelAccessor pLevel, BlockState pState, BlockPos pPos, boolean pLit) {
        pLevel.setBlock(pPos, pState.setValue(LIT, pLit), 11);
    }

    public static void extinguish(@Nullable Player pPlayer, BlockState pState, LevelAccessor pLevel, BlockPos pPos) {
        setLit(pLevel, pState, pPos, false);
        pLevel.playSound(null, pPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 2.0F, 1.0F);
        pLevel.addParticle(ParticleTypes.SMOKE, pPos.getX() + 0.5D, pPos.getY() + 0.8D, pPos.getZ() + 0.5D, 0.0D, 0.1D, 0.0D);
        pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(CANDLES) > 1 && pState.getValue(LIT)) {
            Vec3 candleOffset = new Vec3(0.5D, 0.875D, 0.5D);
            Vec3 spawnPos = candleOffset.add(pPos.getX(), pPos.getY(), pPos.getZ());
            Item candleItem = getCandleByState(pState);
            addParticlesAndSoundForCandle(pLevel, spawnPos, candleItem, pRandom);
        }
    }

    private static void addParticlesAndSoundForCandle(Level pLevel, Vec3 pos, @Nullable Item candleItem, RandomSource pRandom) {
        float offsetRange = 0.03F;
        double px = pos.x() + (double)(pRandom.nextFloat() * 2.0F - 1.0F) * (double)offsetRange;
        double py = pos.y() + 0.05D + (double)(pRandom.nextFloat() * 2.0F - 1.0F) * (double)offsetRange;
        double pz = pos.z() + (double)(pRandom.nextFloat() * 2.0F - 1.0F) * (double)offsetRange;

        ParticleOptions flameParticle = ParticleTypes.SMALL_FLAME;
        ParticleOptions smokeParticle = ParticleTypes.SMOKE;

        if (candleItem == Items.BLAZE_ROD) {
            flameParticle = ParticleTypes.FLAME;
        } else if (candleItem == Items.TORCHFLOWER) {
            flameParticle = ParticleTypes.FALLING_SPORE_BLOSSOM;
        } else if (candleItem != null && candleItem == ImmortalersDelightItems.WARPED_LAUREL.get()) {
            flameParticle = ParticleTypes.SOUL_FIRE_FLAME;
            smokeParticle = ParticleTypes.SOUL;
        } else if (candleItem != null && candleItem == ImmortalersDelightItems.A_BUSH.get()) {
            flameParticle = ParticleTypes.SMOKE;
            smokeParticle = ParticleTypes.LARGE_SMOKE;
        }

        pLevel.addParticle(smokeParticle, px, py, pz, 0.0D, 0.0D, 0.0D);
        pLevel.addParticle(flameParticle, px, py, pz, 0.0D, 0.0D, 0.0D);

        if (pRandom.nextFloat() < 0.17F) {
            pLevel.playLocalSound(
                    pos.x(), pos.y(), pos.z(),
                    SoundEvents.CANDLE_AMBIENT,
                    SoundSource.BLOCKS,
                    1.0F + pRandom.nextFloat(),
                    pRandom.nextFloat() * 0.7F + 0.3F,
                    false
            );
        }
    }
}

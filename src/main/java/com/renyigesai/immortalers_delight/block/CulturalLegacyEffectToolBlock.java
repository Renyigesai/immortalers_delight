package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

@Mod.EventBusSubscriber
public class CulturalLegacyEffectToolBlock extends ReapCropBlock{
    /*
    用于显示附魔台粒子的列表
     */
    public static final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter((p_207914_) -> {
        return Math.abs(p_207914_.getX()) == 2 || Math.abs(p_207914_.getZ()) == 2;
    }).map(BlockPos::immutable).toList();

    /*
     结构空位的轮廓形状
     */
    protected static final VoxelShape OUTLINE_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

    public CulturalLegacyEffectToolBlock(Properties p_52247_) {
        super(p_52247_);
        MinecraftForge.EVENT_BUS.register(this);
    }
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }
    /*
     执行随机 tick 逻辑，移除方块
     */
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        /*
        执行生长逻辑
         */
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, pLevel, pPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int)(25.0F / f) + 1) == 0)) {
                    pLevel.setBlock(pPos,Blocks.AIR.defaultBlockState(),2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }
    /*
     获取树叶方块的光照强度，这里返回1，表示树叶有一定的透光性
     */
    public int getLightBlock(BlockState p_54460_, BlockGetter p_54461_, BlockPos p_54462_) {
        return 1;
    }
    /*
     在客户端定期调用，用于显示附魔台粒子效果
     */
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);

        for(BlockPos blockpos : BOOKSHELF_OFFSETS) {
            if (pRandom.nextInt(16) == 0 && isValidBookShelf(pLevel, pPos, blockpos)) {
                pLevel.addParticle(ParticleTypes.ENCHANT, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 2.0D, (double)pPos.getZ() + 0.5D, (double)((float)blockpos.getX() + pRandom.nextFloat()) - 0.5D, (double)((float)blockpos.getY() - pRandom.nextFloat() - 1.0F), (double)((float)blockpos.getZ() + pRandom.nextFloat()) - 0.5D);
            }
        }

    }
    public static boolean isValidBookShelf(Level pLevel, BlockPos pTablePos, BlockPos pOffsetPos) {
        return pLevel.getBlockState(pTablePos.offset(pOffsetPos)).getEnchantPowerBonus(pLevel, pTablePos.offset(pOffsetPos)) != 0 && pLevel.getBlockState(pTablePos.offset(pOffsetPos.getX() / 2, pOffsetPos.getY(), pOffsetPos.getZ() / 2)).is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    }
    /*
     判断作物是否能在给定环境中存活
     重写存活条件以确保能跟树叶一样浮空放置
     */
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return (true);
    }

    @SubscribeEvent
    public void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
        if (event == null || event.isCanceled()) {
            return;
        }

        // 获取世界和附魔台的位置
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        // 以附魔台为中心，检查 5*5 范围内的方块
        int goldBlockCount = maxAgeToolBlocksInRange(level, pos, 2); // 2 表示从中心到边缘的距离

        if (goldBlockCount > 0) {
            // 如果有金,根据金块数量计算新的附魔等级
            int newLevel = calculateNewLevel(event.getEnchantLevel(), goldBlockCount);

            // 设置新的附魔等级
            event.setEnchantLevel(newLevel);
        }
    }

    /**
     * 处理指定范围内的指定方块
     * @param level 世界
     * @param centerPos 中心位置
     * @param range 范围（从中心到边缘的距离）
     * @return 最大Age
     */
    private int maxAgeToolBlocksInRange(Level level, BlockPos centerPos, int range) {
        int maxAge = 0;
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos checkPos = centerPos.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof CulturalLegacyEffectToolBlock) {
                        int ageHere = this.getAge(level.getBlockState(checkPos));
                        maxAge = maxAge > ageHere + 1 ? maxAge : ageHere + 1;
                    }
                }
            }
        }
        return maxAge;
    }

    /**
     * 范围内最大AGE的当前方块设置附魔等级
     * @param originalLevel 原始附魔等级
     * @param maxAge 范围内最大Age
     * @return 新的附魔等级
     */
    private static int calculateNewLevel(int originalLevel, int maxAge) {
        // 根据范围内最大AGE的当前方块设置附魔等级。
        int newLevel = originalLevel + 8 + 4 * (maxAge - 1);
        return Math.min(30, newLevel);
    }
    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.BOWL_OF_MILLENIAN_BAMBOO.get();
    }

    /*
    获取仙人掌的轮廓形状
     */
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return OUTLINE_SHAPE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        return super.getDrops(p_287732_, p_287596_);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
    @Override
    public boolean isValidBonemealTarget(LevelReader p_255715_, BlockPos p_52259_, BlockState p_52260_, boolean p_52261_) {
        return false;
    }
    @Override
    public void performBonemeal(ServerLevel p_221040_, RandomSource p_221041_, BlockPos p_221042_, BlockState p_221043_) {
        if (this.getAge(p_221043_) < this.getMaxAge()) {
            p_221040_.setBlock(p_221042_, this.getStateForAge(this.getAge(p_221043_) + 1), 2);
        }
    }
}

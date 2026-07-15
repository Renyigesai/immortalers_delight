package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.client.particle.SpiralSoulParticleOption;
import com.renyigesai.immortalers_delight.entities.projectile.EffectCloudBaseEntity;
import com.renyigesai.immortalers_delight.entities.projectile.GasCloudEntity;
import com.renyigesai.immortalers_delight.entities.projectile.WarpedLaurelHitBoxEntity;
import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluids;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriState;

public class WarpedLaurelCrop extends ReapCropBlock {
    public WarpedLaurelCrop(Properties p_52247_) {
        super(p_52247_);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.WARPED_LAUREL_SEEDS.get();
    }

    /** Soul sand/soil for nether mechanics; farmland for normal farming (same as KwatWheat). */
    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.SOUL_SAND) || pState.is(Blocks.SOUL_SOIL) || pState.getBlock() instanceof FarmBlock;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState soil = level.getBlockState(below);
        TriState soilDecision = soil.canSustainPlant(level, below, Direction.UP, state);
        if (!soilDecision.isDefault()) {
            return soilDecision.isTrue();
        }
        if (!this.mayPlaceOn(soil, level, below)) {
            return false;
        }
        if (soil.is(Blocks.SOUL_SAND) || soil.is(Blocks.SOUL_SOIL)) {
            return true;
        }
        return hasSufficientLight(level, pos);
    }

    @Override
    public int getMaxAge() {
        return 6;
    }

    private static boolean canGrowWithoutLight(BlockState soil) {
        return soil.is(Blocks.SOUL_SAND) || soil.is(Blocks.SOUL_SOIL);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        BlockState soil = pLevel.getBlockState(pPos.below());
        if (canGrowWithoutLight(soil) || pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(pState, pLevel, pPos);
                if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(pLevel, pPos, pState, pRandom.nextInt((int)(25.0F / f) + 1) == 0)) {
                    if (i == this.getMaxAge() - 1) {
                        makeAreaOfEffectCloud(pLevel, pPos);
                    }
                    pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                    net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel);
        int j = this.getMaxAge();
        if (this.getAge(pState) == j-1) {
            i = j;
            makeAreaOfEffectCloud(pLevel, pPos);
        } else if (i >= j) {
            i = j-1;
        }

        pLevel.setBlock(pPos, this.getStateForAge(i), 2);
    }

    private void makeAreaOfEffectCloud(Level level, BlockPos pPos) {
        if (level.isClientSide()) return;
        if (!level.getBlockState(pPos.below()).is(BlockTags.SOUL_FIRE_BASE_BLOCKS)) return;
        EffectCloudBaseEntity effectCloud = new WarpedLaurelHitBoxEntity(level, pPos.getX() + 0.5, pPos.getY() + 0.1, pPos.getZ() + 0.5);

        effectCloud.setDuration(64);
        effectCloud.setRadius(3.0F);
        effectCloud.setRadiusOnUse(0.0F);
        effectCloud.setWaitTime(16);
        effectCloud.setRadiusPerTick(0.0f);
        effectCloud.setParticle(ParticleTypes.SOUL_FIRE_FLAME);

        effectCloud.addEffect(new MobEffectInstance(MobEffects.DARKNESS,100));

        level.addFreshEntity(effectCloud);
    }
}

package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import static com.renyigesai.immortalers_delight.entities.EntityNbtHelper.KNOCKBACK_SILVERFISH_TAG;

public class InfestedFallingBlock extends InfestedBlock implements Fallable {

    public InfestedFallingBlock(Block pHostBlock, Properties pProperties) {
        super(pHostBlock, pProperties);
    }

    private void spawnInfestation(ServerLevel pLevel, BlockPos pPos) {
        Silverfish silverfish = EntityType.SILVERFISH.create(pLevel);
        if (silverfish != null) {
            silverfish.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
            // 创建NBT标签
            CompoundTag nbtTag = new CompoundTag();
            nbtTag.putString("Tags", KNOCKBACK_SILVERFISH_TAG);
            silverfish.addAdditionalSaveData(nbtTag);
            pLevel.addFreshEntity(silverfish);
            silverfish.spawnAnim();
        }

    }
    @Override
    public void spawnAfterBreak(BlockState pState, ServerLevel pLevel, BlockPos pPos, ItemStack pStack, boolean pDropExperience) {
        super.spawnAfterBreak(pState, pLevel, pPos, pStack, pDropExperience);
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pStack) == 0) {
            this.spawnInfestation(pLevel, pPos);
        }

    }
    /**
    该方法在方块坠落并破碎后触发，执行以下操作：
    获取坠落实体的包围盒中心坐标。
    触发世界事件（ID 2001）用于播放破坏特效，并传入对应方块状态的ID。
    触发游戏事件 BLOCK_DESTROY，通知系统该位置的方块被破坏。
     */
    public void onBrokenAfterFall(Level pLevel, BlockPos pPos, FallingBlockEntity pFallingBlock) {
        if (pLevel instanceof ServerLevel serverLevel) this.spawnInfestation(serverLevel, pPos);
        Vec3 vec3 = pFallingBlock.getBoundingBox().getCenter();
        pLevel.levelEvent(2001, BlockPos.containing(vec3), Block.getId(pFallingBlock.getBlockState()));
        pLevel.gameEvent(pFallingBlock, GameEvent.BLOCK_DESTROY, vec3);
    }
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        pLevel.scheduleTick(pPos, this, this.getDelayAfterPlace());
    }

    /**
     * 根据指定的邻居方向和邻居状态更新给定的状态，返回一个新的状态。
     * 例如，篱笆会尽可能地与传入的状态建立连接，而湿混凝土粉则会立即返回其固化后的状态。
     * 注意，此方法理想情况下应仅考虑传递的具体方向。
     *
     * @param pState 当前状态，用于生成新的状态。
     * @param pFacing 邻居方向，表示邻居相对于当前状态的位置。
     * @param pFacingState 邻居状态，考虑邻居的状态来更新当前状态。
     * @return 更新后的新状态。
     */
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        pLevel.scheduleTick(pCurrentPos, this, this.getDelayAfterPlace());
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (isFree(pLevel.getBlockState(pPos.below())) && pPos.getY() >= pLevel.getMinBuildHeight()) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(pLevel, pPos, pState);
            this.falling(fallingblockentity);
        }
    }

    protected void falling(FallingBlockEntity pEntity) {
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    public static boolean isFree(BlockState pState) {
        return pState.isAir() || pState.is(BlockTags.FIRE) || pState.liquid() || pState.canBeReplaced();
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(16) == 0) {
            BlockPos blockpos = pPos.below();
            if (isFree(pLevel.getBlockState(blockpos))) {
                ParticleUtils.spawnParticleBelow(pLevel, pPos, pRandom, new BlockParticleOption(ParticleTypes.FALLING_DUST, pState));
            }
        }

    }

    public int getDustColor(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return -16777216;
    }
}

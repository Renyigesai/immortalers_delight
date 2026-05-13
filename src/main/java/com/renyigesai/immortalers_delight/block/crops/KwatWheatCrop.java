package com.renyigesai.immortalers_delight.block.crops;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.ReapCropBlock;
import com.renyigesai.immortalers_delight.entities.projectile.EffectCloudBaseEntity;
import com.renyigesai.immortalers_delight.entities.projectile.GasCloudEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightPotions;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class KwatWheatCrop extends ReapCropBlock {

    private static final BooleanProperty POISON = BooleanProperty.create("poison");

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D)
    };

    public KwatWheatCrop(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(POISON,false));
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.KWAT_WHEAT_SEEDS.get();
    }

    public VoxelShape getShape(BlockState p_51330_, BlockGetter p_51331_, BlockPos p_51332_, CollisionContext p_51333_) {
        return SHAPE_BY_AGE[this.getAge(p_51330_)];
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        return super.getDrops(p_287732_, p_287596_);
    }

    //方块状态发生变化时会调用的方法
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (state.getValue(POISON) && !pNewState.is(state.getBlock()) && !pMovedByPiston) {
            makeAreaOfEffectCloud(level,pPos);
        } else super.onRemove(state, level, pPos, pNewState, pMovedByPiston);
    }
    //Forge扩展的、方块被玩家破坏时会调用的方法，底层原理为调用下方的playerWillDestroy，但这个方法多一个判断判定方块是否会真的被破坏（破坏可以通过事件拦截）
    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    //方块被玩家破坏时会调用的方法，通常用于多部分的方块(例如门)被破坏时的连锁破坏
    @Override
    public void playerWillDestroy(@NotNull Level pLevel, @NotNull BlockPos pPos, BlockState pState, @NotNull Player pPlayer) {
        if (pState.getValue(POISON) && pState.getValue(AGE) == 7) {
            System.out.println("playerWillDestroy");
            makeAreaOfEffectCloud(pLevel,pPos);
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    //制造毒云，用于瓦斯麦轰击
    private void makeAreaOfEffectCloud(Level level, BlockPos pPos) {
        System.out.println("makeAreaOfEffectCloud");
        if (level.isClientSide()) return;
        EffectCloudBaseEntity effectCloud = new GasCloudEntity(level, pPos.getX(), pPos.getY(), pPos.getZ());

        effectCloud.setRadius(3.0F);
        effectCloud.setRadiusOnUse(-0.1F);
        effectCloud.setWaitTime(10);
        effectCloud.setRadiusPerTick(-(effectCloud.getRadius() / (float)effectCloud.getDuration()) * 3.0f);
        effectCloud.setParticle(ImmortalersDelightParticleTypes.KWAT.get());

        effectCloud.setPotion(ImmortalersDelightPotions.STRONG_GAS.get());

        level.addFreshEntity(effectCloud);
    }
    //方块被实体踩踏时会调用的方法
    public void entityInside(BlockState state, Level level, BlockPos pPos, Entity pEntity) {
        System.out.println("entityInside");
        super.entityInside(state, level, pPos, pEntity);
        if (pEntity instanceof LivingEntity) {
            int age = state.getValue(AGE);
            if (state.getValue(POISON) && age >= this.getMaxAge() - 1) {
                List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, new AABB(pPos).inflate(3.0D, 3.0D, 3.0D));
                if (!list.isEmpty()) {
                    for (LivingEntity livingentity : list) {
                        if (!(livingentity.getItemBySlot(EquipmentSlot.HEAD).is(ImmortalersDelightItems.GOLDEN_FABRIC_VEIL.get()))){
                            livingentity.hurt(level.damageSources().cactus(), 2.0F);
                            livingentity.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),120,1));
                        }else {
                            if (livingentity instanceof ServerPlayer serverPlayer) {
                                ImmortalersDelightMod.RESIST_GAS_POISONING_TRIGGER.trigger(serverPlayer);
                            }
                        }
                    }
                }
                spawnParticle(level, pPos);
                level.setBlock(pPos, state.setValue(AGE,5).setValue(POISON,false), 3);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel && state.getValue(POISON) && state.getValue(AGE) >= 3) {
            for (int i = 0; i < 8; i++) {
                Vec3 vec3 = new Vec3(pos.getX() + serverLevel.random.nextFloat(),pos.getY() + serverLevel.random.nextFloat(),pos.getZ() + serverLevel.random.nextFloat());
                serverLevel.sendParticles(
                        ImmortalersDelightParticleTypes.KWAT.get(), vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0.025
                );
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    private void spawnParticle(Level level, BlockPos pPos) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
            double radius = 3.3;
            for (int i = 0; i < 32; i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y;
                if (r <= radius / 3) {
                    serverLevel.sendParticles(
                            ImmortalersDelightParticleTypes.GAS_SMOKE.get(), x, y, z, 1, 0, 0, 0, 0.025
                    );
                } else serverLevel.sendParticles(
                        ImmortalersDelightParticleTypes.KWAT.get(), x, y, z, 1, 0, 0, 0, 0.025
                );
            }
        }
    }


    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        if (pState.getBlock() == this)
            return pLevel.getBlockState(blockpos).canSustainPlant(pLevel, blockpos, Direction.UP, this);
        return this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);
    }

    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.SOUL_SAND) || pState.is(Blocks.SOUL_SOIL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE,POISON);
    }
    //什么情况下这个方块需要随机刻
    public boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(POISON);
    }
    //随机刻生长，在生长到成熟后再生长一次获得poison状态
    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, pLevel, pPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int) (pState.getValue(POISON) ? 25.0F / f : 25.0F - f) + 1) == 0)) {
                    pLevel.setBlock(pPos, pState.setValue(getAgeProperty(),i + 1), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            } else {
                pLevel.setBlock(pPos, pState.setValue(POISON,true), 2);
            }
        }
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        int q = pState.getValue(POISON) || pLevel.random.nextInt(5) < 3 ? this.getBonemealAgeIncrease(pLevel) : 1;
        int i = this.getAge(pState) + q;
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        pLevel.setBlock(pPos, pState.setValue(getAgeProperty(),i), 2);
    }

}

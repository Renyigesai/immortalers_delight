package com.renyigesai.immortalers_delight.block.food;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Iterator;
import java.util.function.Supplier;

public class LargePieBlock extends PieBlock {
    public static final BooleanProperty HALF = BooleanProperty.create("half");
    public LargePieBlock(Properties properties, Supplier<Item> pieSlice) {
        super(properties, pieSlice);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BITES, 0)
                .setValue(HALF,true)
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    protected @NotNull InteractionResult consumeBite(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else if (state.getValue(HALF)){
            ItemStack sliceStack = this.getPieSliceItem();
            ItemStack sliceCopy = sliceStack.copy();
            sliceCopy.finishUsingItem(level,player);

            level.setBlock(pos, (BlockState)state.setValue(HALF,false), 3);

            level.playSound((Player)null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.001);
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.consumeBite(level, pos, state.setValue(HALF,true), player);
        }
    }

    protected @NotNull InteractionResult cutSlice(@NotNull Level level, @NotNull BlockPos pos, BlockState state, @NotNull Player player, @NotNull Item knife) {
        if (state.getValue(HALF)){

            Direction direction = player.getDirection().getOpposite();
            ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
            level.playSound((Player)null, pos, (SoundEvent) ModSounds.BLOCK_FOOD_SLICE.get(), SoundSource.PLAYERS, 0.8F, 0.8F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.001);
            }

            level.setBlock(pos, (BlockState)state.setValue(HALF,false), 3);

            player.awardStat(Stats.ITEM_USED.get(knife));
            return InteractionResult.SUCCESS;
        } else {
            return super.cutSlice(level, pos, state.setValue(HALF,true), player, knife);
        }
    }
}

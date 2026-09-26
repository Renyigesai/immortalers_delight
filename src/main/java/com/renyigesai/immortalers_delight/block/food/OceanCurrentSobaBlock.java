package com.renyigesai.immortalers_delight.block.food;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.function.Supplier;

public class OceanCurrentSobaBlock extends FeastBlock {

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public OceanCurrentSobaBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers, boolean hasServingParticles) {
        super(properties, servingItem, hasLeftovers, hasServingParticles);
        this.registerDefaultState(defaultBlockState()
                .setValue(SERVINGS,4)
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }

    public OceanCurrentSobaBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers);
        this.registerDefaultState(defaultBlockState()
                .setValue(SERVINGS,4)
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return super.use(state, level, pos, player, hand, hit);
        else {
            InteractionResult result = InteractionResult.PASS;
            //需要先开盖才能盛取
            if (state.getValue(OPEN)) {
                result = super.use(state, level, pos, player, hand, hit);
                //不能盛取则关盖
                if (result == InteractionResult.PASS) level.setBlock(pos, state.setValue(OPEN, false), 3);
            } else level.setBlock(pos, state.setValue(OPEN, true), 3);

            return result;
        }
    }

}

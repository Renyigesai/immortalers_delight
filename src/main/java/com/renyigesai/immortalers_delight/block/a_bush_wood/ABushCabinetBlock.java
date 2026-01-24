package com.renyigesai.immortalers_delight.block.a_bush_wood;

import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.utility.MathUtils;

public class ABushCabinetBlock extends CabinetBlock {
    public static final BooleanProperty LIT;
    public ABushCabinetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(LIT,true));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        boolean lit = state.getValue(LIT);
        if (lit){
            if (itemInHand.is(Items.WATER_BUCKET)){
                level.setBlock(pos,state.setValue(LIT,false),3);
                if (!player.getAbilities().instabuild){
                    itemInHand.shrink(1);
                }
                ItemUtils.givePlayerItem(player,new ItemStack(Items.BUCKET));
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }else {
                return super.use(state, level, pos, player, hand, hit);
            }
        }else {
            if (itemInHand.getItem() instanceof FlintAndSteelItem){
                level.setBlock(pos,state.setValue(LIT,true),3);
                if (!player.getAbilities().instabuild){
                    itemInHand.hurtAndBreak(1,player,(entiity)->entiity.broadcastBreakEvent(hand));
                }
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING,OPEN,LIT);
    }

    static {
        LIT = BlockStateProperties.LIT;
    }
}

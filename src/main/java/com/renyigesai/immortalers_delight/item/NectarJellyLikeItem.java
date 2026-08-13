package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.recipe.AggregatedBlock;
import com.renyigesai.immortalers_delight.recipe.ChangeBlockRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class NectarJellyLikeItem extends Item {
    public NectarJellyLikeItem(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Restore cake / feast / pie blocks, or apply a change_block recipe.
     */
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        ItemStack itemstack = pContext.getItemInHand();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState state = level.getBlockState(blockpos);

        if (state.getBlock() instanceof CakeBlock || state.getBlock() instanceof FeastBlock || state.getBlock() instanceof PieBlock) {
            BlockState defaultState = state.getBlock().defaultBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                    && defaultState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                defaultState = defaultState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
            }
            if (!defaultState.equals(state)) {
                level.setBlock(blockpos, defaultState, Block.UPDATE_CLIENTS);
                level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
                Player player = pContext.getPlayer();
                if (player != null && !player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.FAIL;
        }

        Optional<ChangeBlockRecipe> recipeOptional = getCurrentRecipe(pContext);
        if (recipeOptional.isEmpty()) {
            return InteractionResult.PASS;
        }

        ChangeBlockRecipe recipe = recipeOptional.get();
        BlockState target = recipe.getResultBlock();
        BlockState output = target;
        Collection<Property<?>> propertiesCanInherit = AggregatedBlock.getDefaultOrNonProperties(output, true);
        for (Property<?> property : propertiesCanInherit) {
            if (recipe.getPropertiesKeepDefault().contains(property.getName())) {
                continue;
            }
            if (state.hasProperty(property) && output.hasProperty(property)) {
                Comparable<?> currentValue = state.getValue(property);
                if (property.getPossibleValues().contains(currentValue)) {
                    output = AggregatedBlock.setPropertyGeneric(output, property, currentValue.toString());
                }
            }
        }

        level.setBlock(blockpos, output, Block.UPDATE_CLIENTS);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private Optional<ChangeBlockRecipe> getCurrentRecipe(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockState state = level.getBlockState(pContext.getClickedPos());

        if (level.isClientSide()) {
            return Optional.empty();
        }

        NonNullList<BlockState> inputs = NonNullList.withSize(1, state);
        return level.getRecipeManager()
                .getAllRecipesFor(ChangeBlockRecipe.Type.INSTANCE)
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.matchBlocks(inputs, level))
                .findFirst();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip." + ImmortalersDelightMod.MODID + ".ancient_nectar_jelly")
                .withStyle(ChatFormatting.YELLOW));
        super.appendHoverText(stack, context, tooltip, flagIn);
    }
}

package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.recipe.AggregatedBlock;
import com.renyigesai.immortalers_delight.recipe.ChangeBlockRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class NectarJellyLikeItem extends Item {
    public NectarJellyLikeItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        Player player = context.getPlayer();
        if (level.isClientSide || player == null) return InteractionResult.sidedSuccess(true);
        BlockState output = null;
        if (state.getBlock() instanceof CakeBlock || state.getBlock() instanceof FeastBlock || state.getBlock() instanceof PieBlock) {
            output = state.getBlock().defaultBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && output.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) output = output.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
            if (output.equals(state)) return InteractionResult.FAIL;
        } else {
            Optional<ChangeBlockRecipe> recipe = level.getRecipeManager().getAllRecipesFor(ChangeBlockRecipe.Type.INSTANCE).stream().filter(r -> r.matchBlocks(NonNullList.withSize(1, state), level)).findFirst();
            if (recipe.isEmpty()) return InteractionResult.PASS;
            BlockState target = recipe.get().getResultBlock(level.registryAccess());
            output = target;
            Collection<Property<?>> inheritable = AggregatedBlock.getDefaultOrNonProperties(output, true);
            for (Property<?> property : inheritable) {
                if (recipe.get().getPropertiesKeepDefault().contains(property.getName())) continue;
                if (state.hasProperty(property)) output = AggregatedBlock.setPropertyGeneric(output, property, state.getValue(property).toString());
            }
        }
        level.setBlock(context.getClickedPos(), output, Block.UPDATE_CLIENTS);
        level.playSound(null, context.getClickedPos(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!player.getAbilities().instabuild) context.getItemInHand().shrink(1);
        if (player instanceof ServerPlayer serverPlayer) ImmortalersDelightMod.LOGGER.debug("Nectar jelly changed block for {} at {}", serverPlayer.getGameProfile().getName(), context.getClickedPos());
        return InteractionResult.sidedSuccess(false);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        String name = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        tooltip.add(Component.translatable("tooltip." + ImmortalersDelightMod.MODID + "." + name).withStyle(ChatFormatting.YELLOW));
    }
}

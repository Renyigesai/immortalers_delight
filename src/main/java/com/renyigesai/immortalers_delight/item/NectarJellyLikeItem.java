package com.renyigesai.immortalers_delight.item;

import com.google.gson.JsonSyntaxException;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.recipe.AggregatedBlock;
import com.renyigesai.immortalers_delight.recipe.ChangeBlockRecipe;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class NectarJellyLikeItem extends Item {
    public NectarJellyLikeItem(Properties pProperties) {
        super(pProperties);
    }

    /**
     * 花蜜棒冰的修复方块食物方法
     * @param pContext
     * @return
     */
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        ItemStack itemstack = pContext.getItemInHand();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState state = level.getBlockState(blockpos);
        //花蜜棒冰的修复方块，沿用类继承逻辑快速检测方块是否为蛋糕、 FeastBlock、 PieBlock
        if (state.getBlock() instanceof CakeBlock || state.getBlock() instanceof FeastBlock || state.getBlock() instanceof PieBlock) {
            BlockState defaultState = state.getBlock().defaultBlockState();
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                    && defaultState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                defaultState = defaultState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
            }
            if(!defaultState.equals(state)){
                if (!level.isClientSide() && pContext.getPlayer() instanceof ServerPlayer player) {
                    //成就触发器CriticalTriggerRegistry.JELLY_TRIGGER.trigger(player);
                }
                level.setBlock(blockpos, defaultState, Block.UPDATE_CLIENTS);
                level.playSound((Player)null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!pContext.getPlayer().getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        }
        //若不是蛋糕、 FeastBlock、 PieBlock，启用配方检测
        else {
            System.out.println("进入配方审查recipe found");
            //配方检测
            Optional<ChangeBlockRecipe> recipeOptional = getCurrentRecipe(pContext);
            //如果没有匹配到配方。则返回PASS
            if (recipeOptional.isEmpty()) {
                System.out.println("没有找到配方recipe not found");
            } else {
                //从配方中获取目标方块状态
                ChangeBlockRecipe recipe = recipeOptional.get();
                BlockState target = recipe.getResultBlock(level.registryAccess());
                //应用目标方块状态
                BlockState output = target;
                //获取目标方块状态中的默认属性
                Collection<Property<?>> propertiesCanInherit = AggregatedBlock.getDefaultOrNonProperties(output,true);
                System.out.println("可以继承的默认属性：" + propertiesCanInherit);
                //原始方块状态的属性可以继承到默认的属性来
                for (Property<?> property : propertiesCanInherit) {
                    //排除输出时需要保持默认值的属性
                    if (recipe.getPropertiesKeepDefault().contains(property.getName())) continue;
                    // 校验1：原始状态有该属性 + 目标状态也支持该属性
                    if (state.hasProperty(property) && output.hasProperty(property)) {
                        Comparable<?> currentValue = state.getValue(property);
                        // 校验2：通过 Property 的合法值集合，校验值的有效性（核心！）
                        if (property.getPossibleValues().contains(currentValue)) {
                            // 类型安全的设置：借助泛型方法避免通配符不安全
                            output = AggregatedBlock.setPropertyGeneric(output, property, currentValue.toString());
                            if (output.equals(target)) System.out.println("属性" + property + "的值" + currentValue + "不通过泛型安全检查");
                        } else System.out.println("属性" + property + "的值" + currentValue + "无效，有效值为：" + property.getPossibleValues());
                    } else System.out.println("属性" + property + "不被" + output + "支持");
                }

                level.setBlock(blockpos, output, Block.UPDATE_CLIENTS);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

        }
        return InteractionResult.PASS;
    }

    /**
     * 配方检测,如果匹配到配方，则返回该配方，否则返回空Optional
     * @param pContext
     * @return
     */
    private Optional<ChangeBlockRecipe> getCurrentRecipe(UseOnContext pContext) {
        System.out.println("这里是配方审查方法");
        Level level = pContext.getLevel();
        BlockState state = level.getBlockState(pContext.getClickedPos());

        if (!level.isClientSide()) {
            NonNullList<BlockState> inputs = NonNullList.withSize(1, state);
            Collection<ChangeBlockRecipe> recipes =
                    level.getRecipeManager().getAllRecipesFor(ChangeBlockRecipe.Type.INSTANCE);

            System.out.println("所有配方：" + recipes.toString() + "count:" + recipes.size());

            return recipes
                    .stream()
                    .filter(recipe -> recipe.matchBlocks(inputs, level))
            .findFirst();
        }
        return Optional.empty();
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        String string = "tooltip."+ ImmortalersDelightMod.MODID  +"." + stack.getItem();
        tooltip.add(Component.translatable(string).withStyle(ChatFormatting.YELLOW));
    }
}

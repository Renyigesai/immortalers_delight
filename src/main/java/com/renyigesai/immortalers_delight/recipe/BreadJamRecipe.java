package com.renyigesai.immortalers_delight.recipe;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BreadJamRecipe extends CustomRecipe {

    public static final int MAX_TOPPINGS = 8; // 最大涂抹上限

    public BreadJamRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack bread = ItemStack.EMPTY;
        int newIngredientCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ImmortalersDelightTags.Items.FORGE_BREAD)) {
                if (!bread.isEmpty()) return false; // 只能有 1 个面包
                bread = stack;
            } else if (stack.is(ImmortalersDelightTags.Items.CAN_ADD_TO_BREAD)) {
                newIngredientCount++;
            } else {
                return false; // 含有其他无关物品
            }
        }

        if (bread.isEmpty() || newIngredientCount < 1) return false;

        // 计算当前面包已有的原料数
        int existingCount = 0;
        if (bread.hasTag() && bread.getTag().contains("BreadToppings", Tag.TAG_LIST)) {
            existingCount = bread.getTag().getList("BreadToppings", Tag.TAG_COMPOUND).size();
        }

        // 总数不能超过上限 8
        return (existingCount + newIngredientCount) <= MAX_TOPPINGS;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack breadStack = ItemStack.EMPTY;
        List<ItemStack> newIngredients = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ImmortalersDelightTags.Items.FORGE_BREAD)) {
                breadStack = stack;
            } else if (stack.is(ImmortalersDelightTags.Items.CAN_ADD_TO_BREAD)) {
                newIngredients.add(stack);
            }
        }

        if (breadStack.isEmpty() || newIngredients.isEmpty()) return ItemStack.EMPTY;

        // 复制面包基础数据
        ItemStack result = breadStack.copyWithCount(1);
        CompoundTag tag = result.getOrCreateTag();
        ListTag jamList = tag.getList("BreadToppings", Tag.TAG_COMPOUND);

        // 如果超出上限直接返回空（双重防线）
        if (jamList.size() + newIngredients.size() > MAX_TOPPINGS) {
            return ItemStack.EMPTY;
        }

        // 将新的原料信息追加到 NBT
        for (ItemStack ingredient : newIngredients) {
            ItemStack singleItem = ingredient.copyWithCount(1);
            CompoundTag itemTag = new CompoundTag();
            singleItem.save(itemTag);
            jamList.add(itemTag);
        }

        tag.put("BreadToppings", jamList);
        return result;
    }

    /**
     * 合成返还容器：在工作台取走面包时，蜂蜜瓶变玻璃瓶、牛奶桶变铁桶等留在工作台中
     */
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            // ForgeHooks.getCraftingRemainingItem 会自动识别原版及模组中物品的残留容器（如蜂蜜瓶->玻璃瓶）
            remaining.set(i, ForgeHooks.getCraftingRemainingItem(stack));
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ImmortalersDelightRecipeTypes.BREAD_JAM_RECIPE.get();
    }
}

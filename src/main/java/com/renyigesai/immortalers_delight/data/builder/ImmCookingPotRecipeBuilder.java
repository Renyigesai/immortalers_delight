//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.renyigesai.immortalers_delight.data.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
/**By FarmersDelight**/
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ImmCookingPotRecipeBuilder {
    private CookingPotRecipeBookTab tab;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Item result;
    private final int count;
    private final int cookingTime;
    private final float experience;
    private final Item container;
    private final Advancement.Builder advancement = Builder.advancement();

    private ImmCookingPotRecipeBuilder(ItemLike resultIn, int count, int cookingTime, float experience, @Nullable ItemLike container) {
        this.result = resultIn.asItem();
        this.count = count;
        this.cookingTime = cookingTime;
        this.experience = experience;
        this.container = container != null ? container.asItem() : null;
        this.tab = null;
    }

    public static ImmCookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience) {
        return new ImmCookingPotRecipeBuilder(mainResult, count, cookingTime, experience, (ItemLike)null);
    }

    public static ImmCookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience, ItemLike container) {
        return new ImmCookingPotRecipeBuilder(mainResult, count, cookingTime, experience, container);
    }

    public ImmCookingPotRecipeBuilder addIngredient(TagKey<Item> tagIn) {
        return this.addIngredient(Ingredient.of(tagIn));
    }

    public ImmCookingPotRecipeBuilder addIngredient(ItemLike itemIn) {
        return this.addIngredient((ItemLike)itemIn, 1);
    }

    public ImmCookingPotRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
        for(int i = 0; i < quantity; ++i) {
            this.addIngredient(Ingredient.of(new ItemLike[]{itemIn}));
        }

        return this;
    }

    public ImmCookingPotRecipeBuilder addIngredient(Ingredient ingredientIn) {
        return this.addIngredient((Ingredient)ingredientIn, 1);
    }

    public ImmCookingPotRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
        for(int i = 0; i < quantity; ++i) {
            this.ingredients.add(ingredientIn);
        }

        return this;
    }

    public ImmCookingPotRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    public ImmCookingPotRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return this.unlockedBy(criterionName, TriggerInstance.hasItems(items));
    }

    public ImmCookingPotRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
        this.advancement.addCriterion("has_any_ingredient", TriggerInstance.hasItems(new ItemPredicate[]{net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(items).build()}));
        return this;
    }

    public ImmCookingPotRecipeBuilder setRecipeBookTab(CookingPotRecipeBookTab tab) {
        this.tab = tab;
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(this.result);
        this.build(consumerIn, "immortalers_delight:cooking/" + location.getPath());
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
        if ((new ResourceLocation(save)).equals(resourcelocation)) {
            throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, new ResourceLocation(save));
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        if (!this.advancement.getCriteria().isEmpty()) {
            this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
            ResourceLocation advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
            consumerIn.accept(new Result(id, this.result, this.count, this.ingredients, this.cookingTime, this.experience, this.container, this.tab, this.advancement, advancementId));
        } else {
            consumerIn.accept(new Result(id, this.result, this.count, this.ingredients, this.cookingTime, this.experience, this.container, this.tab));
        }

    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final CookingPotRecipeBookTab tab;
        private final List<Ingredient> ingredients;
        private final Item result;
        private final int count;
        private final int cookingTime;
        private final float experience;
        private final Item container;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn, @Nullable Advancement.Builder advancement, @Nullable ResourceLocation advancementId) {
            this.id = idIn;
            this.tab = tabIn;
            this.ingredients = ingredientsIn;
            this.result = resultIn;
            this.count = countIn;
            this.cookingTime = cookingTimeIn;
            this.experience = experienceIn;
            this.container = containerIn;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn) {
            this(idIn, resultIn, countIn, ingredientsIn, cookingTimeIn, experienceIn, containerIn, tabIn, (Advancement.Builder)null, (ResourceLocation)null);
        }

        public void serializeRecipeData(JsonObject json) {
            if (this.tab != null) {
                json.addProperty("recipe_book_tab", this.tab.toString());
            }

            JsonArray arrayIngredients = new JsonArray();
            Iterator var3 = this.ingredients.iterator();

            while(var3.hasNext()) {
                Ingredient ingredient = (Ingredient)var3.next();
                arrayIngredients.add(ingredient.toJson());
            }

            json.add("ingredients", arrayIngredients);
            JsonObject objectResult = new JsonObject();
            objectResult.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            if (this.count > 1) {
                objectResult.addProperty("count", this.count);
            }

            json.add("result", objectResult);
            if (this.container != null) {
                JsonObject objectContainer = new JsonObject();
                objectContainer.addProperty("item", ForgeRegistries.ITEMS.getKey(this.container).toString());
                json.add("container", objectContainer);
            }

            if (this.experience > 0.0F) {
                json.addProperty("experience", this.experience);
            }

            json.addProperty("cookingtime", this.cookingTime);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return (RecipeSerializer)ModRecipeSerializers.COOKING.get();
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement != null ? this.advancement.serializeToJson() : null;
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}

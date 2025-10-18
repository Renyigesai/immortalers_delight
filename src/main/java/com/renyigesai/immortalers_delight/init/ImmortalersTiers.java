package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.item.ImmortalersBoatItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ImmortalersTiers implements Tier {
//    WOOD(0, 59, 2.0F, 0.0F, 15, () -> {
//        return Ingredient.of(ItemTags.PLANKS);
//    }),
//    STONE(1, 131, 4.0F, 1.0F, 5, () -> {
//        return Ingredient.of(ItemTags.STONE_TOOL_MATERIALS);
//    }),
//    IRON(2, 250, 6.0F, 2.0F, 14, () -> {
//        return Ingredient.of(Items.IRON_INGOT);
//    }),
//    DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> {
//        return Ingredient.of(Items.DIAMOND);
//    }),
//    GOLD(0, 32, 12.0F, 0.0F, 22, () -> {
//        return Ingredient.of(Items.GOLD_INGOT);
//    }),
//    NETHERITE(4, 2031, 9.0F, 4.0F, 15, () -> {
//        return Ingredient.of(Items.NETHERITE_INGOT);
//    }),
    RUSTY_IRON(1, 225, 5.0F, 1.0F, 11, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }),
    ANCIENT_KNIFE(2, 750, 6.0F, 2.0F, 14, () -> {
        return Ingredient.of(ImmortalersDelightItems.RUSTY_ANCIENT_BLADE.get());
    }),
    PILLAGER_KNIFE(1, 250, 4.0F, 1.0F, 11, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }),
    MAGIC_POWERED(4, 32, 25.0F, 4.0F, 22, () -> {
        return Ingredient.of(Items.LAPIS_BLOCK);
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ImmortalersTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    //@org.jetbrains.annotations.Nullable public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getTag() { return net.minecraftforge.common.ForgeHooks.getTagFromVanillaTier(this); }
}
package com.renyigesai.immortalers_delight.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ReinforcedEnchantUtil {
    public static Random random = new Random();

    public static int getMaxReinforcedEnchantLevel(int enchantMaxLevel) {
        if (DifficultyModeUtil.isPowerBattleMode()) {
            return enchantMaxLevel * 2;
        } else {
            return enchantMaxLevel;
        }
    }

    private static float highProbability(int enchLevel) {
        return (float) (1.2807 * Math.exp(-0.2239 * enchLevel));
    }

    private static float mediumProbability(int enchLevel) {
        return (float) (1.4601 * Math.exp(-0.3202 * enchLevel));
    }

    private static float lowProbability(int enchLevel) {
        return (float) (1.3602 * Math.exp(-0.4010 * enchLevel));
    }

    private static float rarityBuffer(Holder<Enchantment> holder) {
        int w = holder.value().getWeight();
        float rarityLevel = w <= 1 ? 0 : (w <= 5 ? 1 : (w <= 10 ? 2 : 3));
        float treasurePenalty = holder.is(EnchantmentTags.TREASURE) ? -1 : 0;
        return 0.86F + 0.3F * (rarityLevel + treasurePenalty);
    }

    private static int getOperationCount(int repairCost) {
        int operationCount = 5;
        for (int i = 32; i > 1; i /= 2) {
            if (repairCost >= i) {
                operationCount -= 1;
            } else {
                break;
            }
            if (operationCount <= 0) {
                break;
            }
        }
        return operationCount;
    }

    private static float totalProbability(int enchLevel,
                                          int typesNumber, int capacity,
                                          int playerLv, int enchantCost,
                                          int curseNumber,
                                          int repairCost,
                                          float luck,
                                          float rarityBuffer) {
        boolean flag = DifficultyModeUtil.isPowerBattleMode();
        if (!flag) {
            enchLevel = 2 * enchLevel;
        }
        float condition_1 = flag ? typesNumber - capacity : (float) (typesNumber - 1) / capacity;
        float probability_1 = condition_1 < 1 ? highProbability(enchLevel)
                : (condition_1 < 2 ? mediumProbability(enchLevel)
                : (condition_1 < 3 ? lowProbability(enchLevel)
                : 0.0F));
        int condition_2 = playerLv > 30 ? 0 : (playerLv > 15 ? 1 : 2) + (playerLv >= enchantCost ? 0 : 1);
        float probability_2 = condition_2 < 1 ? highProbability(enchLevel)
                : (condition_2 < 2 ? mediumProbability(enchLevel)
                : (condition_2 < 3 ? lowProbability(enchLevel)
                : 0.0F));
        float probability_3 = curseNumber >= 3 ? highProbability(enchLevel)
                : (curseNumber >= 2 ? mediumProbability(enchLevel)
                : (curseNumber >= 1 ? lowProbability(enchLevel)
                : 0.0F));
        float repairCostGuarantee = (float) Math.log(Math.max(repairCost, 1)) * 0.03f;
        float playerLvGuarantee = playerLv > 8 ? (float) (Math.max(playerLv - 8, 60)) / 500 : 0.0f;
        return (probability_1 + probability_2 / 3 + probability_3 / 3) * (1.0f + 0.1f * luck) * rarityBuffer + repairCostGuarantee + playerLvGuarantee;
    }

    public static boolean canReinforcedEnchantment(ItemStack itemstack) {
        return ReinforcedEnchantment(itemstack, 0, 0, 0.0f, true);
    }

    public static boolean ReinforcedEnchantment(ItemStack itemstack, int capacity, int enchanterLevel, float luck, boolean simulate) {
        if (itemstack.isEmpty()) {
            return false;
        }
        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemstack);
        int TypesNumber = 0;
        int curseNumber = 0;
        int availableNumber = 0;
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            int lvl = enchantments.getLevel(holder);
            if (holder.is(EnchantmentTags.CURSE)) {
                curseNumber++;
            } else {
                TypesNumber++;
                if (lvl < getMaxReinforcedEnchantLevel(holder.value().getMaxLevel())) {
                    availableNumber++;
                }
            }
        }
        if (availableNumber <= 0) {
            return false;
        }
        if (!simulate) {
            int repairCost = itemstack.getOrDefault(DataComponents.REPAIR_COST, 0);
            final int typesForLambda = TypesNumber;
            final int cursesForLambda = curseNumber;
            EnchantmentHelper.updateEnchantments(itemstack, mutable -> {
                Set<Holder<Enchantment>> keys = new HashSet<>(mutable.keySet());
                for (Holder<Enchantment> holder : keys) {
                    int level = mutable.getLevel(holder);
                    if (!holder.is(EnchantmentTags.CURSE) && level < getMaxReinforcedEnchantLevel(holder.value().getMaxLevel())) {
                        float probability = totalProbability(level,
                                typesForLambda, capacity,
                                enchanterLevel, holder.value().getMinCost(level),
                                cursesForLambda,
                                getOperationCount(repairCost),
                                luck,
                                rarityBuffer(holder));
                        if (probability > 0.0F && random.nextFloat() <= probability) {
                            mutable.upgrade(holder, level + 1);
                        }
                    }
                }
            });
        }
        return true;
    }
}

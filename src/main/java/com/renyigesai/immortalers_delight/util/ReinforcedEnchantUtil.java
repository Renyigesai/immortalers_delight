package com.renyigesai.immortalers_delight.util;

import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ReinforcedEnchantUtil {
    public static Random random = new Random();
    public static int getMaxReinforcedEnchantLevel(int enchantMaxLevel) {
        if (DifficultyModeHelper.isPowerBattleMode()) {
            return enchantMaxLevel * 2;
        } else {return enchantMaxLevel;}
    }
    private static float highProbability(int enchLevel) {
        return (float) (1.2807*Math.exp(-0.2239*enchLevel));
    }

    private static float mediumProbability(int enchLevel) {
        return (float) (1.4601*Math.exp(-0.3202*enchLevel));
    }

    private static float lowProbability(int enchLevel) {
        return (float) (1.3602*Math.exp(-0.4010*enchLevel));
    }
    private static float rarityBuffer(Enchantment enchantment) {
        float rarityLevel = enchantment.getRarity() == Enchantment.Rarity.VERY_RARE ? 0
                : (enchantment.getRarity() == Enchantment.Rarity.RARE ? 1
                : (enchantment.getRarity() == Enchantment.Rarity.UNCOMMON ? 2
                : 3));
        return 0.86F + 0.3F*(rarityLevel + (enchantment.isTreasureOnly() ? -1 : 0));
    }
    private static int getOperationCount(int repairCost) {
        int operationCount = 5;
        for (int i = 32; i > 1;i /= 2) {
            if (repairCost >= i) {
                operationCount -= 1;
            } else {break;}
            if (operationCount <= 0) break;
        }
        return operationCount;
    }
    private static float totalProbability(int enchLevel,
                                          int typesNumber,int capacity ,
                                          int playerLv, int enchantCost,
                                          int curseNumber,
                                          int repairCost,
                                          float luck,
                                          float rarityBuffer) {
        boolean flag = DifficultyModeHelper.isPowerBattleMode();
        if (!flag) {enchLevel = 2*enchLevel;}
        float condition_1 = flag ? typesNumber - capacity : (float) (typesNumber-1) / capacity;
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
        float repairCostGuarantee = (float)Math.log(repairCost)*0.03f;
        float playerLvGuarantee = playerLv > 8 ? (float) (Math.max(playerLv - 8,60))/500 : 0.0f;
        return (probability_1 + probability_2 / 3 + probability_3 / 3) * (1.0f + 0.1f*luck) * rarityBuffer + repairCostGuarantee + playerLvGuarantee;
    }

    public static boolean canReinforcedEnchantment(ItemStack itemstack) {
        return ReinforcedEnchantment(itemstack, 0, 0, 0.0f, true);
    }
    public static boolean ReinforcedEnchantment(ItemStack itemstack, int capacity, int enchanterLevel, float luck, boolean simulate) {
        if (itemstack.isEmpty()) return false;
        boolean isEnchantBook = itemstack.getItem() instanceof EnchantedBookItem;
        Map<Enchantment, Integer> enchantments = isEnchantBook ? getEnchantedBookEnchantments(itemstack) : EnchantmentHelper.getEnchantments(itemstack);
        int TypesNumber = 0;
        int curseNumber = 0;
        int availableNumber = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (entry.getKey().isCurse()) {
                curseNumber++;
            } else {
                TypesNumber++;
                if (entry.getValue() < getMaxReinforcedEnchantLevel(entry.getKey().getMaxLevel())) {
                    availableNumber++;
                }
            }
        }
        if (availableNumber <= 0) {return false;}
        if (!simulate) {
            Map<Enchantment, Integer> newEnchantments = new HashMap<>();
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                if (!entry.getKey().isCurse() && entry.getValue() < getMaxReinforcedEnchantLevel(entry.getKey().getMaxLevel())) {
                    float probability = totalProbability(entry.getValue(),
                            TypesNumber, capacity,
                            enchanterLevel, entry.getKey().getMinCost(entry.getValue()),
                            curseNumber,
                            getOperationCount(itemstack.getBaseRepairCost()),
                            luck,
                            rarityBuffer(entry.getKey()));
                    if (probability > 0.0F && random.nextFloat() <= probability) {
                        newEnchantments.put(entry.getKey(), entry.getValue() + 1);
                    }
                }
            }
            if (isEnchantBook) {
                List<EnchantmentInstance> list = getEnchantmentInstances(newEnchantments);
                for(EnchantmentInstance enchantmentinstance : list) {
                    EnchantedBookItem.addEnchantment(itemstack, enchantmentinstance);
                }
            } else {
                for (Map.Entry<Enchantment, Integer> entry : newEnchantments.entrySet()){
                    itemstack.enchant(entry.getKey(), entry.getValue());
                }
            }
        }
        return true;
    }
    public static Map<Enchantment, Integer> getEnchantedBookEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        if (stack.getItem() instanceof EnchantedBookItem) {
            // 获取附魔书的StoredEnchantments标签
            ListTag enchantmentTagList = EnchantedBookItem.getEnchantments(stack);

            for (int i = 0; i < enchantmentTagList.size(); i++) {
                CompoundTag enchantmentTag = enchantmentTagList.getCompound(i);

                // 获取附魔ID和等级
                String enchantmentId = enchantmentTag.getString("id");
                int level = enchantmentTag.getInt("lvl");

                // 将ID转换为Enchantment对象
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantmentId));

                if (enchantment != null) {
                    enchantments.put(enchantment, level);
                }
            }
        }

        return enchantments;
    }

    public static List<EnchantmentInstance> getEnchantmentInstances(Map<Enchantment, Integer> enchantments) {
        List<EnchantmentInstance> instances = new ArrayList<>();

        // 转换为EnchantmentInstance列表
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            instances.add(new EnchantmentInstance(entry.getKey(), entry.getValue()));
        }

        return instances;
    }
}

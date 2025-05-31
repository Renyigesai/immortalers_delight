package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.block.CulturalLegacyEffectToolBlock;
import com.renyigesai.immortalers_delight.block.HimekaidoLeavesGrowing;
import com.renyigesai.immortalers_delight.event.DifficultyModeHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.CULTURAL_LEGACY;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_POISON;

public class CulturalLegacyMobEffect extends MobEffect {
    private static final int[] LEVEL_UP = new int[] {5, 10, 15, 18, 20, 22, 23, 24};
    public static final String BOOK_EDITING = "BookEditingProgress";
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
    private static float totalProbability(int enchLevel,
                                          int typesNumber,int capacity ,
                                          int playerLv, int enchantCost,
                                          int curseNumber,
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
        return (probability_1 + probability_2 / 3 + probability_3 / 3) * (1.0f + 0.1f*luck) * rarityBuffer;
    }

    public CulturalLegacyMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == CULTURAL_LEGACY.get()) {
//            int lv = amplifier > 7 ? 7 : amplifier;
//            if (pEntity.isAlive() && !pEntity.level().isClientSide) {
//                if (pEntity.level().isEmptyBlock(pEntity.getOnPos().above())) {
//                    pEntity.level().setBlock(pEntity.getOnPos().above(), ImmortalersDelightBlocks.CULTURAL_LEGACY.get().defaultBlockState().setValue(CulturalLegacyEffectToolBlock.AGE,lv),2);
//                }
//            }
//            if (pEntity instanceof Player player) {
//                if (player.getEffect(CULTURAL_LEGACY.get()) != null
//                        && Objects.requireNonNull(player.getEffect(CULTURAL_LEGACY.get())).getDuration() == 1) {
//                    player.giveExperienceLevels(LEVEL_UP[lv]);
//                }
//            }
            if (pEntity instanceof Player player && !pEntity.level().isClientSide) {

                if (player.experienceLevel > 0) {
                    ItemStack mainStack = player.getMainHandItem();
                    ItemStack offStack = player.getOffhandItem();
                    ItemStack itemstack = new ItemStack(Items.AIR);
                    if (!mainStack.isEmpty() && mainStack.getItem() instanceof EnchantedBookItem) {
                        itemstack=mainStack;
                    } else if (!offStack.isEmpty() && offStack.getItem() instanceof EnchantedBookItem) {
                        itemstack=offStack;
                    }
                    if (!itemstack.isEmpty()) {
                        if (itemstack.getItem() instanceof EnchantedBookItem enchantedBook) {
                            itemstack.getOrCreateTag();
                            if (!itemstack.getTag().contains(BOOK_EDITING, 99)) {
                                itemstack.getTag().putInt(BOOK_EDITING, 0);
                            }

                            int progress = itemstack.getTag().getInt(BOOK_EDITING);
                            if (progress < 1000) {
                                player.giveExperiencePoints(-1 * Math.max(4, player.getXpNeededForNextLevel()/4));

                                //player.giveExperienceLevels(-1);
                                itemstack.getTag().putInt(BOOK_EDITING, progress + 25);
                            } else {
                                itemstack.getTag().putInt(BOOK_EDITING, progress - 1000);
                                Map<Enchantment, Integer> enchantments = getEnchantedBookEnchantments(itemstack);
                                int TypesNumber = 0;
                                int curseNumber = 0;
                                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                    if (entry.getKey().isCurse()) {curseNumber++;} else {TypesNumber++;}
                                }
                                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                    if (!entry.getKey().isCurse() && entry.getValue() < getMaxReinforcedEnchantLevel(entry.getKey().getMaxLevel())) {
                                        float probability = totalProbability(entry.getValue(),
                                                TypesNumber, amplifier + 1,
                                                player.experienceLevel, entry.getKey().getMinCost(entry.getValue()),
                                                curseNumber,
                                                player.getAttribute(Attributes.LUCK) == null ? 0.0F : (float) player.getAttribute(Attributes.LUCK).getValue(),
                                                rarityBuffer(entry.getKey()));
                                        if (probability > 0.0F && player.getRandom().nextFloat() <= probability) {
                                            enchantments.put(entry.getKey(), entry.getValue() + 1);
                                        }
                                    }
                                }
                                List<EnchantmentInstance> list = getEnchantmentInstances(enchantments);
                                for(EnchantmentInstance enchantmentinstance : list) {
                                    EnchantedBookItem.addEnchantment(itemstack, enchantmentinstance);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 20 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
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

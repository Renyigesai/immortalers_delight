package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.ReinforcedEnchantUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.CULTURAL_LEGACY;

public class CulturalLegacyMobEffect extends MobEffect {
    private static final int[] LEVEL_UP = new int[] {5, 10, 15, 18, 20, 22, 23, 24};
    public static final String BOOK_EDITING = "BookEditingProgress";

    public CulturalLegacyMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 15181790);
    }
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == CULTURAL_LEGACY.get()) {
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
                            if (progress < 1000 && ReinforcedEnchantUtil.canReinforcedEnchantment(itemstack)) {
                                player.giveExperiencePoints(-1 * Math.max(4, player.getXpNeededForNextLevel()/4));
                                itemstack.getTag().putInt(BOOK_EDITING, progress + 25);
                            } else if (progress >= 1000) {
                                ReinforcedEnchantUtil.ReinforcedEnchantment(itemstack, amplifier + 1, player.experienceLevel, player.getLuck(), false);
                                itemstack.getTag().putInt(BOOK_EDITING, progress - 1000);
                                if (pEntity instanceof ServerPlayer serverPlayer) {
                                    ImmortalersDelightMod.LEVEL_UP_ENCHANTMENT_TRIGGER.trigger(serverPlayer);
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
}

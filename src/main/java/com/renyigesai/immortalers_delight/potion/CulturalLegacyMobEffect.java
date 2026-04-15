package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.util.ReinforcedEnchantUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.CULTURAL_LEGACY;

public class CulturalLegacyMobEffect extends BaseMobEffect {
    public static final String BOOK_EDITING = "BookEditingProgress";

    public CulturalLegacyMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 15181790);
    }
    @Override
    public void applyEffectTickInControl(LivingEntity pEntity, int amplifier) {
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
    public boolean isDurationEffectTickInControl(int duration, int amplifier) {
        int i = 20 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }


    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class CulturalLegacyPotionEffect {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();

            CompoundTag tag = stack.getTag();
            if (tag != null) {
                if (tag.contains(CulturalLegacyMobEffect.BOOK_EDITING)) {
                    int progress = tag.getInt(CulturalLegacyMobEffect.BOOK_EDITING);

                    if (progress > 0) {
                        MutableComponent textValue = Component.translatable(
                                "tooltip." +ImmortalersDelightMod.MODID+ ".book_progress", // 翻译键
                                progress // 替换%d占位符
                        );
                        event.getToolTip().add(textValue.withStyle(ChatFormatting.GRAY));
                    }

                }
            }
        }

        @SubscribeEvent
        public static void onEntityAddEffect(MobEffectEvent.Applicable event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity livingEntity
                        && livingEntity.hasEffect(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get())
                        && event.getEffectInstance().getEffect() == ImmortalersDelightMobEffect.CULTURAL_LEGACY.get()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
